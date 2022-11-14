package pl.agh.diffusion_project.pollution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dust extends Pollution{

//    public Dust() {
//        this.values =new ArrayList<>(Collections.nCopies(dust - gas, 0.0f));
//    }

    public Dust(List<Float> values) {
        super(values);
    }

//    public Dust(Pollution pollution) {
//        super(pollution.getDust());
//    }

//    @Override
//    public List<Float> getAll() {
//        List<Float> t = new ArrayList<Float>(Collections.nCopies(dust, 0.0f));
//        for (int i = gas; i < dust; i++){
//            t.set(i, values.get(i - gas));
//        }
//        return t;
//    }
//
//    @Override
//    public List<Float> getGas() {
//        return new ArrayList<Float>(Collections.nCopies(gas, 0.0f));
//    }
//
//    @Override
//    public List<Float> getDust() {
//        return new ArrayList<>(values);
//    }
//
//    @Override
//    public Pollution mod(Pollution pollution) {
//        List<Float> t1 = pollution.getDust();
//        for (int i = 0; i < dust - gas; i++){
//            values.set(i, values.get(i) + t1.get(i));
//        }
//        return this;
//    }
//
//    @Override
//    public Pollution mod(Pollution pollution, Float coefficient) {
//        List<Float> t1 = pollution.getDust();
//        for (int i = 0; i < dust - gas; i++){
//            values.set(i, values.get(i) + (t1.get(i) * coefficient));
//        }
//        return this;
//    }
//
//    @Override
//    public Pollution mod(Pollution pollution, Pollution coefficient) {
//        List<Float> t1 = pollution.getDust();
//        List<Float> t2 = coefficient.getDust();
//        for (int i = 0; i < dust - gas; i++){
//            values.set(i, values.get(i) + (t1.get(i) * t2.get(i)));
//        }
//        return this;
//    }
//
//    @Override
//    public Pollution mod(Pollution pollution, Pollution coefficient, Float globalCoefficient) {
//        List<Float> t1 = pollution.getDust();
//        List<Float> t2 = coefficient.getDust();
//        for (int i = 0; i < dust - gas; i++){
//            values.set(i, values.get(i) + (t1.get(i) * t2.get(i) * globalCoefficient));
//        }
//        return this;
//    }
//
//    @Override
//    public String toString() {
//        return "Dust{" +
//                "values=" + values +
//                '}';
//    }
}
