package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.ReviewHeadDTO;
import hu.okrim.productreviewappcomplete.model.*;
import hu.okrim.productreviewappcomplete.model.compositeKey.ReviewHeadId;
import hu.okrim.productreviewappcomplete.service.ProductService;
import hu.okrim.productreviewappcomplete.service.ReviewHeadService;
import hu.okrim.productreviewappcomplete.service.UserService;
import hu.okrim.productreviewappcomplete.specification.ReviewHeadSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/review-head")
public class ReviewHeadController {

    @Autowired
    private ReviewHeadService reviewHeadService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @GetMapping("/{user}/{product}")
    public ResponseEntity<ReviewHead> findById(@PathVariable("user") Long userId, @PathVariable("product") Long productId) {
        ReviewHeadId id = new ReviewHeadId(userId, productId);
        ReviewHead reviewHead = reviewHeadService.findById(id);
        if (reviewHead != null) {
            return new ResponseEntity<>(reviewHead, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReviewHead>> getAllReviewHeads() {
        List<ReviewHead> reviewHeads = reviewHeadService.findAll();
        if (reviewHeads.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reviewHeads, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createReviewHead(@RequestBody ReviewHeadDTO reviewHeadDTO) {
        User user = userService.findByUsername(reviewHeadDTO.getUsername());
        ReviewHeadId reviewHeadId = new ReviewHeadId(user.getId(), reviewHeadDTO.getProduct().getId());
        ReviewHead reviewHead = new ReviewHead(
                reviewHeadId,
                user,
                reviewHeadDTO.getProduct(),
                LocalDateTime.now(),
                reviewHeadDTO.getDescription(),
                reviewHeadDTO.getRecommended(),
                reviewHeadDTO.getPurchaseCountry(),
                reviewHeadDTO.getValueForPrice()
        );
        reviewHeadService.save(reviewHead);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteReviewHead(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        Long productId = Long.parseLong(request.get("productId"));
        User user = userService.findByUsername(username);

        ReviewHeadId id = new ReviewHeadId(user.getId(), productId);
        try {
            reviewHeadService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String message = ex.getMessage();
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

//    @PostMapping("/multi-delete")
//    public ResponseEntity<?> deleteReviewHeads(@RequestParam("idKeyPairs") Long[] idKeyPairs) {
//        try {
//            for (int i = 0; i < userIds.length; i++) {
//                ReviewHeadId id = new ReviewHeadId(userIds[i], productIds[i]);
//                reviewHeadService.deleteById(id);
//            }
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception ex) {
//            String message = SqlExceptionMessageHandler.reviewHeadDeleteErrorMessage(ex);
//            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
//        }
//    }

    @PutMapping("/{username}/{productId}/modify")
    public ResponseEntity<HttpStatus> modifyReviewHead(@PathVariable("username") String username,
                                                       @PathVariable("productId") Long productId,
                                                       @RequestBody ReviewHeadDTO reviewHeadDTO) {
        User user = userService.findByUsername(username);
        Product product = productService.findById(productId);
        ReviewHeadId id = new ReviewHeadId(user.getId(), product.getId());
        ReviewHead existingReviewHead = reviewHeadService.findById(id);
        if (existingReviewHead == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Update the existing reviewHead with the new data
        existingReviewHead.setDescription(reviewHeadDTO.getDescription());
        existingReviewHead.setDate(LocalDateTime.now());
        existingReviewHead.setRecommended(reviewHeadDTO.getRecommended());
        existingReviewHead.setPurchaseCountry(reviewHeadDTO.getPurchaseCountry());
        existingReviewHead.setValueForPrice(reviewHeadDTO.getValueForPrice());

        reviewHeadService.save(existingReviewHead);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ReviewHead>> searchAspects(@RequestParam(value = "searchText", required = false) String searchText,
                                                      @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                      @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                      @RequestParam("pageSize") Integer pageSize,
                                                      @RequestParam("pageNumber") Integer pageNumber,
                                                      @RequestParam("orderByColumn") String orderByColumn,
                                                      @RequestParam("orderByDirection") String orderByDirection
    ) {
        ReviewHeadSpecificationBuilder<ReviewHead> reviewHeadSpecificationBuilder = new ReviewHeadSpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "productName" -> reviewHeadSpecificationBuilder.withArticleName(searchText);
                case "user" -> reviewHeadSpecificationBuilder.withUsername(searchText);
                case "description" -> reviewHeadSpecificationBuilder.withDescription(searchText);
                case "date" -> reviewHeadSpecificationBuilder.withDate(searchText);
                case "recommended" -> reviewHeadSpecificationBuilder.withRecommended(searchText);
                case "valueForPrice" -> reviewHeadSpecificationBuilder.withValueForPrice(searchText);
                case "purchaseCountry" -> reviewHeadSpecificationBuilder.withPurchaseCountryName(searchText);
                default -> {

                }
            }
        }
        else {
            if(quickFilterValues != null && !quickFilterValues.isEmpty()){
                // When searchColumn is not provided all fields are searched
                reviewHeadSpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<ReviewHead> specification = reviewHeadSpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<ReviewHead> aspectsPage = reviewHeadService.findAllBySpecification(specification, pageable);
        return new ResponseEntity<>(aspectsPage, HttpStatus.OK);
    }
}
