package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;

@Entity
@Table(name = "packaging")
public class Packaging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Short amount;
    @Column(length = 100)
    private String unitOfMeasureName;
    @Column(length = 100)
    private String unitOfMeasure;
    @Column(length = 100)
    private String size;

    public Packaging() {
    }

    public Packaging(Short amount, String unitOfMeasureName, String unitOfMeasure, String size) {
        this.amount = amount;
        this.unitOfMeasureName = unitOfMeasureName;
        this.unitOfMeasure = unitOfMeasure;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public Short getAmount() {
        return amount;
    }

    public void setAmount(Short amount) {
        this.amount = amount;
    }

    public String getUnitOfMeasureName() {
        return unitOfMeasureName;
    }

    public void setUnitOfMeasureName(String unitOfMeasureName) {
        this.unitOfMeasureName = unitOfMeasureName;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
