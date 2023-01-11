/**
 * @authors ${Radosław Bany}; ${Mikołaj Kardyś}; ${Dominik Sulik}
 */

package pl.agh.diffusion_project;

import pl.agh.diffusion_project.infrastructure.CityHolder;
import pl.agh.diffusion_project.sensor.Sensor;
import pl.agh.diffusion_project.updates.CallableUpdate;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Board {
    private final Integer width;
    private final Integer length;
    private final Integer height;

    private final float[][][] cells;
    private final float[][][] newCells;

    private final float[][][] temp;
    private final float[][][] newTemp;

    private final Map<String, float[][][]> data = new HashMap<>();

    List<Integer> indexList;

    private final List<Sensor> sensors = new LinkedList<>();

    public Board(int width, int length, int height){
        this.width = width;
        this.length = length;
        this.height = height;

        this.newCells = new float[width][length][height];
        this.cells = new float[width][length][height];

        this.newTemp = new float[width][length][height];
        this.temp = new float[width][length][height];

        data.put("pollution", cells);
        data.put("new-pollution", newCells);

        data.put("temperature", temp);
        data.put("new-temperature", newTemp);

        indexList = IntStream.rangeClosed(0, width * length * height - 1).boxed().collect(Collectors.toList());
    }

    public Map<String, float[][][]> getData(){
        return data;
    }

    public void addBuildings() {
        for(int i=0; i<width; i++)
            for(int j=0; j<length; j++)
                for(int k=0; k<height; k++)
                    if (CityHolder.getBuildingsInstance().isBuilding(i, j, k))
                        cells[i][j][k] = -1.0f; // BUILDING
    }

    public void updateWithNewValues() {
        indexList.parallelStream().forEach(ind -> {
            int i = ind / (length * height) % width;
            int j = (ind / height) % length;
            int k = ind % height;

            cells[i][j][k] = newCells[i][j][k];
            temp[i][j][k] = newTemp[i][j][k];
        });
    }

    public void updateWithFunction(CallableUpdate callableUpdate) {
        if (callableUpdate.allowParallelUpdate())
            indexList.parallelStream().forEach(ind -> {
                int i = ind / (length * height) % width;
                int j = (ind / height) % length;
                int k = ind % height;

                callableUpdate.update(data, i, j, k);
            });
        else
            for(int i=0; i<width; i++)
                for(int j=0; j<length; j++)
                    for(int k=0; k<height; k++)
                        callableUpdate.update(data, i, j, k);

        updateWithNewValues();
    }

    public void addSensor(int x, int y, int z, int range, String outputFile) throws IOException {
        sensors.add(new Sensor(cells, x, y, z, range, outputFile));
    }

    public void updateSensors() throws IOException {
        for (Sensor s : sensors){
            s.saveReading(cells);
        }
    }

    public void saveSensors() throws IOException {
        for (Sensor s : sensors){
            s.close();
        }
    }

    public void savePollution(String fileName) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName, false))) {
            int x = cells[0].length;
            int y = cells[0][0].length;
            int z = cells.length;

            byte [] bytes = new byte[4 * x * y * z];
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    for (int k = 0; k < z; k++) {
                        int bits = Float.floatToIntBits(cells[k][i][j]);

                        int index = (i * y * z + j * z + k) * 4;

                        bytes[index + 3] = (byte)(bits & 0xff);
                        bytes[index + 2] = (byte)((bits >> 8) & 0xff);
                        bytes[index + 1] = (byte)((bits >> 16) & 0xff);
                        bytes[index] = (byte)((bits >> 24) & 0xff);
                    }
                }
            }
            out.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
