package hu.okrim.productreviewappcomplete.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "v_most_popular_products_of_brands")
public class MostPopularProductsPerBrandView {
    @Id
    private String brand;
    private String article;
    private Double average;
}
