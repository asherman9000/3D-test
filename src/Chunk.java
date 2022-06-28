import perlin.Noise;

import java.util.ArrayList;

public class Chunk {
    private final int chunkX;
    private final int chunkY;
    private final ArrayList<Character> alphabet = new ArrayList<>();
    private String chunkList = "";

    public Chunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        alphabet.add('a');
        alphabet.add('b');
        alphabet.add('c');
        alphabet.add('d');
        alphabet.add('e');
        int x = 16 * chunkX;
        int y = 16 * chunkY;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                chunkList += Noise.setSeed(x, y) + "_";
                x++;
            }
            x = 16 * chunkX;
            y++;
        }
        //encodeString();
    }

    private void encodeString() {
        int length = 1;
        String type;
        StringBuilder num = new StringBuilder();
        String previousNum = "";
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < chunkList.length()-1; i++) {
            char c = chunkList.charAt(i);
            if (c == '_') {
                switch (num.toString()) {
                    case "0":
                        type = "a";
                        break;
                    case "1":
                        type = "b";
                        break;
                    case "2":
                        type = "c";
                        break;
                    case "3":
                        type = "d";
                        break;
                    default:
                        num = new StringBuilder("4");
                        type = "e";
                        break;
                }
                if (num.toString().equals(previousNum)) {
                    length++;
                } else {
                    encoded.append(length).append(type);
                    length = 1;
                }
                previousNum = num.toString();

                num = new StringBuilder();
                continue;
            }
            num.append(c);
        }
        chunkList = encoded.toString();
    }

    /**
     * Only decode an encoded string
     * Always encode string after decoding
     */
    private void decodeString()   {
        StringBuilder encoded = new StringBuilder();
        int length;
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < chunkList.length()-1; i++) {
            char c = chunkList.charAt(i);
            if (c == 'a'  || c == 'b' || c == 'c' || c == 'd' || c == 'e') {
                length = Integer.parseInt(num.toString());
                for (int j = 0; j < length; j++) {
                    encoded.append(alphabet.indexOf(c)).append("_");
                }
                num = new StringBuilder();
                continue;
            }
            num.append(c);
        }

        chunkList = encoded.toString();
    }

    public int getType(int x, int y) {
        //decodeString();
        int tempX = 0;
        int tempY = 0;
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < chunkList.length()-1; i++) {
            char c = chunkList.charAt(i);
            if (c == '_') {
                tempX ++;
                if (tempX == 16) {
                    tempY++;
                    tempX = 1;
                }
                if (tempX == x && tempY == y) {
                    //encodeString();
                    return Integer.parseInt(num.toString())+1;
                }
                num = new StringBuilder();

            } else {
                num.append(c);
            }
        }
        //encodeString();
        return 2;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }
}
