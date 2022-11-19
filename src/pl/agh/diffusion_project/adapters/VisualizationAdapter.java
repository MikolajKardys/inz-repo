package pl.agh.diffusion_project.adapters;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class VisualizationAdapter extends AbstractAdapter{
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

    public String getPollutionDataPath (int iteration) {
        return this.vizPath + this.dataPath + String.format("/iterations/results%d.dat", iteration);
    }

    public void generateConfig (int iterationsNum) {
        this.generateConfig(iterationsNum, true);
    }

    @SuppressWarnings("unchecked")
    public void generateConfig (int iterationsNum, boolean readBuildings) {
        String configPath = this.vizPath + this.dataPath + "/sim-config.json";

        JSONObject mainJson = new JSONObject();
        if (readBuildings)
            mainJson.put("buildings-file", "./data/obstacles.dat");
        else
            mainJson.put("buildings-file", "");

        JSONObject pollutionData = new JSONObject();
        pollutionData.put("iteration-number", iterationsNum);
        JSONArray iterations = new JSONArray();
        for (int i = 0; i < iterationsNum; i++)
            iterations.add(String.format("./data/iterations/results%d.dat", i));
        pollutionData.put("pollution-files", iterations);

        mainJson.put("pollution-data", pollutionData);

        try {
            FileWriter file = new FileWriter(configPath);
            file.write(mainJson.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runVisualization() throws Exception {
        String command = "npm.cmd start --prefix " + this.vizPath + " " + this.nodePath;

        this.runCommands(command, "An error has occurred in VisualizationAdapter:\n");
    }
}
