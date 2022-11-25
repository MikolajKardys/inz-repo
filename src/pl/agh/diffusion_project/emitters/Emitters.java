package pl.agh.diffusion_project.emitters;

import pl.agh.diffusion_project.cells.RefactoredCell;
import pl.agh.diffusion_project.pollution.Pollution;

import java.util.List;

public class Emitters {
    private final float [][] pollutants;

    public Emitters(EmittersLoader emittersLoader, float factor) {
        int dx = emittersLoader.getDx();
        int dy = emittersLoader.getDy();
        this.pollutants = new float[dx][dy];

        for (int i=0; i<dx; i++)
            for (int j=0; j<dy; j++)
                pollutants[i][j] = factor*(emittersLoader.getEmitterValue(i,j)/255F);
    }

    public void updateEmitters(RefactoredCell[][][] cells, int x, int y, int z) {
        if(z == 0) {
            Pollution emittedPollution = new Pollution(List.of(pollutants[x][y]));
            cells[x][y][z].modPollution(emittedPollution);
        }
    }
}
