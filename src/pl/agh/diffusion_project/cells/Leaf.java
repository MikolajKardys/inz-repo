package pl.agh.diffusion_project.cells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.agh.diffusion_project.obstacles.absorber.Absorber;
import pl.agh.diffusion_project.pollution.Dust;
import pl.agh.diffusion_project.pollution.Pollution;

public class Leaf extends Cell {
    private Dust rate;
    private Dust limit;
    private final List<Absorber> absorbers;
//    private final Dust residue = new Dust();
    private Leaf below = null;
    private final Dirt dirt;

    public Leaf(Pollution pollution, Dirt dirt, List<Absorber> absorbers) {
        super(pollution);
        this.absorbers = absorbers;
//        this.rate = new Dust();
//        absorbers.stream().map(Absorber::getRate).forEach(absorber -> rate.mod(absorber));
//        this.limit = new Dust();
        this.dirt = dirt;
    }

    public void absorb(){
//        residue.mod(this.getPollution(), rate);
//        this.pollution.mod(this.pollution, rate, -1.0f);
    }

    public void fallOff(){ //TODO wywoływać od góry na dół
//        List<Float> residueTemp = residue.getDust();
//        List<Float> limitTemp = limit.getDust();
//        List<Float> result = new ArrayList<>(Collections.nCopies(Dust.getDustNum(), 0.0f));
//        for (int i = 0; i < Dust.getDustNum(); i++) {
//            if (residueTemp.get(i)>limitTemp.get(i)){
//                result.set(i, residueTemp.get(i)/2.0f);
//            }
//        }
//        Dust dustResult = new Dust(result);
//        residue.mod(dustResult, -1.0f);
//        if (this.below != null){
//            this.below.modPollution(dustResult);
//        }else{ //TODO spadać na ziemię powoli a nie od razu
//            this.dirt.modPollution(dustResult);
//        }
    }

    public void setBelow(Leaf below) {
        this.below = below;
    }

    public void setRate(Dust rate) {
        this.rate = rate;
    }

    public void setLimit(Dust limit) {
        this.limit = limit;
    }

    public Leaf getBelow() {
        return below;
    }

    public Dirt getDirt() {
        return dirt;
    }
    public List<Absorber> getAbsorbers() {
        return this.absorbers;
    }
//    public Dust getResidue() {
//        return residue;
//    }
}
