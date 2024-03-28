package hu.okrim.productreviewappcomplete.model;

import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewBodyId;
import jakarta.persistence.*;

@Entity
@Table(name = "review_body")
public class ReviewBody {
    @EmbeddedId
    private ReviewBodyId id;

    @Column(name = "score", nullable = false)
    private Short score;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "user", referencedColumnName = "user", insertable = false, updatable = false),
            @JoinColumn(name = "product", referencedColumnName = "product", insertable = false, updatable = false)
    })
    private ReviewHead reviewHead;

    public ReviewBody() {
    }

    public ReviewBody(ReviewBodyId id, Short score, ReviewHead reviewHead) {
        this.id = id;
        this.score = score;
        this.reviewHead = reviewHead;
    }

    public ReviewBodyId getId() {
        return id;
    }

    public Short getScore() {
        return score;
    }

    public void setScore(Short score) {
        this.score = score;
    }

    public ReviewHead getReviewHead() {
        return reviewHead;
    }

    public void setReviewHead(ReviewHead reviewHead) {
        this.reviewHead = reviewHead;
    }
}
