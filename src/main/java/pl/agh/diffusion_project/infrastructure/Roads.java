package pl.agh.diffusion_project.infrastructure;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Roads extends CityElements {

    private Roads(int imageX, int imageY, int mapHeight){
        super(imageX, imageY, mapHeight);
    }

    protected static Roads loadRoadsFromFile(String fileName, int mapHeight) throws IOException {
        BufferedImage img = ImageIO.read(new File(fileName));

        Roads loader = new Roads(img.getWidth(), img.getHeight(), mapHeight);

        for (int i = 0; i < loader.terrainX; i++){
            for  (int j = 0; j < loader.terrainY; j++){
                loader.matrix[i][j] = img.getRGB(i, j);
            }
        }

        return loader;
    }
}
