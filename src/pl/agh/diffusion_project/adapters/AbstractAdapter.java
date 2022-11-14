package pl.agh.diffusion_project.adapters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class AbstractAdapter {
    protected void runCommands(String command, String errorString) throws Exception {
        Process p = Runtime.getRuntime().exec(String.format("cmd.exe /c start cmd /c \"%s & pause\"", command));
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
