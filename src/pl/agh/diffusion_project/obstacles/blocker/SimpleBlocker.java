package pl.agh.diffusion_project.obstacles.blocker;

import pl.agh.diffusion_project.obstacles.ObstacleTypes;

public class SimpleBlocker extends Blocker {
    public SimpleBlocker(int x1, int x2, int y1, int y2, int z1, int z2) {
        super(x1, x2, y1, y2, z1, z1);
    }

    public boolean isBlocked(int x, int y, int z){
        return x >= x1 && x < x2 &&
                y >= y1 && y < y2 &&
                z >= z1 && z < z2;
    }


    @Override
    public int getType() {
        return ObstacleTypes.SIMPLE_BLOCKER.ordinal();
    }
}
