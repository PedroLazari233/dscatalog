package com.pedrolazari.dscatalog.dto;

import com.pedrolazari.dscatalog.entities.Category;
import com.pedrolazari.dscatalog.entities.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter @Setter @NoArgsConstructor
public class ProductDTO {

    private Long id;
    @Size(min = 5, max = 20, message = "Name must have size between 5 and 20 characters")
    @NotBlank(message = "Required field")
    private String name;
    @NotBlank(message = "Required field")
    private String description;
    @Positive(message = "Price must be positive")
    private Double price;
    private String imgUrl;
    @PastOrPresent(message = "The date must be on the present or past")
    private Instant date;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imgUrl = product.getImgUrl();
        this.date = product.getDate();
    }

    public ProductDTO(Product product, Set<Category> categories){
        this(product);
        categories.forEach(category -> this.categories.add(new CategoryDTO(category)));
    }
}
