package at.fhtw.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class ShopOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderItemID")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderID", nullable = false)
    @JsonIgnore
    private ShopOrder order;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ProductID", nullable = false)
    private Product product;

    @NotNull
    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "UnitPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    public Integer getId() {
        return id;
    }

    public ShopOrder getOrder() {
        return order;
    }

    public void setOrder(ShopOrder order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
