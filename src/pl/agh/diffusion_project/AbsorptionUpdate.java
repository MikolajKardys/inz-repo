package pl.agh.diffusion_project;

import pl.agh.diffusion_project.cells.Leaf;
import pl.agh.diffusion_project.pollution.Pollution;

public interface AbsorptionUpdate {
    public Pollution update(Leaf leaf);
}
