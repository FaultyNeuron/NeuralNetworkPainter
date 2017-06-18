import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Georg Plaz.
 * from: http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
 */
public class BresenhamAlgorithm implements LineAlgorithm {
    public static final String BRESENHAM_ALGORITHM_KEY = "bresenham";
    public static final String BRESENHAM_ALGORITHM_NAME = "Bresenham";

    @Override
    public LineAlgorithm.Line line(Point start, Point destination) {
        return new Line(start, destination);
    }

    @Override
    public Object toKey() {
        return BRESENHAM_ALGORITHM_KEY;
    }

    public class Line implements LineAlgorithm.Line {
        private Point start;
        private Point destination;

        public Line(Point start, Point destination) {
            this.start = start;
            this.destination = destination;
        }

        @Override
        public Iterator<Point> edgeIterator() {
            Point[] points = {start, destination};
            return Arrays.asList(points).iterator();
        }

    }

    @Override
    public String toString() {
        return BRESENHAM_ALGORITHM_NAME;
    }

}
