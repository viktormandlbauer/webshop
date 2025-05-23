package at.fhtw.webshop.repository;

import at.fhtw.webshop.enums.OrderStatus;
import at.fhtw.webshop.model.Order;
import at.fhtw.webshop.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int id);

    @Query("SELECT o FROM Order o JOIN o.user u WHERE u.username = :username")
    List<Order> findByUsername(@Param("username") String username);
}