package pl.agh.diffusion_project;


import pl.agh.diffusion_project.adapters.VisualizationAdapter;
import pl.agh.diffusion_project.cells.Mapping;
import pl.agh.diffusion_project.cells.RefactoredCell;
import pl.agh.diffusion_project.obstacles.ObstaclesLoader;
import pl.agh.diffusion_project.pollution.Pollution;
import pl.agh.diffusion_project.wind.SimpleWindUpdate;
import pl.agh.diffusion_project.wind.Wind;
import pl.agh.diffusion_project.wind.WindLoader;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TestGenerateBuilding {
    public static void saveConcentration(RefactoredCell [][][] p, String fileName) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName, false))) {
            int x = p[0].length;
            int y = p[0][0].length;
            int z = p.length;

            out.writeInt(x);
            out.writeInt(y);
            out.writeInt(z);

            byte [] bytes = new byte[4 * x * y * z];
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    for (int k = 0; k < z; k++) {
                        int bits = Float.floatToIntBits(p[k][i][j].getFullCurrentPollution().getAll().get(0));

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

    private static void placeWall(Board board){
        for (int i = 0; i < 150; i++){
            for (int j = 0; j < 1; j++){
                for (int k = 2; k < 5; k++){
                    board.modConcentrationAt(i, j, k, new Pollution(List.of(0.1f)));
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();
        prop.load(new FileInputStream("resources/config.properties"));

        ObstaclesLoader obstacleLoader = ObstaclesLoader.loadObstaclesFromBitmap("testowy.bmp", 50);
        VisualizationAdapter vizAdapter = new VisualizationAdapter(prop.getProperty("visualization_path"));

        vizAdapter.clearIterations();

        SimpleWindUpdate windUpdate = new SimpleWindUpdate();
        windUpdate.setup("testowy-v8-d0-1000-velocity", obstacleLoader);

        Mapping mapping = new Mapping(2,1,
                Arrays.asList(List.of(true), List.of(false))
        );

        RefactoredCell.setMapping(mapping);
        Board board = new Board(150, 150, 50, mapping);

        int iterNumber = 200;

        placeWall(board);

        saveConcentration(board.cells, vizAdapter.getPollutionDataPath(0));

        System.out.println(0);
        for (int i = 0; i < iterNumber; i++){
            board.updateWithFunction(windUpdate);
            saveConcentration(board.cells, vizAdapter.getPollutionDataPath(i + 1));
            System.out.println(i + 1);

            if (i % 5 == 4){
                placeWall(board);
            }
        }

        vizAdapter.generateConfig(iterNumber + 1);
        obstacleLoader.saveInDataFile(vizAdapter.getBuildingDataPath());

        /*
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