package engine.controller;

import engine.dto.*;
import engine.exception.QuizNotFoundException;
import engine.exception.UnauthorizedQuizAccessException;
import engine.model.Quiz;
import engine.model.Completion;
import engine.model.User;
import engine.service.CompletionService;
import engine.service.QuizService;
import engine.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/quizzes")
public class QuizzesController {
    private final QuizService quizService;
    private final UserService userService;
    private final CompletionService completionService;

    public QuizzesController(QuizService quizService, UserService userService, CompletionService completionService) {
        this.quizService = quizService;
        this.userService = userService;
        this.completionService = completionService;
    }

    // Create a quiz
    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody QuizPostRequestDTO quizPostRequestDTO) {
        var title = quizPostRequestDTO.getTitle();
        var text = quizPostRequestDTO.getText();
        var options = quizPostRequestDTO.getOptions();
        var answer = quizPostRequestDTO.getAnswer();

        // Get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var email = authentication.getName();
        User creator = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        // Ensure valid title and text, ensure there are at least 2 options to select from
        if (title == null || title.isEmpty() || text == null || text.isEmpty() || options == null || options.length < 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Quiz quiz = new Quiz(title, text, options, answer, creator);
        quizService.addQuiz(quiz);

        creator.addQuiz(quiz);

        QuizPostResponseDTO quizPostResponseDTO = new QuizPostResponseDTO(quiz.getId(), title, text, options);
        return ResponseEntity.ok(quizPostResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable long id) {
        Optional<Quiz> optionalQuiz = quizService.findQuizById(id);

        if (optionalQuiz.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Quiz quiz = optionalQuiz.get();

        QuizPostResponseDTO quizPostResponseDTO = new QuizPostResponseDTO(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getText(),
                quiz.getOptions()
        );

        return ResponseEntity.ok(quizPostResponseDTO);
    }

    @GetMapping
    public ResponseEntity<QuizPageResponseDTO> getAllQuizzes(@RequestParam(defaultValue = "1") int page, @AuthenticationPrincipal User user) {
        Page<Quiz> quizzes = quizService.getQuizzes(page, user);
        QuizPageResponseDTO res = new QuizPageResponseDTO(quizzes);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/completed")
    public ResponseEntity<CompletionPageResponseDTO> getCompletedQuizzes(@RequestParam(defaultValue = "1") int page, @AuthenticationPrincipal User user) {
        Page<Completion> completedQuizzes = completionService.getCompletedQuizzes(page, user);
        CompletionPageResponseDTO res = new CompletionPageResponseDTO(completedQuizzes);
        return ResponseEntity.ok(res);
    }

    /**
     * Solve a quiz when the client sends a JSON body containing the single key "answer" which value is an array of
     * indexes of all chosen options as the answer
     */
    @PostMapping("/{id}/solve")
    public ResponseEntity<?> submitQuizAnswer(@PathVariable long id,
                                              @RequestBody QuizSolveRequestDTO quizSolveRequestDTO,
                                              @AuthenticationPrincipal User user
    ) {
        Optional<Quiz> optionalQuiz = quizService.findQuizById(id);
        if (optionalQuiz.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        int[] correctQuizAnswers = optionalQuiz.get().getAnswer();
        int[] submittedAnswers = quizSolveRequestDTO.getAnswer();

        // Handle edge case where the req body answer is null or its array is empty
        if (correctQuizAnswers == null || correctQuizAnswers.length == 0) {
            if (submittedAnswers == null || submittedAnswers.length == 0) {
                saveCompletedQuiz(optionalQuiz, user, completionService);
                return ResponseEntity.ok(new QuizSolveResponseDTO(true, "Congratulations, you're right!"));
            }
            return ResponseEntity.ok(new QuizSolveResponseDTO(false, "Wrong answer! Please, try again"));
        }

        if (submittedAnswers.length != correctQuizAnswers.length) {
            return ResponseEntity.ok(new QuizSolveResponseDTO(false, "Wrong answer! Please, try again"));
        }

        for (int i = 0; i < submittedAnswers.length; i++) {
            if (submittedAnswers[i] != correctQuizAnswers[i]) {
                return ResponseEntity.ok(new QuizSolveResponseDTO(false, "Wrong answer! Please, try again"));
            }
        }

        saveCompletedQuiz(optionalQuiz, user, completionService);
        return ResponseEntity.ok(new QuizSolveResponseDTO(true, "Congratulations, you're right!"));
    }

    private static void saveCompletedQuiz(Optional<Quiz> optionalQuiz, User user, CompletionService service) {
        Quiz quiz = optionalQuiz.get();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Completion completedQuiz = new Completion(quiz, user, currentDateTime);
        service.addCompletedQuiz(completedQuiz);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable long id) {
        // Get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var email = authentication.getName();
        User user = userService.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        try {
            Quiz quiz = quizService.findQuizById(id).orElseThrow(QuizNotFoundException::new);
            quizService.deleteQuiz(quiz, user);
            // Deletion successful, remove quiz from User and return 204 No Content
            user.removeQuiz(quiz);
            return ResponseEntity.noContent().build();
        } catch (UnauthorizedQuizAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (QuizNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
