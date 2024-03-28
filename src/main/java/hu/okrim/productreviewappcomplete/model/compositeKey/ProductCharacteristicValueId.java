package hu.okrim.productreviewappcomplete.model.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
@Embeddable
public class ProductCharacteristicValueId implements Serializable {
    @Column(name = "product", nullable = false)
    private Long productId;
    @Column(name = "characteristic", nullable = false)
    private Long characteristicId;

    public ProductCharacteristicValueId() {}

    public ProductCharacteristicValueId(Long productId, Long characteristicId) {
        this.productId = productId;
        this.characteristicId = characteristicId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCharacteristicId() {
        return characteristicId;
    }

    public void setCharacteristicId(Long characteristicId) {
        this.characteristicId = characteristicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCharacteristicValueId that = (ProductCharacteristicValueId) o;
        return Objects.equals(productId, that.productId) && Objects.equals(characteristicId, that.characteristicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, characteristicId);
    }
}

