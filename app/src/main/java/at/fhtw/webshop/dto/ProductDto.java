package at.fhtw.webshop.dto;

import java.math.BigDecimal;

public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private String imageURL;
    private String categoryName;
    private Integer stock;
    private BigDecimal price;
    private BigDecimal avgRating;

    // Konstruktor
    public ProductDto(Integer id, String name, String description, String imageURL,
                      String categoryName, Integer stock, BigDecimal price, BigDecimal avgRating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.categoryName = categoryName;
        this.stock = stock;
        this.price = price;
        this.avgRating = avgRating;
    }

    // Getter
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageURL() { return imageURL; }
    public String getCategoryName() { return categoryName; }
    public Integer getStock() { return stock; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getAvgRating() { return avgRating; }
}
