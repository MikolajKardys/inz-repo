package pl.agh.diffusion_project;

import pl.agh.diffusion_project.cells.Mapping;
import pl.agh.diffusion_project.cells.RefactoredCell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO: calculate neighbours here, not get from cell

public class SimpleUpdate extends CallableUpdate {
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
    List<Float>[][][] neighborsCoefficientSum;
    Float sum = 0.0f;

    public void setup(RefactoredCell[][][] cells, int width, int length, int height, Mapping mapping){
        for (int i = 0; i < 26; i++) {
            sum+=neighborsCoefficient[i];
        }

        neighborsCoefficientSum = new ArrayList[width][length][height];
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
                    neighborsCoefficientSum[x][y][z] = coefficientSum;
//                    System.out.println(x+" "+y+" "+z+" "+coefficientSum);
                }
        System.out.println(sum);
    }

    @Override
    public void update(RefactoredCell[][][] cells, Integer x, Integer y, Integer z) {//TODO padding
        for (int i = 0; i < 26; i++) {
            int tx = x+neighbors[i][0];
            int ty = y+neighbors[i][2];
            int tz = z+neighbors[i][1];
            //TODO czy przy ścianie ciągle ma oddawać wszystkie?
            //TODO zachować nie oddane zanieczyszczenia
            if (tx>=0 && tx<width)
                if (ty>=0 && ty<length)
                    if (tz>=0 && tz<height) {
                        cells[x][y][z].modPollution(cells[tx][ty][tz], neighborsCoefficientSum[tx][ty][tz], neighborsCoefficient[i]/(sum));
                    }
        }
    }
}
