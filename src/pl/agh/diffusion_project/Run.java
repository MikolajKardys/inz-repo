
package pl.agh.diffusion_project;

import org.javatuples.Quartet;
import pl.agh.diffusion_project.adapters.VisualizationAdapter;
import pl.agh.diffusion_project.cells.Mapping;
import pl.agh.diffusion_project.cells.RefactoredCell;
import pl.agh.diffusion_project.obstacles.ObstaclesLoader;
import pl.agh.diffusion_project.obstacles.blocker.Blockers;
import pl.agh.diffusion_project.obstacles.absorber.Absorbers;
import pl.agh.diffusion_project.obstacles.isolator.Isolators;
//import pl.agh.diffusion_project.obstacles.absorber.SimpleAbsorber;
import pl.agh.diffusion_project.pollution.Dust;
//import pl.agh.diffusion_project.pollution.Gas;
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
                Arrays.asList(List.of(true), List.of(false))
        );

        Configuration configuration = new Configuration(20, 20, 20, mapping);

        SimpleUpdate simpleUpdate = new SimpleUpdate();
        configuration.setupDiffusion(simpleUpdate);

        configuration.setConcentrationAt(10, 10, 10, new Pollution(List.of(1.0f)));

        int iterationsNum = 20;
        vizAdapter.clearIterations();
        saveConcentration(configuration.getConcentrationMatrix(), vizAdapter.getPollutionDataPath(0));

        for(int i=0; i<iterationsNum; i++) {
            System.out.println("\n" + (i+1) + " " + iterationsNum);
            configuration.iterateDiffusion(simpleUpdate);
            saveConcentration(configuration.getConcentrationMatrix(), vizAdapter.getPollutionDataPath(i + 1));
            System.out.println((i + 1));
        }

        vizAdapter.generateConfig(iterationsNum + 1, false);

//        RefactoredCell.setMapping(mapping);
//        RefactoredCell cell1 = new RefactoredCell(0, new Pollution(Arrays.asList(0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f)));
//        RefactoredCell cell2 = new RefactoredCell(1, new Pollution(Arrays.asList(0.5f, 1.5f, 2.5f, 3.5f, 4.5f, 5.5f)));
//        System.out.println(cell1);
//        System.out.println(cell2);
//        System.out.println(cell1.getFullCurrentPollution());
//        System.out.println(cell2.getFullCurrentPollution());


        //Blockers test

//        List<Quartet<Integer, Integer, Integer, Pollution>> initialCondition = new ArrayList<>();
//        initialCondition.add(new Quartet<>(2, 3, 3, new Pollution(Arrays.asList(1.0f,1.0f,1.0f,1.0f))));
//
//        Blockers blockers =new Blockers();
//
//        Isolators isolators = new Isolators();
//
//        Absorbers absorbers = new Absorbers();
//        absorbers.addAbsorber(new SimpleAbsorber(1,3,1,3,1,3,
//                new Gas(Arrays.asList(2.0f, 2.0f)),
//                new Gas(Arrays.asList(1.0f, 2.0f)),
//                new Gas(Arrays.asList(0.2f, 0.2f)),
//                new Dust(Arrays.asList(0.5f, 1.0f)),
//                new Dust(Arrays.asList(1.0f, 1.0f))));
//
//        Configuration configuration = new Configuration(4, 4, 4, initialCondition, blockers, isolators, new NoWind(), absorbers);
//        SimpleUpdate simpleUpdate = new SimpleUpdate();
//        SimpleAbsorptionUpdate absorptionUpdate = new SimpleAbsorptionUpdate();
//
//        int iterationsNum = 200;
//        String fileName = "data\\results" + (Objects.requireNonNull(new File("data").list()).length+1) + ".dat";
//
//        configuration.saveDimensions(fileName, iterationsNum);
//        configuration.saveCellsHistory(fileName);
//        configuration.print2DResults();
//
//        for(int i=0; i<iterationsNum; i++) {
//            System.out.println("\n" + (i+1) + " " + iterationsNum);
//            configuration.iterateDiffusion(simpleUpdate);
//            configuration.iterateAbsorption(absorptionUpdate);
//            configuration.iterateDecay();
//            configuration.saveCellsHistory(fileName);
//            configuration.print2DResults();
//        }
//        configuration.print3DResults();

        // Wind test

//        List<Quartet<Integer, Integer, Integer, Float>> initialCondition = new ArrayList<>();
//        initialCondition.add(new Quartet<>(9, 0, 1, 9.9f));
////        initialCondition.add(new Quartet<>(9, 1, 3, 9.9f));
//
////        Configuration configuration = new Configuration(20, 6, 6, initialCondition, new Blockers(), new Isolators(), new SimpleWind());
//        Configuration configuration = new Configuration(20, 3, 3, initialCondition, new Blockers(), new Isolators(), new SimpleWind());
//
//        SimpleUpdate simpleUpdate = new SimpleUpdate();
//        WindUpdate windUpdate = new SimpleWindUpdate();
//
//        int iterationsNum = 5;
//        String fileName = "data\\results" + (Objects.requireNonNull(new File("data").list()).length+1) + ".dat";
//
//        configuration.saveDimensions(fileName, iterationsNum);
//        configuration.saveCellsHistory(fileName);
//        configuration.print2DResults();
//
//        for(int i=0; i<iterationsNum; i++) {
//            System.out.println("\n" + (i+1) + " " + iterationsNum);
//            configuration.iterate(simpleUpdate, windUpdate);
//            configuration.saveCellsHistory(fileName);
//            configuration.print2DResults();
//        }
//        configuration.print3DResults();
    }
}
