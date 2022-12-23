package pl.agh.diffusion_project.updates.emitters;

import pl.agh.diffusion_project.infrastructure.CityHolder;

public class Emitters {
    public static float TEMPERATURE_FACTOR = 1.0f;

    private final float [][] pollutants;

    public Emitters(EmittersLoader emittersLoader) {
        int blockSize = CityHolder.getBuildingsInstance().getBlockSize();

        int dx = emittersLoader.getDx();
        int dy = emittersLoader.getDy();
        this.pollutants = new float[dx][dy];

        for (int i=0; i<dx; i++)
            for (int j=0; j<dy; j++)
                pollutants[i][j] = emittersLoader.getEmitterValue(i,j)/255F / (blockSize * blockSize);
    }

    public void updateEmitters(float[][][] newCells, float[][][] newTemperature, int x, int y, int z) {
        if(z == 0) {
            newCells[x][y][z] += SimpleEmitterUpdate.POLLUTION_FACTOR * pollutants[x][y];

            if(pollutants[x][y] > 0)
                newTemperature[x][y][z] += TEMPERATURE_FACTOR * pollutants[x][y];
        }
    }
}
