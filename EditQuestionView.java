import javax.swing.*;
import java.awt.*;

public class EditQuestionView extends JPanel {
    public EditQuestionView(QuestionStore store, Runnable onEdited) {
        setLayout(new BorderLayout());

        DefaultListModel<Question> model = new DefaultListModel<>();
        JList<Question> list = new JList<>(model);
        refresh(model, store);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton editBtn = new JButton("Modifier");
        add(editBtn, BorderLayout.SOUTH);

        editBtn.addActionListener(e -> {
            Question selected = list.getSelectedValue();
            if (selected == null) return;
            JTextField questionField = new JTextField(selected.getQuestion());
            JTextField answerField = new JTextField(selected.getAnswer());

            int result = JOptionPane.showConfirmDialog(this, new Object[]{
                "Question:", questionField,
                "Réponse:", answerField
            }, "Modifier la question", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                selected.setQuestion(questionField.getText());
                selected.setAnswer(answerField.getText());
                refresh(model, store);
                onEdited.run();
            }
        });
    }

    private void refresh(DefaultListModel<Question> model, QuestionStore store) {
        model.clear();
        for (Question q : store.getQuestions()) {
            model.addElement(q);
        }
    }
}
