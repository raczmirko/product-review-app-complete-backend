package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "characteristic")
public class Characteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    /* Unit of measure and its name must be both present or both null.
    This is handled on the database level with a CHECK constraint. */
    @Column(length = 100)
    private String unitOfMeasureName;
    @Column(length = 100)
    private String unitOfMeasure;
    @Column(length = 100)
    private String description;
    @ManyToMany(mappedBy = "characteristics")
    private Set<Category> categories;

    public Characteristic() {
    }

    public Characteristic(String name, String unitOfMeasureName, String unitOfMeasure, String description) {
        this.name = name;
        this.unitOfMeasureName = unitOfMeasureName;
        this.unitOfMeasure = unitOfMeasure;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
