import java.io.*;
import java.util.*;

public class QuestionStore {
    private final List<Question> questions = new ArrayList<>();

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void removeQuestion(Question q) {
        questions.remove(q);
    }

    public void clear() {
        questions.clear();
    }

    // Importer depuis un fichier TXT
    public void importFromFile(File file) throws IOException {
        questions.clear();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String question = line.trim();
            if (question.isEmpty()) continue;
            String answer = reader.readLine();
            if (answer == null) answer = "";
            questions.add(new Question(question, answer.trim()));
            reader.readLine(); // ligne vide entre deux questions
        }
        reader.close();
    }

    // Exporter vers un fichier TXT
    public void exportToFile(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (Question q : questions) {
            writer.write(q.getQuestion() + "\n");
            writer.write(q.getAnswer() + "\n\n");
        }
        writer.close();
    }
}
