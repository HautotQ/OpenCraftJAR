import javax.swing.*;
import java.awt.*;
import ContentView;

public class Launcher {

    public static void main(String[] args) {
        // S'assure que l'interface Swing démarre sur le thread principal de l'UI
        SwingUtilities.invokeLater(() -> {
            // Initialisation de la fenêtre principale
            JFrame frame = new JFrame("OpenCraft");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Application du thème ou des polices (remplace OCUtils)
            OCUtils.getFont();
            OCUtils.applyCSS(frame);

            // Chargement du contenu principal (équivalent de ContentView JavaFX)
            ContentView content = new ContentView();
            frame.setContentPane(content);

            // Centrer la fenêtre et l’afficher
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}