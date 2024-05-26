package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.ProductCharacteristicValueDTO;
import hu.okrim.productreviewappcomplete.mapper.ProductCharacteristicValueMapper;
import hu.okrim.productreviewappcomplete.model.ProductCharacteristicValue;
import hu.okrim.productreviewappcomplete.service.ProductCharacteristicsValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product-characteristic-value")
public class ProductCharacteristicValueController {
    @Autowired
    ProductCharacteristicsValueService pcvService;
    @PostMapping("/create")
    public ResponseEntity<?> createProductCharacteristicValue(@RequestBody ProductCharacteristicValueDTO pcvDTO){
        ProductCharacteristicValue pcv = ProductCharacteristicValueMapper.mapToProductCharacteristicValue(pcvDTO);
        try {
            pcvService.save(pcv);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
