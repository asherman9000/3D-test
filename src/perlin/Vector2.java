package perlin;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Vector2 {
    private double dirX;
    private double dirY;
    public void setDirX(double dirX) {
        this.dirX = dirX;
    }

    public void setDirY(double dirY) {
        this.dirY = dirY;
    }

    public Vector2(double dirX, double dirY) {
        this.dirX = dirX;
        this.dirY = dirY;
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Vector2 down() {
        return new Vector2(0, -1);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Vector2 left() {
        return new Vector2(-1, 0);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Vector2 up() {
        return new Vector2(0, 1);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Vector2 one() {
        return new Vector2(1, 1);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Vector2 right() {
        return new Vector2(1, 0);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Vector2 zero() {
        return new Vector2(0, 0);
    }

    public static double magnitude(@NotNull Vector2 v) {
        return Math.sqrt(Math.pow(v.dirX, 2) + Math.pow(v.dirY, 2));
    }

    @Contract("_ -> new")
    public static @NotNull Vector2 normalize(@NotNull Vector2 v) {
        return new Vector2(v.getDirX()/magnitude(v), v.getDirY()/magnitude(v));
    }

    public double getDirX() {
        return dirX;
    }

    public double getDirY() {
        return dirY;
    }

    public Vector2 add(@NotNull Vector2 v) {
        return new Vector2(this.dirX+v.dirX,this.dirY+v.dirY);
    }

    public Vector2 sub(@NotNull Vector2 v) {
        return new Vector2(this.dirX-v.dirX,this.dirY-v.dirY);
    }
    
    public Vector2 mult(double d) {
        return new Vector2(this.dirX*d, this.dirY*d);
    }

    public Vector2 div(double d) {
        return new Vector2(this.dirX/d, this.dirY/d);
    }

    public static double Angle(Vector2 from, Vector2 to) {
        return Math.acos(dot(from, to)/(magnitude(from)*magnitude(to)));
    }

    public static double dot(@NotNull Vector2 lhs, @NotNull Vector2 rhs) {
        return (lhs.getDirX() * rhs.getDirX()) + (lhs.getDirY() * rhs.getDirY());
    }

    public static Vector2 lerp(Vector2 from, Vector2 to, double percent) {
        return from.add((to.sub(from)).mult(percent));
    }

    public static Vector2 vectorToPoint(Vector2 end, Vector2 start) {
        return new Vector2(start.getDirX()+end.getDirX(), start.getDirY()+end.getDirY());
    }
}
