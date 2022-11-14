package pl.agh.diffusion_project.obstacles.isolator;

import java.util.ArrayList;
import java.util.List;

public class Isolators {
    final List<Isolator> isolators;

    public Isolators(List<Isolator> isolators) {
        this.isolators = isolators;
    }

    public Isolators() {
        isolators = new ArrayList<>();
    }

    public float coefficient(int x, int y, int z){
        return isolators.stream().filter(i -> i.inIsolator(x, y, z))
                .map(i -> i.coefficient(x, y, z))
                .reduce(1.0f, (subtotal, element) -> subtotal * element);
    }

    public boolean inIsolator(int x, int y, int z){
        return isolators.stream().map(i -> i.inIsolator(x, y, z))
                .reduce(false, (subtotal, element) -> subtotal || element);
    }

    public void addIsolator(Isolator isolator){
        this.isolators.add(isolator);
    }
}
