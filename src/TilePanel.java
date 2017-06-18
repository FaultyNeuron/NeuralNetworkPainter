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
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            paintGraphics(g);
        }else{
            super.paint(g);
        }
    }

    public void paintGraphics(Graphics g) {
        for (int x = 0; x < getWidth(); x += tile.getWidth()) {
            for (int y = 0; y < getHeight(); y += tile.getHeight()) {
                g.drawImage(tile.getCanvas(), x, y, null);
            }
        }
    }

    public Tile getTile() {
        return tile;
    }

    public void save() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(tile.getWidth(), tile.getHeight(), BufferedImage.TYPE_INT_ARGB);
        paintGraphics(bufferedImage.createGraphics());
        Util.savePng(bufferedImage, "picture");
    }
}
