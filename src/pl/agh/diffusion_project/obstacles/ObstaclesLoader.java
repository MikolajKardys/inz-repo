package pl.agh.diffusion_project.obstacles;

import pl.agh.diffusion_project.obstacles.blocker.Blocker;
import pl.agh.diffusion_project.obstacles.blocker.SimpleBlocker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObstaclesLoader {
    private final int terrainX;
    private final int terrainY;
    private final int terrainZ;
    private final int [][] heightMatrix;

    private ObstaclesLoader(int imageX, int imageY, int mapHeight){
        this.terrainX = imageX;
        this.terrainY = imageY;
        this.terrainZ = mapHeight;
        this.heightMatrix = new int[imageX][imageY];
    }

    public static ObstaclesLoader loadObstaclesFromBitmap(String fileName, int blockSize, int mapHeight) throws IOException {
        BufferedImage img = ImageIO.read(new File(fileName));

        ObstaclesLoader loader = new ObstaclesLoader(img.getHeight(), img.getWidth(), mapHeight);

        for (int i = 0; i < loader.terrainX; i++){
            for  (int j = 0; j < loader.terrainY; j++){
                int height = (255 - new Color(img.getRGB(j, i)).getBlue()) / blockSize;

                if (height > 0){
                    loader.heightMatrix[i][j] = height;
                }
            }
        }

        return loader;
    }

    public static ObstaclesLoader loadObstaclesFromBitmap(String fileName, int mapHeight) throws IOException {
        return loadObstaclesFromBitmap(fileName, 1, mapHeight);
    }

    public List<Blocker> toBlockerList(){
        List<Blocker> blockers = new ArrayList<>();

        for (int i = 0; i < this.terrainX; i++){
            for (int j = 0; j < this.terrainY; j++){
                final int height = heightMatrix[i][j];

                if (height > 0) {
                    blockers.add(new SimpleBlocker(
                            i, i + 1,
                            0, height,
                            j + 1, j + 2
                    ));
                }
            }
        }

        return blockers;
    }

    public List<Integer> getBlockerDims(){
        return List.of(this.terrainX, 255, this.terrainY);
    }

    public void saveInDataFile(String fileName){
        final List<Blocker> blockers = this.toBlockerList();

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName, false))) {
            out.writeInt(this.terrainX);
            out.writeInt(this.terrainZ);
            out.writeInt(this.terrainY);

            out.writeInt(blockers.size());

            for (Blocker blocker : blockers){
                out.write(blocker.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean isBuilding(int x, int y, int z) {
        return heightMatrix[x][y] > z;
    }
}