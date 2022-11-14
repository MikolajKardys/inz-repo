package pl.agh.diffusion_project;

import pl.agh.diffusion_project.adapters.FetchBuildingsAdapter;
import pl.agh.diffusion_project.adapters.VisualizationAdapter;
import pl.agh.diffusion_project.obstacles.ObstaclesLoader;

import java.io.FileInputStream;
import java.util.Properties;

public class TestGenerateBuilding {
    public static void main(String[] args) throws Exception {
        final String buildingFile = "result-1234.bmp";

        Properties prop = new Properties();
        prop.load(new FileInputStream("resources/config.properties"));

        FetchBuildingsAdapter buildingAdapter = new FetchBuildingsAdapter(prop.getProperty("fetch_buildings_path"));
        VisualizationAdapter vizAdapter = new VisualizationAdapter(prop.getProperty("visualization_path"));

        buildingAdapter.setParameter("lat-range", "0.001");
        buildingAdapter.setParameter("lon-range", "0.002");
        //buildingAdapter.setParameter("block-size", "1");
        buildingAdapter.setParameter("result-file", buildingFile);
        buildingAdapter.fetchBuildings();

        ObstaclesLoader loader = ObstaclesLoader.loadObstaclesFromBitmap(buildingFile);
        loader.saveInDataFile(vizAdapter.getBuildingDataPath());

        vizAdapter.runVisualization();
    }
}
