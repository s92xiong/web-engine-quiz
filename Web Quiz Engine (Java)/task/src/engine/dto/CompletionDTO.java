package engine.dto;

import engine.model.Completion;
import engine.model.Quiz;

import java.time.LocalDateTime;

public class CompletionDTO {
    private final long id;
    private final LocalDateTime completedAt;

    public CompletionDTO(Completion completion) {
        this.id = completion.getQuiz().getId();
        this.completedAt = completion.getCompletedAt();
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
