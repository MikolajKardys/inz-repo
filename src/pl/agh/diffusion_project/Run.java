
package pl.agh.diffusion_project;

import pl.agh.diffusion_project.adapters.VisualizationAdapter;
import pl.agh.diffusion_project.cells.Mapping;
import pl.agh.diffusion_project.obstacles.ObstaclesLoader;
import pl.agh.diffusion_project.pollution.Pollution;

import java.io.*;
import java.util.*;

public class Run {
    public static void saveConcentration(Pollution [][][] p, String fileName) {
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
                        int bits = Float.floatToIntBits(p[k][i][j].getFastAll().get(0));

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

    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("resources/config.properties"));

        ObstaclesLoader obstacleLoader = ObstaclesLoader.loadObstaclesFromBitmap("testowy.bmp", 50);
        VisualizationAdapter vizAdapter = new VisualizationAdapter(prop.getProperty("visualization_path"));

        Mapping mapping = new Mapping(2,1,
                Arrays.asList(List.of(false), List.of(false))
        );

        Configuration configuration = new Configuration(150, 150, 50, mapping);

        SimpleUpdate simpleUpdate = new SimpleUpdate();
        configuration.setupDiffusion(simpleUpdate);

        for (int i = 0; i < 10; i++)
            configuration.modConcentrationAt(i, 0, 25, new Pollution(List.of(2.0f)));

        int iterationsNum = 100;
        vizAdapter.clearIterations();
        saveConcentration(configuration.getConcentrationMatrix(), vizAdapter.getPollutionDataPath(0));

        for(int i=0; i<iterationsNum; i++) {
            System.out.println("\n" + (i+1) + " " + iterationsNum);
            configuration.iterateDiffusion(simpleUpdate);
            saveConcentration(configuration.getConcentrationMatrix(), vizAdapter.getPollutionDataPath(i + 1));
        }

        obstacleLoader.saveInDataFile(vizAdapter.getBuildingDataPath());
        vizAdapter.generateConfig(iterationsNum + 1, true);
    }
}
