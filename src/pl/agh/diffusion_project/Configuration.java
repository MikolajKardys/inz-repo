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
//    private final Blockers blockers;
//    private final Isolators isolators;
//    private final Absorbers absorbers;
    private final int width, length, height;

//    public Configuration() {
//        this(100, 100, 100, new ArrayList<>());
//    }
//
//    public Configuration(int width, int length, int height) {
//        this(width, length, height, new ArrayList<>());
//    }
//
//    public Configuration(List<Quartet<Integer, Integer, Integer, Pollution>> initialCondition) {
//        this(100, 100, 100, initialCondition);
//    }

    public Configuration(int width, int length, int height, List<Quartet<Integer, Integer, Integer, Pollution>> initialCondition, Mapping mapping) {
        this.width = width;
        this.length = length;
        this.height = height;
        this.mapping = mapping;
        RefactoredCell.setMapping(mapping);
        this.board = new Board(width, length, height, initialCondition, mapping);
        this.cellsHistory = board.get3DConcentrationMatrix();
    }

//    public Configuration(int width, int length, int height, List<Quartet<Integer, Integer, Integer, Pollution>> initialCondition, Blockers blockers) {
//        this.width = width;
//        this.length = length;
//        this.height = height;
//        this.blockers = blockers;
//        this.isolators = new Isolators();
//        this.absorbers = new Absorbers();
//        this.board = new Board(width, length, height, initialCondition, this.blockers, this.isolators);
//        this.cellsHistory = board.get3DConcentrationMatrix();
//    }
//
//    public Configuration(int width, int length, int height, List<Quartet<Integer, Integer, Integer, Pollution>> initialCondition, Blockers blockers, Isolators isolators) {
//        this.width = width;
//        this.length = length;
//        this.height = height;
//        this.blockers = blockers;
//        this.isolators = isolators;
//        this.absorbers = new Absorbers();
//        this.board = new Board(width, length, height, initialCondition, this.blockers, this.isolators);
//        this.cellsHistory = board.get3DConcentrationMatrix();
//    }
//
//    public Configuration(int width, int length, int height, List<Quartet<Integer, Integer, Integer, Pollution>> initialCondition, Blockers blockers, Isolators isolators, Wind wind) {
//        this.width = width;
//        this.length = length;
//        this.height = height;
//        this.blockers = blockers;
//        this.isolators = isolators;
//        this.absorbers = new Absorbers();
//        this.board = new Board(width, length, height, initialCondition, this.blockers, this.isolators);
//        this.cellsHistory = board.get3DConcentrationMatrix();
//        this.board.addWind(wind);
//    }
//
//    public Configuration(int width, int length, int height, List<Quartet<Integer, Integer, Integer, Pollution>> initialCondition, Blockers blockers, Isolators isolators, Wind wind, Absorbers absorbers) {
//        this.width = width;
//        this.length = length;
//        this.height = height;
//        this.blockers = blockers;
//        this.isolators = isolators;
//        this.absorbers = absorbers;
//        this.board = new Board(width, length, height, initialCondition, this.blockers, this.isolators, this.absorbers);
//        this.cellsHistory = board.get3DConcentrationMatrix();
//        this.board.addWind(wind);
//    }

    public void setupDiffusion(CallableUpdate callableUpdate) {
        callableUpdate.setup(width, length, height);
    }

    public void iterateDiffusion(CallableUpdate callableUpdate) {
        board.updateWithFunction(callableUpdate);
    }

//    public void iterateAbsorption(AbsorptionUpdate simpleAbsorptionUpdate) {
//        board.updateAbsorption(simpleAbsorptionUpdate);
//    }

//    public void iterateDecay() {
//        board.updateDecay();
//    }

    public void iterate(CallableUpdate callableUpdate) {
        board.updateWithFunction(callableUpdate);
    }

    public void iterate(CallableUpdate callableUpdate, AbsorptionUpdate simpleAbsorptionUpdate) {
        board.updateWithFunction(callableUpdate);
//        board.updateAbsorption(simpleAbsorptionUpdate);
//        board.updateDecay();
    }

    public void saveCellsHistory(String fileName) {
        cellsHistory = board.get3DConcentrationMatrix();
//        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName, true))) {
////            Pollution[][][] cells = cellsHistory;
//            List<Float>[][][] values = new ArrayList[width][length][height];
//
//            out.writeInt(Pollution.getGasNum());
//            out.writeInt(Pollution.getDustNum()-Pollution.getGasNum());
//
//            for(int i=0; i<width; i++)
//                for(int j=0; j<length; j++)
//                    for(int k=0; k<height; k++){
//                        values[i][j][k] = cellsHistory[i][j][k].getAll();
//                    }
//            for(int n=0; n<Pollution.getDustNum(); n++) {
//                for(int i=0; i<width; i++)
//                    for(int j=0; j<length; j++)
//                        for(int k=0; k<height; k++){
//                            out.writeFloat(values[i][j][k].get(n));
//                        }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void saveDimensions(String fileName, Integer iterations) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName, true))) {
            out.writeFloat((float)iterations);
            out.writeFloat((float)board.getWidth());
            out.writeFloat((float)board.getLength());
            out.writeFloat((float)board.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print2DResults() {
        float sum;
        float allSum = (float)0.0;
        Pollution[][][] cells = cellsHistory;
        for(int n=0; n< mapping.getPollutionTypeCount(); n++) {
            System.out.printf("pollution num %d\n", n);
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    sum = (float) 0.0;
                    for (int k = 0; k < cells[i][j].length; k++) {
                        sum += cells[i][j][k].getAll().get(n);
                    }
                    System.out.printf("%.3f ", sum);
                    allSum += sum;
                }
                System.out.println();
            }
//            printPollution(n);
        }
        System.out.println(allSum);
    }

    public void print3DResults() {
        float sum;
        Pollution[][][] cells = cellsHistory;
        for (int k = 0; k < cells[0][0].length; k++) {
            float allSum = (float)0.0;
            for (Pollution[][] cell : cells) {
                for (Pollution[] pollutions : cell) {
                    sum = (float) 0.0;
                    sum += pollutions[k].getAll().get(0);
                    System.out.printf("%.3f ", sum);
                    allSum += sum;
                }
                System.out.println();
            }
            System.out.println(allSum);
        }
//        printPollution(0);
    }
//    public void printPollution( int n){
//        float capturedPollution=0.0f;
//        float decayedPollution=0.0f;
//        if (n<= mapping.getPollutionTypeCount()) {
//            for (Absorber a : board.getAbsorbers()) {
//                capturedPollution += a.getPollution().getAll().get(n);
//                decayedPollution += a.getDecayedPollution().getAll().get(n);
//            }
//            System.out.println("Captured pollution: " + capturedPollution);
//            System.out.println("Decayed pollution: " + decayedPollution);
//        }
//        if (n>mapping.getPollutionTypeCount()){
//            float residuePollution= board.getResiduePollution().getDust().get(n-Pollution.getGasNum());
//            float pollutionOnGround= board.getPollutionOnGround().getDust().get(n-Pollution.getGasNum());
//            System.out.println("On leaves pollution: "+residuePollution);
//            System.out.println("Fallen pollution: "+pollutionOnGround);
//        }
    }
