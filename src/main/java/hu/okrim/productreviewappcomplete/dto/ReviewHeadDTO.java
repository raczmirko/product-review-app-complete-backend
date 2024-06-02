package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Country;
import hu.okrim.productreviewappcomplete.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewHeadDTO {
    private Product product;
    private String username;
    private String description;
    private Short valueForPrice;
    private Boolean recommended;
    private Country purchaseCountry;
}
