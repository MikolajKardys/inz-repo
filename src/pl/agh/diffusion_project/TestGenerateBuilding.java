package pl.agh.diffusion_project;


import pl.agh.diffusion_project.adapters.VisualizationAdapter;
import pl.agh.diffusion_project.obstacles.ObstaclesLoader;
import pl.agh.diffusion_project.wind.Wind;
import pl.agh.diffusion_project.wind.WindLoader;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class TestGenerateBuilding {
    public static void saveConcentration(float [][][] oldVal, float [][][] newVal, String fileName) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName, false))) {
            int x = oldVal[0].length;
            int y = oldVal[0][0].length;
            int z = oldVal.length;

            out.writeInt(x);
            out.writeInt(y);
            out.writeInt(z);

            byte [] bytes = new byte[4 * x * y * z];
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    for (int k = 0; k < z; k++) {
                        int bits = Float.floatToIntBits(oldVal[k][i][j] + newVal[k][i][j]);

                        int index = (i * y * z + j * z + k) * 4;

                        bytes[index + 3] = (byte)(bits & 0xff);
                        bytes[index + 2] = (byte)((bits >> 8) & 0xff);
                        bytes[index + 1] = (byte)((bits >> 16) & 0xff);
                        bytes[index] = (byte)((bits >> 24) & 0xff);
                    }
                }
            }
            out.write(bytes);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void placeWall(float[][][] oldTab){
        for (int i = 0; i < 150; i++){
            for (int j = 0; j < 50; j++){
                for (int k = 0; k < 5; k++)
                    oldTab[i][k][j] = 0.3f;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();
        prop.load(new FileInputStream("resources/config.properties"));
        ObstaclesLoader obstacleLoader = ObstaclesLoader.loadObstaclesFromBitmap("testowy.bmp", 50);
        VisualizationAdapter vizAdapter = new VisualizationAdapter(prop.getProperty("visualization_path"));
        obstacleLoader.saveInDataFile(vizAdapter.getBuildingDataPath());
        /*

        Properties prop = new Properties();
        prop.load(new FileInputStream("resources/config.properties"));

        ObstaclesLoader obstacleLoader = ObstaclesLoader.loadObstaclesFromBitmap("testowy.bmp", 50);
        VisualizationAdapter vizAdapter = new VisualizationAdapter(prop.getProperty("visualization_path"));

        vizAdapter.clearIterations();

        WindLoader windLoader = WindLoader.loadWindFromFile("testowy-v8-d0-1000-velocity");

        Wind wind = new Wind(windLoader, obstacleLoader);

        float[][][] newPollutions = new float[windLoader.getDx()+2][windLoader.getDy()+2][windLoader.getDz()+1];
        float[][][] oldTab = new float[windLoader.getDx()][windLoader.getDy()][windLoader.getDz()];

<<<<<<< HEAD
        placeWall(oldTab);

        int iterNumber = 200;

        saveConcentration(oldTab, newPollutions, vizAdapter.getPollutionDataPath(0));

        System.out.println(0);
        for (int i = 0; i < iterNumber; i++){
            wind.updateWind(oldTab, newPollutions);
            saveConcentration(oldTab, newPollutions, vizAdapter.getPollutionDataPath(i + 1));
            System.out.println(i + 1);
=======
        oldTab[50][0][0] = 1.0f;

        int iterNumber = 100;
        //System.out.println(0);
        //printNonZero(oldTab);
        for (int i = 0; i < iterNumber; i++){
            wind.updateWind(oldTab, newPollutions);
            //saveConcentration(oldTab, vizAdapter.getPollutionDataPath(i + 1));
            System.out.println(i+1);
            printSum(oldTab);
            //printNonZero(oldTab);
            oldTab[50][0][0] = 1.0f;
>>>>>>> 9ec860afa408acf1023d5b692de5084c5c4eb270
        }

        vizAdapter.generateConfig(iterNumber + 1);
        obstacleLoader.saveInDataFile(vizAdapter.getBuildingDataPath());


        Properties prop = new Properties();
        prop.load(new FileInputStream("resources/config.properties"));

        ObstaclesLoader obstacleLoader = ObstaclesLoader.loadObstaclesFromBitmap("empty.bmp");
        VisualizationAdapter vizAdapter = new VisualizationAdapter(prop.getProperty("visualization_path"));

        WindLoader windLoader = WindLoader.loadWindFromFile("empty-v8-d0-1000-velocity");

        Wind wind = new Wind(windLoader, obstacleLoader);

        float [][][] oldTab = new float[windLoader.getDx()][windLoader.getDy()][windLoader.getDz()];

        oldTab[5][5][5] = 1.0f;

        saveConcentration(oldTab, vizAdapter.getPollutionDataPath(0));
        printNonZero(oldTab);
        for (int i = 0; i < 20; i++){
            wind.updateWind(oldTab);
            System.out.println(i);
            printNonZero(oldTab);
            saveConcentration(oldTab, vizAdapter.getPollutionDataPath(i + 1));
        }


        vizAdapter.generateConfig(21, false);

        printNonZero(oldTab);
        for (int i = 0; i < 10; i++){
            wind.updateWind(oldTab);
            printNonZero(oldTab);
        }


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