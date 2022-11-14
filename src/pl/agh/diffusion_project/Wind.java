package pl.agh.diffusion_project;

import pl.agh.diffusion_project.cells.Cell;
import pl.agh.diffusion_project.cells.RefactoredCell;

public interface Wind {
    void wind(RefactoredCell[][][] cell, int width, int length, int height);
}
