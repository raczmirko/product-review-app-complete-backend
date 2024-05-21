package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.model.Product;
import hu.okrim.productreviewappcomplete.model.ProductImage;
import hu.okrim.productreviewappcomplete.repository.ProductImageRepository;
import hu.okrim.productreviewappcomplete.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/product-image")
public class ProductImageController {
    @Autowired
    ProductImageRepository productImageRepository;
    @Autowired
    ProductRepository productRepository;

    @PostMapping(path = "{productId}/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createProductImage(@PathVariable("productId") Long productId,
                                                @RequestParam MultipartFile[] files) {
        Optional<Product> productOptional = productRepository.findById(productId);

        System.out.println(Arrays.toString(files));

        if (productOptional.isPresent() && files != null && files.length != 0) {
            try {
                Product product = productOptional.get();

                for (MultipartFile file : files) {
                    byte[] imageBytes = file.getBytes();
                    productImageRepository.save(new ProductImage(product, imageBytes));
                }
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception ex) {
                String message = "Failed to upload images: " + ex.getMessage();
                return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            String message = "Product with ID " + productId + " not found or images are missing.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}
