package pl.agh.diffusion_project.convection;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TemperatureLoader {
    private final int dx, dy, dz;
    private final float[][][] temperature;

    private TemperatureLoader(int dx, int dy, int dz){
        this.dx=dx;
        this.dy=dy;
        this.dz=dz;
        this.temperature = new float[dx][dy][dz];
    }

    public int getDx() {
        return dx;
    }
    public int getDy() {
        return dy;
    }
    public int getDz() {
        return dz;
    }

    public float getTemperature(int i, int j, int k) {
        return temperature[i][j][k];
    }

    public static TemperatureLoader loadTemperatureFromFile(String fileName, int dx, int dy, int dz)
            throws IOException {

        TemperatureLoader temperatureLoader = new TemperatureLoader(dx, dy, dz);

        DataInputStream input = new DataInputStream(new FileInputStream(fileName));
        int i=0, j=0, k=0;
        float temp;
        while (input.available() > 0) {
            temp = input.readFloat();
            temperatureLoader.temperature[i][j][k] = temp;
            k++;
            if(k >= dz) {
                k=0;
                j++;
                if(j >= dy) {
                    j=0;
                    i++;
                }
            }
        }
        input.close();

        return temperatureLoader;
    }
}
