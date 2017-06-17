import java.awt.*;

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
//            float cursorX;
//            double cursorY;
            private Point cursor;
            private Point step1 = new Point();
            private Point step2 = new Point();
            private Point delta = Util.subtract(destination, start);
            int longest;
            int shortest;
            int numerator;
            int dx1, dy1, dx2, dy2;

            public LineIterator() {
                setCursor(start);
            }

            @Override
            public boolean hasNext() {
                return !destination.equals(cursor);
            }

            @Override
            public Point next() {
//                Point delta = Util.subtract(destination, cursor);
//                float deltaX = destination.x-cursorX;
//                float deltaY = destination.y-cursorY;
//                float deltaLength = ((float)Util.length(deltaX, deltaY))/1.5f;
//                cursorX = cursorX+deltaX/deltaLength;
//                cursorY = cursorY+deltaY/deltaLength;
//                float newX = cursor.x+delta.x/deltaLength;
//                float newY = cursor.y+delta.y/deltaLength;
//                return cursor = new Point(Math.round(newX), Math.round(newY));
                numerator += shortest ;
                if (!(numerator<longest)) {
                    numerator -= longest ;
                    return cursor = Util.add(cursor, dx1, dy1);
                } else {
                    return cursor = Util.add(cursor, dx2, dy2);
                }
            }

            @Override
            public void setCursor(Point cursor) {
                this.cursor = cursor;
                dx1 = dy1 = dx2 = dy2 = 0;
                int w = delta.x ;
                int h = delta.y ;
                if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
                if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
                if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
                longest = Math.abs(w);
                shortest = Math.abs(h);
                if (!(longest>shortest)) {
                    longest = Math.abs(h) ;
                    shortest = Math.abs(w) ;
                    if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
                    dx2 = 0 ;
                }
                numerator = longest >> 1;
            }
        }
    }

    @Override
    public String toString() {
        return BRESENHAM_ALGORITHM_NAME;
    }
}
