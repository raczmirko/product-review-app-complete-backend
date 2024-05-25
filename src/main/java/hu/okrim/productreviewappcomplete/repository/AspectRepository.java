package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Aspect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AspectRepository extends JpaRepository<Aspect, Long> {
    Page<Aspect> findAll(Specification<Aspect> specification, Pageable pageable);
}
