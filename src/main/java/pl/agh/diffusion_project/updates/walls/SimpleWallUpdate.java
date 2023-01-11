/**
 * @author ${Mikołaj Kardyś}
 */

package pl.agh.diffusion_project.updates.walls;

import org.json.simple.JSONArray;
import pl.agh.diffusion_project.updates.CallableUpdate;

import java.util.*;

public class SimpleWallUpdate extends CallableUpdate {
    private int xBorder;
    private int yBorder;
    private String direction;

    private final float[] wallCoeffs = new float[4];

    public static float POLLUTION_FACTOR;

    private float getWallCoeff(int x, int y, int ignore){
        float coeff = 0f;

        if (x == 0)
            coeff = Math.max(wallCoeffs[0] * POLLUTION_FACTOR, coeff);
        if (y == 0)
            coeff = Math.max(wallCoeffs[1] * POLLUTION_FACTOR, coeff);
        else if (x == xBorder)
            coeff = Math.max(wallCoeffs[2] * POLLUTION_FACTOR, coeff);
        if (y == yBorder){
            coeff = Math.max(wallCoeffs[3] * POLLUTION_FACTOR, coeff);
        }

        return coeff;
    }

    @Override
    public boolean allowParallelUpdate() {
        return true;
    }

    public void setup(int width, int length, JSONArray walls){
        xBorder = width - 1;
        yBorder = length - 1;

        List<String> directions = List.of(new String[]{"W", "N", "E", "S"});

        for (Object wall : walls){
            int directionIndex = directions.indexOf((String) wall);
            wallCoeffs[directionIndex] = 1f;
        }
    }

    @Override
    public void update(Map<String, float[][][]> data, Integer x, Integer y, Integer z) {
        float wallCoeff = getWallCoeff(x, y, z);
        if (wallCoeff > 0){
            data.get("new-pollution")[x][y][z] = getWallCoeff(x, y, z);
        }
    }
}
