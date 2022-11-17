package pl.agh.diffusion_project.cells;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mapping {

    private final Integer cellTypeCount;
    private final Integer pollutionTypeCount;

    private final List<List<Integer>> indexMatrix;
    private final List<List<Boolean>> containsMatrix;
    private final List<List<List<Pair<Integer, Integer>>>> mappingMatrix;

    public Mapping(Integer cellTypeCount, Integer pollutionTypeCount, List<List<Boolean>> containsMatrix) {
        this.cellTypeCount = cellTypeCount;
        this.pollutionTypeCount = pollutionTypeCount;
        this.indexMatrix =  new ArrayList(cellTypeCount);
        this.containsMatrix = containsMatrix;
        this.mappingMatrix = new ArrayList<>(Collections.nCopies(cellTypeCount, null));
        for (int i = 0; i < cellTypeCount; i++) {
            this.mappingMatrix.set(i, new ArrayList<>(Collections.nCopies(cellTypeCount, null)));
            for (int j = 0; j < cellTypeCount; j++) {
                int x = 0;
                int y = 0;
                for (int k = 0; k < pollutionTypeCount; k++) {
                    if (containsMatrix.get(i).get(k) && containsMatrix.get(j).get(k)) {
                        x++;
                    }
                }
                this.mappingMatrix.get(i).set(j, new ArrayList<>(x));
                x = 0;
                for (int k = 0; k < pollutionTypeCount; k++) {
                    if (containsMatrix.get(i).get(k) && containsMatrix.get(j).get(k)){
                        this.mappingMatrix.get(i).get(j).add(new Pair<>(x, y));
                    }
                    if (containsMatrix.get(i).get(k)){
                        x++;
                    }
                    if (containsMatrix.get(j).get(k)){
                        y++;
                    }
                }
            }
            indexMatrix.add(new ArrayList(mappingMatrix.get(i).get(i).size()));
            for (int k = 0; k < pollutionTypeCount; k++) {
                if (containsMatrix.get(i).get(k))
                    indexMatrix.get(i).add(k);
            }
        }
    }

    public List<Integer> getIndexMatrix(Integer type) {
        return indexMatrix.get(type);
    }

    public List<Boolean> getContainsMatrix(Integer type) {
        return containsMatrix.get(type);
    }

    public List<Pair<Integer, Integer>> getMappingMatrix(Integer type1, Integer type2) {
        return mappingMatrix.get(type1).get(type2);
    }

    public Integer getCellTypeCount() {
        return cellTypeCount;
    }

    public Integer getCellTypeSize(Integer type) {
        return indexMatrix.get(type).size();
    }

    public Integer getPollutionTypeCount() {
        return pollutionTypeCount;
    }
}
