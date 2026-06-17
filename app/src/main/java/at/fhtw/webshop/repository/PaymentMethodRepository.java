package at.fhtw.webshop.repository;

import at.fhtw.webshop.model.PaymentMethod;
import at.fhtw.webshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    List<PaymentMethod> findByUser(User user);
}
