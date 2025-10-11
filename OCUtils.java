import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

public class OCUtils {

    /**
     * Affiche une boîte de dialogue d'information ou d'erreur.
     */
    public static void showAlert(String type, String title, String headerText, String message) {
        int messageType;

        switch (type.toLowerCase()) {
            case "error":
                messageType = JOptionPane.ERROR_MESSAGE;
                break;
            case "warning":
                messageType = JOptionPane.WARNING_MESSAGE;
                break;
            case "info":
            default:
                messageType = JOptionPane.INFORMATION_MESSAGE;
                break;
        }

        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    /**
     * Affiche une boîte de dialogue avec Oui / Annuler et retourne true si Oui.
     */
    public static boolean showAlertBool(String type, String title, String headerText, String message) {
        int optionType = JOptionPane.YES_NO_OPTION;
        int messageType;

        switch (type.toLowerCase()) {
            case "error":
                messageType = JOptionPane.ERROR_MESSAGE;
                break;
            case "warning":
                messageType = JOptionPane.WARNING_MESSAGE;
                break;
            case "info":
            default:
                messageType = JOptionPane.QUESTION_MESSAGE;
                break;
        }

        int result = JOptionPane.showConfirmDialog(null, message, title, optionType, messageType);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Applique un style Swing à la fenêtre.
     * Remplace le CSS JavaFX par une apparence native Swing.
     */
    public static void applyCSS(Window window) {
        try {
            // Tu peux adapter ce code pour charger un LookAndFeel custom
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(window);
            System.out.println("✅ Thème Swing appliqué avec succès !");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'application du thème : " + e.getMessage());
        }
    }

    /**
     * Charge et applique une police JetBrainsMono depuis les ressources.
     */
    public static void getFont() {
        try (InputStream fontStream = OCUtils.class.getResourceAsStream("/fonts/JetBrainsMono-Bold.ttf")) {
            if (fontStream == null) {
                System.err.println("❌ Fichier de police introuvable !");
                return;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            System.out.println("✅ Police chargée : " + font.getFontName());
        } catch (Exception e) {
            System.err.println("❌ Erreur de chargement de la police : " + e.getMessage());
        }
    }
}