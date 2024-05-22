package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.ProductCharacteristicValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductCharacteristicsValueService {
    public ProductCharacteristicValue findByProductId(Long id);
    public ProductCharacteristicValue findByCharacteristicId(Long id);
    void deleteById(Long id);
    void save (ProductCharacteristicValue productCharacteristicValue);
    List<ProductCharacteristicValue> findAll();
    Page<ProductCharacteristicValue> findAllBySpecification(Specification<ProductCharacteristicValue> specification, Pageable pageable);
}
