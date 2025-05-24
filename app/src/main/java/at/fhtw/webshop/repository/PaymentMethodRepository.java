package at.fhtw.webshop.repository;
import at.fhtw.webshop.dto.PaymentMethodDto;
import at.fhtw.webshop.model.PaymentMethod;
import at.fhtw.webshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    List<PaymentMethodDto> getPaymentMethodsByUserID(User userID);
}