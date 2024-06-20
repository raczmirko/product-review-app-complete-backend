package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.dto.DashboardMostActiveUserDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardReviewByMonthDTO;
import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.ReviewHead;
import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewHeadId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewHeadRepository extends JpaRepository<ReviewHead, ReviewHeadId> {
    Page<ReviewHead> findAll(Specification<ReviewHead> specification, Pageable pageable);

    Optional<ReviewHead> findByUserAndProduct(User user, Product product);

    @Query("SELECT new hu.okrim.productreviewappcomplete.dto.DashboardReviewByMonthDTO(DATENAME(MONTH, r.date), COUNT(r)) " +
            "FROM ReviewHead r " +
            "WHERE DATEPART(YEAR, r.date) = :currentYear " +
            "GROUP BY DATENAME(MONTH, r.date) " +
            "ORDER BY DATENAME(MONTH, r.date) ASC")
    List<DashboardReviewByMonthDTO> findThisYearsReviewsGroupByMonth(@Param("currentYear") int currentYear);
}
