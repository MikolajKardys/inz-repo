package pl.agh.diffusion_project.obstacles.blocker;

import pl.agh.diffusion_project.obstacles.Obstacle;

public abstract class  Blocker extends Obstacle {
    protected Blocker(int x1, int x2, int y1, int y2, int z1, int z2) {
        super(x1, x2, y1, y2, z1, z2);
    }

    abstract public boolean isBlocked(int x, int y, int z);
}
