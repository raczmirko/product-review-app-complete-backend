package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product_image")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product", referencedColumnName = "id")
    private Product product;

    @Lob
    @Column(name = "image", columnDefinition = "varbinary(max)")
    private byte[] image;

    public ProductImage() {}

    public ProductImage(Product product, byte[] image) {
        this.product = product;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

