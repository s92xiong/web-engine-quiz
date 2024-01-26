package engine.service;

import engine.model.Completion;
import engine.model.User;
import engine.repository.CompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CompletionService {
    private final CompletionRepository completionRepository;

    @Autowired
    public CompletionService(CompletionRepository completionRepository) {
        this.completionRepository = completionRepository;
    }

    public void addCompletedQuiz(Completion completion) {
        completionRepository.save(completion);
    }

    public Page<Completion> getCompletedQuizzes(int page, User user) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("completedAt").descending());
        return completionRepository.findByUserOrderByCompletedAtDesc(user, pageable);
    }
}
