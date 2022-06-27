import perlin.Noise;

import java.util.ArrayList;
import java.awt.Color;
import java.util.Random;

public class Screen {
    private Noise n = new Noise();
    public int[][] map;
    public int mapWidth, mapHeight, width, height;
    public ArrayList<Texture> textures;
    private ArrayList<Chunk> chunks = new ArrayList<>();

    public Screen(int[][] m, int mapW, int mapH, ArrayList<Texture> tex, int w, int h) {
        map = m;
        mapWidth = mapW;
        mapHeight = mapH;
        textures = tex;
        width = w;
        height = h;
    }

    public int[] update(Camera camera, int[] pixels) {
        for(int n=0; n<pixels.length/2; n++) {
            if(pixels[n] != Color.DARK_GRAY.getRGB()) pixels[n] = Color.DARK_GRAY.getRGB();
        }
        for(int i=pixels.length/2; i<pixels.length; i++){
            if(pixels[i] != Color.gray.getRGB()) pixels[i] = Color.gray.getRGB();
        }

        for(int x=0; x<width; x=x+1) {
            double cameraX = 2 * x / (double)(width) -1;
            double rayDirX = camera.xDir + camera.xPlane * cameraX;
            double rayDirY = camera.yDir + camera.yPlane * cameraX;
            //Map position
            int mapX = (int)camera.xPos;
            int mapY = (int)camera.yPos;
            //length of ray from current position to next x or y-side
            double sideDistX;
            double sideDistY;
            //Length of ray from one side to next in map
            double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
            double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));
            double perpWallDist;
            //Direction to go in x and y
            int stepX, stepY;
            boolean hit = false;//was a wall hit
            int side=0;//was the wall vertical or horizontal
            //Figure out the step direction and initial distance to a side
            if (rayDirX < 0)
            {
                stepX = -1;
                sideDistX = (camera.xPos - mapX) * deltaDistX;
            }
            else
            {
                stepX = 1;
                sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX;
            }
            if (rayDirY < 0)
            {
                stepY = -1;
                sideDistY = (camera.yPos - mapY) * deltaDistY;
            }
            else
            {
                stepY = 1;
                sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
            }
            int distance = 0;
            //Loop to find where the ray hits a wall
            if (!doesChunkExist((int) Math.floor(mapX/16), (int) Math.floor(mapY/16))) {
                chunks.add(new Chunk((int) Math.floor(mapX/16), (int) Math.floor(mapY/16)));
            }
            while(!hit) {
                //Jump to next square
                if (sideDistX < sideDistY)
                {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                }
                else
                {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                //Check if ray has hit a wall
                //System.out.println(mapX + ", " + mapY + ", " + map[mapX][mapY]);
                try {
                    if (FindChunk((int) Math.floor(mapX/16), (int) Math.floor(mapY/16)).getType(mapX%16, mapY%16) <= 3) hit = true;
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    break;
                }
                distance++;

                if (distance > 75) {
                    break;
                }
            }
            //Calculate distance to the point of impact
            if(side==0)
                perpWallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2f) / rayDirX);
            else
                perpWallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2f) / rayDirY);
            //Now calculate the height of the wall based on the distance from the camera
            int lineHeight;
            if(perpWallDist > 0) lineHeight = Math.abs((int)(height / perpWallDist));
            else lineHeight = height;
            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight/2+ height/2;
            if(drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight/2 + height/2;
            if(drawEnd >= height)
                drawEnd = height - 1;
            //add a texture
            int texNum = FindChunk((int) Math.floor(mapX/16), (int) Math.floor(mapY/16)).getType(mapX, mapY);
            if (texNum > 3) {
                texNum = 0;
            }
            double wallX;//Exact position of where wall was hit
            if(side==1) {//If its a y-axis wall
                wallX = (camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2f) / rayDirY) * rayDirX);
            } else {//X-axis wall
                wallX = (camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2f) / rayDirX) * rayDirY);
            }
            wallX-=Math.floor(wallX);
            //x coordinate on the texture
            int texX = (int) (wallX * (textures.get(texNum).SIZEX));
            if(side == 0 && rayDirX > 0) texX = textures.get(texNum).SIZEX - texX - 1;
            if(side == 1 && rayDirY < 0) texX = textures.get(texNum).SIZEX - texX - 1;
            //calculate y coordinate on texture
            for(int y=drawStart; y<drawEnd; y++) {
                int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) / 2;
                int color;
                if(side==0) color = textures.get(texNum).pixels[Math.abs(texX + (texY * textures.get(texNum).SIZEY))];
                else color = (textures.get(texNum).pixels[Math.abs(texX + (texY * textures.get(texNum).SIZEY))]>>1) & 8355711;//Make y sides darker
                pixels[Math.abs(x + y*(width))] = color;
            }
        }
        return pixels;
    }

    private Chunk FindChunk(int chunkX, int chunkY) {
        for (Chunk chunk : chunks) {
            if(chunk.getChunkX() == chunkX && chunk.getChunkY() == chunkY) {
                return chunk;
            }
        }
        Chunk chunk = new Chunk(chunkX, chunkY);
        chunks.add(chunk);
        return chunk;
    }

    private boolean doesChunkExist(int chunkX, int chunkY) {
        for (Chunk chunk : chunks) {
            if(chunk.getChunkX() == chunkX && chunk.getChunkY() == chunkY) {
                return true;
            }
        }

        return false;
    }
}