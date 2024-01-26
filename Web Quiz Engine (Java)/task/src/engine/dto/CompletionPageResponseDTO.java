package engine.dto;

import engine.model.Completion;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class CompletionPageResponseDTO {
    private final int totalPages;
    private final long totalElements;
    private final boolean last;
    private final boolean first;
    private final boolean empty;
    private final List<CompletionDTO> content;

    public CompletionPageResponseDTO(Page<Completion> completedQuizzes) {
        this.totalPages = completedQuizzes.getTotalPages();
        this.totalElements = completedQuizzes.getTotalElements();
        this.last = completedQuizzes.isLast();
        this.first = completedQuizzes.isFirst();
        this.empty = completedQuizzes.isEmpty();
        this.content = mapQuizzesToDTOs(completedQuizzes.getContent());
    }

    private List<CompletionDTO> mapQuizzesToDTOs(List<Completion> quizzes) {
        // Map each QuizCompletion object to QuizCompletionDTO
        return quizzes.stream()
                .map(CompletionDTO::new)
                .collect(Collectors.toList());
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public boolean isLast() {
        return last;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isEmpty() {
        return empty;
    }

    public List<CompletionDTO> getContent() {
        return content;
    }
}
