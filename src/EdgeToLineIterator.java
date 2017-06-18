import java.awt.*;
import java.util.Iterator;

/**
 * Created by Georg Plaz.
 */
public class EdgeToLineIterator implements LineAlgorithm.Line.LineIterator {
    private BresenhamLineIterator lineIterator;
    private Iterator<Point> edgeIterator;
    private Point lastEdge;

    public EdgeToLineIterator(Iterator<Point> edgeIterator) {
        this.edgeIterator = edgeIterator;
        lastEdge = edgeIterator.next();
        nextLineIterator();
    }

    @Override
    public void setCursor(Point cursor) {
        lineIterator.setCursor(cursor);
    }

    @Override
    public boolean hasNext() {
        return lineIterator.hasNext();
    }

    @Override
    public Point next() {
        Point next = lineIterator.next();
        if (!lineIterator.hasNext()) {
            nextLineIterator();
        }
        return next;
    }

    private void nextLineIterator() {
        if (edgeIterator.hasNext()) {
            Point nextEdge = edgeIterator.next();
            lineIterator = new BresenhamLineIterator(lastEdge, nextEdge);
            lastEdge = nextEdge;
        }
    }
}
