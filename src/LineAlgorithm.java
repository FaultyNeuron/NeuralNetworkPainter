import java.awt.*;
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
        default LineAlgorithm.Line.LineIterator iterator() {
            return new EdgeToLineIterator(edgeIterator());
        }
        Iterator<Point> edgeIterator();
        interface LineIterator extends Iterator<Point>{
            void setCursor(Point cursor);
        }
    }
}
