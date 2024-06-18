package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.DashboardMostActiveUserDTO;
import hu.okrim.productreviewappcomplete.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    ArticleService articleService;
    @Autowired
    AspectService aspectService;
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CharacteristicService characteristicService;
    @Autowired
    CountryService countryService;
    @Autowired
    PackagingService packagingService;
    @Autowired
    ProductService productService;
    @Autowired
    ReviewHeadService reviewHeadService;
    @Autowired
    UserService userService;

    @GetMapping("/record-amounts")
    public ResponseEntity<Map<String, Integer>> getRecordAmounts(){
        Map<String, Integer> returnMap = new HashMap<>();
        returnMap.put("articles", articleService.findAll().size());
        returnMap.put("aspects", aspectService.findAll().size());
        returnMap.put("brands", brandService.findAll().size());
        returnMap.put("categories", categoryService.findAll().size());
        returnMap.put("characteristics", characteristicService.findAll().size());
        returnMap.put("countries", countryService.findAll().size());
        returnMap.put("packagings", packagingService.findAll().size());
        returnMap.put("products", productService.findAll().size());
        returnMap.put("reviews", reviewHeadService.findAll().size());
        returnMap.put("users", userService.findAll().size());
        return new ResponseEntity<>(returnMap, HttpStatus.OK);
    }

    @GetMapping("/most-active-users")
    public ResponseEntity<List<DashboardMostActiveUserDTO>> getMostActiveUsers(){
        Pageable topThree = PageRequest.of(0, 3);
        List<DashboardMostActiveUserDTO> returnMap = userService.findMostActiveUsers(topThree);
        return new ResponseEntity<>(returnMap, HttpStatus.OK);
    }
}
