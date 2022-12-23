package pl.agh.diffusion_project.updates.emitters;

import pl.agh.diffusion_project.updates.CallableUpdate;

import java.io.IOException;
import java.util.Map;

public class SimpleEmitterUpdate extends CallableUpdate {
    private Emitters emitters;
    public static float POLLUTION_FACTOR;

    @Override
    public boolean allowParallelUpdate() {
        return false;
    }

    public void setup(String emittersFileName) {
        try {
            EmittersLoader emittersLoader = EmittersLoader.loadEmittersFromBitmap(emittersFileName);
            this.emitters = new Emitters(emittersLoader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Map<String, float[][][]> data, Integer x, Integer y, Integer z) {
        emitters.updateEmitters(data.get("new-pollution"), data.get("new-temperature"), x, y, z);
    }
}