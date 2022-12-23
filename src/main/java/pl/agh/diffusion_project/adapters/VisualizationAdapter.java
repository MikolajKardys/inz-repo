package pl.agh.diffusion_project.adapters;

import org.json.simple.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class VisualizationAdapter extends AbstractAdapter {
    private final String vizPath;

    private final String nodePath;
    private final String dataPath;

    public VisualizationAdapter(String vizPath) {
        this.vizPath = vizPath;

        this.nodePath = "";
        this.dataPath = "/public/data";
    }

    public String getBuildingDataPath () {
        return this.vizPath + this.dataPath + "/obstacles.dat";
    }

    public String getRoadDataPath () {
        return this.vizPath + this.dataPath + "/roads.dat";
    }

    public String getPollutionDataPath (int iteration) {
        return this.vizPath + this.dataPath + String.format("/iterations/results%d.dat", iteration);
    }

    public void clearIterations () {
        File dir = new File(this.vizPath + this.dataPath + "/iterations");
		if (!dir.exists()){
			dir.mkdirs();
		}
        for(File file: Objects.requireNonNull(dir.listFiles()))
            if (!file.isDirectory()) {
                file.delete();
            }
    }

    @SuppressWarnings("unchecked")
    public void generateConfig (int width, int length, int height, int iterationsNum, boolean readBuildings, boolean readRoads) {
        String configPath = this.vizPath + this.dataPath + "/sim-config.json";

        JSONObject mainJson = new JSONObject();

        JSONObject dims = new JSONObject();
        dims.put("width", width);
        dims.put("length", length);
        dims.put("height", height);
        mainJson.put("dimensions", dims);

        if (readBuildings)
            mainJson.put("buildings-file", "./data/obstacles.dat");
        else
            mainJson.put("buildings-file", "");
        if (readRoads)
            mainJson.put("roads-file", "./data/roads.dat");
        else
            mainJson.put("roads-file", "");


        JSONObject pollutionData = new JSONObject();
        pollutionData.put("iteration-number", iterationsNum);
        pollutionData.put("pollution-file-prefix", "./data/iterations/results");

        mainJson.put("pollution-data", pollutionData);

        try {
            FileWriter file = new FileWriter(configPath);
            file.write(mainJson.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runAdapter() throws Exception {
        String command = "npm start --prefix " + this.vizPath + " " + this.nodePath;

        this.runCommands(command, "An error has occurred in VisualizationAdapter:\n");
    }
}
