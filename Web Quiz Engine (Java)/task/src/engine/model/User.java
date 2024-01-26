package engine.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;
    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
    private List<Quiz> quizzes; // List of quizzes created by the users
    @OneToMany(mappedBy = "user")
    private final List<Completion> completions = new ArrayList<>();

    public User() {}

    public User(String email, String password, List<Quiz> quizzes) {
        this.email = email;
        this.password = password;
        this.quizzes = quizzes;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
//        quiz.setCreator(this);
    }

    public void removeQuiz(Quiz quiz) {
        quizzes.remove(quiz);
//        quiz.setCreator(null);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Set as true
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Set as true
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Set as true
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Set as true
        return true;
    }
}

