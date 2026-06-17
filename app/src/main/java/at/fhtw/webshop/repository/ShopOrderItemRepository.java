package at.fhtw.webshop.repository;

import at.fhtw.webshop.model.ShopOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopOrderItemRepository extends JpaRepository<ShopOrderItem, Integer> {
}
