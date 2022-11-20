
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
        Mapping mapping = new Mapping(2,2,
                new ArrayList<>(
                        Arrays.asList(
                                new ArrayList<>(Arrays.asList(true, true, false, true, false, true)),
                                new ArrayList<>(Arrays.asList(true,false)),
                                new ArrayList<>(Arrays.asList(true,true))
                        )));

        List<Quartet<Integer, Integer, Integer, Pollution>> initialCondition = new ArrayList<>();
        initialCondition.add(new Quartet<>(2, 3, 3, new Pollution(Arrays.asList(1.0f,1.0f,1.0f,1.0f,1.0f,1.0f))));
        initialCondition.add(new Quartet<>(2, 3, 3, new Pollution(Arrays.asList(1.0f, 2.0f))));
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
    }
}
