package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.BrandDTO;
import hu.okrim.productreviewappcomplete.dto.CategoryDTO;
import hu.okrim.productreviewappcomplete.mapper.BrandMapper;
import hu.okrim.productreviewappcomplete.mapper.CategoryMapper;
import hu.okrim.productreviewappcomplete.model.Brand;
import hu.okrim.productreviewappcomplete.model.Category;
import hu.okrim.productreviewappcomplete.repository.CategoryRepository;
import hu.okrim.productreviewappcomplete.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getCategories() {
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }
    @PostMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("multi-delete/{ids}")
    public ResponseEntity<HttpStatus> deleteCategories(@PathVariable("ids") Long[] ids){
        for(Long id : ids) {
            categoryService.deleteCategoryById(id);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/modify")
    public ResponseEntity<HttpStatus> modifyCategory(@PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO){
        Category existingCategory = categoryService.findCategoryById(id);

        if (existingCategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());
        existingCategory.setParentCategory(categoryDTO.getParentCategory());

        categoryService.saveCategory(existingCategory);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.saveCategory(CategoryMapper.mapToCategory(categoryDTO));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
