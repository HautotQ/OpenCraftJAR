import javax.swing.*;
import java.awt.*;
import AppStorage;
import Settings;

public class SettingsView extends JPanel {

    private static final AppStorage storage = new AppStorage(System.getProperty("user.home") + "config.properties");
    private static final Settings settings = new Settings();

    public SettingsView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        JCheckBox toggleCoolMode = new JCheckBox("Mode Cool (recommandé si vous étudiez de la théorie)");
        toggleCoolMode.setSelected(Settings.isCoolMode());

        toggleCoolMode.addActionListener(e -> {
            boolean enabled = toggleCoolMode.isSelected();
            Settings.setCoolMode(enabled);
            storage.saveSetting("coolMode", String.valueOf(enabled));
        });

        add(toggleCoolMode);
    }
}