package pl.agh.diffusion_project.obstacles.isolator;

import pl.agh.diffusion_project.obstacles.Obstacle;

public abstract class Isolator extends Obstacle {
    protected Isolator(int x1, int x2, int y1, int y2, int z1, int z2) {
        super(x1, x2, y1, y2, z1, z2);
    }

    abstract public boolean inIsolator(int x, int y, int z);
    abstract public float coefficient(int x, int y, int z);
}
