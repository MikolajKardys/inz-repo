/**
 * @author ${Mikołaj Kardyś}
 */

package pl.agh.diffusion_project.infrastructure;

import org.json.simple.JSONObject;

import java.io.IOException;

public class CityHolder {
    private static Buildings buildingsInstance;
    private static Roads roadsInstance;

    public static Buildings getBuildingsInstance(){
        return buildingsInstance;
    }

    public static Roads getRoadsInstance(){
        return roadsInstance;
    }

    public static void loadBuildingsFromJSON(JSONObject object) throws IOException {
        String fileName = (String)((JSONObject) object.get("data-files")).get("buildings-file");
        int mapHeight = ((Long)((JSONObject) object.get("dimensions")).get("height")).intValue();
        int blockSize = ((Long)((JSONObject) object.get("dimensions")).get("block-size")).intValue();

        buildingsInstance = Buildings.loadBuildingsFromFile(fileName, blockSize, mapHeight);
    }

    public static void loadRoadsFromJSON(JSONObject object) throws IOException {
        String fileName = (String)((JSONObject) object.get("data-files")).get("roads-file");
        int mapHeight = ((Long)((JSONObject) object.get("dimensions")).get("height")).intValue();

        roadsInstance = Roads.loadRoadsFromFile(fileName, mapHeight);
    }
}
