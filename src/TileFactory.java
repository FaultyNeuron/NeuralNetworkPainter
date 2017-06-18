import java.awt.*;

/**
 * Created by Georg Plaz.
 */
public class TileFactory {
    private TileProperties tileProperties;

    public TileFactory(TileProperties tileProperties) {
        this.tileProperties = tileProperties;
    }

    public Tile create() throws TileProperties.IllegalTypeException {
        Color backgroundColour = null;
        if (tileProperties.getBoolean(TileProperties.BACKGROUND_DRAW_KEY)) {
            backgroundColour = new Color(tileProperties.getInt(TileProperties.BACKGROUND_RED_KEY),
                    tileProperties.getInt(TileProperties.BACKGROUND_GREEN_KEY),
                    tileProperties.getInt(TileProperties.BACKGROUND_BLUE_KEY));
        }
        return new Tile(
                tileProperties.getInt(TileProperties.TILE_WIDTH_KEY),
                tileProperties.getInt(TileProperties.TILE_HEIGHT_KEY),
                tileProperties.getInt(TileProperties.NEURON_COUNT_KEY),
                tileProperties.getInt(TileProperties.SEED_KEY),
                tileProperties.getBoolean(TileProperties.DRAW_BORDER_KEY),
                tileProperties.getInt(TileProperties.NEURON_CONNECTIONS_MIN_KEY),
                tileProperties.getInt(TileProperties.NEURON_CONNECTIONS_MAX_KEY),
                tileProperties.getLineAlgorithm(TileProperties.NEURON_CONNECTIONS_DRAWING_ALGORITHM_KEY),
                tileProperties.getFloat(TileProperties.STRAIGHTNESS_KEY),
                tileProperties.getBoolean(TileProperties.NEURON_COLLISION_AVOID_KEY),
                tileProperties.getInt(TileProperties.NEURON_MIN_PIXEL_KEY),
                tileProperties.getInt(TileProperties.NEURON_MAX_PIXEL_KEY),
                backgroundColour,
                tileProperties.getBoolean(TileProperties.TILING_ACTIVE_KEY),
                tileProperties.getBoolean(TileProperties.ANTI_ALIASING_KEY),
                tileProperties.getBoolean(TileProperties.NEURON_HOLLOW_KEY)
        );
    }

}
