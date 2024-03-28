package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "article", nullable = false)
    private Article article;
    @ManyToOne
    @JoinColumn(name = "packaging", nullable = false)
    private Packaging packaging;

    public Product() {
    }

    public Product(Article article, Packaging packaging) {
        this.article = article;
        this.packaging = packaging;
    }

    public Long getId() {
        return id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Packaging getPackaging() {
        return packaging;
    }

    public void setPackaging(Packaging packaging) {
        this.packaging = packaging;
    }
}
