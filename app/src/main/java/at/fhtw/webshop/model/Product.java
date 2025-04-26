package at.fhtw.webshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @Column(name = "ProductID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CategoryID", nullable = false)
    private Category categoryID;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "Stock", nullable = false)
    private Integer stock;

    @NotNull
    @Column(name = "Price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ColumnDefault("0.00")
    @Column(name = "AvgRating", precision = 3, scale = 2)
    private BigDecimal avgRating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Category getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Category categoryID) {
        this.categoryID = categoryID;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(BigDecimal avgRating) {
        this.avgRating = avgRating;
    }

}