package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.DashboardMostActiveUserDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardReviewByMonthDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardUserBestRatedProductsDTO;
import hu.okrim.productreviewappcomplete.dto.DashboardUserRatingsPerCategoryDTO;
import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.model.views.MostPopularArticlesPerBrandView;
import hu.okrim.productreviewappcomplete.service.*;
import hu.okrim.productreviewappcomplete.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    MostPopularArticlesPerBrandViewService mostPopularArticlesPerBrandViewService;
    @Autowired
    JwtUtil tokenUtil;

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
    public ResponseEntity<List<DashboardMostActiveUserDTO>> getMostActiveUsers(@RequestParam("userCount") Integer userCount){
        if(userCount == 0) userCount = 3;
        Pageable topThree = PageRequest.of(0, userCount);
        List<DashboardMostActiveUserDTO> returnList = userService.findMostActiveUsers(topThree);
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("/reviews-this-year")
    public ResponseEntity<List<DashboardReviewByMonthDTO>> getReviewsThisYear(){
        List<DashboardReviewByMonthDTO> returnList = reviewHeadService.findThisYearsReviewsGroupByMonth();
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("/user-reviews-per-category")
    public ResponseEntity<List<DashboardUserRatingsPerCategoryDTO>> getUserReviewsPerCategory(HttpServletRequest request){
        User user = userService.findByUsername(tokenUtil.extractUserFromToken(request));
        List<DashboardUserRatingsPerCategoryDTO> returnList = reviewHeadService.findUserRatingsPerCategory(user.getId());
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("/user-best-rated-products")
    public ResponseEntity<List<DashboardUserBestRatedProductsDTO>> getUserBestRatedProducts(HttpServletRequest request){
        User user = userService.findByUsername(tokenUtil.extractUserFromToken(request));
        List<DashboardUserBestRatedProductsDTO> returnList = reviewHeadService.findUserBestRatedProducts(user.getId());
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }

    @GetMapping("/view-most-popular-articles-per-brand")
    public ResponseEntity<List<MostPopularArticlesPerBrandView>> getMostPopularProductsPerBrand(){
        List<MostPopularArticlesPerBrandView> returnList = mostPopularArticlesPerBrandViewService.findAll();
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }
}
