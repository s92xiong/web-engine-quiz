package engine.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity or Model for a Completed Quiz
 */
@Entity
@Table(name = "completion")
public class Completion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime completedAt;

    public Completion() {}

    public Completion(Quiz quiz, User user, LocalDateTime completedAt) {
        this.quiz = quiz;
        this.user = user;
        this.completedAt = completedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
