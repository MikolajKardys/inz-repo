package pl.agh.diffusion_project;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.agh.diffusion_project.adapters.*;
import pl.agh.diffusion_project.infrastructure.CityHolder;
import pl.agh.diffusion_project.updates.SimpleUpdatesHolder;
import pl.agh.diffusion_project.updates.emitters.SimpleEmitterUpdate;
import pl.agh.diffusion_project.updates.walls.SimpleWallUpdate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application {
    private final static String CONFIG_PATH = "./config.json";

    private static JSONObject jsonObject;

    private static EmittersAdapter emittersAdapter;
    private static FetchBuildingsAdapter fetchBuildingsAdapter;
    private static VisualizationAdapter vizAdapter;
    private static WindAdapter windAdapter;

    private static JSONObject getJSONConfig() throws IOException, ParseException {
        Path filePath = Path.of(CONFIG_PATH);
        String content = Files.readString(filePath);

        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(content);
    }

    private static void setupAdapters(){
        emittersAdapter = new EmittersAdapter((String)((JSONObject)jsonObject.get("adapters")).get("pollution-emitters-path"));
        fetchBuildingsAdapter = new FetchBuildingsAdapter((String)((JSONObject)jsonObject.get("adapters")).get("fetch-buildings-path"));
        vizAdapter = new VisualizationAdapter((String)((JSONObject)jsonObject.get("adapters")).get("visualization-path"));
        windAdapter = new WindAdapter((String)((JSONObject)jsonObject.get("adapters")).get("wind-calculator-path"));
    }

    private static void runSimulation() throws IOException {
        CityHolder.loadBuildingsFromJSON(jsonObject);
        CityHolder.loadRoadsFromJSON(jsonObject);

        int width = ((Long)((JSONObject)jsonObject.get("dimensions")).get("width")).intValue();
        int length = ((Long)((JSONObject)jsonObject.get("dimensions")).get("length")).intValue();
        int height = ((Long)((JSONObject)jsonObject.get("dimensions")).get("height")).intValue();
        int iterationNum = ((Long)jsonObject.get("iterations")).intValue();

        int iterationsSave = ((Long)jsonObject.get("iterations-save")).intValue();
        int iterationsNonWind = 5;

        Timeline emitterTimeline = new Timeline((JSONArray)((JSONObject)jsonObject.get("updates")).get("emitter-timeline"), iterationNum);
        Timeline wallTimeline = new Timeline((JSONArray)((JSONObject)jsonObject.get("updates")).get("wall-timeline"), iterationNum);

        Board board = new Board(width, length, height);
        board.addBuildings();

        SimpleUpdatesHolder holder = SimpleUpdatesHolder.loadUpdatesFromJSON(jsonObject);

        vizAdapter.clearIterations();

        CityHolder.getBuildingsInstance().saveInDataFile(vizAdapter.getBuildingDataPath());
        CityHolder.getRoadsInstance().saveInDataFile(vizAdapter.getRoadDataPath());
        vizAdapter.generateConfig(width, length, height, iterationNum / iterationsSave + 1, true, true);

        board.savePollution(vizAdapter.getPollutionDataPath(0));
        for (int i = 0; i < iterationNum; i++){
            SimpleEmitterUpdate.POLLUTION_FACTOR = emitterTimeline.getTimelineFactor(i);
            SimpleWallUpdate.POLLUTION_FACTOR = wallTimeline.getTimelineFactor(i);

            board.updateWithFunction(holder.getWindUpdate());
            if ((i + 1) % iterationsNonWind == 0){
                board.updateWithFunction(holder.getWallUpdate());
                board.updateWithFunction(holder.getDiffusionUpdate());
                board.updateWithFunction(holder.getEmitterUpdate());
            }

            if ((i + 1) % iterationsSave == 0){
                board.savePollution(vizAdapter.getPollutionDataPath((i + 1) / iterationsSave));
            }

            System.out.print(i + 1 + "/" + iterationNum + "\r");
        }
        System.out.print(iterationNum + "/" + iterationNum);
    }

    private static void runAdapter(String [] args) throws Exception {;
        AbstractAdapter runAdapter;

        switch (args[1]){
            case "calculate-emitters" ->
                runAdapter = emittersAdapter;
            case "fetch-buildings" ->
                runAdapter = fetchBuildingsAdapter;
            case "run-visualization" ->
                runAdapter = vizAdapter;
            case "calculate-wind" ->
                runAdapter = windAdapter;
            default -> throw new Exception("Unknown adapter selected");
        }

        for (int i = 2; i < args.length; i++){
            runAdapter.setParameter(args[i]);
        }

        runAdapter.runAdapter();
    }

    public static void main(String[] args) throws Exception {
        jsonObject = getJSONConfig();

        setupAdapters();

        String mainCommand = args[0];
        if (mainCommand.equals("run-simulation")){
            runSimulation();
        } else if (mainCommand.equals("run-adapter")) {
            runAdapter(args);
        } else {
            throw new Exception("Unknown main command selected");
        }
    }
}
