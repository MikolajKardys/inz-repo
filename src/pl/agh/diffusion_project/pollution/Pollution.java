package pl.agh.diffusion_project.pollution;

import org.javatuples.Pair;
import pl.agh.diffusion_project.cells.RefactoredCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//TODO przechowywać różne wektory definiowane na starcie programu
//TODO każdy program można wykonać na każdym wektorze
//TODO wykonywać różne programy co ilość kroków albo wcale gdzie chcemy
public class Pollution {
//    protected static Integer gas = 2;
//    protected static Integer dust = 4; //kończy się na 4, długość = dust-gas
    protected List<Float> values;

    public Pollution(int size) {
        this.values = new ArrayList<Float>(Collections.nCopies(size, 0.0f));
    }

//    public Pollution() {
//        this(dust);
//    }

    public Pollution(List<Float> values) {
        this.values = values;
    }

    public Pollution(Pollution pollution) {
        List<Float> floats = pollution.getFastAll();
        this.values = new ArrayList<>(Collections.nCopies(floats.size(), 0.0f));
        for (int i = 0; i < floats.size(); i++){
            values.set(i, pollution.values.get(i));
        }
    }

    private Pollution(Integer size) {
        this.values = new ArrayList<>(Collections.nCopies(size, 0.0f));
    }

    public static Pollution fromOtherType(Pollution pollution, Integer size, List<Pair<Integer, Integer>> mapping) {
        Pollution returnPollution = new Pollution(size);
        List<Float> values = returnPollution.getFastAll();
        List<Float> t = pollution.getFastAll();
        for (Pair<Integer, Integer> pair: mapping) {
            values.set(pair.getValue1(), t.get(pair.getValue0()));
        }
        return returnPollution;
    }

    public static Pollution fromFull(Pollution pollution, Integer size, List<Boolean> mapping) {
        Pollution returnPollution = new Pollution(size);
        List<Float> values = returnPollution.getFastAll();
        List<Float> floats = pollution.getFastAll();
        Iterator<Float> floatIterator = floats.iterator();
        Iterator<Boolean> booleanIterator = mapping.iterator();
        int i = 0;
        while (floatIterator.hasNext() && booleanIterator.hasNext()) {
            Float f = floatIterator.next();
            if (booleanIterator.next()){
                values.set(i, f);
                i++;
            }
        }
        return returnPollution;
    }

    public static Pollution toFull(Pollution pollution, Integer size, List<Boolean> mapping) {
        Pollution returnPollution = new Pollution(size);
        List<Float> values = returnPollution.getFastAll();
        List<Float> floats = pollution.getFastAll();
        Iterator<Float> floatIterator = floats.iterator();
        Iterator<Boolean> booleanIterator = mapping.iterator();
        int i = 0;
        while (floatIterator.hasNext() && booleanIterator.hasNext()) {
            if (booleanIterator.next()){
                values.set(i, floatIterator.next());
            }
            i++;
        }
        return returnPollution;
    }

//    public static Integer getGasNum() {
//        return gas;
//    }
//
//    public static Integer getDustNum() {
//        return dust;
//    }

    public List<Float> getAll() {
        return new ArrayList<>(values);
    }

    public List<Float> getFastAll() {
        return values;
    }

//    public List<Float> getGas() {
//        return new ArrayList<>(values.subList(0, gas));
//    }
//
//    public List<Float> getDust() {
//        return new ArrayList<>(values.subList(gas, dust));
//    }
//
//    public Pollution mod(Pollution pollution) {
//        List<Float> t1 = pollution.getAll();
//        for (int i = 0; i < dust; i++){
//            values.set(i, values.get(i) + t1.get(i));
//        }
//        return this;
//    }
//
//    public Pollution mod(Pollution pollution, Float coefficient) {
//        List<Float> t1 = pollution.getAll();
//        for (int i = 0; i < dust; i++){
//            values.set(i, values.get(i) + (t1.get(i) * coefficient));
//        }
//        return this;
//    }
//
//    public Pollution mod(Pollution pollution, Pollution coefficient) {
//        List<Float> t1 = pollution.getAll();
//        List<Float> t2 = coefficient.getAll();
//        for (int i = 0; i < dust; i++){
//            values.set(i, values.get(i) + (t1.get(i) * t2.get(i)));
//        }
//        return this;
//    }
//
//    public Pollution mod(Pollution pollution, Pollution coefficient, Float globalCoefficient) {
//        List<Float> t1 = pollution.getAll();
//        List<Float> t2 = coefficient.getAll();
//        for (int i = 0; i < dust; i++){
//            values.set(i, values.get(i) + (t1.get(i) * t2.get(i) * globalCoefficient));
//        }
//        return this;
//    }

    /**
     * Modifies pollution values.
     *
     * @param pollution    pollution increased by
     * @param mapping      mapping of the parameter pollution to class target pollution
     */
    public void mod(Pollution pollution, List<Pair<Integer, Integer>> mapping) {
        System.out.println(pollution);
        System.out.println(mapping);
        List<Float> t = pollution.getFastAll();
        System.out.println(values);
        for (Pair<Integer, Integer> pair: mapping) {
            int left = pair.getValue0();
            int right = pair.getValue1();
            values.set(right, values.get(right) + t.get(left));
            System.out.println(values);
        }
    }

    /**
     * Modifies pollution values with global coefficient.
     *
     * @param pollution    pollution increased by
     * @param mapping      mapping of the parameter cell pollution to target cell pollution
     * @param coefficient  coefficient for modification
     */
    public void mod(Pollution pollution, List<Pair<Integer, Integer>> mapping, Float coefficient) {
        List<Float> pollutionValues = pollution.getFastAll();
        for (Pair<Integer, Integer> pair: mapping) {
            int left = pair.getValue0();
            int right = pair.getValue1();
            values.set(right, values.get(right) + pollutionValues.get(left) * coefficient);
        }
    }

    /**
     * Modifies pollution values with coefficient for each value.
     *
     * @param pollution    pollution increased by
     * @param mapping      mapping of the parameter cell pollution to target cell pollution
     * @param coefficients coefficients of the parameter pollution
     */
    public void mod(Pollution pollution, List<Pair<Integer, Integer>> mapping, List<Float> coefficients) {
        List<Float> pollutionValues = pollution.getFastAll();
        for (Pair<Integer, Integer> pair: mapping) {
            int left = pair.getValue0();
            int right = pair.getValue1();
            values.set(right, values.get(right) + pollutionValues.get(left) * coefficients.get(left));
        }
    }

    /**
     * Modifies pollution values with coefficient for each value and does opposite to parameter rest pollution.
     *
     * @param pollution     pollution increased by
     * @param restPollution pollution increased by
     * @param mapping       mapping of the parameter cell pollution to target cell pollution
     * @param coefficients  coefficients of the parameter pollution
     * @param coefficient   coefficient for modification
     */
    public void mod(Pollution pollution, Pollution restPollution, List<Pair<Integer, Integer>> mapping, List<Float> coefficients, Float coefficient) {
        List<Float> pollutionValues = pollution.getFastAll();
        List<Float> restPollutionValues = restPollution.getFastAll();
        for (Pair<Integer, Integer> pair: mapping) {
            int left = pair.getValue0();
            int right = pair.getValue1();
            float value = pollutionValues.get(left) * coefficient;
            values.set(right, values.get(right) + value * coefficients.get(left));
//            restPollutionValues.set(right, restPollutionValues.get(right) + value * coefficients.get(left));
            //ŻLE, wielowątkowość
        }
    }

    public void reset() {
        for (int i = 0; i < values.size(); i++) {
            values.set(i, 0.0f);
        }
    }

    @Override
    public String toString() {
        return "Pollution{" +
                "values=" + values +
                '}';
    }
}
