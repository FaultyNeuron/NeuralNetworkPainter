import javax.swing.*;
import javax.xml.bind.JAXBPermission;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Georg Plaz.
 */
public class NetworkPanel extends JPanel {
    private TilePanel tilePanel;

    private JFrame settingsFrame = new JFrame();
    private SettingsPanel settingsPanel;

    public NetworkPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        Tile tile = tileFactory.create();
        tilePanel = new TilePanel(1200, 800);
        add(tilePanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        add(buttonPanel);
        Button saveTileButton = new Button("Save Tile");
        saveTileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tilePanel.getTile().save();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        Button savePictureButton = new Button("Save Picture");
        savePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tilePanel.save();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        Button settingsButton = new Button("Settings");
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsFrame = new JFrame("Settings");
                settingsFrame.add(settingsPanel);
                settingsButton.setEnabled(false);
                settingsFrame.setVisible(true);
                settingsFrame.pack();
                settingsFrame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.out.println("closing settings..");
                        settingsButton.setEnabled(true);
//                        if(settingsPanel.hasChanged()){
//                            settingsPanel.saveSettings();
//                        }
                    }
                });
                settingsFrame.setLocationRelativeTo(NetworkPanel.this);
            }
        });
        buttonPanel.add(saveTileButton);
        buttonPanel.add(savePictureButton);
        buttonPanel.add(settingsButton);

        settingsPanel = new SettingsPanel(tilePanel);
    }

//    public void reload(){
////        tileFactory.setSeed(new Random().nextLong());
//        tilePanel.setTile(tileFactory.create());
//    }
}
