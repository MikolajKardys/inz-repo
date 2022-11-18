package pl.agh.diffusion_project;

import org.javatuples.Quartet;
import pl.agh.diffusion_project.cells.*;
import pl.agh.diffusion_project.pollution.Pollution;


import java.util.List;

public class Board {
    private final Integer width;
    private final Integer length;
    private final Integer height;
    private final RefactoredCell[][][] cells;
    private final Mapping mapping;
    private Integer time = 0;

    public Board(int width, int length, int height, List<Quartet<Integer, Integer, Integer, Pollution>> initialCondition, Mapping mapping){
        this.width = width;
        this.length = length;
        this.height = height;
        this.mapping = mapping;
        this.cells = new RefactoredCell[width][length][height];

        Pollution pollution = new Pollution(mapping.getPollutionTypeCount());
        for(int i=0; i<width; i++) {
            for (int j = 0; j < length; j++) {
                for (int k = 0; k < height; k++) {
                    if (i<2) {
                        cells[i][j][k] = new RefactoredCell(0, pollution);
                    }
                    else {
                        cells[i][j][k] = new RefactoredCell(1, pollution);
                    }
                }
            }
        }

        int x,y,z;
        for(Quartet<Integer, Integer, Integer, Pollution> cond : initialCondition) {
            x = cond.getValue0();
            y = cond.getValue1();
            z = cond.getValue2();
            if(x >= 0 && y >= 0 && z >= 0 && x < width && y < length && z < height){
                cells[x][y][z].modPollution(cond.getValue3());
                cells[x][y][z].update();
                System.out.println(cells[x][y][z]);
                System.out.println(cells[x][y][z].getCellType());
            }
        }
    }

    public void setupDiffusion(CallableUpdate callableUpdate) {
        callableUpdate.setup(cells, width, height, length, mapping);
    }

    public void updateWithFunction(CallableUpdate callableUpdate) {
        CallableUpdate.width = width;
        CallableUpdate.length = length;
        CallableUpdate.height = height;
        for(int i=0; i<width; i++)
            for(int j=0; j<length; j++)
                for(int k=0; k<height; k++)
                    callableUpdate.update(cells, i, j, k);

        for(int i=0; i<width; i++)
            for(int j=0; j<length; j++)
                for(int k=0; k<height; k++)
                    cells[i][j][k].update();
    }

    public void addWind(Wind wind){
        wind.wind(cells , width, length, height);
    }

    public void updateWindWithFunction(WindUpdate windUpdate) {
        for(int i=0; i<width; i++)
            for(int j=0; j<length; j++)
                for(int k=0; k<height; k++)
                    windUpdate.update(cells, i, j, k, time);

        for(int i=0; i<width; i++)
            for(int j=0; j<length; j++)
                for(int k=0; k<height; k++)
                    cells[i][j][k].update();
        time++;
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

    public Integer getWidth() {
        return width;
    }

    public Integer getLength() {
        return length;
    }

    public Integer getHeight() {
        return height;
    }
}
