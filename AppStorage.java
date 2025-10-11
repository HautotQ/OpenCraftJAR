import javax.swing.*;
import java.awt.*;
import Question;
import QuestionStore;

public class AddQuestionView extends JPanel {
    private final JTextField queryField;
    private final JTextField answerField;
    private final QuestionStore questionStore;

    public AddQuestionView(QuestionStore store) {
        this.questionStore = store;

        // Mise en page verticale
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel title = new JLabel("Ajouter une Question");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Champ Question
        JLabel queryLabel = new JLabel("Question :");
        queryField = new JTextField();
        queryField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        queryField.setToolTipText("Tapez la question ici...");

        // Champ Réponse
        JLabel answerLabel = new JLabel("Réponse :");
        answerField = new JTextField();
        answerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        answerField.setToolTipText("Tapez la réponse ici...");

        // Bouton Ajouter
        JButton addButton = new JButton("Ajouter");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> addQuestion());

        // Espacements
        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(15));
        add(queryLabel);
        add(queryField);
        add(Box.createVerticalStrut(10));
        add(answerLabel);
        add(answerField);
        add(Box.createVerticalStrut(15));
        add(addButton);
    }

    private void addQuestion() {
        String questionText = queryField.getText().trim();
        String answerText = answerField.getText().trim();

        if (!questionText.isEmpty() && !answerText.isEmpty()) {
            Question question = new Question(questionText, answerText);
            questionStore.addQuestion(question);
            queryField.setText("");
            answerField.setText("");

            System.out.println("Ajout : " + questionText + " => " + answerText);
            System.out.println("Taille actuelle : " + questionStore.getObservableQuestions().size());
            questionStore.getObservableQuestions().forEach(q -> System.out.println("-> " + q.getQuery()));

            JOptionPane.showMessageDialog(this, "Question ajoutée avec succès !");
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez remplir les deux champs.", "Champs manquants", JOptionPane.WARNING_MESSAGE);
        }
    }
}