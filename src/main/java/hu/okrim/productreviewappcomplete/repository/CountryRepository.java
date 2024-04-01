package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CountryRepository extends CrudRepository<Country, String> {
    List<Country> findByOrderByNameAsc();
}
