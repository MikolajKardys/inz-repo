/**
 * @author ${Mikołaj Kardyś}
 */

package pl.agh.diffusion_project;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Timeline {
    private final SortedMap<Integer, Float> timelineMap = new TreeMap<>();

    public Timeline(JSONArray timelineArray, int iterationNum) {
        for (Object o : timelineArray) {
            int iteration = ((Long)((JSONObject) o).get("iteration")).intValue();
            float concentration = ((Double)((JSONObject) o).get("concentration")).floatValue();

            if (iteration > iterationNum) {
                break;
            } else {
                timelineMap.put(iteration, concentration);
            }
        }

        if (timelineMap.size() == 0){
            timelineMap.put(0, 0f);
        }

        timelineMap.put(0, timelineMap.get(timelineMap.firstKey()));
        timelineMap.put(iterationNum - 1, timelineMap.get(timelineMap.lastKey()));
    }

    public float getTimelineFactor(int iteration){
        int lower = timelineMap.headMap(iteration).size() > 0 ? timelineMap.headMap(iteration).lastKey() : 0;
        int upper = timelineMap.tailMap(iteration).firstKey();

        int dist = upper - lower;

        float emitterFactor = dist > 0 ? (float)(iteration - lower) / (float) dist : 0;

        return (1 - emitterFactor) * timelineMap.get(lower) + emitterFactor *  timelineMap.get(upper);
    }
}
