package pl.agh.diffusion_project.adapters;


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

    public void runVisualization() throws Exception {
        String command = "npm start --prefix " + this.vizPath + " " + this.nodePath;

        this.runCommands(command, "An error has occurred in VisualizationAdapter:\n");
    }
}
