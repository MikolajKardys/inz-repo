/**
 * @author ${Mikołaj Kardyś}
 */

package pl.agh.diffusion_project.infrastructure;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Buildings extends CityElements {
    private int blockSize = 1;

    protected Buildings(int imageX, int imageY, int mapHeight) {
        super(imageX, imageY, mapHeight);
    }

    protected static Buildings loadBuildingsFromFile(String fileName, int blockSize, int mapHeight) throws IOException {
        BufferedImage img = ImageIO.read(new File(fileName));

        Buildings loader = new Buildings(img.getWidth(), img.getHeight(), mapHeight);

        for (int i = 0; i < loader.terrainX; i++) {
            for (int j = 0; j < loader.terrainY; j++) {
                int height = (255 - new Color(img.getRGB(i, j)).getBlue()) / blockSize;

                if (height > 0) {
                    loader.matrix[i][j] = height;
                }
            }
        }

        loader.blockSize = blockSize;

        return loader;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public Boolean isBuilding(int x, int y, int z) {
        return matrix[x][y] > z;
    }
}
