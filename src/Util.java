import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Georg Plaz.
 */
public class Util {
    public static final String PROJECT_NAME = "Random Neural Network Painter";
    public static final Point[] EIGHT_NEIGHBOR = new Point[]{new Point(-1, -1), new Point(0, -1), new Point(1, -1), new Point(1, 0), new Point(1, 1), new Point(0, 1), new Point(-1, 1), new Point(-1, 0)};

    public static void savePng(BufferedImage canvas, String baseName) throws IOException {
        String postFix = "";
        File f;
        int i = 2;
        while((f = new File(baseName+postFix+".png")).exists()){
            postFix = "_"+i++;
        }
        ImageIO.write(canvas, "PNG", f);
    }

    public static Color interpolate(Color first, Color second, double weight){
        int r = interpolate(first.getRed(), second.getRed(), weight);
        int g = interpolate(first.getGreen(), second.getGreen(), weight);
        int b = interpolate(first.getBlue(), second.getBlue(), weight);
        return new Color(r, g, b);
    }

    public static Point subtract(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }

    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    public static Point add(Point a, int dx, int dy) {
        return new Point(a.x + dx, a.y + dy);
    }

    public static double distance(Point a, Point b){
        int xDist = a.x - b.x;
        int yDist = a.y - b.y;
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }

    public static Point normalize(Point a) {
        return new Point(signum(a.x), signum(a.y));
    }

    public static int interpolate(int first, int second, double weight){
        return (int) (first*weight+second*(1-weight));
    }

    public static int signum(int i) {
        return i < 0 ? -1 : (i > 0 ? 1 : 0);
    }

    public static Point[] eightNeighbor() {
        return EIGHT_NEIGHBOR;
    }

    public static double clip(double value, double lower, double upper) {
        return Math.min(Math.max(value, lower), upper);
    }

    public static double length(Point delta) {
        return Math.sqrt(delta.x * delta.x + delta.y * delta.y);
    }

    public static double length(double x, double y) {
        return Math.sqrt(x*x+y*y);
    }
}
