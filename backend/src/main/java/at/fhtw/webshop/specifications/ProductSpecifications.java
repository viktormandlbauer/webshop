package at.fhtw.webshop.specifications;

import at.fhtw.webshop.model.Product;
import at.fhtw.webshop.dto.ProductSearchCriteria;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications {

    public static Specification<Product> withCriteria(ProductSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getName() != null && !criteria.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"));
            }

            if (criteria.getCategoryName() != null && !criteria.getCategoryName().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("categoryID").get("name")),
                        "%" + criteria.getCategoryName().toLowerCase() + "%"));
            }

            if (criteria.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
            }
            if (criteria.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
            }
            if (criteria.getMinRating() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("avgRating"), criteria.getMinRating()));
            }
            if (criteria.getMaxRating() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("avgRating"), criteria.getMaxRating()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}