import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Georg Plaz.
 */
public class Tile {
    private int width;
    private int height;
    private final boolean drawBorder;
    private List<StageNeuron> neuronsOnStage;
    private List<Neuron> allNeurons;
    private BufferedImage canvas;
    private Graphics2D canvasGraphics;
    private int minConnections;
    private int maxConnections;
    private LineAlgorithm lineAlgorithm;
    private Random random;
    private float straightness;
    private boolean avoidTouching;
    private int minPixelSize;
    private int maxPixelSize;
    private static final int MAX_AVOID_TOUCH_ATTEMPTS = 100;


    public Tile(int width, int height, int neuronCount, long seed, boolean drawBorder,
                int minConnections, int maxConnections, LineAlgorithm lineAlgorithm,
                float straightness, boolean avoidTouching, int minPixelSize, int maxPixelSize,
                Color backgroundColour, boolean tilingActive) {
        this.width = width;
        this.height = height;
        this.drawBorder = drawBorder;
        this.minConnections = minConnections;
        this.maxConnections = maxConnections;
        this.lineAlgorithm = lineAlgorithm;
        this.straightness = straightness;
        this.avoidTouching = avoidTouching;
        this.minPixelSize = minPixelSize;
        this.maxPixelSize = maxPixelSize;
        neuronsOnStage = new LinkedList<>();
        allNeurons = new LinkedList<>();
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        canvasGraphics = canvas.createGraphics();
        canvasGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,   RenderingHints.VALUE_ANTIALIAS_ON);
        if (backgroundColour != null) {
            canvasGraphics.setColor(backgroundColour);
            canvasGraphics.fillRect(0, 0, width, height);
        }
        if(drawBorder) {
            canvasGraphics.setColor(Color.GRAY);
            canvasGraphics.drawRect(0, 0, width, height);
        }
        random = new Random(seed);
        Random randoms[] = new Random[neuronCount];
        for (int i = 0; i < neuronCount; i++) {
            randoms[i] = new Random(random.nextLong());
        }
        for (int i = 0; i < neuronCount; i++) {
            StageNeuron newNeuron = createNeuron(randoms[i], avoidTouching, tilingActive);
            if (newNeuron == null) {
                System.out.println("skipped a neuron..");
                break;
            }

            neuronsOnStage.add(newNeuron);
            allNeurons.add(newNeuron);
            allNeurons.addAll(newNeuron.copies);
        }
        List<StageNeuron> neuronsBySize = new ArrayList<>(neuronsOnStage);
        Collections.sort(neuronsBySize, (o1, o2) -> (int) ((o1.getSize()-o2.getSize())*100));
        for (StageNeuron neuron : neuronsBySize) {
            int neuronLinkCount = neuron.getTargetLinkCount();
            Iterator<Neuron> neighborIterator = neuron.neighborsSorted().iterator();
            while(neighborIterator.hasNext() && neuron.getTotalLinkCount()<neuronLinkCount){
                Neuron neighbor = neighborIterator.next();
                int neighborLinkCount = neighbor.getTargetLinkCount();
                if(!neuron.isLinked(neighbor) && neighbor.getTotalLinkCount() < neighborLinkCount) {
                    if(!neuron.copies.contains(neighbor) || neuron.getTotalLinkCount()+1<neuronLinkCount) {
                        neuron.link(neighbor);
                    }
                }
            }
        }
        for(Neuron neuron : allNeurons){
            neuron.paint();
        }
    }

    private boolean anyIntersectsAny(Collection<? extends Neuron> neurons) {
        for (Neuron neuron : neurons) {
            if (intersectsAny(neuron)) {
                return true;
            }
        }
        return false;
    }

    public StageNeuron createNeuron(Random random, boolean avoidTouching, boolean tilingActive) {
        double size = getRandomDoubleUnevenDistributed(random);
        Color neuronColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
        int offset = 0;
        if (!tilingActive) {
            offset = toPixelSize(size, minPixelSize, maxPixelSize);
        }
        System.out.println(offset);
        for (int j = 0; j < MAX_AVOID_TOUCH_ATTEMPTS; j++) {
            int x = getRandomInteger(random, offset/2, width - offset);
            int y = getRandomInteger(random, offset/2, height - offset);
            System.out.println(x + ", " + y);
            StageNeuron newNeuron = new StageNeuron(x, y, size, neuronColor, tilingActive);
            Collection<Neuron> neuronPlusCopies = new HashSet<>(newNeuron.copies);
            neuronPlusCopies.add(newNeuron);
            if (!avoidTouching || !intersectsAny(newNeuron)) {
                return newNeuron;
            }
        }
        return null;
    }

    public boolean intersectsAny(Neuron neuron){
        for(Neuron other : allNeurons){
            if(neuron.intersects(other)){
                return true;
            }
        }
        return false;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public BufferedImage getCanvas() {
        return canvas;
    }

    public void save() throws IOException {
        Util.savePng(canvas, "tile");
    }

    private static int toPixelSize(double size, int minPixel, int maxPixel) {
        return (int) Math.round(size * (maxPixel - minPixel) + minPixel);
    }

    private abstract class Neuron {
        private int x;
        private int y;

        private Neuron(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void paint(){
            canvasGraphics.setColor(getNeuronColor());
            canvasGraphics.fillOval(getLeft(), getTop(), getPixelSize(), getPixelSize());
        }

        public int getLeft() {
            return x - getPixelSize() / 2;
        }

        public int getTop() {
            return y - getPixelSize() / 2;
        }

        public abstract double getSize();

        public int getPixelSize(){
            return toPixelSize(getSize(), minPixelSize, maxPixelSize);
        }

        public List<Neuron> neighborsSorted() {
            List<Neuron> neighbors = new ArrayList<>(allNeurons);
            Collections.sort(neighbors, (o1, o2) -> (int) ((distance(o1) - distance(o2))));
            neighbors.remove(0); // remove itself..
            return neighbors;
        }

        public double distance(Neuron other) {
            return Util.distance(getPosition(), other.getPosition());
        }

        public int getY() {
            return y;
        }

        public int getX() {
            return x;
        }

//        public List<Neuron> getChildNeurons() {
//            return childNeurons;
//        }

        public Point getPosition() {
            return new Point(x, y);
        }

        public abstract int getTotalLinkCount();

        public int getTargetLinkCount(){
            return (int) Math.max(0, getSize()*((maxConnections+1) - minConnections)+ minConnections);
        }

        public abstract void receiveLink(StageNeuron neuron);

        public abstract Color getNeuronColor();

        public boolean intersects(Neuron other) {
            double distance = Util.distance(other.getPosition(), getPosition());
            return distance < getPixelSize()/2.+other.getPixelSize()/2.;
        }
    }

    private double getRandomDoubleUnevenDistributed(Random random) {
        return Math.pow(random.nextDouble(), 2);
    }

    private int getRandomIntegerUnevenDistributed(Random random, int lower, int upper) {
        return (int) (getRandomDoubleUnevenDistributed(random) * upper) + lower;
    }

    private int getRandomInteger(Random random, int lower, int upper) {
        return (int) (random.nextDouble()* upper + lower);
    }

    private class StageNeuron extends Neuron {
        private final double size;
        private int copyLinkCount = 0;
        private Set<CopiedNeuron> copies = new HashSet<>();
        private Set<Neuron> linkedNeurons = new HashSet<>();
        private Color neuronColor;

        private StageNeuron(int x, int y, double size, Color neuronColor, boolean tilingActive) {
            super(x, y);
            this.size = size;
            this.neuronColor = neuronColor;
            if (tilingActive) {
                for (Point p : Util.eightNeighbor()) {
                    new CopiedNeuron(this, p.x, p.y);
                }
            }
        }

        @Override
        public int getTotalLinkCount() {
            return linkedNeurons.size()+ copyLinkCount;
        }

        @Override
        public void receiveLink(StageNeuron neuron) {
            linkedNeurons.add(neuron);
        }

        public void increaseLinkedCopyCount() {
            copyLinkCount++;
        }

        public void addCopy(CopiedNeuron neuron) {
            copies.add(neuron);
        }


        public void link(Neuron neighbor) {
            Point cursor = getPosition();
            Point target = neighbor.getPosition();
            float totalDistance = (float) Util.distance(cursor, target);
            LineAlgorithm.Line.LineIterator lineIterator = lineAlgorithm.line(cursor, target).iterator();
            while(lineIterator.hasNext()){
                if(random.nextFloat()<straightness) {
                    cursor = lineIterator.next();
                }else{
                    cursor = Util.add(cursor, Util.eightNeighbor()[random.nextInt(8)]);
                    lineIterator.setCursor(cursor);
                }
                double colourWeight = Util.clip(Util.distance(cursor, target)/totalDistance, 0, 1);

                Tile.this.paintPixel(cursor, 1, Util.interpolate(getNeuronColor(), neighbor.getNeuronColor(), colourWeight));
            }
            linkedNeurons.add(neighbor);
            neighbor.receiveLink(this);
        }

        private int getThickness(Neuron neuron, Point current){
            double distance = Util.distance(current, neuron.getPosition());
            double factor = 1-distance/(neuron.getPixelSize()*4);
            factor = Math.pow(factor, 2);
            double thickness = Math.max(neuron.getPixelSize(), factor*neuron.getPixelSize());
            return (int) thickness;
        }

        public boolean isLinked(Neuron neuron){
            return linkedNeurons.contains(neuron);
        }

        @Override
        public double getSize() {
            return size;
        }

        @Override
        public Color getNeuronColor() {
            return neuronColor;
        }
    }

    private void paintLine(Point from, Point to, Color color) {
        canvasGraphics.drawLine(from.x, from.y, to.x, to.y);
    }

    private void paintPixel(Point point, int thickness, Color color) {
        Point center = toGrid(point);
        if(thickness==1) {
            canvas.setRGB(center.x, center.y, color.getRGB());
        }else{
            canvasGraphics.setColor(color);
            canvasGraphics.fillOval(center.x-thickness/2, center.y-thickness/2, thickness, thickness);
        }
    }

    private class CopiedNeuron extends Neuron {
        private StageNeuron original;

        private CopiedNeuron(StageNeuron original, int deltaX, int deltaY) {
            super(original.getX() + deltaX * width, original.getY() + deltaY * height);
            this.original = original;
            original.addCopy(this);
        }

        public StageNeuron getOriginal() {
            return original;
        }

        @Override
        public double getSize() {
            return original.getSize();
        }

        @Override
        public int getTotalLinkCount() {
            return original.getTotalLinkCount();
        }

        @Override
        public void receiveLink(StageNeuron neuron) {
            original.increaseLinkedCopyCount();
        }

        @Override
        public Color getNeuronColor() {
            return original.getNeuronColor();
        }
    }

    private Point toGrid(Point point) {
        int x = (point.x + width*100) % width;
        int y = (point.y + height*100) % height;
        return new Point(x, y);
    }

}
