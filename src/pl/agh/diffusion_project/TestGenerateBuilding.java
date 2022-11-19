package pl.agh.diffusion_project;

import org.javatuples.Triplet;
import pl.agh.diffusion_project.adapters.VisualizationAdapter;
import pl.agh.diffusion_project.adapters.WindAdapter;
import pl.agh.diffusion_project.obstacles.ObstaclesLoader;
import pl.agh.diffusion_project.pollution.Pollution;
import pl.agh.diffusion_project.wind.Wind;
import pl.agh.diffusion_project.wind.WindLoader;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class TestGenerateBuilding {
    private static void saveConcentration(float [][][] p, String fileName) {

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName, false))) {
            int x = p.length;
            int y = p[0].length;
            int z = p[0][0].length;

            out.writeInt(x);
            out.writeInt(y);
            out.writeInt(z);

            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    for (int k = 0; k < z; k++) {
                        out.writeFloat(p[i][j][k]);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printNonZero(float [][][] p) {
        int x = p.length;
        int y = p[0].length;
        int z = p[0][0].length;

        for (int i = 0; i < x; i++){
            for (int j = 0; j < y; j++){
                for (int k = 0; k < z; k++){
                    if (p[i][j][k] > 0){
                        System.out.println(i + " " + j + " " + k + " " + p[i][j][k]);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();
        prop.load(new FileInputStream("resources/config.properties"));

        ObstaclesLoader obstacleLoader = ObstaclesLoader.loadObstaclesFromBitmap("empty.bmp");

        WindLoader windLoader = WindLoader.loadWindFromFile("empty-v8-d60-1000-velocity");
        Wind wind = new Wind(windLoader, obstacleLoader);

        float [][][] oldTab = new float[windLoader.getDx()][windLoader.getDy()][windLoader.getDz()];

        oldTab[5][5][5] = 1.0f;

        printNonZero(oldTab);
        for (int i = 0; i < 10; i++){
            wind.updateWind(oldTab);
            printNonZero(oldTab);
        }

        /*
        Properties prop = new Properties();
        prop.load(new FileInputStream("resources/config.properties"));

        WindAdapter windAdapter = new WindAdapter(prop.getProperty("wind_calculator_path"));
        windAdapter.setParameter("m", "empty.bmp");
        windAdapter.setParameter("z", "20");
        windAdapter.setParameter("s", "empty-result");

        windAdapter.calculateWind();


        bstaclesLoader obstacleLoader = ObstaclesLoader.loadObstaclesFromBitmap("empty.bmp");

        WindLoader windLoader = WindLoader.loadWindFromFile("empty-v8-d0-100-velocity");
        Wind wind = new Wind(windLoader, obstacleLoader);

        float [][][] oldTab = new float[windLoader.getDx()][windLoader.getDy()][windLoader.getDz()];O


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