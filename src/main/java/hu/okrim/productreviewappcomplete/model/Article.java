package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @ManyToOne
    @JoinColumn(name = "brand", nullable = false)
    private Brand brand;
    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private Category category;
    @Column(length = 1000)
    private String description;

    public Article() {
    }

    public Article(String name, Brand brand, Category category, String description) {
        this.name = name;
        this.brand = brand;
        this.category = category;
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
