/**
 * @author ${Dominik Sulik}
 */

package pl.agh.diffusion_project.updates.wind;

import org.javatuples.Quartet;
import org.javatuples.Triplet;
import pl.agh.diffusion_project.infrastructure.CityHolder;

import java.util.ArrayList;

public class Wind {
    private final int dx, dy, dz;
    private final ArrayList<Quartet<Short, Short, Short, Float>>[][][] neighborsFactors;
    private final float [][][] windSpeed;
    private final float [][][] incrementor;
    private float MAX_WIND_SPEED = 0F;

    static private final float PI = (float)Math.PI;
    static private final float PI_DIV_2 = (float)Math.PI/2;
    static private final float PI3_DIV_4 = (float)(3*Math.PI/4);
    static private final float PI_DIV_4 = (float)Math.PI/4;

    private final boolean PRINT_SECTOR = false;

    public Wind(WindLoader windLoader) {
        this.dx = windLoader.getDy();
        this.dy = windLoader.getDx();
        this.dz = windLoader.getDz();
        this.neighborsFactors = new ArrayList[dx][dy][dz];
        this.incrementor = new float[dx][dy][dz];
        this.windSpeed = new float[dx][dy][dz];

        float speed;
        for(int i=0; i<dx; i++) {
            for(int j=0; j<dy; j++) {
                for(int k=0; k<dz; k++) {
                    Triplet<Float, Float, Float> vector = windLoader.getVector(j, i, k);
                    speed = (float)Math.sqrt(vector.getValue0()*vector.getValue0() + vector.getValue1()*vector.getValue1() + vector.getValue2()*vector.getValue2());

                    if(CityHolder.getBuildingsInstance().isBuilding(i, j, k))
                        windSpeed[i][j][k] = -1F;
                    else
                        windSpeed[i][j][k] = speed;

                    if(speed > MAX_WIND_SPEED)
                        MAX_WIND_SPEED = speed;
                }
            }
        }

        for(int i=0; i<dx; i++) {
            for(int j=0; j<dy; j++) {
                for(int k=0; k<dz; k++) {
                    Triplet<Float, Float, Float> vector = windLoader.getVector(j, i, k);
                    neighborsFactors[i][j][k] = getNeighborsFactors(i, j, k, vector);
                }
            }
        }
        MAX_WIND_SPEED = 10;
    }

    private ArrayList<Quartet<Short, Short, Short, Float>> getNeighborsFactors(
            int i, int j, int k,Triplet<Float, Float, Float> vector) {

        float x = vector.getValue0();
        float y = vector.getValue1();
        float z = vector.getValue2();
        float theta = getAngle(x, y);
        float fi = getAngle((float)Math.sqrt(x * x / 2 + y * y / 2), z);
        ArrayList<Triplet<Integer, Integer, Float>> xyFactors = new ArrayList<>();

        if(theta == 0F) {
            if(PRINT_SECTOR)
                System.out.println("I");
            xyFactors.add(new Triplet<>(1, 0, 1F));
        } else if(theta > 0F && theta < PI_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("I/II");
            float factor = theta/PI_DIV_4;
            xyFactors.add(new Triplet<>(1, 0, 1F - factor));
            xyFactors.add(new Triplet<>(1, 1, factor));
        } else if(theta == PI_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("II");
            xyFactors.add(new Triplet<>(1, 1, 1F));
        } else if(theta > PI_DIV_4 && theta < PI_DIV_2) {
            if(PRINT_SECTOR)
                System.out.println("II/III");
            float factor = (theta-PI_DIV_4)/PI_DIV_4;
            xyFactors.add(new Triplet<>(1, 1, 1F- factor));
            xyFactors.add(new Triplet<>(0, 1, factor));
        } else if(theta == PI_DIV_2) {
            if(PRINT_SECTOR)
                System.out.println("III");
            xyFactors.add(new Triplet<>(0, 1, 1F));
        } else if(theta > PI_DIV_2 && theta < PI3_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("III/IV");
            float factor = (theta-PI_DIV_2)/PI_DIV_4;
            xyFactors.add(new Triplet<>(0, 1, 1F - factor));
            xyFactors.add(new Triplet<>(-1, 1, factor));
        } else if(theta == PI3_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("IV");
            xyFactors.add(new Triplet<>(-1, 1, 1F));
        } else if(theta > PI3_DIV_4 && theta < PI) {
            if(PRINT_SECTOR)
                System.out.println("IV/V");
            float factor = (theta-PI3_DIV_4)/PI_DIV_4;
            xyFactors.add(new Triplet<>(-1, 1, 1F - factor));
            xyFactors.add(new Triplet<>(-1, 0, factor));
        } else if(theta == PI) {
            if(PRINT_SECTOR)
                System.out.println("V");
            xyFactors.add(new Triplet<>(-1, 0, 1F));
        } else if(theta < 0 && theta > -PI_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("I/VIII");
            float factor = -theta/PI_DIV_4;
            xyFactors.add(new Triplet<>(1, 0, 1F - factor));
            xyFactors.add(new Triplet<>(1, -1, factor));
        } else if(theta == -PI_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("VIII");
            xyFactors.add(new Triplet<>(1, -1, 1F));
        } else if(theta < -PI_DIV_4 && theta > -PI_DIV_2) {
            if(PRINT_SECTOR)
                System.out.println("VII/VIII");
            float factor = (-theta-PI_DIV_4)/PI_DIV_4;
            xyFactors.add(new Triplet<>(1, -1, 1F - factor));
            xyFactors.add(new Triplet<>(0, -1, factor));
        } else if(theta == -PI_DIV_2) {
            if(PRINT_SECTOR)
                System.out.println("VII");
            xyFactors.add(new Triplet<>(0, -1, 1F));
        } else if(theta < -PI_DIV_2 && theta > -PI3_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("VI/VII");
            float factor = (-theta-PI_DIV_2)/PI_DIV_4;
            xyFactors.add(new Triplet<>(0, -1, 1F - factor));
            xyFactors.add(new Triplet<>(-1, -1, factor));
        } else if(theta == -PI3_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("VI");
            xyFactors.add(new Triplet<>(-1, -1, 1F));
        } else if(theta < -PI3_DIV_4 && theta > -PI) {
            if(PRINT_SECTOR)
                System.out.println("V/VI");
            float factor = (-theta-PI3_DIV_4)/PI_DIV_4;
            xyFactors.add(new Triplet<>(-1, -1, 1F - factor));
            xyFactors.add(new Triplet<>(-1, 0, factor));
        }

        ArrayList<Quartet<Integer, Integer, Integer, Float>> xyzFactors = new ArrayList<>();
        if(fi == 0F) {
            if(PRINT_SECTOR)
                System.out.println("0");
            for(Triplet<Integer, Integer, Float> t : xyFactors)
                xyzFactors.add(new Quartet<>(t.getValue0(), t.getValue1(), 0, t.getValue2()));
        } else if(fi > 0F && fi < PI_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("0/A");
            for(Triplet<Integer, Integer, Float> t : xyFactors) {
                float factor1 = fi/PI_DIV_4;
                float factor2 = 1F - factor1;
                xyzFactors.add(new Quartet<>(t.getValue0(), t.getValue1(), 0, factor2*t.getValue2()));
                xyzFactors.add(new Quartet<>(t.getValue0(), t.getValue1(), 1, factor1*t.getValue2()));
            }
        } else if(fi == PI_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("A");
            for(Triplet<Integer, Integer, Float> t : xyFactors)
                xyzFactors.add(new Quartet<>(t.getValue0(), t.getValue1(), 1, t.getValue2()));
        } else if(fi > PI_DIV_4 && fi < PI_DIV_2) {
            if(PRINT_SECTOR)
                System.out.println("A/B");
            for(Triplet<Integer, Integer, Float> t : xyFactors) {
                float factor1 = (fi-PI_DIV_4)/PI_DIV_4;
                float factor2 = 1F - factor1;
                xyzFactors.add(new Quartet<>(t.getValue0(), t.getValue1(), 1, factor2*t.getValue2()));
                xyzFactors.add(new Quartet<>(t.getValue0(), t.getValue1(), 1, factor1*t.getValue2()));
            }
        } else if(fi == PI_DIV_2) {
            if(PRINT_SECTOR)
                System.out.println("B");
            xyzFactors.add(new Quartet<>(0, 0, 1, 1F));
        } else if(fi < 0F && fi > -PI_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("0/-A");
            for(Triplet<Integer, Integer, Float> t : xyFactors) {
                float factor1 = -fi/PI_DIV_4;
                float factor2 = 1F - factor1;
                xyzFactors.add(new Quartet<>(t.getValue0(), t.getValue1(), 0, factor2*t.getValue2()));
                xyzFactors.add(new Quartet<>(t.getValue0(), t.getValue1(), -1, factor1*t.getValue2()));
            }
        } else if(fi == -PI_DIV_4) {
            if(PRINT_SECTOR)
                System.out.println("-A");
            for(Triplet<Integer, Integer, Float> t : xyFactors)
                xyzFactors.add(new Quartet<>(t.getValue0(), t.getValue1(), -1, t.getValue2()));
        } else if(fi < -PI_DIV_4 && fi > -PI_DIV_2) {
            if(PRINT_SECTOR)
                System.out.println("-A/-B");
            for(Triplet<Integer, Integer, Float> t : xyFactors) {
                float factor1 = (-fi-PI_DIV_4)/PI_DIV_4;
                float factor2 = 1F - factor1;
                xyzFactors.add(new Quartet<>(t.getValue0(), t.getValue1(), -1, factor2*t.getValue2()));
                xyzFactors.add(new Quartet<>(t.getValue0(), t.getValue1(), -1, factor1*t.getValue2()));
            }
        } else if(fi == -PI_DIV_2) {
            if(PRINT_SECTOR)
                System.out.println("-B");
            xyzFactors.add(new Quartet<>(0, 0, -1, 1F));
        }

        int iNeiInc, jNeiInc, kNeiInc;
        short iNeiS, jNeiS, kNeiS;
        ArrayList<Quartet<Short, Short, Short, Float>> xyzFactorsWithoutBuildings = new ArrayList<>();
        float sum = 0F;

        for(Quartet<Integer, Integer, Integer, Float> quartet : xyzFactors) {
            iNeiInc = quartet.getValue0();
            jNeiInc = quartet.getValue1();
            kNeiInc = quartet.getValue2();

            if(!neighbourExists(i, j, k, iNeiInc, jNeiInc, kNeiInc) || windSpeed[i+iNeiInc][j+jNeiInc][k+kNeiInc] >= 0) {
                xyzFactorsWithoutBuildings.add(
                        new Quartet<>((short) iNeiInc, (short) jNeiInc, (short) kNeiInc, quartet.getValue3()));
                sum += quartet.getValue3();
            }
        }

        ArrayList<Quartet<Short, Short, Short, Float>> xyzFactorsWithIndexes = new ArrayList<>();
        for(Quartet<Short, Short, Short, Float> quartet : xyzFactorsWithoutBuildings) {
            iNeiInc = quartet.getValue0();
            jNeiInc = quartet.getValue1();
            kNeiInc = quartet.getValue2();
            iNeiS = (short)(i+iNeiInc);
            jNeiS = (short)(j+jNeiInc);
            kNeiS = (short)(k+kNeiInc);

            xyzFactorsWithIndexes.add(new Quartet<>(iNeiS, jNeiS, kNeiS, quartet.getValue3()/sum));
        }

        return xyzFactorsWithIndexes;
    }

    private boolean neighbourExists(int i, int j, int k, int iNei, int jNei, int kNei) {
        return i+iNei >= 0 && j+jNei >= 0 && k+kNei >= 0 && i+iNei < dx && j+jNei < dy && k+kNei < dz;
    }

    private float getAngle(float x, float y) {
        return (float)Math.atan2(y, x);
    }

    public void updateWind(float [][][] cells, float[][][] newCells, float [][][] temp, float[][][] newTemp, int x, int y, int z) {
        incrementor[x][y][z] += windSpeed[x][y][z];
        if(incrementor[x][y][z] >= MAX_WIND_SPEED) {
            for(Quartet<Short, Short, Short, Float> q : neighborsFactors[x][y][z]) {
                float lostPollution = cells[x][y][z] * q.getValue3();
                try {
                    newCells[q.getValue0()][q.getValue1()][q.getValue2()] += lostPollution;

                    float tempChange = temp[x][y][z] * q.getValue3();

                    newTemp[x][y][z] -= tempChange;
                    newTemp[q.getValue0()][q.getValue1()][q.getValue2()] += tempChange;
                } catch (IndexOutOfBoundsException ignore) {}
                newCells[x][y][z] -= lostPollution;
            }
            incrementor[x][y][z] -= MAX_WIND_SPEED;
        }
    }
}