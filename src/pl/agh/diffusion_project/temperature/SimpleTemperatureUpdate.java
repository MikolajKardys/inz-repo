package pl.agh.diffusion_project.temperature;

import pl.agh.diffusion_project.CallableUpdate;
import pl.agh.diffusion_project.cells.RefactoredCell;
import pl.agh.diffusion_project.pollution.Pollution;

public class SimpleTemperatureUpdate extends CallableUpdate {
    @Override
    public void update(RefactoredCell[][][] cells, Integer x, Integer y, Integer z) {
        if(z < cells[0][0].length-1) {
            cells[x][y][z].newTemp += cells[x][y][z].temp;
            float tempDiff = cells[x][y][z].temp - cells[x][y][z + 1].temp;
            if (Math.random() < tempDiff) {
                cells[x][y][z + 1].newTemp += tempDiff;
                cells[x][y][z].newTemp -= tempDiff;

                Pollution myPollution = cells[x][y][z].getFullCurrentPollution();
                cells[x][y][z + 1].modPollution(myPollution);
            }
        }
    }
}
