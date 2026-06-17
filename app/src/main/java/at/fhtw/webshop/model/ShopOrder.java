package at.fhtw.webshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ShopOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @NotNull
    @Column(name = "OrderDate", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "InvoiceNumber", length = 40)
    private String invoiceNumber;

    @Column(name = "PaymentLabel", length = 80)
    private String paymentLabel;

    @Column(name = "VoucherCode", length = 5)
    private String voucherCode;

    @NotNull
    @Column(name = "Total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @NotNull
    @Column(name = "VoucherDiscount", nullable = false, precision = 10, scale = 2)
    private BigDecimal voucherDiscount = BigDecimal.ZERO;

    @NotNull
    @Column(name = "FinalTotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalTotal;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopOrderItem> items = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPaymentLabel() {
        return paymentLabel;
    }

    public void setPaymentLabel(String paymentLabel) {
        this.paymentLabel = paymentLabel;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getVoucherDiscount() {
        return voucherDiscount;
    }

    public void setVoucherDiscount(BigDecimal voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }

    public BigDecimal getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(BigDecimal finalTotal) {
        this.finalTotal = finalTotal;
    }

    public List<ShopOrderItem> getItems() {
        return items;
    }

    public void addItem(ShopOrderItem item) {
        item.setOrder(this);
        items.add(item);
    }
}
