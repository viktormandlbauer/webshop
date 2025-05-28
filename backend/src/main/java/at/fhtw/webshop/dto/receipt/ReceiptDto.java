package at.fhtw.webshop.dto.receipt;

import at.fhtw.webshop.dto.AddressDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReceiptDto {
    private int orderId;
    private LocalDateTime date;
    private String customerName;
    private AddressDto billingAddress;
    private AddressDto shippingAddress;
    private List<ReceiptItemDto> items;
    private BigDecimal total;

    // Getter und Setter
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public AddressDto getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(AddressDto billingAddress) {
        this.billingAddress = billingAddress;
    }

    public AddressDto getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressDto shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<ReceiptItemDto> getItems() {
        return items;
    }

    public void setItems(List<ReceiptItemDto> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ReceiptDto{" +
                "orderId=" + orderId +
                ", date=" + date +
                ", customerName='" + customerName + '\'' +
                ", billingAddress=" + billingAddress +
                ", shippingAddress=" + shippingAddress +
                ", items=" + items +
                ", total=" + total +
                '}';
    }
}