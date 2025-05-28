package at.fhtw.webshop.repository;

import at.fhtw.webshop.model.Order;
import at.fhtw.webshop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderID(Order order);
}
