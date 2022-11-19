package pl.agh.diffusion_project;

import org.javatuples.Triplet;
import pl.agh.diffusion_project.obstacles.ObstaclesLoader;
import pl.agh.diffusion_project.wind.Wind;
import pl.agh.diffusion_project.wind.WindLoader;

public class TestGenerateBuilding {
    public static void main(String[] args) throws Exception {
        ObstaclesLoader obstacleLoader = ObstaclesLoader.loadObstaclesFromBitmap("testowy.bmp");

        WindLoader windLoader = WindLoader.loadWindFromFile("testowy-v8-d0-100-velocity");
        Wind wind = new Wind(windLoader, obstacleLoader);

        wind.updateWind(new Float[0][0][0]);

        /*
        final String buildingFile = "result-1234.bmp";

        Properties prop = new Properties();
        prop.load(new FileInputStream("resources/config.properties"));

        FetchBuildingsAdapter buildingAdapter = new FetchBuildingsAdapter(prop.getProperty("fetch_buildings_path"));
        VisualizationAdapter vizAdapter = new VisualizationAdapter(prop.getProperty("visualization_path"));
        WindAdapter windAdapter = new WindAdapter(prop.getProperty("wind_calculator_path"));

        buildingAdapter.setParameter("lat-range", "0.001");
        buildingAdapter.setParameter("lon-range", "0.002");
        //buildingAdapter.setParameter("block-size", "1");
        buildingAdapter.setParameter("result-file", buildingFile);
        buildingAdapter.fetchBuildings();

        windAdapter.setParameter("m", "testowy.bmp");
        windAdapter.setParameter("z", "50");
        windAdapter.setParameter("v", "8");
        windAdapter.setParameter("r", "100");
        windAdapter.setParameter("i", "100");
        windAdapter.setParameter("d", "0");
        windAdapter.setParameter("s", "testowy-v8-d0");
        windAdapter.setParameter("c", "1");
        //windAdapter.calculateWind();

        ObstaclesLoader loader = ObstaclesLoader.loadObstaclesFromBitmap("testowy.bmp");
        loader.saveInDataFile(vizAdapter.getBuildingDataPath());

        vizAdapter.runVisualization();
         */
    }
}
