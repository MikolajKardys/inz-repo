package pl.agh.diffusion_project.wind;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.javatuples.Triplet;
public class WindLoader {
    private final int dx, dy, dz;
    private final Triplet<Float, Float, Float> [][][] windField;

    private WindLoader(int dx, int dy, int dz){
        this.dx=dx;
        this.dy=dy;
        this.dz=dz;
        this.windField = new Triplet[dx][dy][dz];
    }

    public int getDx() {
        return dx;
    }
    public int getDy() {
        return dy;
    }
    public int getDz() {
        return dz;
    }
    public Triplet<Float, Float, Float> getVector(int i, int j, int k) {
        return windField[i][j][k];
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
                int index = 0;
                for(int i=0; i<dx; i++) {
                    for(int j=0; j<dy; j++) {
                        for(int k=0; k<dz; k++) {
                            String[] parts = vectors[index].split(";");

                            float x = Math.round(1000*Float.parseFloat(parts[0]))/1000F;
                            float y = Math.round(1000*Float.parseFloat(parts[1]))/1000F;
                            float z = Math.round(1000*Float.parseFloat(parts[2]))/1000F;
                            windLoader.windField[i][j][k] = new Triplet<>(x, y, z);

                            index += 1;
                        }
                    }
                }
            }
        }

        return windLoader;
    }
}
