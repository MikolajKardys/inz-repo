package pl.agh.diffusion_project.convection;

import pl.agh.diffusion_project.obstacles.ObstaclesLoader;

public class Convection {
    private final int dx, dy, dz;
    private final ObstaclesLoader obstaclesLoader;
    private final float [][][] temperatureDiffs;
    private final float [][][] incrementor;

    private final float CONVECTION_TEMPERATURE = 0.1F;
    private final float CONVECTION_FACTOR = 0.25F;

    public Convection(TemperatureLoader temperatureLoader, ObstaclesLoader obstaclesLoader) {
        this.dx = temperatureLoader.getDx();
        this.dy = temperatureLoader.getDy();
        this.dz = temperatureLoader.getDz();
        this.obstaclesLoader = obstaclesLoader;

        this.incrementor = new float[dx][dy][dz];
        this.temperatureDiffs = new float[dx][dy][dz];
        for (int i = 0; i < dx; i++)
            for (int j = 0; j < dy; j++)
                for (int k = 0; k < dz - 1; k++)
                    temperatureDiffs[i][j][k] = temperatureLoader.getTemperature(i, j, k) - temperatureLoader.getTemperature(i, j, k + 1);
    }

    //TODO: fix k+1 == dz case
    public void convectPollutions(float[][][] oldPollutions, float[][][] newPollutions) {
        for (int i = 0; i < dx; i++)
            for (int j = 0; j < dy; j++)
                for (int k = 0; k < dz; k++)
                    if(!obstaclesLoader.isBuilding(i, j, k)) {
                        incrementor[i][j][k] += temperatureDiffs[i][j][k];
                        if(incrementor[i][j][k] >= CONVECTION_TEMPERATURE) {
                            newPollutions[i][j][k+1] += CONVECTION_FACTOR * oldPollutions[i][j][k];
                            oldPollutions[i][j][k] -= CONVECTION_FACTOR * oldPollutions[i][j][k];
                        }
                    }

        for (int i = 0; i < dx; i++)
            for (int j = 0; j < dy; j++)
                for (int k = 0; k < dz; k++)
                    if(!obstaclesLoader.isBuilding(i, j, k)) {
                        oldPollutions[i][j][k] += newPollutions[i][j][k];
                        newPollutions[i][j][k] = 0F;
                        incrementor[i][j][k] -= CONVECTION_TEMPERATURE;
                    }
    }
}
