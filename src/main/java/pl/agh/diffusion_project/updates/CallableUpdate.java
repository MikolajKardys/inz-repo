/**
 * @authors ${Radosław Bany}; ${Mikołaj Kardyś}
 */

package pl.agh.diffusion_project.updates;


import java.util.Map;

public abstract class CallableUpdate {
    public abstract boolean allowParallelUpdate();

    public void update(Map<String, float[][][]> data, Integer x, Integer y, Integer z){}
}
