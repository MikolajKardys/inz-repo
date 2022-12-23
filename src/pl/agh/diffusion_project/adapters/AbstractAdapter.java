package pl.agh.diffusion_project.adapters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAdapter {
    protected final Map<String, String> parameters = new HashMap<>();

    public abstract void runAdapter() throws Exception;

    public void setParameter(String parameter){
        if (parameter.contains("=")){
            String [] elements = parameter.split("=");
            parameters.put(elements[0], elements[1]);
        } else {
            parameters.put(parameter, "");
        }
    }

    protected void runCommands(String command, String errorString) throws Exception {
        Process p = Runtime.getRuntime().exec(String.format("%s", command));
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

        char c = (char) r.read();
        while (Character.isDefined(c)) {
            System.out.print(c);
            c = (char) r.read();
        }

        if (p.exitValue() != 0){
            String errorStr = new String(p.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            throw new Exception(errorString + errorStr);
        }
    }
}
