package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Brand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends CrudRepository<Brand, Long> {
    List<Brand> findAllByOrderByName();
}
