package engine.dto;

import engine.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class QuizPageResponseDTO {
    private final int totalPages;
    private final long totalElements;
    private final boolean last;
    private final boolean first;
    private final Sort sort;
    private final int number;
    private final int numberOfElements;
    private final int size;
    private final boolean empty;
    private final Pageable pageable;
    private final List<QuizDTO> content;

    public QuizPageResponseDTO(Page<Quiz> quizzes) {
        this.totalPages = quizzes.getTotalPages();
        this.totalElements = quizzes.getTotalElements();
        this.last = quizzes.isLast();
        this.first = quizzes.isFirst();
        this.sort = quizzes.getSort();
        this.number = quizzes.getNumber();
        this.numberOfElements = quizzes.getNumberOfElements();
        this.size = quizzes.getSize();
        this.empty = quizzes.isEmpty();
        this.pageable = quizzes.getPageable();
        this.content = mapQuizzesToDTOs(quizzes.getContent());
    }

    private List<QuizDTO> mapQuizzesToDTOs(List<Quiz> quizzes) {
        // Map each Quiz object to QuizDTO
        return quizzes.stream()
                .map(QuizDTO::new)
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

    public Sort getSort() {
        return sort;
    }

    public int getNumber() {
        return number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public List<QuizDTO> getContent() {
        return content;
    }
}
