package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.dto.DashboardMostActiveUserDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardReviewByMonthDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardUserBestRatedProductsDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardUserRatingsPerCategoryDTO;
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

    @Query("SELECT new hu.okrim.productreviewappcomplete.dto.DashboardUserRatingsPerCategoryDTO(" +
                "c, " +
                "ROUND((COUNT(1) * 1.0 / (SELECT COUNT(r2) FROM ReviewHead r2 WHERE r2.user.id = :userId)) * 100, 2), " +
                "COUNT(1)" +
            ") " +
            "FROM ReviewHead r " +
            "JOIN Product p ON r.product.id = p.id " +
            "JOIN Article a ON p.article.id = a.id " +
            "JOIN Category c ON a.category.id = c.id " +
            "WHERE r.user.id = :userId " +
            "GROUP BY c ")
    List<DashboardUserRatingsPerCategoryDTO> findUserRatingsPerCategory(@Param("userId") Long userId);

//    @Query("SELECT new hu.okrim.productreviewappcomplete.dto.DashboardUserBestRatedProductsDTO(" +
//            "rh.product, " +
//            "ROUND(AVG(rh.valueForPrice) + COALESCE(AVG(rb.score), 0), 2) AS scoreAverage, " +
//            "DENSE_RANK() OVER(ORDER BY AVG(rh.valueForPrice) + COALESCE(AVG(rb.score), 0) DESC)) " +
//            "FROM ReviewHead rh LEFT JOIN ReviewBody rb " +
//            "ON rh.product.id = rb.id.productId AND rh.user.id = rb.id.userId " +
//            "WHERE rh.user.id = :userId " +
//            "GROUP BY rh.product " +
//            "ORDER BY scoreAverage DESC " +
//            "LIMIT 3")
    @Query( "WITH helper_table AS (" +
            "SELECT " +
            "rh.product AS product, " +
            "ROUND(AVG(rh.valueForPrice) + COALESCE(AVG(rb.score), 0), 2) AS scoreAverage, " +
            "DENSE_RANK() OVER(ORDER BY AVG(rh.valueForPrice) + COALESCE(AVG(rb.score), 0) DESC) AS rank " +
            "FROM ReviewHead rh LEFT JOIN ReviewBody rb " +
            "ON rh.product.id = rb.id.productId AND rh.user.id = rb.id.userId " +
            "WHERE rh.user.id = :userId " +
            "GROUP BY rh.product) " +
        "SELECT new hu.okrim.productreviewappcomplete.dto.DashboardUserBestRatedProductsDTO(product, scoreAverage, rank) " +
        "FROM helper_table " +
        "WHERE rank <= 3 " +
        "ORDER BY scoreAverage DESC")
    List<DashboardUserBestRatedProductsDTO> findUserBestRatedProducts(@Param("userId") Long userId);
}
