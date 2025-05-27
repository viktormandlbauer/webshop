package at.fhtw.webshop.repository;

import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.model.Review;
import at.fhtw.webshop.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    boolean existsByProductIDAndUserID(Product product, User user);
    List<Review> findByProductID(@NotNull Product productID);
}
