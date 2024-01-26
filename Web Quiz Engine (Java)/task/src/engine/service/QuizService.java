package engine.service;

import engine.exception.UnauthorizedQuizAccessException;
import engine.model.Quiz;
import engine.model.User;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public void addQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    public Page<Quiz> getQuizzes(int page, User user) {
        // Adjust sorting based on your requirements
        // One solution is to specify the Sort in the Pageable to sort by date in descending order
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id"));
        return quizRepository.findAll(pageable);
    }

    public void deleteQuiz(Quiz quiz, User authenticatedUser) {
        if (authenticatedUser.getId() == quiz.getCreator().getId()) {
            quizRepository.delete(quiz);
        } else {
            throw new UnauthorizedQuizAccessException();
        }
    }

    public Optional<Quiz> findQuizById(long id) {
        return quizRepository.findById(id);
    }
}
