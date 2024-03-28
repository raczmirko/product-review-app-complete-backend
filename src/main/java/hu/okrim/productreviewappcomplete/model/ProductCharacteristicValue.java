package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.model.compositeKey.ProductCharacteristicValueId;
import jakarta.persistence.*;

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

    public ProductCharacteristicValue() {
    }

    public ProductCharacteristicValue(Product product, Characteristic characteristic, String value) {
        this.product = product;
        this.characteristic = characteristic;
        this.value = value;
    }

    public ProductCharacteristicValueId getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
