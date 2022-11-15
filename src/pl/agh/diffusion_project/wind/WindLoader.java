package pl.agh.diffusion_project.wind;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.javatuples.Triplet;
public class WindLoader {
    private final Triplet<Float, Float, Float> [][][] windField;

    private WindLoader(int dx, int dy, int dz){
        this.windField = new Triplet[dx][dy][dz];
    }

    public static WindLoader loadWindFromFile(String fileName) throws IOException {
        File myObj = new File(fileName);
        Scanner myReader = new Scanner(myObj);
        int dx = 0, dy = 0, dz = 0;
        WindLoader windLoader = new WindLoader(0,0,0);
        int lineNumber = 0;
        while(myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if(lineNumber == 0) {
                dx = Integer.parseInt(data.split(";")[0]);
                dy = Integer.parseInt(data.split(";")[1]);
                dz = Integer.parseInt(data.split(";")[2]);
                windLoader = new WindLoader(dx, dy, dz);
                lineNumber++;
            } else {
                String[] vectors =  data.split(" ");
                for(int i=0; i<dx; i++) {
                    for(int j=0; j<dy; j++) {
                        for(int k=0; k<dz; k++) {
                            int index = i*dx + j*dy + k;
                            String[] parts = vectors[index].split(";");
                            windLoader.windField[i][j][k] = new Triplet<>(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
                        }
                    }
                }
            }
        }

        return new WindLoader(dx, dy, dz);
    }
}
