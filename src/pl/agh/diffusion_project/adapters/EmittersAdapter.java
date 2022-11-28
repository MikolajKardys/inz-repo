package pl.agh.diffusion_project.adapters;

import java.util.HashMap;
import java.util.Map;


public class EmittersAdapter extends AbstractAdapter{
    private final Map<String, String> parameters = new HashMap<>();
    private final String execFilePath;

    public EmittersAdapter (String execFilePath){
        this.execFilePath = execFilePath;
    }

    public void setParameter(String key, String value){
        parameters.put(key, value);
    }

    public void createEmitters() throws Exception {
        StringBuilder command = new StringBuilder(execFilePath);

        for (String key : parameters.keySet()){
            String parameter = String.format(" --%s %s",key, parameters.get(key));
            command.append(parameter);
        }

        this.runCommands(command.toString(), "An error has occurred in emitters creation:\n");
    }
}