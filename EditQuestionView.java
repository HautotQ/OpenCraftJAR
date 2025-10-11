import javax.swing.*;
import java.awt.*;

public class EditQuestionView extends JPanel {
    private final JTextField questionField;
    private final JTextField answerField;
    private final JButton saveButton;
    private final Question question;
    private final QuestionStore store;

    public EditQuestionView(Question question, QuestionStore store) {
        this.question = question;
        this.store = store;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel questionLabel = new JLabel("Modifier la question :");
        questionField = new JTextField(question.getQuery());
        questionField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel answerLabel = new JLabel("Modifier la réponse :");
        answerField = new JTextField(question.getAnswer());
        answerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        saveButton = new JButton("Enregistrer");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(e -> saveChanges());

        add(Box.createVerticalStrut(10));
        add(questionLabel);
        add(questionField);
        add(Box.createVerticalStrut(10));
        add(answerLabel);
        add(answerField);
        add(Box.createVerticalStrut(15));
        add(saveButton);
    }

    private void saveChanges() {
        String newQuery = questionField.getText().trim();
        String newAnswer = answerField.getText().trim();

        if (newQuery.isEmpty() || newAnswer.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "La question et la réponse ne peuvent pas être vides.",
                "Champs manquants",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        question.setQuery(newQuery);
        question.setAnswer(newAnswer);
        store.updateQuestion(question);

        // Ferme la fenêtre si contenue dans un JDialog
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }
}
