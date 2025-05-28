package at.fhtw.webshop.dto.product;

import java.math.BigDecimal;

public class ProductUpdateDto {
    private Integer id;
    private String name;
    private String description;
    private String imageURL;
    private Integer categoryId;
    private Integer stock;
    private BigDecimal price;

    public ProductUpdateDto() {}

    public ProductUpdateDto(Integer id, String name, String description, String imageURL,
                            Integer categoryId, Integer stock, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.categoryId = categoryId;
        this.stock = stock;
        this.price = price;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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
}
