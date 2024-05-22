package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.ProductCharacteristicValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCharacteristicValueRepository extends JpaRepository<ProductCharacteristicValue, Long> {
    ProductCharacteristicValue findByProductId(Long productId);

    ProductCharacteristicValue findByCharacteristicId(Long characteristicId);

    Page<ProductCharacteristicValue> findAll(Specification<ProductCharacteristicValue> specification, Pageable pageable);
}
