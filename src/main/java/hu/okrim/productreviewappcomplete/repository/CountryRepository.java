package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, String> {

}
