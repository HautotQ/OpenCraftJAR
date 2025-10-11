import javax.swing.*;
import java.awt.*;

public class AddQuestionView extends JPanel {
    public AddQuestionView(QuestionStore store, Runnable onAdded) {
        setLayout(new GridLayout(3, 2));

        JTextField questionField = new JTextField();
        JTextField answerField = new JTextField();
        JButton addButton = new JButton("Ajouter");

        add(new JLabel("Question:"));
        add(questionField);
        add(new JLabel("Réponse:"));
        add(answerField);
        add(new JLabel(""));
        add(addButton);

        addButton.addActionListener(e -> {
            if (!questionField.getText().isEmpty() && !answerField.getText().isEmpty()) {
                store.addQuestion(new Question(questionField.getText(), answerField.getText()));
                questionField.setText("");
                answerField.setText("");
                onAdded.run();
            } else {
                JOptionPane.showMessageDialog(this, "Remplis tous les champs !");
            }
        });
    }
}
