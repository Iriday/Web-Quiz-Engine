package engine.quiz;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class QuizCompletionInfo {
    private long id; //completed quiz id

    @Id
    private String completedAt;

    @JsonIgnore
    private String completedBy;

    public QuizCompletionInfo() {
    }

    public QuizCompletionInfo(String completedBy, long completedQuizId, String completedAt) {
        this.completedBy = completedBy;
        this.id = completedQuizId;
        this.completedAt = completedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long completedQuizId) {
        this.id = completedQuizId;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}
