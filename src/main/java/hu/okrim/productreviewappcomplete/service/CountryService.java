package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.dto.CountryDTO;
import hu.okrim.productreviewappcomplete.model.Country;

public interface CountryService {
    public Country getCountryByCountryCode(String countryCode);
    public Country saveCountry(Country country);
    public void deleteCountryByCountryCode(String countryCode);
}
