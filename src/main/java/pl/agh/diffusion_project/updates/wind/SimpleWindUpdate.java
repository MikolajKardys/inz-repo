/**
 * @authors ${Dominik Sulik}; ${Mikołaj Kardyś}
 */

package pl.agh.diffusion_project.updates.wind;

import pl.agh.diffusion_project.updates.CallableUpdate;

import java.io.IOException;
import java.util.Map;

public class SimpleWindUpdate extends CallableUpdate {
    private Wind wind;

    @Override
    public boolean allowParallelUpdate() {
        return false;
    }

    public void setup(String windFileName) {
        try {
            WindLoader windLoader = WindLoader.loadWindFromFile(windFileName);

            this.wind = new Wind(windLoader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Map<String, float[][][]> data, Integer x, Integer y, Integer z) {
        wind.updateWind(
                data.get("pollution"),
                data.get("new-pollution"),
                data.get("temperature"),
                data.get("new-temperature"),
                x, y, z
        );
    }
}
