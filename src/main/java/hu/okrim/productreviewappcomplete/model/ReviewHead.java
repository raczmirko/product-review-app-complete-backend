package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewHeadId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review_head")
public class ReviewHead {
    @EmbeddedId
    private ReviewHeadId id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user")
    private User user;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product")
    private Product product;
    @Column(nullable = false)
    private Instant date;
    @Column(nullable = false, length = 1000)
    private String description;
    @Column(nullable = false)
    private Boolean recommended;
    @ManyToOne
    @JoinColumn(name = "purchase_country", nullable = false)
    private Country purchaseCountry;
    @Column(nullable = false)
    private Short valueForPrice;
}
