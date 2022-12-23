package pl.agh.diffusion_project.updates;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pl.agh.diffusion_project.updates.diffusion.SimpleDiffusionUpdate;
import pl.agh.diffusion_project.updates.emitters.SimpleEmitterUpdate;
import pl.agh.diffusion_project.updates.walls.SimpleWallUpdate;
import pl.agh.diffusion_project.updates.wind.SimpleWindUpdate;

public class SimpleUpdatesHolder {
    private final SimpleDiffusionUpdate diffusionUpdate;
    private final SimpleEmitterUpdate emitterUpdate;
    private final SimpleWallUpdate wallUpdate;
    private final SimpleWindUpdate windUpdate;

    private SimpleUpdatesHolder(SimpleDiffusionUpdate diffusionUpdate, SimpleEmitterUpdate emitterUpdate, SimpleWallUpdate wallUpdate, SimpleWindUpdate windUpdate) {
        this.diffusionUpdate = diffusionUpdate;
        this.emitterUpdate = emitterUpdate;
        this.wallUpdate = wallUpdate;
        this.windUpdate = windUpdate;
    }

    public static SimpleUpdatesHolder loadUpdatesFromJSON(JSONObject object){
        int width = ((Long)((JSONObject)object.get("dimensions")).get("width")).intValue();
        int length = ((Long)((JSONObject)object.get("dimensions")).get("length")).intValue();
        int height = ((Long)((JSONObject)object.get("dimensions")).get("height")).intValue();

        SimpleDiffusionUpdate diffusionUpdate = new SimpleDiffusionUpdate();
        diffusionUpdate.setup(width, length, height);

        SimpleEmitterUpdate emitterUpdate = new SimpleEmitterUpdate();
        String emitterFile = (String)((JSONObject) object.get("data-files")).get("emitters-file");
        emitterUpdate.setup(emitterFile);

        SimpleWallUpdate wallUpdate = new SimpleWallUpdate();
        JSONArray array = ((JSONArray)((JSONObject) object.get("updates")).get("walls"));
        wallUpdate.setup(width, length, array);

        SimpleWindUpdate windUpdate = new SimpleWindUpdate();
        String windFile = (String)((JSONObject) object.get("data-files")).get("wind-file");
        windUpdate.setup(windFile);

        return new SimpleUpdatesHolder(diffusionUpdate, emitterUpdate, wallUpdate, windUpdate);
    }

    public SimpleDiffusionUpdate getDiffusionUpdate() {
        return diffusionUpdate;
    }

    public SimpleEmitterUpdate getEmitterUpdate() {
        return emitterUpdate;
    }

    public SimpleWallUpdate getWallUpdate() {
        return wallUpdate;
    }

    public SimpleWindUpdate getWindUpdate() {
        return windUpdate;
    }
}
