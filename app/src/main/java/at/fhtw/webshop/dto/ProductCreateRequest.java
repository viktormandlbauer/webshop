package at.fhtw.webshop.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class ProductCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String categoryName;

    @Min(0)
    private Integer stock;

    @DecimalMin("0.00")
    private BigDecimal price;

    @DecimalMin("0.00")
    private BigDecimal avgRating;

    private String imageURL;

    private MultipartFile imageFile;

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCategoryName()
    {
        return categoryName;
    }
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public int getStock()
    {
        return stock;
    }
    public void setStock(int stock)
    {
        this.stock = stock;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public MultipartFile getImageFile()
    {
        return imageFile;
    }
    public void setImageFile(MultipartFile image)
    {
        this.imageFile = image;
    }

    public BigDecimal getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(BigDecimal avgRating) {
        this.avgRating = avgRating;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
