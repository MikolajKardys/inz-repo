package pl.agh.diffusion_project;

import org.javatuples.Quartet;
import pl.agh.diffusion_project.cells.Mapping;
import pl.agh.diffusion_project.cells.RefactoredCell;
import pl.agh.diffusion_project.obstacles.absorber.Absorber;
import pl.agh.diffusion_project.obstacles.blocker.Blockers;
import pl.agh.diffusion_project.obstacles.absorber.Absorbers;
import pl.agh.diffusion_project.obstacles.isolator.Isolators;
import pl.agh.diffusion_project.pollution.Pollution;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private final Mapping mapping;
    private final Board board;
    private Pollution[][][] cellsHistory;
    private final int width, length, height;


    public Configuration(int width, int length, int height, Mapping mapping) {
        this.width = width;
        this.length = length;
        this.height = height;
        this.mapping = mapping;
        RefactoredCell.setMapping(mapping);
        this.board = new Board(width, length, height, mapping);
        this.cellsHistory = board.get3DConcentrationMatrix();
    }

    public void setupDiffusion(CallableUpdate callableUpdate) {
        board.setupDiffusion(callableUpdate);
    }


    public void iterateDiffusion(CallableUpdate callableUpdate) {
        board.updateWithFunction(callableUpdate);
    }


    public Pollution[][][] getConcentrationMatrix() {
        return board.get3DConcentrationMatrix();
    }

    public void setConcentrationAt(int x, int y, int z, Pollution p) {
        board.get3DConcentrationMatrix()[x][y][z] = p;
    }
}