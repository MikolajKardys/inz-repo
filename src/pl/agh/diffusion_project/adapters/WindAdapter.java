package pl.agh.diffusion_project.adapters;

import java.util.HashMap;
import java.util.Map;

public class WindAdapter extends AbstractAdapter {
    private final String execFilePath;

    public WindAdapter (String execFilePath){
        this.execFilePath = execFilePath;
    }

    @Override
    public void runAdapter() throws Exception {
        StringBuilder command = new StringBuilder(execFilePath);

        for (String key : parameters.keySet()){
            String parameter = String.format(" -%s %s",key, parameters.get(key));
            command.append(parameter);
        }

        this.runCommands(command.toString(), "An error has occurred in wind calculator:\n");
    }
}
