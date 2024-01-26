package engine.repository;

import engine.model.Completion;
import engine.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletionRepository extends PagingAndSortingRepository<Completion, Long> {
    // Custom queries:

    // findByUser: Tell Spring Data JPA that we want to find entities based on the User field
    // OrderByCompletedAtDesc: Specify the sorting order based on the completedAt field in descending order
    // Spring Data JPA derives the query from the method name and automatically generates the appropriate SQL query
    Page<Completion> findByUserOrderByCompletedAtDesc(User user, Pageable pageable);
}
