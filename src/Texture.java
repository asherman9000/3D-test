import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture {
    public enum Textures {
        WOOD,
        BRICK,
        BLUESTONE,
        STONE,
        EMPTY
    }
    public static Texture wood = new Texture("res/wood.png", 281, 187);
    public static Texture brick = new Texture("res/redbrick.png", 271, 180);
    public static Texture bluestone = new Texture("res/bluestone.png", 288, 180);
    public static Texture stone = new Texture("res/greystone.png", 224, 180);
    public int[] pixels;
    private String loc;
    public final int SIZEX;
    public final int SIZEY;

    public Texture(String location, int sizeX, int sizeY) {
        loc = location;
        SIZEX = sizeX;
        SIZEY = sizeY;
        pixels = new int[SIZEX * SIZEY];
        load();
    }

    private void load() {
        try {
            BufferedImage image = ImageIO.read(new File(loc));
            int w = image.getWidth();
            int h = image.getHeight();
            image.getRGB(0, 0, w, h, pixels, 0, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Textures numToEnum(int num) {
        switch(num) {
            case 0:
                return Textures.WOOD;
            case 1:
                return Textures.BRICK;
            case 2:
                return Textures.BLUESTONE;
            case 3:
                return Textures.STONE;
            default:
                return Textures.EMPTY;
        }
    }
}