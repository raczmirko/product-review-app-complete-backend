package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aspect")
public class Aspect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 100)
    private String question;
    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private Category category;

    public Aspect() {
    }

    public Aspect(String name, String question, Category category) {
        this.name = name;
        this.question = question;
        this.category = category;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
