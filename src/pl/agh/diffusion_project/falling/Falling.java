package pl.agh.diffusion_project.falling;

import pl.agh.diffusion_project.obstacles.ObstaclesLoader;

public class Falling {
    private final int dx, dy, dz;
    private final ObstaclesLoader obstaclesLoader;

    public Falling(ObstaclesLoader obstaclesLoader, int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.obstaclesLoader = obstaclesLoader;
    }

    public void fallPollutions(float[][][] oldPollutions, float[][][] newPollutions, float factor) {
        for (int i = 0; i < dx; i++)
            for (int j = 0; j < dy; j++)
                for (int k = 1; k < dz; k++)
                    if(!obstaclesLoader.isBuilding(i, j, k-1)) {
                        newPollutions[i][j][k-1] += factor * oldPollutions[i][j][k];
                        oldPollutions[i][j][k] -= factor * oldPollutions[i][j][k];
                    }
    }
}
