package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Brand;
import hu.okrim.productreviewappcomplete.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService{
    @Autowired
    BrandRepository brandRepository;
    @Override
    public Brand getBrandById(Long id) {
        return null;
    }

    @Override
    public void deleteBrandById(Long id) {

    }

    @Override
    public void saveBrand(Brand brand) {

    }

    @Override
    public List<Brand> getBrands() {
        return brandRepository.findAllByOrderByName();
    }
}
