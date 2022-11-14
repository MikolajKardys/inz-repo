package pl.agh.diffusion_project.obstacles;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Obstacle {
    protected final int x1, x2, y1, y2, z1, z2;

    protected Obstacle(int x1, int x2, int y1, int y2, int z1, int z2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
    }

    abstract protected int getType();

    public byte [] getBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(getType());
        dos.writeInt(x1);
        dos.writeInt(x2);
        dos.writeInt(y1);
        dos.writeInt(y2);
        dos.writeInt(z1);
        dos.writeInt(z2);
        dos.flush();
        return bos.toByteArray();
    }
}
