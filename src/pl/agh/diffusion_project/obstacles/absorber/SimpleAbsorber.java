//package pl.agh.diffusion_project.obstacles.absorber;
//
//import pl.agh.diffusion_project.obstacles.ObstacleTypes;
//import pl.agh.diffusion_project.pollution.Dust;
////import pl.agh.diffusion_project.pollution.Gas;
//import pl.agh.diffusion_project.pollution.Pollution;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class SimpleAbsorber extends Absorber {
////    private final int volume;
////    private final Gas capacity, absorption, decay;
////    private final Dust rate, limit;
////    private Gas pollution = new Gas();
////    private Gas delta = new Gas();
////    private Gas history = new Gas();
//
////    public SimpleAbsorber(int x1, int x2, int y1, int y2, int z1, int z2, Gas capacity, Gas absorption, Gas decay,
////                          Dust rate, Dust limit) {
////        super(x1, x2, y1, y2, z1, z2);
////        this.absorption = absorption;
////        this.capacity = capacity;
////        this.decay = decay;
////        this.rate = rate;
////        this.limit = limit;
////        this.volume = (x2-x1)*(y2-y1)*(z2-z1);
////    }
//
//    @Override
//    public boolean isAbsorbing(int x, int y, int z){
//        return x >= x1 && x < x2 &&
//                y >= y1 && y < y2 &&
//                z >= z1 && z < z2;
//    }
//
//    @Override
//    public void update(){
////        System.out.println("update "+pollution);
////        System.out.println("delta "+delta);
//        pollution.mod(delta);
//        history.mod(delta);
//        delta = new Gas();
////        System.out.println("update "+pollution);
//    }
//
//    @Override
//    public Gas add(Pollution cellPollution){
//        List<Float> result = new ArrayList<>(Collections.nCopies(Dust.getDustNum(), 0.0f));
//        List<Float> cellPollutionTemp = cellPollution.getGas();
//        List<Float> pollutionTemp = pollution.getGas();
//        List<Float> capacityTemp = capacity.getGas();
//        List<Float> absorptionTemp = absorption.getGas();
//        for (int i = 0; i < Gas.getGasNum(); i++) {
//            pollutionTemp.set(i,
//                    Math.min(
//                            Math.max(
//                                    cellPollutionTemp.get(i) - pollutionTemp.get(i)/volume,
//                                    0.0f
//                            ) * absorptionTemp.get(i) / 2.0f,
//                            capacityTemp.get(i)-pollutionTemp.get(i)
//                    )
//            );
//        }
//        Gas resultGas = new Gas(result);
//        delta.mod(resultGas);
////        if(cellPollution!=0.0f){
////            System.out.println("new add");
////            System.out.println(cellPollution);
////            System.out.println(t);
////            System.out.println(Math.max(cellPollution-pollution/volume, 0.0f)*absorption/2.0f);
////            System.out.println(cellPollution-pollution/volume);
////        }
//        return resultGas;
//    }
//
//    @Override
//    public void decay(){
//        pollution.mod(pollution,decay,-1.0f);
////        pollution-=pollution*decay;
//    }
//
//    @Override
//    public Gas getPollution() {
//        return pollution;
//    }
//
//    @Override
//    public Gas getDecayedPollution() {
//        return history;
//    }
//
//
//    @Override
//    protected int getType() {
//        return ObstacleTypes.SIMPLE_ABSORBER.ordinal();
//    }
//
//    @Override
//    public Dust getRate() {
//        return rate;
//    }
//
//    @Override
//    public Dust getLimit(){
//        return limit;
//    }
//}
