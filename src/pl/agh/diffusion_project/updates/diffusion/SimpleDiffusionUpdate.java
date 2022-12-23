package pl.agh.diffusion_project.updates.diffusion;

import org.json.simple.JSONObject;
import pl.agh.diffusion_project.infrastructure.CityHolder;
import pl.agh.diffusion_project.updates.CallableUpdate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SimpleDiffusionUpdate extends CallableUpdate {
    static private final int [][] neighbors = {
            { 1,  1,  1},
            { 1,  1, -1},
            { 1, -1,  1},
            { 1, -1, -1},
            {-1,  1,  1},
            {-1,  1, -1},
            {-1, -1,  1},
            {-1, -1, -1},
            { 0,  1,  1},
            { 0,  1, -1},
            { 0, -1,  1},
            { 0, -1, -1},
            { 1,  0,  1},
            { 1,  0, -1},
            {-1,  0,  1},
            {-1,  0, -1},
            { 1,  1,  0},
            { 1, -1,  0},
            {-1,  1,  0},
            {-1, -1,  0},
            { 0,  0,  1},
            { 0,  0, -1},
            { 0,  1,  0},
            { 0, -1,  0},
            { 1,  0,  0},
            {-1,  0,  0},

    };
    static private final float [] neighborsCoefficient = {
            0.577350f,
            0.577350f,
            0.577350f,
            0.577350f,
            0.577350f,
            0.577350f,
            0.577350f,
            0.577350f,
            0.7071f,
            0.7071f,
            0.7071f,
            0.7071f,
            0.7071f,
            0.7071f,
            0.7071f,
            0.7071f,
            0.7071f,
            0.7071f,
            0.7071f,
            0.7071f,
            1.0f,
            1.0f,
            1.0f,
            1.0f,
            1.0f,
            1.0f
    };

    Float sum = 0.0f;

    private float [][][] coefficients;

    public static float BIG_CONST = 1f;

    @Override
    public boolean allowParallelUpdate() {
        return true;
    }

    public void setup(int width, int length, int height){

        Mapping mapping = new Mapping(2,1,
                Arrays.asList(List.of(true), List.of(false))
        );

        RefactoredCell [][][] cells = new RefactoredCell[width][length][height];
        RefactoredCell.setMapping(mapping);
        for (int i = 0; i < width; i++){
            for (int j = 0; j < length; j++){
                for (int k = 0; k < height; k++){
                    if (!CityHolder.getBuildingsInstance().isBuilding(i, j, k))
                        cells[i][j][k] = new RefactoredCell(0, new Pollution(mapping.getCellTypeCount()));
                    else
                        cells[i][j][k] = new RefactoredCell(1, new Pollution(mapping.getCellTypeCount()));
                }
            }
        }

        coefficients = new float[width][length][height];

        for (int i = 0; i < 26; i++) {
            neighborsCoefficient[i] *= BIG_CONST;
            sum+=neighborsCoefficient[i];
        }

        for (int x = 0; x < width; x++)
            for (int y = 0; y < length; y++)
                for (int z = 0; z < height; z++) {
                    RefactoredCell cell = cells[x][y][z];
                    List<Float> coefficientSum = new ArrayList(mapping.getCellTypeSize(cells[x][y][z].getCellType()));
                    for (int i = 0; i < mapping.getCellTypeSize(cell.getCellType()); i++) {
                        coefficientSum.add(0.0f);
                    }
                    for (int i = 0; i < 26; i++) {
                        int tx = x+neighbors[i][0];
                        int ty = y+neighbors[i][2];
                        int tz = z+neighbors[i][1];
                        if (tx>=0 && tx<width)
                            if (ty>=0 && ty<length)
                                if (tz>=0 && tz<height) {
                                    RefactoredCell neighborCell = cells[tx][ty][tz];
                                    for (Integer type : mapping.getIndexMatrix(cell.getCellType()))
                                        if (mapping.getContainsMatrix(neighborCell.getCellType()).get(type))
                                            coefficientSum.set(type, coefficientSum.get(type) + neighborsCoefficient[i]);
                                }
                    }
                    for (int i = 0; i < mapping.getCellTypeSize(cell.getCellType()); i++) {
                        coefficientSum.set(i, sum/coefficientSum.get(i));
                    }

                    if (!coefficientSum.isEmpty())
                        coefficients[x][y][z] = coefficientSum.get(0);
                    else
                        coefficients[x][y][z] = -1.0f;


                    if (!mapping.getContainsMatrix(cell.getCellType()).get(0)){
                        coefficients[x][y][z] = -2.0f;
                    }
                }
    }

    @Override
    public void update(Map<String, float[][][]> data, Integer x, Integer y, Integer z) {
        float [][][] oldCells = data.get("pollution");
        float [][][] newCells = data.get("new-pollution");

        int width = newCells.length;
        int length = newCells[0].length;
        int height = newCells[0][0].length;

        newCells[x][y][z] = oldCells[x][y][z] * (1 - BIG_CONST);

        if (coefficients[x][y][z] != -2){
            for (int i = 0; i < 26; i++) {
                int tx = x+neighbors[i][0];
                int ty = y+neighbors[i][2];
                int tz = z+neighbors[i][1];
                if (tx>=0 && tx<width)
                    if (ty>=0 && ty<length)
                        if (tz>=0 && tz<height) {
                            if (coefficients[tx][ty][tz] >= 0)
                                newCells[x][y][z] += oldCells[tx][ty][tz] * neighborsCoefficient[i]/(sum) * coefficients[tx][ty][tz] * BIG_CONST;
                        }
            }
        }
    }
}
