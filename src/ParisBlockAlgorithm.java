import java.awt.*;

/**
 * Created by Georg Plaz.
 */
public class ParisBlockAlgorithm implements LineAlgorithm {
    public static final String PARIS_CITY_BLOCK_ALGORITHM_KEY = "paris_city_block";
    public static final String PARIS_CITY_BLOCK_ALGORITHM_NAME = "Paris City Block";

    @Override
    public LineAlgorithm.Line line(Point start, Point destination) {
        return new Line(start, destination);
    }

    @Override
    public Object toKey() {
        return PARIS_CITY_BLOCK_ALGORITHM_KEY;
    }

    public class Line implements LineAlgorithm.Line{
        private Point start;
        private Point destination;

        public Line(Point start, Point destination) {
            this.start = start;
            this.destination = destination;

        }

        @Override
        public LineAlgorithm.Line.LineIterator iterator() {
            return new LineIterator();
        }

        public class LineIterator implements LineAlgorithm.Line.LineIterator{
            private Point cursor;
            float totalLength;
            public LineIterator(){
                cursor = start;
                totalLength = (float) Util.distance(cursor, destination);
            }
            @Override
            public boolean hasNext() {
                return !cursor.equals(destination);
            }

            @Override
            public Point next() {
                Point delta = Util.subtract(destination, cursor);
                return cursor = Util.add(cursor, Util.normalize(delta));
            }

            @Override
            public void setCursor(Point point) {
                cursor = point;
            }
        }
    }

}
