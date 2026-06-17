package at.fhtw.webshop.repository;

import at.fhtw.webshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByNameIgnoreCase(String name);

}
