package pl.agh.diffusion_project.pollution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//public class Gas extends Pollution{

//    public Gas() {
//        this.values =new ArrayList<>(Collections.nCopies(gas, 0.0f));
//    }
//
//    public Gas(List<Float> values) {
//        super(values);
//    }
//
//    public Gas(Pollution pollution) {
//        super(pollution.getGas());
//    }
//
//    @Override
//    public List<Float> getAll() {
//        List<Float> t = new ArrayList<Float>(Collections.nCopies(dust, 0.0f));
//        for (int i = 0; i < gas; i++){
//            t.set(i, values.get(i));
//        }
//        return t;
//    }
//
//    @Override
//    public List<Float> getGas() {
//        return new ArrayList<>(values);
//    }
//
//    @Override
//    public List<Float> getDust() {
//        return new ArrayList<Float>(Collections.nCopies(gas, 0.0f));
//    }
//
//    @Override
//    public Pollution mod(Pollution pollution) {
//        List<Float> t1 = pollution.getGas();
//        for (int i = 0; i < gas; i++){
//            values.set(i, values.get(i) + t1.get(i));
//        }
//        return this;
//    }
//
//    @Override
//    public Pollution mod(Pollution pollution, Float coefficient) {
//        List<Float> t1 = pollution.getGas();
//        for (int i = 0; i < gas; i++){
//            values.set(i, values.get(i) + (t1.get(i) * coefficient));
//        }
//        return this;
//    }
//
//    @Override
//    public Pollution mod(Pollution pollution, Pollution coefficient) {
//        List<Float> t1 = pollution.getGas();
//        List<Float> t2 = coefficient.getGas();
//        for (int i = 0; i < gas; i++){
//            values.set(i, values.get(i) + (t1.get(i) * t2.get(i)));
//        }
//        return this;
//    }
//
//    @Override
//    public Pollution mod(Pollution pollution, Pollution coefficient, Float globalCoefficient) {
//        List<Float> t1 = pollution.getGas();
//        List<Float> t2 = coefficient.getGas();
//        for (int i = 0; i < gas; i++){
//            values.set(i, values.get(i) + (t1.get(i) * t2.get(i) * globalCoefficient));
//        }
//        return this;
//    }
//
//    @Override
//    public String toString() {
//        return "Gas{" +
//                "values=" + values +
//                '}';
//    }
//}
