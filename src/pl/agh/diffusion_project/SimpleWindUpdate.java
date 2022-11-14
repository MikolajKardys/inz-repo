package pl.agh.diffusion_project;

import pl.agh.diffusion_project.cells.RefactoredCell;
import pl.agh.diffusion_project.pollution.Pollution;

import java.sql.Ref;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleWindUpdate implements WindUpdate{

    @Override
    public Pollution update(RefactoredCell[][][] cells, Integer x, Integer y, Integer z, Integer time) {
        Pollution newPollution = new Pollution(cells[x][y][z].getFullCurrentPollution());
//        List<Pair<Cell, Pair<Boolean, Integer>>> windNeighbors = cells[x][y][z].getWindNeighbors();
//
//
//        windNeighbors = windNeighbors.stream().filter(i -> time % i.getValue1().getValue1() == 0)
//                .collect(Collectors.toList());
//
//        for (Pair<Cell, Pair<Boolean, Integer>> i : windNeighbors){
//            if (!i.getValue1().getValue0()) {
////                if (newConcentration != 0.0f)
////                    System.out.println("set 0 from "+newConcentration);
//                newPollution = new Pollution();
//                break;
//            }
//        }
//        for (Pair<Cell, Pair<Boolean, Integer>> i : windNeighbors){
//            if (i.getValue1().getValue0()) {
//                List<Pair<Cell, Pair<Boolean, Integer>>> neighborWindNeighbors = i.getValue0().getWindNeighbors();
//                Long count = neighborWindNeighbors.stream().filter(j -> time % j.getValue1().getValue1() == 0)
//                        .filter(j -> !j.getValue1().getValue0()).count();
////                if (i.getValue0().getConcentration() / count != 0.0f)
////                    System.out.println("add "+i.getValue0().getConcentration() / count+" to " + newConcentration);
//                newPollution.mod(i.getValue0().getPollution(), (1.0f / (float) count));// += i.getValue0().getConcentration() / count;
//            }
//        }

        return newPollution;
    }
}
