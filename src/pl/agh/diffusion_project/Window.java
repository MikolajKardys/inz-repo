package pl.agh.diffusion_project;

import pl.agh.diffusion_project.obstacles.blocker.SimpleBlocker;

public class Window extends SimpleBlocker {
    final int axis;
    public Window(int x1, int x2, int y1, int y2, int z1, int z2, int axis) {
        super(x1, x2, y1, y2, z1, z2); // free space in wall
        this.axis = axis; // specifies axis perpendicular to wall which window is in
    }

    @Override
    public boolean isBlocked(int x, int y, int z){
        return switch (this.axis) {
            case 1 -> x >= x1 && x < x2 &&
                    (y < y1 || y >= y2 ||
                    z < z1 || z >= z2);
            case 2 -> x < x1 && x >= x2 &&
                    y >= y1 && y < y2 &&
                    z < z1 && z >= z2;
            case 3 -> x < x1 && x >= x2 &&
                    y < y1 && y >= y2 &&
                    z >= z1 && z < z2;
            default -> false;
        };
    }
}
