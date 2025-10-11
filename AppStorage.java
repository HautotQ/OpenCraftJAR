import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class AppStorage {
    private final Properties props;
    private final File file;

    public AppStorage(String filename) {
        block7: {
            this.props = new Properties();
            this.file = new File(filename);
            try {
                if (!this.file.exists()) break block7;
                try (FileReader reader = new FileReader(this.file);){
                    this.props.load(reader);
                }
            } catch (IOException e) {
                System.err.println("Erreur au chargement de " + filename + " : " + e.getMessage());
            }
        }
    }

    public void set(String key, String value) {
        this.props.setProperty(key, value);
        this.save();
    }

    public String get(String key, String defaultValue) {
        return this.props.getProperty(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(this.get(key, String.valueOf(defaultValue)));
    }

    public void setBoolean(String key, boolean value) {
        this.set(key, String.valueOf(value));
    }

    public void save() {
        try (FileOutputStream fos = new FileOutputStream(this.file);){
            this.props.store(fos, "AppStorage Preferences");
            fos.getFD().sync();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement de " + this.file.getName() + " : " + e.getMessage());
        }
    }
}

