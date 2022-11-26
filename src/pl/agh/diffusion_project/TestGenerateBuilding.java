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

        //SimpleUpdate diffusionUpdate = new SimpleUpdate();
        //diffusionUpdate.setup(board.cells, 150, 150, 50, mapping);

        int iterNumber = 300;

        placeWall(board);

        saveConcentration(board.cells, vizAdapter.getPollutionDataPath(0));

        System.out.println(0);
        for (int i = 0; i < iterNumber; i++){
            board.updateWithFunction(windUpdate);
            //board.updateWithFunction(diffusionUpdate);

            saveConcentration(board.cells, vizAdapter.getPollutionDataPath(i + 1));
            System.out.println(i + 1);

            if (i % 5 == 4){
                placeWall(board);
            }
        }

        vizAdapter.generateConfig(iterNumber + 1);
        obstacleLoader.saveInDataFile(vizAdapter.getBuildingDataPath());
    }
}