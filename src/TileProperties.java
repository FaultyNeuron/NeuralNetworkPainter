import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Georg Plaz.
 */
public class TileProperties {
    public static final int DEFAULT_TILE_WIDTH = 800;
    public static final int DEFAULT_TILE_HEIGHT = 600;
    public static final int DEFAULT_NEURON_COUNT = 100;
    public static final int DEFAULT_MIN_CONNECTIONS = 1;
    public static final int DEFAULT_MAX_CONNECTIONS = 8;
    public static final double DEFAULT_STRAIGHTNESS = 1;
    private Properties properties;
    public static final String NEURON_COUNT_KEY = "neuron.count";

    public static final String TILE_WIDTH_KEY = "tile.width";
    public static final String TILE_HEIGHT_KEY = "tile.height";
    public static final String NEURON_CONNECTIONS_MIN_KEY = "neuron.connections.min";
    public static final String NEURON_CONNECTIONS_MAX_KEY = "neuron.connections.max";
    public static final String NEURON_CONNECTIONS_DRAWING_ALGORITHM_KEY = "neuron.connections.drawing";
    public static final String NEURON_COLLISION_AVOID_KEY = "neuron.collision.avoid";
    public static final String SEED_KEY = "seed";
    public static final String STRAIGHTNESS_KEY = "straightness";
    public static final String DRAW_BORDER_KEY = "tile.border.draw";

    private LinkedHashSet<Object> keys = new LinkedHashSet<>();

    public TileProperties() {
        properties = new Properties(){
            @Override
            public synchronized Enumeration<Object> keys() {
                return Collections.enumeration(keys);
            }
        };
        putObject(TILE_WIDTH_KEY, DEFAULT_TILE_WIDTH);
//        System.out.println("got: "+properties.get(TILE_WIDTH_KEY));
        putObject(TILE_HEIGHT_KEY, DEFAULT_TILE_HEIGHT);
        putObject(NEURON_COUNT_KEY, DEFAULT_NEURON_COUNT);
        putObject(NEURON_CONNECTIONS_MIN_KEY, DEFAULT_MIN_CONNECTIONS);
        putObject(NEURON_CONNECTIONS_MAX_KEY, DEFAULT_MAX_CONNECTIONS);
        putObject(NEURON_CONNECTIONS_DRAWING_ALGORITHM_KEY, ParisBlockAlgorithm.PARIS_CITY_BLOCK_ALGORITHM_KEY);
        putObject(NEURON_COLLISION_AVOID_KEY, true);
        putObject(DRAW_BORDER_KEY, false);
        putObject(SEED_KEY, 42);
        putObject(STRAIGHTNESS_KEY, DEFAULT_STRAIGHTNESS);
    }

    public void putObject(String key, Object value) {
        keys.remove(key);
        keys.add(key);
        properties.put(key, value.toString());
    }


    public void loadFrom(File loadFrom) throws IOException {
        FileInputStream stream = new FileInputStream(loadFrom);
        properties.load(stream);
        stream.close();
    }

    public void storeTo(File file) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file);
        properties.store(outputStream, "properties for the "+Util.PROJECT_NAME+" project.");
        outputStream.close();
    }

    public int getInt(String key) throws IllegalTypeException {
        String valueAsString = properties.getProperty(key);
        try{
            return Integer.parseInt(valueAsString);
        }catch (NumberFormatException e){
            throw new IllegalTypeException(key, valueAsString, "integer");
        }
    }

    public float getFloat(String key) throws IllegalTypeException {
        String valueAsString = properties.getProperty(key);
        try{
            return Float.parseFloat(valueAsString);
        }catch (NumberFormatException e){
            throw new IllegalTypeException(key, valueAsString, "float");
        }
    }

    public boolean getBoolean(String key) throws IllegalTypeException {
        String valueAsString = properties.getProperty(key);
        try{
            return Boolean.parseBoolean(valueAsString);
        }catch (NumberFormatException e){
            throw new IllegalTypeException(key, valueAsString, "boolean");
        }
    }

//    public void putObject(String key, Object value){
//        keys.remove(key);
//        keys.add(key);
//        properties.put(key, value.toString());
//    }

    public LineAlgorithm getLineAlgorithm(String key) throws IllegalTypeException {
        String valueAsString = properties.getProperty(key);
        switch (valueAsString){
            case ParisBlockAlgorithm.PARIS_CITY_BLOCK_ALGORITHM_KEY:
                return new ParisBlockAlgorithm();
            case CityBlockAlgorithm.CITY_BLOCK_ALGORITHM_KEY:
                return new CityBlockAlgorithm();
            case BresenhamAlgorithm.BRESENHAM_ALGORITHM_KEY:
                return new BresenhamAlgorithm();
            default:
                throw new IllegalTypeException(key, valueAsString, "line algorithm");
        }
    }


    public class IllegalTypeException extends Exception{
        public IllegalTypeException(String key, String value, String convertTo) {
            super("Couldn't parse value \""+value+"\" stored for key \""+key+"\" to "+convertTo+".");
        }
    }
}
