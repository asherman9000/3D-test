import perlin.Noise;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Camera implements KeyListener {
    private Noise n = new Noise();
    public double xPos, yPos, xDir, yDir, xPlane, yPlane;
    public boolean left, right, forward, back;
    public double MOVE_SPEED = .08;
    public final double ROTATION_SPEED = .045;
    private boolean NOCLIP = false;

    public Camera(double x, double y, double xd, double yd, double xp, double yp) {
        xPos = x;
        yPos = y;
        xDir = xd;
        yDir = yd;
        xPlane = xp;
        yPlane = yp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent key) {
        if ((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = true;
        if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = true;
        if ((key.getKeyCode() == KeyEvent.VK_UP))
            forward = true;
        if ((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = true;
        if ((key.getKeyCode() == KeyEvent.VK_E))
            NOCLIP = !NOCLIP;
        if (key.getKeyCode() == KeyEvent.VK_SPACE)
            MOVE_SPEED += 0.01;
        if (key.getKeyCode() == KeyEvent.VK_SHIFT)
            MOVE_SPEED -= 0.01;

    }

    @Override
    public void keyReleased(KeyEvent key) {
        if ((key.getKeyCode() == KeyEvent.VK_LEFT))
            left = false;
        if ((key.getKeyCode() == KeyEvent.VK_RIGHT))
            right = false;
        if ((key.getKeyCode() == KeyEvent.VK_UP))
            forward = false;
        if ((key.getKeyCode() == KeyEvent.VK_DOWN))
            back = false;
    }

    public void update(int[][] map) {
        if (!NOCLIP) {
            if (forward) {
                if (Noise.setSeed((int) (xPos + xDir * MOVE_SPEED), (int) yPos) > 3 || (xPos == 0 && yPos == 0)) {
                    xPos += xDir * MOVE_SPEED;
                }
                if (Noise.setSeed((int) xPos, (int) (yPos + yDir * MOVE_SPEED)) > 3 || (xPos == 0 && yPos == 0))
                    yPos += yDir * MOVE_SPEED;
            }
            if (back) {
                if (Noise.setSeed((int) (xPos - xDir * MOVE_SPEED), (int) yPos) > 3 || (xPos == 0 && yPos == 0)) {
                    xPos -= xDir * MOVE_SPEED;
                }
                if (Noise.setSeed((int) xPos, (int) (yPos - yDir * MOVE_SPEED)) > 3 || (xPos == 0 && yPos == 0))
                    yPos -= yDir * MOVE_SPEED;
            }
        } else {
            if (forward) {
                xPos += xDir * MOVE_SPEED;
                yPos += yDir * MOVE_SPEED;
            }
            if (back) {
                xPos -= xDir * MOVE_SPEED;
                yPos -= yDir * MOVE_SPEED;
            }
        }
        if (right) {
            double oldxDir = xDir;
            xDir = xDir * Math.cos(-ROTATION_SPEED) - yDir * Math.sin(-ROTATION_SPEED);
            yDir = oldxDir * Math.sin(-ROTATION_SPEED) + yDir * Math.cos(-ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(-ROTATION_SPEED) - yPlane * Math.sin(-ROTATION_SPEED);
            yPlane = oldxPlane * Math.sin(-ROTATION_SPEED) + yPlane * Math.cos(-ROTATION_SPEED);
        }
        if (left) {
            double oldxDir = xDir;
            xDir = xDir * Math.cos(ROTATION_SPEED) - yDir * Math.sin(ROTATION_SPEED);
            yDir = oldxDir * Math.sin(ROTATION_SPEED) + yDir * Math.cos(ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane = xPlane * Math.cos(ROTATION_SPEED) - yPlane * Math.sin(ROTATION_SPEED);
            yPlane = oldxPlane * Math.sin(ROTATION_SPEED) + yPlane * Math.cos(ROTATION_SPEED);
        }
    }

}