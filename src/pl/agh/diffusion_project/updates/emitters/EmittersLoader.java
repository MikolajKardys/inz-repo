package pl.agh.diffusion_project.updates.emitters;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EmittersLoader {
    private final int dx, dy;
    private final int [][] pixelsValues;

    public EmittersLoader(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
        this.pixelsValues = new int[dx][dy];
    }

    public static EmittersLoader loadEmittersFromBitmap(String fileName) throws IOException {
        BufferedImage img = ImageIO.read(new File(fileName));

        EmittersLoader loader = new EmittersLoader(img.getWidth(), img.getHeight());

        for (int i = 0; i < loader.dx; i++)
            for (int j = 0; j < loader.dy; j++)
                loader.pixelsValues[i][j] = new Color(img.getRGB(i, j)).getBlue();

        return loader;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getEmitterValue(int i, int j) {
        return pixelsValues[i][j];
    }
}
