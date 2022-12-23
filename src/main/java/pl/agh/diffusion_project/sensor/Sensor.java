package pl.agh.diffusion_project.sensor;

import pl.agh.diffusion_project.infrastructure.CityHolder;

import java.io.*;


public class Sensor {
    private final int[] ranges = new int[6];
    private final BufferedWriter  outputStream;

    public Sensor(float [][][] cells, int x, int y, int z, int range, String outputFile) throws IOException {
        int xRange = cells.length;
        int yRange = cells[0].length;
        int zRange = cells[0][0].length;

        this.ranges[0] = Math.max(x - range + 1, 0);
        this.ranges[2] = Math.max(y - range + 1, 0);
        this.ranges[4] = Math.max(z - range + 1, 0);

        this.ranges[1] = Math.min(xRange, x + range);
        this.ranges[3] = Math.min(yRange, y + range);
        this.ranges[5] = Math.min(zRange, z + range);

        this.outputStream = new BufferedWriter(new FileWriter(outputFile));
    }

    public void saveReading(float [][][] cells) throws IOException {
        float reading = 0;
        int readingsNumber = 0;

        for (int i = this.ranges[0]; i < this.ranges[1]; i++)
            for (int j = this.ranges[2]; j < this.ranges[3]; j++)
                for (int k = this.ranges[4]; k < this.ranges[5]; k++){
                    if (!CityHolder.getBuildingsInstance().isBuilding(i, j, k)){
                        reading += cells[i][j][k];
                        readingsNumber += 1;
                    }
                }

        System.out.println(reading / readingsNumber);

        this.outputStream.write(reading / readingsNumber + "\n");
    }

    public void close() throws IOException {
        this.outputStream.close();
    }
}
