package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;

@Entity
@Table(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    @ManyToOne
    @JoinColumn(name = "country", nullable = false)
    private Country countryOfOrigin;
    @Column(length = 1000)
    private String description;

    public Brand() {
    }

    public Brand(String name, Country countryOfOrigin, String description) {
        this.name = name;
        this.countryOfOrigin = countryOfOrigin;
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

    public Country getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(Country countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
