package pl.agh.diffusion_project.obstacles.blocker;

import java.util.ArrayList;
import java.util.List;

public class Blockers {
    final List<Blocker> blockers;

    public Blockers(List<Blocker> blockers) {
        this.blockers = blockers;
    }

    public Blockers() {
        blockers = new ArrayList<>();
    }

    public boolean isBlocked(int x, int y, int z){
        return blockers.stream().map(i -> i.isBlocked(x, y, z))
                .reduce(false, (subtotal, element) -> subtotal || element);
    }

    public void addBlocker(Blocker blocker){
        this.blockers.add(blocker);
    }
}
