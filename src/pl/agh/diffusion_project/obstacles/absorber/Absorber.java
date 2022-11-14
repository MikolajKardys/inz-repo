package pl.agh.diffusion_project.obstacles.absorber;

import pl.agh.diffusion_project.obstacles.Obstacle;
import pl.agh.diffusion_project.pollution.Dust;
//import pl.agh.diffusion_project.pollution.Gas;
import pl.agh.diffusion_project.pollution.Pollution;

public abstract class  Absorber extends Obstacle {
    protected Absorber(int x1, int x2, int y1, int y2, int z1, int z2) {
        super(x1, x2, y1, y2, z1, z2);
    }

    abstract public boolean isAbsorbing(int x, int y, int z);
//    abstract public Gas add(Pollution cellPollution);
    abstract public void update();
    abstract public void decay();
//    abstract public Gas getPollution();
//    abstract public Gas getDecayedPollution();
    abstract public Dust getRate();
    abstract public Dust getLimit();
}
