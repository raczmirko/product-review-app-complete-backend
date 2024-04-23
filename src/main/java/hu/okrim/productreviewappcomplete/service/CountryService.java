package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CountryService {
    public Country getCountryByCountryCode(String countryCode);
    public void saveCountry(Country country);
    public void deleteByCountryCode(String countryCode);
    List<Country> getCountries();
    Page<Country> findAllBySpecification(Specification<Country> specification, Pageable pageable);
}
