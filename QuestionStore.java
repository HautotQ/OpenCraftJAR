import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionStore {

    private final List<Question> questions = new ArrayList<>();
    private static final String SAVE_PATH = System.getProperty("user.home") + "/.opencraft-questions.txt";

    public QuestionStore() {
        loadQuestions();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
        saveQuestions();
    }

    public void deleteQuestion(Question question) {
        questions.remove(question);
        saveQuestions();
    }

    public void updateQuestion(Question question) {
        int index = questions.indexOf(question);
        if (index >= 0) {
            questions.set(index, question);
            saveQuestions();
        }
    }

    public void saveQuestions() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAVE_PATH))) {
            for (Question q : questions) {
                writer.write(q.getQuery());
                writer.newLine();
                writer.write(q.getAnswer());
                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la sauvegarde des questions : " + e.getMessage());
        }
    }

    public void loadQuestions() {
        Path path = Paths.get(SAVE_PATH);
        if (!Files.exists(path)) {
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            questions.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String questionText = line.trim();
                if (questionText.isEmpty()) continue;
                String answer = reader.readLine();
                if (answer == null) break;
                questions.add(new Question(questionText, answer.trim()));
                reader.readLine(); // Ligne vide
            }
        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement des questions : " + e.getMessage());
        }
    }
}