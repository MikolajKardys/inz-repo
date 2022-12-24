/**
 * @author ${Mikołaj Kardyś}
 */

package pl.agh.diffusion_project.infrastructure;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class CityElements {
    protected final int terrainX;
    protected final int terrainY;
    protected final int terrainZ;
    protected final int [][] matrix;

    protected CityElements(int imageX, int imageY, int mapHeight){
        this.terrainX = imageX;
        this.terrainY = imageY;
        this.terrainZ = mapHeight;
        this.matrix = new int[imageX][imageY];
    }

    public void saveInDataFile(String fileName){
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName, false))) {
            for (int i = 0; i < this.terrainX; i++){
                for (int j = 0; j < this.terrainY; j++){
                    out.writeInt(matrix[i][j]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
