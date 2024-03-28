package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    @ManyToOne
    @JoinColumn(name = "parent_category")
    private Category parentCategory;
    @Column(length = 1000)
    private String description;
    @ManyToMany
    @JoinTable(
            name = "category_x_characteristic",
            joinColumns = @JoinColumn(name = "category"),
            inverseJoinColumns = @JoinColumn(name = "characteristic")
    )
    private Set<Characteristic> characteristics;

    public Category() {
    }

    public Category(String name, Category parentCategory, String description) {
        this.name = name;
        this.parentCategory = parentCategory;
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

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Characteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Set<Characteristic> characteristics) {
        this.characteristics = characteristics;
    }
}
