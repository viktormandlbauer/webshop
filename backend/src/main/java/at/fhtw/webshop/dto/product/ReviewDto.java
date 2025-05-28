package at.fhtw.webshop.dto.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReviewDto {

    @NotNull
    private String review;

    @NotNull
    @Min(value = 1, message = "Review rating must be at least 1.")
    @Max(value = 5, message = "Review rating must not exceed 5.")
    private Integer rating;

    public ReviewDto(){}

    public ReviewDto(String review, Integer rating) {
        this.review = review;
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
