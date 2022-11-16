package pl.agh.diffusion_project;

import pl.agh.diffusion_project.cells.RefactoredCell;

public abstract class CallableUpdate {
    static protected final int [][] neighbors = {
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
    static protected final float [] neighborsCoefficient = {
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

    public static int width = 0;
    public static int length = 0;
    public static int height = 0;

    void setup(int width, int length, int height){}

    public abstract void update(RefactoredCell[][][] cells, Integer x, Integer y, Integer z);
}
