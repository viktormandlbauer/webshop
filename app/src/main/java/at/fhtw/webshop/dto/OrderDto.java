package at.fhtw.webshop.dto;

import at.fhtw.webshop.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderDto {
    private Integer orderId;
    private LocalDate date;
    private BigDecimal total;
    private OrderStatus status;

    public OrderDto(Integer orderId, LocalDate date, BigDecimal total, OrderStatus status) {
        this.orderId = orderId;
        this.date = date;
        this.total = total;
        this.status = status;
    }

    public OrderDto() {

    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}