import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Georg Plaz.
 */
public interface LineAlgorithm{
    Line line(Point start, Point destination);

    default void setRandom(Random random){}

    Object toKey();

    interface Line extends Iterable<Point>{
        @Override
        LineIterator iterator();
        interface LineIterator extends Iterator<Point>{
            void setCursor(Point cursor);
        }
    }
}
