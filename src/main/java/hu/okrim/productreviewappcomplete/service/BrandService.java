package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BrandService {
    Brand getBrandById(Long id);
    void deleteBrandById(Long id);
    void saveBrand (Brand brand);
    List<Brand> getBrands();
    Page<Brand> findAllBySpecification(Specification<Brand> specification, Pageable pageable);
}
