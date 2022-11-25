package pl.agh.diffusion_project.emitters;

import pl.agh.diffusion_project.CallableUpdate;
import pl.agh.diffusion_project.cells.RefactoredCell;

import java.io.IOException;

public class SimpleEmitterUpdate extends CallableUpdate {
    private Emitters emitters;

    public void setup(String emittersFileName, float emitFactor) {
        try {
            EmittersLoader emittersLoader = EmittersLoader.loadEmittersFromBitmap(emittersFileName);
            this.emitters = new Emitters(emittersLoader, emitFactor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(RefactoredCell[][][] cells, Integer x, Integer y, Integer z) {
        emitters.updateEmitters(cells, x, y, z);
    }
}