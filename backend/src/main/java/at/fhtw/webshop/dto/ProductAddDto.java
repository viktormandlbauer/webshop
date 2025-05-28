package at.fhtw.webshop.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class ProductAddDto {

    @NotBlank(message = "Name ist erforderlich")
    private String name;

    @NotBlank(message = "Beschreibung ist erforderlich")
    private String description;

    @NotNull(message = "Preis ist erforderlich")
    @DecimalMin(value = "0.01", message = "Preis muss größer als 0 sein")
    private BigDecimal price;

    @NotNull(message = "Bestand ist erforderlich")
    @Min(value = 0, message = "Bestand darf nicht negativ sein")
    private Integer stock;

    @NotNull(message = "Kategorie ist erforderlich")
    private Integer categoryId;

    @NotNull(message = "Produktbild ist erforderlich")
    private MultipartFile imageFile;

    // === Getter und Setter ===

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
