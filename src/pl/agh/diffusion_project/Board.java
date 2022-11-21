package pl.agh.diffusion_project;

import pl.agh.diffusion_project.cells.*;
import pl.agh.diffusion_project.obstacles.Obstacle;
import pl.agh.diffusion_project.obstacles.ObstaclesLoader;
import pl.agh.diffusion_project.pollution.Pollution;


public class Board {
    private final Integer width;
    private final Integer length;
    private final Integer height;
    private final RefactoredCell[][][] cells;
    private final Mapping mapping;

    public Board(int width, int length, int height, Mapping mapping){
        this.width = width;
        this.length = length;
        this.height = height;
        this.mapping = mapping;
        this.cells = new RefactoredCell[width][length][height];

        Pollution pollution = new Pollution(mapping.getPollutionTypeCount());
        for(int i=0; i<width; i++) {
            for (int j = 0; j < length; j++) {
                for (int k = 0; k < height; k++) {
                    cells[i][j][k] = new RefactoredCell(0, pollution);
                }
            }
        }
    }

    public void addObstacles(ObstaclesLoader obstaclesLoader) {
        Pollution pollution = new Pollution(mapping.getPollutionTypeCount());

        for(int i=0; i<width; i++)
            for(int j=0; j<length; j++)
                for(int k=0; k<height; k++)
                    if (obstaclesLoader.isBuilding(i, j, k))
                        cells[i][j][k] = new RefactoredCell(1, pollution); // BUILDING
    }

    public void setupDiffusion(CallableUpdate callableUpdate) {
        callableUpdate.setup(cells, width, height, length, mapping);
    }

    public void updateWithNewValues() {
        for(int i=0; i<width; i++)
            for(int j=0; j<length; j++)
                for(int k=0; k<height; k++)
                    cells[i][j][k].update();
    }

    public void updateWithFunction(CallableUpdate callableUpdate) {
        CallableUpdate.width = width;
        CallableUpdate.length = length;
        CallableUpdate.height = height;
        for(int i=0; i<width; i++)
            for(int j=0; j<length; j++)
                for(int k=0; k<height; k++)
                    callableUpdate.update(cells, i, j, k);

        updateWithNewValues();
    }

    public Pollution[][][] get3DConcentrationMatrix() {
        Pollution[][][] newPollution = new Pollution[width][length][height];

        for(int i=0; i<width; i++)
            for(int j=0; j<length; j++)
                for(int k=0; k<height; k++){
                    newPollution[i][j][k] = cells[i][j][k].getFullCurrentPollution();
                }

        return newPollution;
    }

    public void modConcentrationAt(int x, int y, int z, Pollution p){
        this.cells[x][y][z].modPollution(p);
        this.cells[x][y][z].update();
    }
}
