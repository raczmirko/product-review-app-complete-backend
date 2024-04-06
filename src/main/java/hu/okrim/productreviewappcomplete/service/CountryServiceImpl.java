package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.dto.CountryDTO;
import hu.okrim.productreviewappcomplete.model.Country;
import hu.okrim.productreviewappcomplete.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService{
    @Autowired
    CountryRepository countryRepository;
    @Override
    public Country getCountryByCountryCode(String countryCode) {
        return null;
    }

    @Override
    public void saveCountry(Country country) {
        countryRepository.save(country);
    }

    @Override
    public void deleteCountryByCountryCode(String countryCode) {
        countryRepository.deleteAllByCountryCode(countryCode);
    }

    @Override
    public List<Country> getCountries() {
        return countryRepository.findByOrderByNameAsc();
    }

    @Override
    public Page<Country> findAllBySpecification(Specification<Country> specification, Pageable pageable) {
        return countryRepository.findAll(specification, pageable);
    }
}
