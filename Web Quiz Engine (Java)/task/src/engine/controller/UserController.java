package engine.controller;

import engine.dto.UserRegistrationDTO;
import engine.exception.QuizNotFoundException;
import engine.exception.UnauthorizedQuizAccessException;
import engine.model.Quiz;
import engine.model.User;
import engine.service.QuizService;
import engine.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final QuizService quizService;

    public UserController(UserService userService, QuizService quizService) {
        this.userService = userService;
        this.quizService = quizService;
    }

    @GetMapping("/check-auth")
    public String checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Logged in user: " + authentication.getName();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO, BindingResult result) {

        // If validation errors on the email or password, return 400 (BAD REQUEST) status code
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        var email = userRegistrationDTO.getEmail();
        var password = userRegistrationDTO.getPassword();
        List<Quiz> quizzes = new CopyOnWriteArrayList<>();

        try {
            userService.registerUser(email, password, quizzes);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
