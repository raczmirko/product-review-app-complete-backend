package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Brand;

import java.util.List;

public interface BrandService {
    Brand getBrandById(Long id);
    void deleteBrandById(Long id);
    void saveBrand (Brand brand);
    List<Brand> getBrands();
}
