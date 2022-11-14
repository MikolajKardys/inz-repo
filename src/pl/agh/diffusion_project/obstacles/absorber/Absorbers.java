package pl.agh.diffusion_project.obstacles.absorber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toCollection;


public class Absorbers implements Iterable<Absorber> {
    final List<Absorber> absorbers;

    public Absorbers(List<Absorber> absorbers) {
        this.absorbers = absorbers;
    }

    public Absorbers() {
        absorbers = new ArrayList<>();
    }

    public boolean isAbsorbing(int x, int y, int z) {
        return absorbers.stream().map(i -> i.isAbsorbing(x, y, z))
                .reduce(false, (subtotal, element) -> subtotal || element);
    }

    public  List<Absorber> getAbsorbers(int x, int y, int z){
        return absorbers.stream().filter(i -> i.isAbsorbing(x, y, z)).collect(toCollection(ArrayList::new));
    }

    public void addAbsorber(Absorber absorber){
        this.absorbers.add(absorber);
    }

    public void update() {
        for(Absorber a : absorbers)
            a.update();
    }

    public void decay() {
        for(Absorber a : absorbers)
            a.decay();
    }

    @Override
    public Iterator<Absorber> iterator() {
        return absorbers.iterator();
    }
}
