package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.ReviewHead;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewHeadId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ReviewHeadService {
    ReviewHead findById(ReviewHeadId id);
    void deleteById(ReviewHeadId id);
    void save (ReviewHead reviewHead);
    List<ReviewHead> findAll();
    Page<ReviewHead> findAllBySpecification(Specification<ReviewHead> specification, Pageable pageable);
}
