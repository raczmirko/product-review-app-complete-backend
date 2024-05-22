package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.ProductCharacteristicValue;
import hu.okrim.productreviewappcomplete.repository.ProductCharacteristicValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCharacteristicValueServiceImpl implements ProductCharacteristicsValueService{
    @Autowired
    ProductCharacteristicValueRepository productCharacteristicValueRepository;
    @Override
    public ProductCharacteristicValue findByProductId(Long id) {
        return productCharacteristicValueRepository.findByProductId(id);
    }

    @Override
    public ProductCharacteristicValue findByCharacteristicId(Long id) {
        return productCharacteristicValueRepository.findByCharacteristicId(id);
    }

    @Override
    public void deleteById(Long id) {
        productCharacteristicValueRepository.deleteById(id);
    }

    @Override
    public void save(ProductCharacteristicValue productCharacteristicValue) {
        productCharacteristicValueRepository.save(productCharacteristicValue);
    }

    @Override
    public List<ProductCharacteristicValue> findAll() {
        return productCharacteristicValueRepository.findAll();
    }

    @Override
    public Page<ProductCharacteristicValue> findAllBySpecification(Specification<ProductCharacteristicValue> specification, Pageable pageable) {
        return productCharacteristicValueRepository.findAll(specification, pageable);
    }
}
