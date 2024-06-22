package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.views.MostPopularProductsPerBrandView;
import hu.okrim.productreviewappcomplete.repository.MostPopularProductsPerBrandViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MostPopularProductsPerBrandViewServiceImpl implements MostPopularProductsPerBrandViewService {
    @Autowired
    MostPopularProductsPerBrandViewRepository mostPopularProductsPerBrandViewRepository;
    @Override
    public List<MostPopularProductsPerBrandView> findAll() {
        return mostPopularProductsPerBrandViewRepository.findAll();
    }
}
