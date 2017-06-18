import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

import static java.lang.Math.*;

/**
 * Created by Georg Plaz.
 */
public class CityBlockAlgorithm implements LineAlgorithm {
    public static final String CITY_BLOCK_ALGORITHM_KEY = "city_block";
    public static final String CITY_BLOCK_ALGORITHM_NAME = "City Block";

    @Override
    public Line line(Point start, Point destination) {
        return new Line(start, destination);
    }

    @Override
    public Object toKey() {
        return CITY_BLOCK_ALGORITHM_KEY;
    }

    private class Line implements LineAlgorithm.Line {
        private Point start;
        private Point destination;

        public Line(Point start, Point destination) {
            this.start = start;
            this.destination = destination;
        }

//        @Override
//        public LineAlgorithm.Line.LineIterator iterator() {
//            return new LineIterator();
//        }

        @Override
        public Iterator<Point> edgeIterator() {
            java.util.List<Point> points = new LinkedList<>();
            points.add(start);
            Point delta = Util.subtract(destination, start);
            if (abs(delta.x) > 0 && abs(delta.y) > 0) {
                Point midPoint;
                if((abs(delta.x) > abs(delta.y))){
                    midPoint = Util.add(start, 0, delta.y);
                }else{
                    midPoint = Util.add(start, delta.x, 0);
                }
                points.add(midPoint);
            }

            points.add(destination);
            return points.iterator();
        }

//        private class LineIterator implements LineAlgorithm.Line.LineIterator {
////            private Point delta;
//            private Point cursor = start;
//            @Override
//            public boolean hasNext() {
//                return !destination.equals(cursor);
//            }
//
//            @Override
//            public Point next() {
//                Point delta = Util.subtract(destination, cursor);
//                if((abs(delta.x) > abs(delta.y) || delta.x == 0) && delta.y != 0){
//                    return cursor = Util.add(cursor, 0, Util.signum(delta.y));
//                }else{
//                    return cursor = Util.add(cursor, Util.signum(delta.x), 0);
//                }
//            }
//
//            @Override
//            public void setCursor(Point cursor) {
//                this.cursor = cursor;
//            }
//        }
    }

    @Override
    public String toString() {
        return CITY_BLOCK_ALGORITHM_NAME;
    }
}
