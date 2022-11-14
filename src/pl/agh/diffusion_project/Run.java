
package pl.agh.diffusion_project;

import org.javatuples.Quartet;
import pl.agh.diffusion_project.cells.Mapping;
import pl.agh.diffusion_project.cells.RefactoredCell;
import pl.agh.diffusion_project.obstacles.blocker.Blockers;
import pl.agh.diffusion_project.obstacles.absorber.Absorbers;
import pl.agh.diffusion_project.obstacles.isolator.Isolators;
//import pl.agh.diffusion_project.obstacles.absorber.SimpleAbsorber;
import pl.agh.diffusion_project.pollution.Dust;
//import pl.agh.diffusion_project.pollution.Gas;
import pl.agh.diffusion_project.pollution.Pollution;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Run {
    public static void main(String[] args) {
        //Permutations test

        Mapping mapping = new Mapping(1,1,
                new ArrayList<>(
                        Arrays.asList(
//                                new ArrayList<>(Arrays.asList(true, true, false, true, false, true)),
//                                new ArrayList<>(Arrays.asList(true, false, true, true, true, true))
                                new ArrayList<>(Arrays.asList(true))
                        )));

        List<Quartet<Integer, Integer, Integer, Pollution>> initialCondition = new ArrayList<>();
//        initialCondition.add(new Quartet<>(2, 3, 3, new Pollution(Arrays.asList(1.0f,1.0f,1.0f,1.0f,1.0f,1.0f))));
        initialCondition.add(new Quartet<>(2, 3, 3, new Pollution(Arrays.asList(1.0f))));
        Configuration configuration = new Configuration(4, 4, 4, initialCondition, mapping);
        SimpleUpdate simpleUpdate = new SimpleUpdate();
        configuration.setupDiffusion(simpleUpdate);

        int iterationsNum = 5;

        configuration.print2DResults();

        for(int i=0; i<iterationsNum; i++) {
            System.out.println("\n" + (i+1) + " " + iterationsNum);
            configuration.iterateDiffusion(simpleUpdate);
            configuration.saveCellsHistory(null);
            configuration.print2DResults();
        }
        configuration.print3DResults();

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
