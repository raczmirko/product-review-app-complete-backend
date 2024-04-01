package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.CountryDTO;
import hu.okrim.productreviewappcomplete.mapper.CountryMapper;
import hu.okrim.productreviewappcomplete.model.Country;
import hu.okrim.productreviewappcomplete.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {
    @Autowired
    CountryService countryService;
    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable String countryCode) {
        return new ResponseEntity<>(countryService.getCountryByCountryCode(countryCode).getName(), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Country>> getCountries() {
        return new ResponseEntity<>(countryService.getCountries(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> saveCountry(@RequestBody CountryDTO countryDTO) {
        countryService.saveCountry(CountryMapper.mapToCountry(countryDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
