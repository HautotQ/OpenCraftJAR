import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ViewQuestionsView extends JPanel {

    private final QuestionStore questionStore;
    private final DefaultListModel<Question> listModel = new DefaultListModel<>();
    private final JList<Question> listView = new JList<>(listModel);

    public ViewQuestionsView(QuestionStore store) {
        this.questionStore = store;
        store.loadQuestions();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // Titre et boutons
        JLabel title = new JLabel("Liste des Questions");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        JButton btnImporter = new JButton("Importer");
        JButton btnExporter = new JButton("Exporter");
        JButton btnDeleteAll = new JButton("Tout supprimer");

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(title);
        topPanel.add(btnImporter);
        topPanel.add(btnExporter);
        topPanel.add(btnDeleteAll);

        add(topPanel, BorderLayout.NORTH);

        // Liste des questions
        refreshList();

        listView.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JTextArea text = new JTextArea("Q: " + value.getQuery() + "\nR: " + value.getAnswer());
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            text.setEditable(false);
            if (isSelected) {
                text.setBackground(new Color(220, 235, 255));
            }
            return text;
        });

        // Menu contextuel
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Modifier");
        JMenuItem deleteItem = new JMenuItem("Supprimer");
        contextMenu.add(editItem);
        contextMenu.add(deleteItem);

        listView.setComponentPopupMenu(contextMenu);

        editItem.addActionListener(e -> {
            Question selected = listView.getSelectedValue();
            if (selected != null) openEditDialog(selected);
        });

        deleteItem.addActionListener(e -> {
            Question selected = listView.getSelectedValue();
            if (selected != null) {
                questionStore.deleteQuestion(selected);
                refreshList();
            }
        });

        add(new JScrollPane(listView), BorderLayout.CENTER);

        // Actions boutons
        btnImporter.addActionListener(e -> importText());
        btnExporter.addActionListener(e -> exporterQuestions());
        btnDeleteAll.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Si vous n'avez pas enregistré vos fichiers, tout sera perdu.\nTout supprimer ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                questionStore.getObservableQuestions().clear();
                questionStore.saveQuestions();
                refreshList();
            }
        });
    }

    private void refreshList() {
        listModel.clear();
        for (Question q : questionStore.getObservableQuestions()) {
            listModel.addElement(q);
        }
    }

    private void exporterQuestions() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Exporter les questions");
        chooser.setFileFilter(new FileNameExtensionFilter("Fichier clist ou txt", "clist", "txt"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Question q : questionStore.getObservableQuestions()) {
                    writer.write(q.getQuery() + "|" + q.getAnswer());
                    writer.newLine();
                }
            } catch (IOException ex) {
                showError("Erreur lors de l’export : " + ex.getMessage());
            }
        }
    }

    private void importText() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Importer un fichier clist/txt");
        chooser.setFileFilter(new FileNameExtensionFilter("Fichier clist/txt", "clist", "txt"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                questionStore.getObservableQuestions().clear();
                List<Question> parsed = parseQuestions(content);
                questionStore.getObservableQuestions().addAll(parsed);
                questionStore.saveQuestions();
                refreshList();
            } catch (IOException ex) {
                showError("Erreur lors de la lecture : " + ex.getMessage());
            }
        }
    }

    private void openEditDialog(Question question) {
        JTextField questionField = new JTextField(question.getQuery());
        JTextField answerField = new JTextField(question.getAnswer());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Modifier la question :"));
        panel.add(questionField);
        panel.add(new JLabel("Modifier la réponse :"));
        panel.add(answerField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modifier la question", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            question.setQuery(questionField.getText().trim());
            question.setAnswer(answerField.getText().trim());
            questionStore.updateQuestion(question);
            refreshList();
        }
    }

    private List<Question> parseQuestions(String fileContents) {
        List<Question> parsed = new ArrayList<>();
        String[] blocks = fileContents.split("\\R\\R");
        for (String block : blocks) {
            String[] qa = block.split("\\R", 2);
            if (qa.length >= 2) {
                parsed.add(new Question(qa[0].trim(), qa[1].trim()));
            }
        }
        return parsed;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
