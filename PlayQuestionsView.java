import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import Question;
import QuestionStore;
import Settings;

public class PlayQuestionsView extends JPanel {
    private final QuestionStore questionStore;
    private Question currentQuestion;
    private boolean showingAnswer = false;
    private String userAnswer = "";
    private List<Question> remainingQuestions = new ArrayList<>();
    private boolean allQuestionsAnsweredOnce = false;
    private final List<Question> incorrectQuestions = new ArrayList<>();
    private final Set<Question> incorrectQuestionSet = new HashSet<>();
    private boolean shouldReaskIncorrectQuestion = false;
    private int score = 0;
    private final Consumer<JPanel> onEndView;

    // Composants Swing
    private final JLabel questionLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel progressLabel = new JLabel("", SwingConstants.CENTER);
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JTextField answerField = new JTextField();
    private final JLabel remainingLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton checkButton = new JButton("Vérifier la réponse");

    public PlayQuestionsView(QuestionStore store, Consumer<JPanel> onEndView) {
        this.questionStore = store;
        this.onEndView = onEndView;

        this.remainingQuestions = new ArrayList<>(store.getObservableQuestions());
        Collections.shuffle(this.remainingQuestions);
        if (!this.remainingQuestions.isEmpty()) {
            this.currentQuestion = this.remainingQuestions.get(0);
        }

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (store.getObservableQuestions().isEmpty()) {
            JLabel label = new JLabel("Pas de questions enregistrées...", SwingConstants.CENTER);
            add(label);
            return;
        }

        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        progressBar.setStringPainted(true);
        answerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        checkButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        checkButton.addActionListener(e -> checkAnswer());

        add(remainingLabel);
        add(Box.createVerticalStrut(10));
        add(progressLabel);
        add(Box.createVerticalStrut(5));
        add(progressBar);
        add(Box.createVerticalStrut(15));
        add(questionLabel);
        add(Box.createVerticalStrut(10));
        add(answerField);
        add(Box.createVerticalStrut(10));
        add(checkButton);

        askNextQuestion();
    }

    private void updateUIState() {
        if (allQuestionsAnsweredOnce) {
            showEndView();
            return;
        }

        if (currentQuestion != null) {
            questionLabel.setText(currentQuestion.getQuery());
            remainingLabel.setText("Questions restantes : " + remainingQuestions.size());

            int total = questionStore.getObservableQuestions().size();
            double progress = (double) (total - remainingQuestions.size()) / total;
            progressBar.setValue((int) (progress * 100));
            progressLabel.setText("Progression : " + (int) (progress * 100) + "%");
        }
    }

    private void askNextQuestion() {
        if (shouldReaskIncorrectQuestion) {
            shouldReaskIncorrectQuestion = false;
            if (!incorrectQuestions.isEmpty()) {
                remainingQuestions.addAll(incorrectQuestions);
                incorrectQuestions.clear();
            }
        }

        if (remainingQuestions.isEmpty()) {
            allQuestionsAnsweredOnce = true;
            updateUIState();
            return;
        }

        currentQuestion = remainingQuestions.remove(0);
        showingAnswer = false;
        userAnswer = "";
        answerField.setText("");
        updateUIState();
    }

    private void checkAnswer() {
        userAnswer = answerField.getText().trim().toLowerCase();

        if (userAnswer.isEmpty()) {
            showAlert("Réponse manquante", "La réponse à mettre est :\n" + currentQuestion.getAnswer(), JOptionPane.ERROR_MESSAGE);
            incorrectQuestions.add(currentQuestion);
            incorrectQuestionSet.add(currentQuestion);
            shouldReaskIncorrectQuestion = true;
            answerField.setText("");
            return;
        }

        String correctAnswer = currentQuestion.getAnswer().trim().toLowerCase();

        if (userAnswer.equalsIgnoreCase(correctAnswer)
            || (isApproximatelyEqual(userAnswer, correctAnswer) && Settings.isCoolMode())) {

            if (!incorrectQuestionSet.contains(currentQuestion)) {
                score++;
            }
            askNextQuestion();
        } else {
            showAlert("Mauvaise réponse", "La bonne réponse est :\n" + currentQuestion.getAnswer(), JOptionPane.ERROR_MESSAGE);
            incorrectQuestions.add(currentQuestion);
            incorrectQuestionSet.add(currentQuestion);
            shouldReaskIncorrectQuestion = true;
            answerField.setText("");
        }
    }

    private void showAlert(String title, String message, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    private void showEndView() {
        JPanel endPanel = new JPanel();
        endPanel.setLayout(new BoxLayout(endPanel, BoxLayout.Y_AXIS));
        endPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel endLabel = new JLabel(
            "<html><div style='text-align:center;'>Fin des questions !<br><br>Score : " +
            score + "/" + questionStore.getObservableQuestions().size() + "</div></html>",
            SwingConstants.CENTER
        );
        endLabel.setFont(new Font("Arial", Font.BOLD, 20));
        endLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        endPanel.add(endLabel);

        if (onEndView != null) {
            onEndView.accept(endPanel);
        }
    }

    private boolean isApproximatelyEqual(String input, String target) {
        return levenshteinDistance(input, target) <= 4;
    }

    private int levenshteinDistance(String a, String b) {
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) {
            costs[j] = j;
        }

        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}