package at.fhtw.webshop.dto.admin;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OrderListItemDto {
    private int orderId;
    private BigDecimal total;
    private LocalDate date;
    private String status;

    public OrderListItemDto(int orderId, BigDecimal total, LocalDate date, String status) {
        this.orderId = orderId;
        this.total = total;
        this.date = date;
        this.status = status;
    }

    // Getter und Setter
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}