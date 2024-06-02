package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.ReviewHead;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewHeadId;
import hu.okrim.productreviewappcomplete.repository.ReviewHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewHeadServiceImpl implements ReviewHeadService{
    @Autowired
    ReviewHeadRepository reviewHeadRepository;
    @Override
    public ReviewHead findById(ReviewHeadId id) {
        return reviewHeadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.getProductId(), ReviewHead.class));
    }

    @Override
    public void deleteById(ReviewHeadId id) {
        reviewHeadRepository.deleteById(id);
    }

    @Override
    public void save(ReviewHead reviewHead) {
        reviewHeadRepository.save(reviewHead);
    }

    @Override
    public List<ReviewHead> findAll() {
        return reviewHeadRepository.findAll();
    }

    @Override
    public Page<ReviewHead> findAllBySpecification(Specification<ReviewHead> specification, Pageable pageable) {
        return reviewHeadRepository.findAll(specification, pageable);
    }
}
