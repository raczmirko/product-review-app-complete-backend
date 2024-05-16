package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Brand;
import hu.okrim.productreviewappcomplete.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAll(Specification<Category> specification, Pageable pageable);
    Optional<List<Category>> findAllByParentCategoryId(Long id);
    @Query("SELECT c FROM Category c WHERE c NOT IN (SELECT DISTINCT p.parentCategory FROM Category p WHERE p.parentCategory IS NOT NULL)")
    List<Category> findLeafCategories();
}
