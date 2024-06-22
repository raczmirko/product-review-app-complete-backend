package hu.okrim.productreviewappcomplete.model.views;

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
@Table(name = "v_most_popular_articles_of_categories")
public class MostPopularArticlesPerCategoryView {
    @Id
    private String category;
    private String article;
    private Double average;
}
