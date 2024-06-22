package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.views.MostPopularProductsPerBrandView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MostPopularProductsPerBrandViewRepository extends JpaRepository<MostPopularProductsPerBrandView, String> {
}
