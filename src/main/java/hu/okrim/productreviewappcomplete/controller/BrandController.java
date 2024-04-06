package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.BrandDTO;
import hu.okrim.productreviewappcomplete.mapper.BrandMapper;
import hu.okrim.productreviewappcomplete.model.Brand;
import hu.okrim.productreviewappcomplete.service.BrandService;
import hu.okrim.productreviewappcomplete.specification.BrandSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    BrandService brandService;
    @Autowired

    @GetMapping("/all")
    public ResponseEntity<List<Brand>> getBrands() {
        return new ResponseEntity<>(brandService.getBrands(), HttpStatus.OK);
    }
    @PostMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> deleteBrand(@PathVariable("id") Long id){
        brandService.deleteBrandById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createBrand(@RequestBody BrandDTO brandDTO){
        brandService.saveBrand(BrandMapper.mapToBrand(brandDTO));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Brand>> searchBrands(@RequestParam(value = "searchText", required = false) String searchText,
                                                    @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                    @RequestParam("pageSize") Integer pageSize,
                                                    @RequestParam("pageNumber") Integer pageNumber,
                                                    @RequestParam("orderByColumn") String orderByColumn,
                                                    @RequestParam("orderByDirection") String orderByDirection) {
        BrandSpecificationBuilder<Brand> brandBrandSpecificationBuilder = new BrandSpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "name":
                    brandBrandSpecificationBuilder.withName(searchText);
                    break;
                case "description":
                    brandBrandSpecificationBuilder.withDescription(searchText);
                    break;
                case "countryOfOrigin":
                    brandBrandSpecificationBuilder.withCountry(searchText);
                    break;
                default:
                    // Handle unknown search columns
                    break;
            }
        }
        Specification<Brand> specification = brandBrandSpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<Brand> brandsPage = brandService.findAllBySpecification(specification, pageable);
        return ResponseEntity.ok(brandsPage);
    }
}
