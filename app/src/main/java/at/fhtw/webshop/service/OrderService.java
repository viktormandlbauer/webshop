package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.OrderDto;
import at.fhtw.webshop.model.Order;
import at.fhtw.webshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderDto> getOrdersForCurrentUser(int userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(order -> new OrderDto(
                order.getId(),
                order.getDate(),
                order.getTotal(),
                order.getStatus()
        )).collect(Collectors.toList());
    }
}