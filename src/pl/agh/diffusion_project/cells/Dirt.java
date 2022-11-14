package pl.agh.diffusion_project.cells;

import pl.agh.diffusion_project.cells.Cell;
import pl.agh.diffusion_project.pollution.Pollution;

public class Dirt extends Cell {
    public Dirt(Pollution pollution) {
        super(pollution);
        block();
    }
}
