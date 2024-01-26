package engine.service;

import engine.exception.QuizNotFoundException;
import engine.exception.UnauthorizedQuizAccessException;
import engine.model.Quiz;
import engine.model.User;
import engine.repository.QuizRepository;
import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final QuizRepository quizRepository;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, QuizRepository quizRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.quizRepository = quizRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public User registerUser(String email, String password, List<Quiz> quizzes) {
        // Handle duplicate email
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already taken");
        }

        // Encrypt the password
        String encryptedPassword = passwordEncoder.encode(password);

        // Create and save the user
        User user = new User(email, password, quizzes);
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
