package at.fhtw.webshop.service;

import at.fhtw.webshop.dto.AddressDto;
import at.fhtw.webshop.dto.CustomerOrderDto;
import at.fhtw.webshop.dto.OrderDto;
import at.fhtw.webshop.dto.OrderItemDto;
import at.fhtw.webshop.dto.receipt.ReceiptDto;
import at.fhtw.webshop.dto.receipt.ReceiptItemDto;
import at.fhtw.webshop.enums.OrderStatus;
import at.fhtw.webshop.model.*;
import at.fhtw.webshop.repository.*;
import at.fhtw.webshop.security.CustomUserDetails;
import at.fhtw.webshop.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ProductRepository productRepository;

    private final ReceiptPdfService receiptPdfService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, AddressRepository addressRepository, OrderItemRepository orderItemRepository, PaymentMethodRepository paymentMethodRepository, ProductRepository productRepository, ReceiptPdfService receiptPdfService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.productRepository = productRepository;
        this.receiptPdfService = receiptPdfService;
    }


    /**
     * public List<OrderDto> getOrderForUser(String username) {
     * List<Order> orders = orderRepository.findByUsername(username);
     * return orders.stream().map(order -> new OrderDto(
     * order.getId(),
     * order.getCreatedDate(),
     * order.getSumPrice(),
     * order.getStatus()
     * )).collect(Collectors.toList());
     * }
     * <p>
     * public List<OrderDto> getAllOrders() {
     * List<Order> orders = orderRepository.findAll();
     * return orders.stream().map(order -> new OrderDto(
     * order.getId(),
     * order.getDate(),
     * order.getTotal(),
     * order.getStatus()
     * )).collect(Collectors.toList());
     * }
     **/

    public void newCustomerOrder(CustomerOrderDto customerOrderDto, CustomUserDetails userDetails) {
        Order order = new Order();
        order.setStatus(String.valueOf(OrderStatus.PENDING));
        BigDecimal totalPrice = BigDecimal.ZERO;

        // @TODO: Should be checked if it's the users data
        User customer = this.userRepository.findByUsername(userDetails.getUsername());
        Address shippingAddress = this.addressRepository.findAddressById(customerOrderDto.getShippingAddressId());
        Address billingAddress = this.addressRepository.findAddressById(customerOrderDto.getBillingAddressId());
        PaymentMethod paymentMethod = this.paymentMethodRepository.findPaymentMethodById(customerOrderDto.getPaymentMethodId());

        // Setze die Basisinformationen der Bestellung
        order.setUserID(customer);
        order.setBillingAddressID(shippingAddress);
        order.setShippingAddressID(billingAddress);
        order.setPaymentMethodID(paymentMethod);
        order.setSumPrice(totalPrice);

        this.orderRepository.save(order);

        // Berechne den Gesamtpreis und füge die Artikel hinzu

        ReceiptDto receiptDto = new ReceiptDto();;

        List<ReceiptItemDto> receiptItemDtos = new ArrayList<>();

        for (OrderItemDto orderItemDto : customerOrderDto.getOrderItemDtoList()) {


            logger.info("Processing order item: {}", orderItemDto);

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(orderItemDto.getQuantity());

            // @TODO: Reduce the number of queries by using a batch fetch or similar approach
            // @TODO: Reduce the quantity of products in stock
            Product product = this.productRepository.findProductById(orderItemDto.getProductId());
            orderItem.setOrderID(order);
            orderItem.setProductID(product);

            // Berechne den Preis für das aktuelle Produkt
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(orderItemDto.getQuantity()));

            totalPrice = totalPrice.add(itemTotal);

            this.orderItemRepository.save(orderItem);

            ReceiptItemDto receiptItemDto = new ReceiptItemDto();

            receiptItemDto.setProductId(product.getId());
            receiptItemDto.setProductName(product.getName());
            receiptItemDto.setQuantity(orderItemDto.getQuantity());
            receiptItemDto.setPricePerUnit(product.getPrice());
            receiptItemDto.setSum(itemTotal);

            receiptItemDtos.add(receiptItemDto);
        }

        receiptDto.setItems(receiptItemDtos);

        // Setze den Gesamtpreis der Bestellung
        order.setSumPrice(totalPrice);
        this.orderRepository.save(order);

        receiptDto.setOrderId(order.getId());
        receiptDto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());

        receiptDto.setBillingAddress(
                new AddressDto(
                        billingAddress.getStreetAddress(),
                        billingAddress.getCity(),
                        billingAddress.getPostalCode(),
                        billingAddress.getCountry()
                )
        );

        receiptDto.setShippingAddress(
                new AddressDto(
                        shippingAddress.getStreetAddress(),
                        shippingAddress.getCity(),
                        shippingAddress.getPostalCode(),
                        shippingAddress.getCountry()
                )
        );

        receiptDto.setTotal(totalPrice);
        receiptDto.setDate(LocalDateTime.now());
        receiptDto.setOrderId(order.getId());

        receiptPdfService.generateReceiptPdf(receiptDto);
    }
}