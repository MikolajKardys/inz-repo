package pl.agh.diffusion_project.obstacles.isolator;

import pl.agh.diffusion_project.obstacles.ObstacleTypes;

public class SimpleIsolator extends Isolator {
    public SimpleIsolator(int x1, int x2, int y1, int y2, int z1, int z2) {
        super(x1, x2, y1, y2, z1, z2);
    }

    @Override
    public boolean inIsolator(int x, int y, int z) {
        return x >= x1 && x < x2 &&
                y >= y1 && y < y2 &&
                z >= z1 && z < z2;
    }

    @Override
    public float coefficient(int x, int y, int z) {
        return 0.1f;
    }

    public float coefficient() {
        return 0.1f;
    }

    @Override
    protected int getType() {
        return ObstacleTypes.SIMPLE_ISOLATOR.ordinal();
    }

}
