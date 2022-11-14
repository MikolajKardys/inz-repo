package pl.agh.diffusion_project.cells;

import org.javatuples.Pair;
import pl.agh.diffusion_project.pollution.Pollution;

import java.util.ArrayList;
import java.util.List;

public class Cell {//TODO stałe źródło zanieczyszczeń
    protected Pollution pollution;
    private boolean blocked = false;
//    private List<Absorber> absorbers;
    private final List<Pair<Cell, Float>> neighbors;
    private final List<Pair<Cell, Pair<Boolean, Integer>>> windNeighbors; // true if incoming, false if outgoing

    public Cell(Pollution pollution) {
        this.pollution = pollution;

        this.neighbors = new ArrayList<>();
        this.windNeighbors = new ArrayList<>();
    }

    public void addNeighbor(Cell nei, Float coef) {
        neighbors.add(new Pair<>(nei, coef));
    }
    public void addWindNeighbor(Cell nei, Boolean forward, Integer freq) {
        windNeighbors.add(new Pair<>(nei, new Pair<>(forward, freq)));
    }

    public Pollution getPollution() {
        return pollution;
    }

    public void setPollution(Pollution pollution) {
        this.pollution = pollution;
    }

    public void modPollution(Pollution pollution) {
//        this.pollution.mod(pollution);
    }

    public List<Pair<Cell, Float>> getNeighbors() {
        return neighbors;
    }
    public List<Pair<Cell, Pair<Boolean, Integer>>> getWindNeighbors() {
        return windNeighbors;
    }
//    public void setAbsorbers(List<Absorber> absorbers) {
//        this.absorbers = absorbers;
//    }
    public void block(){ this.blocked = true;}
    public boolean isBlocked(){ return this.blocked;}
}
