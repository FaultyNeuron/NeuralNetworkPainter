import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Georg Plaz.
 */
public class SettingsPanel extends JPanel {
    public static final int MAX_SLIDER_VALUE = 500;
    public static final int SIZE_INCREMENT = 50;
    public static final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";

    private TileFactory tileFactory;
    private TilePanel tilePanel;
    private SpinnerNumberModel tileWidthModel;
    private SpinnerNumberModel tileHeightModel;
    private SpinnerNumberModel neuronCountModel;
    private SpinnerNumberModel seedModel;
    private SpinnerNumberModel minConnectionsModel;
    private SpinnerNumberModel maxConnectionsModel;
    private JComboBox<String> lineAlgorithmDropDown = new JComboBox<>(new String[]{
            ParisBlockAlgorithm.PARIS_CITY_BLOCK_ALGORITHM_NAME,
            CityBlockAlgorithm.CITY_BLOCK_ALGORITHM_NAME,
            BresenhamAlgorithm.BRESENHAM_ALGORITHM_NAME});
    private String[] lineAlgorithmKeys = new String[]{
            ParisBlockAlgorithm.PARIS_CITY_BLOCK_ALGORITHM_KEY,
            CityBlockAlgorithm.CITY_BLOCK_ALGORITHM_KEY,
            BresenhamAlgorithm.BRESENHAM_ALGORITHM_KEY};
//    private ChaoticLineAlgorithm chaoticLineAlgorithm = new ChaoticLineAlgorithm(0.5f);
//    private JLabel straightnessSliderLabel;
    private JSlider straightnessSlider;
    private JCheckBox drawBoxCheckBox;
    private JCheckBox avoidTouchingCheckBox;
    private TileProperties tileProperties;
    private boolean changed = false;

//    private SpinnerListModel drawBoxModel;

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

            add(new JLabel("Tile width:"));
            tileWidthModel = new SpinnerNumberModel(
                    tileProperties.getInt(TileProperties.TILE_WIDTH_KEY), SIZE_INCREMENT, Integer.MAX_VALUE, SIZE_INCREMENT);
            JSpinner widthSpinner = new JSpinner(tileWidthModel);
            widthSpinner.addChangeListener(e -> {
                tileProperties.putObject(TileProperties.TILE_WIDTH_KEY, tileWidthModel.getValue());
                update();
            });
            add(widthSpinner);

            add(new JLabel("Tile height:"));
            tileHeightModel = new SpinnerNumberModel(
                    tileProperties.getInt(TileProperties.TILE_HEIGHT_KEY), SIZE_INCREMENT, Integer.MAX_VALUE, SIZE_INCREMENT);
            JSpinner heightSpinner = new JSpinner(tileHeightModel);
            heightSpinner.addChangeListener(e -> {
                tileProperties.putObject(TileProperties.TILE_HEIGHT_KEY, tileHeightModel.getValue());
                update();
            });
            add(heightSpinner);

            add(new JLabel("Neuron count:"));
            neuronCountModel = new SpinnerNumberModel(
                    tileProperties.getInt(TileProperties.NEURON_COUNT_KEY), 0, Integer.MAX_VALUE, 1);
            JSpinner neuronCountSpinner = new JSpinner(neuronCountModel);
            neuronCountSpinner.addChangeListener(e -> {
                tileProperties.putObject(TileProperties.NEURON_COUNT_KEY, neuronCountModel.getValue());
                update();
            });
            add(neuronCountSpinner);

            add(new JLabel("Seed:"));
            seedModel = new SpinnerNumberModel(tileProperties.getInt(TileProperties.SEED_KEY), Long.MIN_VALUE, Long.MAX_VALUE, 1);
            JSpinner seedSpinner = new JSpinner(seedModel);
            seedModel.addChangeListener(e -> {
                tileProperties.putObject(TileProperties.SEED_KEY, seedModel.getValue());
                update();
            });
            add(seedSpinner);

            add(new JLabel("Min connections:"));
            minConnectionsModel = new SpinnerNumberModel(
                    tileProperties.getInt(TileProperties.NEURON_CONNECTIONS_MIN_KEY), 0, Integer.MAX_VALUE, 1);
            JSpinner minConnectionSpinner = new JSpinner(minConnectionsModel);
            minConnectionsModel.addChangeListener(e -> {
                tileProperties.putObject(TileProperties.NEURON_CONNECTIONS_MIN_KEY, minConnectionsModel.getValue());
                update();
            });
            add(minConnectionSpinner);

            add(new JLabel("Max connections:"));
            maxConnectionsModel = new SpinnerNumberModel(
                    tileProperties.getInt(TileProperties.NEURON_CONNECTIONS_MAX_KEY), 0, Integer.MAX_VALUE, 1);
            JSpinner maxConnectionSpinner = new JSpinner(maxConnectionsModel);
            maxConnectionsModel.addChangeListener(e -> {
                tileProperties.putObject(TileProperties.NEURON_CONNECTIONS_MAX_KEY, maxConnectionsModel.getValue());
                update();
            });
            add(maxConnectionSpinner);

            add(new JLabel("Line algorithm:"));
//            lineAlgorithmDropDown = new JComboBox<>(new LineAlgorithm[]{
//                    new ParisBlockAlgorithm(), new CityBlockAlgorithm(), new BresenhamAlgorithm()});
            lineAlgorithmDropDown.addActionListener(e -> {
                tileProperties.putObject(TileProperties.NEURON_CONNECTIONS_DRAWING_ALGORITHM_KEY,
                        lineAlgorithmKeys[lineAlgorithmDropDown.getSelectedIndex()]);
                update();
            });
            add(lineAlgorithmDropDown);

            add(new JLabel("Straightness:"));
            straightnessSlider = new JSlider(1, MAX_SLIDER_VALUE, (int) (MAX_SLIDER_VALUE * TileProperties.DEFAULT_STRAIGHTNESS));
            straightnessSlider.addChangeListener(e -> {
                tileProperties.putObject(TileProperties.STRAIGHTNESS_KEY,
                        straightnessSlider.getValue() / ((float) MAX_SLIDER_VALUE));
                update();
            });
            add(straightnessSlider);

            add(new JLabel("Draw tile border:"));
            drawBoxCheckBox = new JCheckBox();
            drawBoxCheckBox.setSelected(tileProperties.getBoolean(TileProperties.DRAW_BORDER_KEY));
            drawBoxCheckBox.addChangeListener(e -> {
                tileProperties.putObject(TileProperties.DRAW_BORDER_KEY, drawBoxCheckBox.isSelected());
                update();
            });
            add(drawBoxCheckBox);

            add(new JLabel("Try to avoid touching neurons:"));
            avoidTouchingCheckBox = new JCheckBox();
            avoidTouchingCheckBox.setSelected(tileProperties.getBoolean(TileProperties.NEURON_COLLISION_AVOID_KEY));
            avoidTouchingCheckBox.addChangeListener(e -> {
                tileProperties.putObject(TileProperties.NEURON_COLLISION_AVOID_KEY, avoidTouchingCheckBox.isSelected());
                update();
            });
            add(avoidTouchingCheckBox);

            tilePanel.setTile(tileFactory.create());
        } catch (TileProperties.IllegalTypeException e){
            e.printStackTrace();
        }
    }

    private void update(){
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
