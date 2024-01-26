package engine.repository;

import engine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Add any custom queries if needed
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
