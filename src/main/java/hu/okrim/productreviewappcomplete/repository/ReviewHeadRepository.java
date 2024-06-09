package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.ReviewHead;
import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewHeadId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewHeadRepository extends JpaRepository<ReviewHead, ReviewHeadId> {
    Page<ReviewHead> findAll(Specification<ReviewHead> specification, Pageable pageable);

    Optional<ReviewHead> findByUserAndProduct(User user, Product product);
}
