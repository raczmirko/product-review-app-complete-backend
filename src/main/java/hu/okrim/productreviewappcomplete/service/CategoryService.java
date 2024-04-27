package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CategoryService {
    Category findCategoryById (Long id);
    void deleteCategoryById (Long id);
    void saveCategory (Category category);
    List<Category> getCategories();
    Page<Category> findAllBySpecification(Specification<Category> specification, Pageable pageable);
}
