package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.model.compositeKey.ProductCharacteristicValueId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_characteristic_value")
public class ProductCharacteristicValue {

    @EmbeddedId
    private ProductCharacteristicValueId id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne
    @MapsId("characteristicId")
    @JoinColumn(name = "characteristic")
    private Characteristic characteristic;

    @Column(name = "value", nullable = false, length = 100)
    private String value;
}
