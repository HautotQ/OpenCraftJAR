import javax.swing.*;
import java.awt.*;
import java.io.File;

public class QuestionListView extends JPanel {
    private final JList<Question> list;
    private final DefaultListModel<Question> model;
    private final QuestionStore store;

    public QuestionListView(QuestionStore store) {
        this.store = store;
        setLayout(new BorderLayout());

        model = new DefaultListModel<>();
        list = new JList<>(model);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton importBtn = new JButton("Importer");
        JButton exportBtn = new JButton("Exporter");
        buttons.add(importBtn);
        buttons.add(exportBtn);
        add(buttons, BorderLayout.SOUTH);

        importBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    store.importFromFile(chooser.getSelectedFile());
                    refresh();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
                }
            }
        });

        exportBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    store.exportToFile(chooser.getSelectedFile());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
                }
            }
        });

        refresh();
    }

    public void refresh() {
        model.clear();
        for (Question q : store.getQuestions()) {
            model.addElement(q);
        }
    }
}
