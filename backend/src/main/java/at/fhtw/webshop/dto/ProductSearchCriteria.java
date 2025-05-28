package at.fhtw.webshop.dto;

import org.hibernate.validator.constraints.Range;

public class ProductSearchCriteria {
    private String name;
    private String categoryName;
    private Double minPrice;
    private Double maxPrice;


    @Range(min = 1, max = 5)
    private Double minRating;

    @Range(min = 1, max = 5)
    private Double maxRating;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }
    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }
    public Double getMinRating() { return minRating; }
    public void setMinRating(Double minRating) { this.minRating = minRating; }
    public Double getMaxRating() { return maxRating; }
    public void setMaxRating(Double maxRating) { this.maxRating = maxRating; }

    @Override
    public String toString() {
        return "ProductSearchCriteria{" +
                "name='" + name + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minRating=" + minRating +
                ", maxRating=" + maxRating +
                '}';
    }
}
