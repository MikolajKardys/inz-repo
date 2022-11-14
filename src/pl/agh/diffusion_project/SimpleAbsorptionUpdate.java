package pl.agh.diffusion_project;

import pl.agh.diffusion_project.cells.Leaf;
import pl.agh.diffusion_project.obstacles.absorber.Absorber;
import pl.agh.diffusion_project.pollution.Pollution;

public class SimpleAbsorptionUpdate implements AbsorptionUpdate {
    @Override
    public Pollution update(Leaf leaf) {
        Pollution oldPollution = leaf.getPollution();
        Pollution newPollution = new Pollution(oldPollution);
//        for (Absorber a : leaf.getAbsorbers()){
//            newPollution.mod(a.add(oldPollution), -1.0f);//-=a.add(oldConcentration);
//        }
        return newPollution;
    }
}

