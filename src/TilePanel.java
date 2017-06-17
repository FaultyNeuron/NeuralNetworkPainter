import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Georg Plaz.
 */
public class TilePanel extends JPanel{
    private final int width;
    private final int height;
    private Tile tile;

    public TilePanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
    }

    public void setTile(Tile tile){
        this.tile = tile;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if(tile!=null) {
            for (int x = 0; x < getWidth(); x += tile.getWidth()) {
                for (int y = 0; y < getHeight(); y += tile.getHeight()) {
                    g.drawImage(tile.getCanvas(), x, y, null);
                }
            }
        }else{
            super.paint(g);
        }
    }

    public Tile getTile() {
        return tile;
    }

    public void save() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        paint(bufferedImage.createGraphics());
        Util.savePng(bufferedImage, "picture");
    }
}
