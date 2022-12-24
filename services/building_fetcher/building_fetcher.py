"""
@Author: Mikołaj Kardyś
"""

import overpy
from PIL import Image
import numpy as np
from shapely.geometry.polygon import Polygon
from shapely.geometry import MultiPoint, MultiPolygon, LineString
import argparse

# Constants
EARTH_CIRC = 40075 * 1e3  # circumference of the Earth in meters


def arg_parse():
    arg_parser = argparse.ArgumentParser(description='Please provide de necessary values:')

    arg_parser.add_argument('--lat-start', default=50.06147, type=float, help='Latitude of the starting point')
    arg_parser.add_argument('--lon-start', default=19.93645, type=float, help='Longitude of the starting point')
    arg_parser.add_argument('--lat-range', default=0.002, type=float,
                            help='Range of latitude from the starting point (in coordinates)')
    arg_parser.add_argument('--lon-range', default=0.002, type=float,
                            help='Range of longitude from the starting point (in coordinates)')
    arg_parser.add_argument('--block-size', default=1, type=float, help='Side of discretization unit (in meters)')

    arg_parser.add_argument('--result-file', default="result", type=str, help='Name of the resulting file')

    arg_parser.add_argument('--spin-angle', default=0, type=int, help='Angle of rotation')

    arg_parser.add_argument('--fill-missing', default=False, action=argparse.BooleanOptionalAction,
                            help='Fill missing data with random values')

    return arg_parser


class BuildingGetter:
    DEFAULT_HEIGHT = 10
    METERS_PER_LEVEL = 3
    ROAD_TYPES = {
        "red": ["motorway", "motorway_link", "trunk", "trunk_link"],
        "green": ["primary", "primary_link", "secondary", "secondary_link"],
        "blue": ["tertiary", "tertiary_link", "residential"]
    }
    """
    This class is responsible for fetching building data from an OSM API and returning a useful form

    :static_field BUILDING_LEVEL_HEIGHT: An average height of a single building level
    """

    @staticmethod
    def _get_road_channel(road_type):
        if road_type in BuildingGetter.ROAD_TYPES["red"]:
            return 0
        elif road_type in BuildingGetter.ROAD_TYPES["green"]:
            return 1
        elif road_type in BuildingGetter.ROAD_TYPES["blue"]:
            return 2
        return -1

    @staticmethod
    def _get_height(way):
        """
        Private method for extracting heights of buildings from way tags

        :param way: building parameters extracted from response
        :return: height of building extracted from tags;
        :float: int
        """

        building_height = -1

        if 'height' in way.tags.keys():
            building_height = float(way.tags['height'])
        elif 'building:levels' in way.tags.keys():
            levels = way.tags['building:levels'].split(";")[0]
            building_height = float(levels) * BuildingGetter.METERS_PER_LEVEL

        return building_height

    @staticmethod
    def send_request(lat, long, lat_rad, long_rad):
        """
        This is the main function of this class; Use it to fetch building data from the OSM API for a specified area

        :param lat: latitude of the point of origin
        :param long: longitude of the point of origin
        :param lat_rad: area radius along the latitude axis
        :param long_rad: area radius along the longitude axis
        :return: two item tuple: list of polygons, list of polygon heights (height is -1 if not specified)
        :rtype: tuple( list(list(tuple)), list(float) )
        """
        s = lat - lat_rad
        n = lat + lat_rad
        w = long - long_rad
        e = long + long_rad

        api = overpy.Overpass()
        req_result = api.query(f"""
            <osm-script>
              <union>
                <query type="way">
                  <has-kv k="building"/>      
                  <bbox-query s="{s}" n="{n}" w="{w}" e="{e}"/>
                </query>
                <query type="way">    
                  <has-kv k="building:part"/>
                  <bbox-query s="{s}" n="{n}" w="{w}" e="{e}"/>
                </query>    
                <query type="relation">    
                  <has-kv k="building"/>
                  <bbox-query s="{s}" n="{n}" w="{w}" e="{e}"/>
                </query>  
                <query type="way">
                  <has-kv k="highway"/>      
                  <bbox-query s="{s}" n="{n}" w="{w}" e="{e}"/>
                </query>
              </union>
              <print mode="body"/>
              <recurse type="down"/>
              <print mode="skeleton" order="quadtile"/>
            </osm-script>""")

        req_polygons = []
        req_heights = []
        req_roads = []
        req_road_channels = []

        for way in req_result.ways:
            if 'highway' in way.tags.keys():
                line = []
                for node in way.nodes:
                    line.append((float(node.lon), float(node.lat)))

                road_channel_ind = BuildingGetter._get_road_channel(way.tags["highway"])

                if not road_channel_ind == -1:
                    req_roads.append(line)
                    req_road_channels.append(road_channel_ind)

            elif 'building' in way.tags.keys() or 'building:part' in way.tags.keys():
                polygon = []
                for node in way.nodes:
                    polygon.append((float(node.lon), float(node.lat)))

                building_height = BuildingGetter._get_height(way)

                req_polygons.append(polygon)
                req_heights.append(building_height)

        for relation in req_result.relations:
            if 'building' in relation.tags.keys():
                for member in relation.members:
                    if member.role == 'outer':
                        way = member.resolve()

                        polygon = []
                        for node in way.nodes:
                            polygon.append((float(node.lon), float(node.lat)))

                        b_height = BuildingGetter._get_height(way)
                        if b_height == -1:
                            b_height = BuildingGetter._get_height(relation)

                        req_polygons.append(polygon)
                        req_heights.append(b_height)

        return req_polygons, req_heights, req_roads, req_road_channels


class PointGrid:
    def __init__(self):
        multi = MultiPolygon([Polygon(pl) for pl in polygons])

        self.bounds = multi.bounds

        x_steps = np.arange(self.bounds[0], self.bounds[2], step_size)
        y_steps = np.arange(self.bounds[1], self.bounds[3], step_size)

        X, Y = np.meshgrid(x_steps, y_steps)

        self.x_dim = len(x_steps)
        self.y_dim = len(y_steps)
        self.points = MultiPoint(list(zip(X.flatten(), Y.flatten())))

    def get_point_index(self, point):
        x_index = (point.x - self.bounds[0]) / step_size
        y_index = (point.y - self.bounds[1]) / step_size

        return round(x_index), round(y_index)


def translate_coords(old_polygons, start_x, start_y):
    new_polygons = []

    for pol in old_polygons:
        new_polygon = []

        for point in pol:
            x_diff, y_diff = point[0] - start_x, point[1] - start_y

            new_x = x_diff / 360 * (EARTH_CIRC * np.cos(np.radians(start_y)))
            new_y = y_diff / 360 * EARTH_CIRC

            new_polygon.append((new_x, new_y))

        new_polygons.append(new_polygon)

    return new_polygons


def rotate(point_list, wind_angle):
    a_tab = [np.array(pl) for pl in point_list]

    theta = np.radians(wind_angle)
    c, s = np.cos(theta), np.sin(theta)
    r = np.array(((c, -s), (s, c)))

    return [np.dot(A, r.T) for A in a_tab]


if __name__ == "__main__":
    parser = arg_parse()
    args = parser.parse_args()

    x_start = args.lon_start
    y_start = args.lat_start
    x_int = args.lon_range
    y_int = args.lat_range
    step_size = args.block_size

    print("Fetching data from server...", end="\r")
    polygons, heights, roads, road_channels = BuildingGetter.send_request(y_start, x_start, y_int, x_int)
    print("Data from server fetched    ")

    polygons = translate_coords(polygons, x_start, y_start)
    roads = translate_coords(roads, x_start, y_start)

    polygons = rotate(polygons, args.spin_angle)
    roads = rotate(roads, args.spin_angle)

    grid = PointGrid()
    x_dim, y_dim, points = grid.x_dim, grid.y_dim, grid.points

    result_matrix = np.zeros((x_dim, y_dim)) + [255]

    i = 0
    intersections = []
    for poly in polygons:
        print(f"Calculating building-grid intersections pt.1 in progress; {i}/{len(polygons)} completed", end="\r")
        i += 1
        p = Polygon(poly)
        intersections.append(points.intersection(p))

    i = 0
    for result, height in zip(intersections, heights):
        print(f"Calculating building-grid intersections pt.2 in progress; {i}/{len(polygons)} completed", end="\r")
        i += 1
        if type(result) == MultiPoint:
            result_points = list(zip(*[(point.xy[0][0], point.xy[1][0]) for point in result.geoms]))

            for result_point in result.geoms:
                x_ind, y_ind = grid.get_point_index(result_point)

                if height == -1:
                    height = 0

                new_hue = np.round(255 - height)
                old_hue = result_matrix[x_ind, y_dim - y_ind - 1]

                if old_hue > new_hue:
                    result_matrix[x_ind, y_dim - y_ind - 1] = new_hue

    i = 0
    print(args)
    if args.fill_missing:
        for result in intersections:
            print(f"Calculating building-grid intersections pt.3 in progress; {i}/{len(polygons)} completed", end="\r")
            i += 1
            if type(result) == MultiPoint:
                result_points = list(zip(*[(point.xy[0][0], point.xy[1][0]) for point in result.geoms]))

                rand_height = (55 * np.random.random() + 200)

                for result_point in result.geoms:
                    x_ind, y_ind = grid.get_point_index(result_point)

                    new_hue = 255 - rand_height
                    old_hue = result_matrix[x_ind, y_dim - y_ind - 1]

                    if old_hue == 255:
                        result_matrix[x_ind, y_dim - y_ind - 1] = new_hue

    print(f"Calculating building-grid intersections completed; {len(polygons)}/{len(polygons)} completed              ")

    img = Image.new('L', (x_dim + 2, y_dim + 2), "white")
    pixels = img.load()
    for i in range(x_dim):
        for j in range(y_dim):
            pixels[i + 1, j + 1] = int(result_matrix[i, j])

    file_name = args.result_file
    img.save(file_name + ".bmp", "bmp")

    print(f"Results saved to {file_name}.bmp")

    result_matrix = np.zeros((x_dim, y_dim, 3), dtype=int)

    i = 0
    for road, road_channel in zip(roads, road_channels):
        print(f"Calculating road-grid intersections in progress; {i}/{len(roads)} completed", end="\r")
        i += 1

        p = LineString(road).buffer(distance=2, cap_style=2)

        result = points.intersection(p)

        if type(result) == MultiPoint:
            result_points = list(zip(*[(point.xy[0][0], point.xy[1][0]) for point in result.geoms]))

            for result_point in result.geoms:
                x_ind, y_ind = grid.get_point_index(result_point)

                result_matrix[x_ind, y_dim - y_ind - 1, road_channel] = 255

    print(f"Calculating road-grid intersections completed; {len(roads)}/{len(roads)} completed  ")

    img = Image.new('RGB', (x_dim + 2, y_dim + 2))
    pixels = img.load()
    for i in range(x_dim):
        for j in range(y_dim):
            pixels[i + 1, j + 1] = (result_matrix[i, j, 0], result_matrix[i, j, 1], result_matrix[i, j, 2])

    img.save(file_name + "_roads.bmp", "bmp")

    print(f"Results saved to {file_name}_roads.bmp")
