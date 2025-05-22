package at.fhtw.webshop.dto;

import java.math.BigDecimal;

public class OrderItemDto {

    private String productName;
    private int quantity;
    private BigDecimal pricePerUnit;
    private BigDecimal totalPrice;

    public OrderItemDto() {}

    public OrderItemDto(String productName, int quantity, BigDecimal pricePerUnit, BigDecimal totalPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.totalPrice = totalPrice;
    }

    // Getters and setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
