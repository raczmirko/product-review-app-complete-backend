package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, String> {
    List<Country> findByOrderByNameAsc();
}
