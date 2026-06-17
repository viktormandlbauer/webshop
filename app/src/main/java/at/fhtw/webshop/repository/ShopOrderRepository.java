package at.fhtw.webshop.repository;

import at.fhtw.webshop.model.ShopOrder;
import at.fhtw.webshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopOrderRepository extends JpaRepository<ShopOrder, Integer> {
    List<ShopOrder> findByUserOrderByOrderDateAsc(User user);
}
