package engine.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String text;
    private String[] options;
    private int[] answer;
    @ManyToOne
    @JoinColumn(name = "user_id") // Foreign key column in the quizzes table
    private User creator; // Reference to the user who created the quiz
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Completion> completions = new ArrayList<>();

    public Quiz() {}

    public Quiz(String title, String text, String[] options, int[] answer, User creator) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public long getId() {
        return id;
    }

    public int[] getAnswer() {
        return answer;
    }

    public User getCreator() {
        return creator;
    }

    public List<Completion> getCompletions() {
        return completions;
    }
}
