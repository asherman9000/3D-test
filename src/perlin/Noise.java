package perlin;

import java.util.Random;

public class Noise {
    private static final Random r = new Random();
    private static final double seed = r.nextDouble();
    private static final Perlin p = new Perlin(10);

    public Noise () {
    }

    public static int setSeed(int x, int y) {
        r.setSeed((long) (p.OctavePerlin((x*seed)/765d , (y*seed)/765d, 0.45, 3, 3) * 1000));

        return r.nextInt(15);
    }
}
