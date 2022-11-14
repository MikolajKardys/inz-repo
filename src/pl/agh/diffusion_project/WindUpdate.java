package pl.agh.diffusion_project;

import pl.agh.diffusion_project.cells.Cell;
import pl.agh.diffusion_project.cells.RefactoredCell;
import pl.agh.diffusion_project.pollution.Pollution;

public interface WindUpdate  {
    Pollution update(RefactoredCell[][][] cells, Integer x, Integer y, Integer z, Integer time);
}
