package pl.agh.diffusion_project.wind;

import pl.agh.diffusion_project.CallableUpdate;
import pl.agh.diffusion_project.cells.Mapping;
import pl.agh.diffusion_project.cells.RefactoredCell;
import pl.agh.diffusion_project.obstacles.ObstaclesLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleWindUpdate extends CallableUpdate {
    private Wind wind;

    public void setup(String windFileName, ObstaclesLoader obstaclesLoader) {
        try {
            WindLoader windLoader = WindLoader.loadWindFromFile(windFileName);

            this.wind = new Wind(windLoader, obstaclesLoader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(RefactoredCell[][][] cells, Integer x, Integer y, Integer z) {
        wind.updateWind(cells, x, y, z);
    }
}
