package pl.agh.diffusion_project;

import org.javatuples.Quartet;
import pl.agh.diffusion_project.cells.Mapping;
import pl.agh.diffusion_project.cells.RefactoredCell;
import pl.agh.diffusion_project.obstacles.ObstaclesLoader;
import pl.agh.diffusion_project.obstacles.absorber.Absorber;
import pl.agh.diffusion_project.obstacles.blocker.Blockers;
import pl.agh.diffusion_project.obstacles.absorber.Absorbers;
import pl.agh.diffusion_project.obstacles.isolator.Isolators;
import pl.agh.diffusion_project.pollution.Pollution;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
    private final Mapping mapping;
    private final Board board;
    private Pollution[][][] cellsHistory;
    private final int width, length, height;

    private final Map<String, CallableUpdate> updates = new HashMap<>();


    public Configuration(int width, int length, int height, Mapping mapping) {
        this.width = width;
        this.length = length;
        this.height = height;
        this.mapping = mapping;
        RefactoredCell.setMapping(mapping);
        this.board = new Board(width, length, height, mapping);
        this.cellsHistory = board.get3DConcentrationMatrix();
    }

    public void addObstacles(ObstaclesLoader obstaclesLoader){
        board.addObstacles(obstaclesLoader);
    }

    public void setupDiffusion(CallableUpdate callableUpdate) {
        board.setupDiffusion(callableUpdate);
    }

    public void addUpdate(String updateName, CallableUpdate callableUpdate) {
        this.updates.put(updateName, callableUpdate);
    }

    public void iterateUpdate(String updateName){
        this.board.updateWithFunction(this.updates.get(updateName));
    }


    public void iterateDiffusion(CallableUpdate callableUpdate) {
        board.updateWithFunction(callableUpdate);
    }


    public Pollution[][][] getConcentrationMatrix() {
        return board.get3DConcentrationMatrix();
    }

    public void modConcentrationAt(int x, int y, int z, Pollution p) {
        board.modConcentrationAt(x, y, z, p);
    }
}