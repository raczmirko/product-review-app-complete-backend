package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Article;
import hu.okrim.productreviewappcomplete.model.Packaging;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private Article article;
    private Packaging packaging;
}
