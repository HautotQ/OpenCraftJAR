import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class StudyView extends JPanel {
    private final Queue<Question> queue;
    private final JLabel questionLabel;
    private final JTextField answerField;
    private final JButton validateButton;
    private Question current;

    public StudyView(QuestionStore store) {
        setLayout(new BorderLayout());

        // Crée une liste pour mélanger les questions
        List<Question> shuffled = new LinkedList<>(store.getQuestions());
        Collections.shuffle(shuffled);
        queue = new LinkedList<>(shuffled);

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(questionLabel, BorderLayout.NORTH);

        answerField = new JTextField();
        add(answerField, BorderLayout.CENTER);

        validateButton = new JButton("Valider");
        add(validateButton, BorderLayout.SOUTH);

        validateButton.addActionListener(e -> checkAnswer());
        nextQuestion();
    }

    private void checkAnswer() {
        if (current == null) return;
        String answer = answerField.getText().trim();
        if (answer.equalsIgnoreCase(current.getAnswer())) {
            JOptionPane.showMessageDialog(this, "✅ Bonne réponse !");
        } else {
            JOptionPane.showMessageDialog(this, "❌ Mauvaise réponse. La bonne réponse était : " + current.getAnswer());
            queue.add(current); // reposer à la fin
        }
        nextQuestion();
    }

    private void nextQuestion() {
        answerField.setText("");
        current = queue.poll();
        if (current == null) {
            questionLabel.setText("🎉 Terminé !");
            answerField.setEnabled(false);
            validateButton.setEnabled(false);
        } else {
            questionLabel.setText(current.getQuestion());
        }
    }
}
