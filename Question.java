import java.util.Objects;

public class Question {
    private String query;
    private String answer;

    public Question(String query, String answer) {
        this.query = query;
        this.answer = answer;
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Question)) {
            return false;
        }
        Question other = (Question)obj;
        return Objects.equals(this.query, other.query) && Objects.equals(this.answer, other.answer);
    }

    public int hashCode() {
        return Objects.hash(this.query, this.answer);
    }

    public String toString() {
        return this.query + " \u2192 " + this.answer;
    }
}

