import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Georg Plaz.
 */
public class SettingsPanel extends JPanel {
    public static final int MAX_SLIDER_VALUE = 500;
    public static final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";

    private TileFactory tileFactory;
    private TilePanel tilePanel;
    private JComboBox<String> lineAlgorithmDropDown = new JComboBox<>(new String[]{
            ParisBlockAlgorithm.PARIS_CITY_BLOCK_ALGORITHM_NAME,
            CityBlockAlgorithm.CITY_BLOCK_ALGORITHM_NAME,
            BresenhamAlgorithm.BRESENHAM_ALGORITHM_NAME});
    private String[] lineAlgorithmKeys = new String[]{
            ParisBlockAlgorithm.PARIS_CITY_BLOCK_ALGORITHM_KEY,
            CityBlockAlgorithm.CITY_BLOCK_ALGORITHM_KEY,
            BresenhamAlgorithm.BRESENHAM_ALGORITHM_KEY};
    private JSlider straightnessSlider;
    private JCheckBox drawBoxCheckBox;
    private JCheckBox avoidTouchingCheckBox;
    private TileProperties tileProperties;
    private boolean changed = false;

    public SettingsPanel(TilePanel tilePanel) {
        try {
            tileProperties = new TileProperties();
            try {
                tileProperties.loadFrom(new File(CONFIG_PROPERTIES_FILE_NAME));
            } catch (IOException e) {
                System.err.println("Couldn't read config file. Using defaults instead..");
//                e.printStackTrace();
            }

            tileFactory = new TileFactory(tileProperties);
            this.tilePanel = tilePanel;
            LayoutManager boxLayout = new GridLayout(0, 1);//new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(boxLayout);

            addSpinners("Tile dimensions:", TileProperties.TILE_WIDTH_KEY, TileProperties.TILE_HEIGHT_KEY);
            addSpinner("Neuron count:", TileProperties.NEURON_COUNT_KEY);
            addSpinner("Seed:", TileProperties.SEED_KEY);
            addSpinners("Min/Max connections:", TileProperties.NEURON_CONNECTIONS_MIN_KEY, TileProperties.NEURON_CONNECTIONS_MAX_KEY);
            addSpinners("Min/Max neuron size:", TileProperties.NEURON_MIN_PIXEL_KEY, TileProperties.NEURON_MAX_PIXEL_KEY);
            addCheckbox("Paint background", TileProperties.BACKGROUND_DRAW_KEY);
            addSpinners("Background Colour:",TileProperties.BACKGROUND_RED_KEY,
                    TileProperties.BACKGROUND_GREEN_KEY, TileProperties.BACKGROUND_BLUE_KEY);
//            addSpinner("Max neuron size:", TileProperties.NEURON_MAX_PIXEL_KEY);

            add(new JLabel("Line algorithm:"));
            lineAlgorithmDropDown.addActionListener(e -> {
                tileProperties.putObject(TileProperties.NEURON_CONNECTIONS_DRAWING_ALGORITHM_KEY,
                        lineAlgorithmKeys[lineAlgorithmDropDown.getSelectedIndex()]);
                valueChanged();
            });
            add(lineAlgorithmDropDown);

            add(new JLabel("Straightness:"));
            straightnessSlider = new JSlider(1, MAX_SLIDER_VALUE, (int) (MAX_SLIDER_VALUE * TileProperties.DEFAULT_STRAIGHTNESS));
            straightnessSlider.addChangeListener(e -> {
                if (!straightnessSlider.getValueIsAdjusting()) {
                    tileProperties.putObject(TileProperties.STRAIGHTNESS_KEY,
                            straightnessSlider.getValue() / ((float) MAX_SLIDER_VALUE));
                    valueChanged();
                }
            });
            add(straightnessSlider);

            addCheckbox("Draw tile border", TileProperties.DRAW_BORDER_KEY);
            addCheckbox("Try to avoid touching neurons", TileProperties.NEURON_COLLISION_AVOID_KEY);
            addCheckbox("Create connected tiles", TileProperties.TILING_ACTIVE_KEY);

            tilePanel.setTile(tileFactory.create());
        } catch (TileProperties.IllegalTypeException e){
            e.printStackTrace();
        }
    }

    private void addCheckbox(String label, String key) throws TileProperties.IllegalTypeException {
//        add(new JLabel(label));
        JCheckBox checkBox = new JCheckBox(label);
        checkBox.setSelected(tileProperties.getBoolean(key));
        checkBox.addItemListener(l -> {
            tileProperties.putObject(key, checkBox.isSelected());
            valueChanged();
        });
        add(checkBox);
    }

    private void addSpinners(String label, String... keys) throws TileProperties.IllegalTypeException {
        add(new JLabel(label));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        add(panel);
        for (String key : keys) {
            JSpinner spinner = createSpinner(key);
            Dimension preferredSize = spinner.getPreferredSize();
            spinner.setPreferredSize(new Dimension(preferredSize.width / keys.length, preferredSize.height));
            panel.add(spinner);
        }
    }

    private void addSpinner(String label, String key) throws TileProperties.IllegalTypeException {
        add(new JLabel(label));
        add(createSpinner(key));
    }

    private JSpinner createSpinner(String key) throws TileProperties.IllegalTypeException {
        SpinnerNumberModel model = new SpinnerNumberModel(tileProperties.getInt(key), 0, Integer.MAX_VALUE, 1);
        JSpinner spinner = new JSpinner(model);
        model.addChangeListener(e -> {
            tileProperties.putObject(key, model.getValue());
            valueChanged();
        });
        return spinner;
    }

    private void valueChanged(){
        changed = true;
        try {
            tilePanel.setTile(tileFactory.create());
        } catch (TileProperties.IllegalTypeException e) {
            e.printStackTrace();
        }
        saveSettings();
    }


    public void saveSettings(){
        System.out.println("saving..");
        try {
            tileProperties.storeTo(new File(CONFIG_PROPERTIES_FILE_NAME));
            changed = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void changed(){
//        for(ChangedListener changedListener : changedListeners){
//            changedListener.changed();
//        }
//    }

    public boolean hasChanged() {
        return changed;
    }

//    public void save() {
//
//    }

//    public interface ChangedListener{
//        void changed();
//    }
}
