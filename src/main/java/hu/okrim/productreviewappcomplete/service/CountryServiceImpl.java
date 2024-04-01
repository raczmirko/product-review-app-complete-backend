package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.dto.CountryDTO;
import hu.okrim.productreviewappcomplete.model.Country;
import hu.okrim.productreviewappcomplete.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public void deleteCountryByCountryCode(String countryCode) {

    }

    @Override
    public List<Country> getCountries() {
        List<Country> countries = countryRepository.findByOrderByNameAsc();
        return countries;
    }
}
