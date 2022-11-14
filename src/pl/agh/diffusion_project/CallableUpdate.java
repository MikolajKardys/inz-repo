package pl.agh.diffusion_project;

import pl.agh.diffusion_project.cells.RefactoredCell;

public abstract class CallableUpdate {
    public static int width = 0;
    public static int length = 0;
    public static int height = 0;

    void setup(int width, int length, int height){}

    void update(RefactoredCell[][][] cells, Integer x, Integer y, Integer z){}
}
