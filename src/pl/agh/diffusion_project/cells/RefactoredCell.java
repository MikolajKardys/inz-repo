package pl.agh.diffusion_project.cells;

import org.javatuples.Pair;
import pl.agh.diffusion_project.pollution.Pollution;

import java.util.List;

public class RefactoredCell {

    private final Integer cellType;

    private Pollution currentPollution;
    private Pollution newPollution;

    private static Mapping mapping;

    public RefactoredCell(Integer cellType, Pollution pollution){
        this.cellType = cellType;
        this.currentPollution = Pollution.fromFull(pollution, mapping.getMappingMatrix(cellType, cellType).size(), mapping.getContainsMatrix(cellType));
        this.newPollution = new Pollution(currentPollution);
    }

    public Pollution getFullCurrentPollution(){
        return Pollution.toFull(currentPollution, mapping.getPollutionTypeCount(), mapping.getContainsMatrix(cellType));
    }

    private Pollution getCurrentPollutionForType(Integer type){
        return Pollution.fromOtherType(currentPollution, mapping.getMappingMatrix(type, type).size(), mapping.getMappingMatrix(cellType, type));
    }

    public void update(){
//        System.out.println("newPollution");
//        System.out.println(newPollution);
//        System.out.println("currentPollution");
//        System.out.println(currentPollution);
        Pollution t = currentPollution;
        currentPollution = newPollution;
        newPollution = t;
        newPollution.reset();
//        System.out.println("newPollution2");
//        System.out.println(newPollution);
//        System.out.println("currentPollution2");
//        System.out.println(currentPollution);
    }

    public static void setMapping(Mapping mapping) {
        RefactoredCell.mapping = mapping;
    }

    /**
     * Modifies cell new pollution values.
     *
     * @param pollution    pollution by which current pollution is modified
     */
    public void modPollution(Pollution pollution) {//TODO general mapping
        this.newPollution.mod(
                Pollution.fromFull(pollution, mapping.getMappingMatrix(cellType, cellType).size(), mapping.getContainsMatrix(cellType)),
                mapping.getMappingMatrix(this.cellType, this.cellType));
    }

    /**
     * Modifies cell new pollution values.
     *
     * @param cell         cell from which current pollution is taken
     */
    public void modPollution(RefactoredCell cell) {
        this.newPollution.mod(cell.currentPollution, mapping.getMappingMatrix(cell.cellType, this.cellType));
    }

    /**
     * Modifies cell new pollution values with global coefficient.
     *
     * @param cell         cell from which current pollution is taken
     * @param coefficient  coefficient for modification
     */
    public void modPollution(RefactoredCell cell, Float coefficient) {
        this.newPollution.mod(cell.currentPollution, mapping.getMappingMatrix(cell.cellType, this.cellType), coefficient);
    }

    /**
     * Modifies cell new pollution values with coefficient for each value.
     *
     * @param cell         cell from which current pollution is taken
     * @param coefficients coefficients of the parameter cell pollution
     */
    public void modPollution(RefactoredCell cell, List<Float> coefficients) {
        this.newPollution.mod(cell.currentPollution, mapping.getMappingMatrix(cell.cellType, this.cellType), coefficients);
    }

    /**
     * Modifies cell new pollution values with coefficient for each value and does opposite to parameter cell new pollution.
     *
     * @param cell         cell from which current pollution is taken
     * @param coefficients coefficients of the parameter cell pollution
     * @param coefficient  coefficient for modification
     */
    public void modPollution(RefactoredCell cell, List<Float> coefficients, Float coefficient) {
        this.newPollution.mod(cell.currentPollution, mapping.getMappingMatrix(cell.cellType, this.cellType), coefficients, coefficient);
//        System.out.println(newPollution);
    }

    public Integer getCellType() {
        return cellType;
    }

    @Override
    public String toString() {
        return "RefactoredCell{" +
                "currentPollution=" + currentPollution +
                '}';
    }
}
