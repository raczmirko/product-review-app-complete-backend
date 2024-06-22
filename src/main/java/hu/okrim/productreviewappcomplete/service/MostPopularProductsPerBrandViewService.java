package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.views.MostPopularProductsPerBrandView;

import java.util.List;

public interface MostPopularProductsPerBrandViewService {
    public List<MostPopularProductsPerBrandView> findAll();
}
