package pl.agh.diffusion_project;

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
    float[][][] neighborsCoefficientSum;
    static List<Float> t2 = Arrays.asList(1.0f);
//    static List<Float> t2 = Arrays.asList(1.0f,1.0f,1.0f,1.0f,1.0f,1.0f);

    public void setup(int width, int length, int height){
        neighborsCoefficientSum = new float[width][length][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < length; y++)
                for (int z = 0; z < height; z++) {
                    neighborsCoefficientSum[x][y][z]=0.0f;
                    for (int i = 0; i < 26; i++) {
                        int tx = x+neighbors[i][0];
                        int ty = y+neighbors[i][2];
                        int tz = z+neighbors[i][1];
                        if (tx>=0 && tx<width)
                            if (ty>=0 && ty<length)
                                if (tz>=0 && tz<height) {
                                    if (x == 2 && y == 3 & z == 3)
                                        System.out.println(neighborsCoefficient[i]);
                                    neighborsCoefficientSum[x][y][z] += neighborsCoefficient[i];//TODO dla każdego typu odzielnie
                                }
                    }
                }
    }

    @Override
    public void update(RefactoredCell[][][] cells, Integer x, Integer y, Integer z) {//TODO padding
        if (x==2&&y==3&z==3)
            System.out.println(neighborsCoefficientSum[x][y][z]);
        for (int i = 0; i < 26; i++) {
            int tx = x+neighbors[i][0];
            int ty = y+neighbors[i][2];
            int tz = z+neighbors[i][1];
            //TODO czy przy ścianie ciągle ma oddawać wszystkie?
            //TODO zachować nie oddane zanieczyszczenia
            if (tx>=0 && tx<width)
                if (ty>=0 && ty<length)
                    if (tz>=0 && tz<height) {
                        if (x==2&&y==3&z==3)
                        System.out.println(neighborsCoefficient[i]/neighborsCoefficientSum[x][y][z]);
                        cells[x][y][z].modPollution(cells[tx][ty][tz], neighborsCoefficient[i]/neighborsCoefficientSum[tx][ty][tz]);
                    }
        }
    }
}
