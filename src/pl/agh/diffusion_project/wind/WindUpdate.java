package pl.agh.diffusion_project.wind;

import pl.agh.diffusion_project.CallableUpdate;
import pl.agh.diffusion_project.cells.RefactoredCell;

import java.io.IOException;

public class WindUpdate extends CallableUpdate {
    private final WindLoader windLoader;

    public WindUpdate(WindLoader windLoader) {
        this.windLoader = windLoader;
    }

    public WindUpdate(String windFile) throws IOException {
        this(WindLoader.loadWindFromFile(windFile));
    }

    @Override
    public void update(RefactoredCell[][][] cells, Integer x, Integer y, Integer z) {
        for (int i = 0; i < 26; i++) {
            int tx = x+neighbors[i][0];
            int ty = y+neighbors[i][2];
            int tz = z+neighbors[i][1];
        }
    }
}
