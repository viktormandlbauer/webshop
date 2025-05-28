package at.fhtw.webshop.repository;

import at.fhtw.webshop.dto.ProductAddDto;
import at.fhtw.webshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    Product findProductById(Integer productId);
}