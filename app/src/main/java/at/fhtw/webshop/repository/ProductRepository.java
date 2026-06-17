package at.fhtw.webshop.repository;

import at.fhtw.webshop.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    Page<Product> findByCategory_NameIgnoreCase(String categoryName, Pageable pageable);
    List<Product> findTop20ByNameContainingIgnoreCase(String name);
}
