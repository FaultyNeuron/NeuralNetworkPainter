import java.awt.*;

/**
 * Created by Georg Plaz.
 */
public class BresenhamLineIterator implements LineAlgorithm.Line.LineIterator {
    //            float cursorX;
//            double cursorY;
    private Point cursor;
    private Point delta;
    int longest;
    int shortest;
    int numerator;
    int dx1, dy1, dx2, dy2;
    private Point start;
    private Point destination;

    public BresenhamLineIterator(Point start, Point destination) {
        this.start = start;
        this.destination = destination;
        delta = Util.subtract(destination, start);
        setCursor(this.start);
    }

    @Override
    public boolean hasNext() {
        return !destination.equals(cursor);
    }

    @Override
    public Point next() {
        numerator += shortest;
        if (!(numerator < longest)) {
            numerator -= longest;
            cursor = Util.add(cursor, dx1, dy1);
        } else {
            cursor = Util.add(cursor, dx2, dy2);
        }
        return cursor;
    }

    @Override
    public void setCursor(Point cursor) {
        delta = Util.subtract(destination, cursor);
        this.cursor = cursor;
        dx1 = dy1 = dx2 = dy2 = 0;
        int w = delta.x;
        int h = delta.y;
        if (w < 0) dx1 = -1;
        else if (w > 0) dx1 = 1;
        if (h < 0) dy1 = -1;
        else if (h > 0) dy1 = 1;
        if (w < 0) dx2 = -1;
        else if (w > 0) dx2 = 1;
        longest = Math.abs(w);
        shortest = Math.abs(h);
        if (!(longest > shortest)) {
            longest = Math.abs(h);
            shortest = Math.abs(w);
            if (h < 0) dy2 = -1;
            else if (h > 0) dy2 = 1;
            dx2 = 0;
        }
        numerator = longest >> 1;
    }
}
