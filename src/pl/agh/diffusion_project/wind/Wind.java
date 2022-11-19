package pl.agh.diffusion_project.wind;

import org.javatuples.Quartet;
import org.javatuples.Triplet;
import pl.agh.diffusion_project.obstacles.ObstaclesLoader;

import java.util.ArrayList;

public class Wind {
    private final int dx, dy, dz;
    private final ArrayList<Quartet<Byte, Byte, Byte, Float>>[][][] windCoef;
    private final float [][][] windSpeed;
    private final float [][][] incrementor;
    private float MAX_WIND_SPEED = 0F;
    public Wind(WindLoader windLoader, ObstaclesLoader obstaclesLoader) {
        this.dx = windLoader.getDx();
        this.dy = windLoader.getDy();
        this.dz = windLoader.getDz();
        this.windCoef = new ArrayList[dx][dy][dz];
        this.incrementor = new float[dx][dy][dz];
        this.windSpeed = new float[dx][dy][dz];

        float speed;
        for(int i=0; i<dx; i++) {
            for(int j=0; j<dy; j++) {
                for(int k=0; k<dz; k++) {
                    Triplet<Float, Float, Float> vector = windLoader.getVector(i, j, k);
                    speed = (float)Math.sqrt(vector.getValue0()*vector.getValue0() + vector.getValue1()*vector.getValue1() + vector.getValue2()*vector.getValue2());
                    windSpeed[i][j][k] = speed;
                    if(obstaclesLoader.isBuilding(i, j, k))
                        windSpeed[i][j][k] = -1F;
                    if(speed > MAX_WIND_SPEED)
                        MAX_WIND_SPEED = speed;
                    windCoef[i][j][k] = getNeiWithCoef(i, j, k, vector);
                }
            }
        }
    }

    private ArrayList<Quartet<Byte, Byte, Byte, Float>> getNeiWithCoef(int myi, int myj, int myk, Triplet<Float, Float, Float> vector) {
        float x = vector.getValue0();
        float y = vector.getValue1();
        float z = vector.getValue2();
        float theta = getAngle(x, y);
        float fi = getAngle((float)Math.sqrt(x * x / 2 + y * y / 2), z);
        ArrayList<Triplet<Byte, Byte, Float>> xy = new ArrayList<>();
        if(theta == 0F) {
            //System.out.print("I");
            xy.add(new Triplet<>((byte)1, (byte)0, 1F));
        } else if(theta > 0F && theta < (float)Math.PI/4) {
            //System.out.print("I/II");
            float coef = theta/((float)Math.PI/4);
            xy.add(new Triplet<>((byte)1, (byte)0, coef));
            xy.add(new Triplet<>((byte)1, (byte)1, 1F - coef));
        } else if(theta == (float)Math.PI/4) {
            //System.out.print("II");
            xy.add(new Triplet<>((byte)1, (byte)1, 1F));
        } else if(theta > (float)Math.PI/4 && theta < (float)Math.PI/2) {
            //System.out.print("II/III");
            float coef = (theta-(float)Math.PI/4)/((float)Math.PI/4);
            xy.add(new Triplet<>((byte)1, (byte)1, coef));
            xy.add(new Triplet<>((byte)0, (byte)1, 1F - coef));
        } else if(theta == (float)Math.PI/2) {
            //System.out.print("III");
            xy.add(new Triplet<>((byte)0, (byte)1, 1F));
        } else if(theta > (float)Math.PI/2 && theta < (float)3*Math.PI/4) {
            //System.out.print("III/IV");
            float coef = (theta-(float)Math.PI/2)/((float)Math.PI/4);
            xy.add(new Triplet<>((byte)0, (byte)1, coef));
            xy.add(new Triplet<>((byte)-1, (byte)1, 1F - coef));
        } else if(theta == (float)3*Math.PI/4) {
            //System.out.print("IV");
            xy.add(new Triplet<>((byte)-1, (byte)1, 1F));
        } else if(theta > (float)3*Math.PI/4 && theta < (float)Math.PI) {
            //System.out.print("IV/V");
            float coef = (theta-(float)(3*Math.PI/4))/((float)Math.PI/4);
            xy.add(new Triplet<>((byte)-1, (byte)1, coef));
            xy.add(new Triplet<>((byte)-1, (byte)0, 1F - coef));
        } else if(theta == (float)Math.PI) {
            //System.out.print("V");
            xy.add(new Triplet<>((byte)-1, (byte)0, 1F));
        } else if(theta < 0 && theta > (float)-Math.PI/4) {
            //System.out.print("I/VIII");
            float coef = -theta/((float)Math.PI/4);
            xy.add(new Triplet<>((byte)1, (byte)0, coef));
            xy.add(new Triplet<>((byte)1, (byte)-1, 1F - coef));
        } else if(theta == (float)-Math.PI/4) {
            //System.out.print("VIII");
            xy.add(new Triplet<>((byte)1, (byte)-1, 1F));
        } else if(theta < (float)-Math.PI/4 && theta > (float)-Math.PI/2) {
            //System.out.print("VII/VIII");
            float coef = (-theta-(float)Math.PI/4)/((float)Math.PI/4);
            xy.add(new Triplet<>((byte)1, (byte)-1, coef));
            xy.add(new Triplet<>((byte)0, (byte)-1, 1F - coef));
        } else if(theta == (float)-Math.PI/2) {
            //System.out.print("VII");
            xy.add(new Triplet<>((byte)0, (byte)-1, 1F));
        } else if(theta < (float)-Math.PI/2 && theta > (float)-3*Math.PI/4) {
            //System.out.print("VI/VII");
            float coef = (-theta-(float)Math.PI/2)/((float)Math.PI/4);
            xy.add(new Triplet<>((byte)0, (byte)-1, coef));
            xy.add(new Triplet<>((byte)-1, (byte)-1, 1F - coef));
        } else if(theta == (float)(-3*Math.PI/4)) {
            //System.out.print("VI");
            xy.add(new Triplet<>((byte)-1, (byte)-1, 1F));
        } else if(theta < (float)-3*Math.PI/4 && theta > (float)-Math.PI) {
            //System.out.print("V/VI");
            float coef = (-theta-(float)(3*Math.PI/4))/((float)Math.PI/4);
            xy.add(new Triplet<>((byte)-1, (byte)-1, coef));
            xy.add(new Triplet<>((byte)-1, (byte)0, 1F - coef));
        }

        ArrayList<Quartet<Byte, Byte, Byte, Float>> xyz = new ArrayList<>();
        if(fi == 0F) {
            //System.out.print(" I\n");
            for(Triplet<Byte, Byte, Float> t : xy)
                xyz.add(new Quartet<>(t.getValue0(), t.getValue1(), (byte)0, t.getValue2()));
        } else if(fi > 0F && fi < (float)Math.PI/4) {
            //System.out.print(" I/II\n");
            for(Triplet<Byte, Byte, Float> t : xy) {
                float coef1 = fi/((float)Math.PI/4);
                float coef2 = 1F - coef1;
                xyz.add(new Quartet<>(t.getValue0(), t.getValue1(), (byte)0, coef1*t.getValue2()));
                xyz.add(new Quartet<>(t.getValue0(), t.getValue1(), (byte)1, coef2*t.getValue2()));
            }
        } else if(fi == (float)Math.PI/4) {
            //System.out.print(" II\n");
            for(Triplet<Byte, Byte, Float> t : xy)
                xyz.add(new Quartet<>(t.getValue0(), t.getValue1(), (byte)1, t.getValue2()));
        } else if(fi > (float)Math.PI/4 && fi < (float)Math.PI/2) {
            //System.out.print(" II/III\n");
            for(Triplet<Byte, Byte, Float> t : xy) {
                float coef1 = (fi-(float)Math.PI/4)/((float)Math.PI/4);
                float coef2 = 1F - coef1;
                xyz.add(new Quartet<>(t.getValue0(), t.getValue1(), (byte)1, coef1*t.getValue2()));
                xyz.add(new Quartet<>(t.getValue0(), t.getValue1(), (byte)1, coef2*t.getValue2()));
            }
        } else if(fi == (float)Math.PI/2) {
            //System.out.print(" III\n");
            xyz.add(new Quartet<>((byte)0, (byte)0, (byte)1, 1F));
        } else if(fi < 0F && fi > -(float)Math.PI/4) {
            //System.out.print(" -I/II\n");
            for(Triplet<Byte, Byte, Float> t : xy) {
                float coef1 = -fi/((float)Math.PI/4);
                float coef2 = 1F - coef1;
                xyz.add(new Quartet<>(t.getValue0(), t.getValue1(), (byte)0, coef1*t.getValue2()));
                xyz.add(new Quartet<>(t.getValue0(), t.getValue1(), (byte)-1, coef2*t.getValue2()));
            }
        } else if(fi == -(float)Math.PI/4) {
            //System.out.print(" -II\n");
            for(Triplet<Byte, Byte, Float> t : xy)
                xyz.add(new Quartet<>(t.getValue0(), t.getValue1(), (byte)-1, t.getValue2()));
        } else if(fi < -(float)Math.PI/4 && fi > -(float)Math.PI/2) {
            //System.out.print(" -II/III\n");
            for(Triplet<Byte, Byte, Float> t : xy) {
                float coef1 = (-fi-(float)Math.PI/4)/((float)Math.PI/4);
                float coef2 = 1F - coef1;
                xyz.add(new Quartet<>(t.getValue0(), t.getValue1(), (byte)-1, coef1*t.getValue2()));
                xyz.add(new Quartet<>(t.getValue0(), t.getValue1(), (byte)-1, coef2*t.getValue2()));
            }
        } else if(fi == -(float)Math.PI/2) {
            //System.out.print(" -III\n");
            xyz.add(new Quartet<>((byte)0, (byte)0, (byte)-1, 1F));
        }

        Byte i, j, k;
        ArrayList<Quartet<Byte, Byte, Byte, Float>> result = new ArrayList<>();
        float sum = 0F;
        for(Quartet<Byte, Byte, Byte, Float> q : xyz) {
            i = q.getValue0();
            j = q.getValue1();
            k = q.getValue2();
            if(myi+i >= 0 && myj+j >= 0 && myk+k >= 0 && myi+i < dx && myj+j < dy && myk+k < dz)
                if (windSpeed[myi + i][myj + j][myk + k] >= 0) {
                    result.add(new Quartet<>(i, j, k, q.getValue3()));
                    sum += q.getValue3();
                }
        }
        ArrayList<Quartet<Byte, Byte, Byte, Float>> result2 = new ArrayList<>();
        for(Quartet<Byte, Byte, Byte, Float> q : xyz) {
            i = q.getValue0();
            j = q.getValue1();
            k = q.getValue2();
            if(myi+i >= 0 && myj+j >= 0 && myk+k >= 0 && myi+i < dx && myj+j < dy && myk+k < dz)
                result2.add(new Quartet<>(i, j, k, q.getValue3()/sum));
        }

        return result2;
    }

    private float getAngle(float x, float y) {
        return (float)Math.atan2(y, x);
    }

    public void updateWind(float[][][] oldPollutions) {
        float[][][] newPollutions = new float[dx][dy][dz];

        //for(int m=0; m<MAX_WIND_SPEED; m++) {
            for (int i = 0; i < dx; i++) {
                for (int j = 0; j < dy; j++) {
                    for (int k = 0; k < dz; k++) {
                        updateMicroWind(oldPollutions, newPollutions, i, j, k);
                    }
                }
            }
            for (int i = 0; i < dx; i++) {
                for (int j = 0; j < dy; j++) {
                    for (int k = 0; k < dz; k++) {
                        u(oldPollutions, newPollutions, i, j, k);
                    }
                }
            }
        //}
    }

    private void updateMicroWind(float[][][] oldPollutions, float[][][] newPollutions, int i, int j, int k) {
        incrementor[i][j][k] += windSpeed[i][j][k];
        if(incrementor[i][j][k] >= MAX_WIND_SPEED) {
            System.out.println(windCoef[i][j][k]);
            for(Quartet<Byte, Byte, Byte, Float> q : windCoef[i][j][k]) {
                newPollutions[i + q.getValue0()][j + q.getValue1()][k + q.getValue2()] += q.getValue3() * oldPollutions[i][j][k];
            }
            if(windCoef[i][j][k].size() > 0)
                oldPollutions[i][j][k] = 0F;
        }
    }

    private void u(float[][][] oldPollutions, float[][][] newPollutions, int i, int j, int k) {
        if(incrementor[i][j][k] >= MAX_WIND_SPEED) {
            oldPollutions[i][j][k] += newPollutions[i][j][k];
            newPollutions[i][j][k] = 0F;

            incrementor[i][j][k] -= MAX_WIND_SPEED;
        }
    }
}
