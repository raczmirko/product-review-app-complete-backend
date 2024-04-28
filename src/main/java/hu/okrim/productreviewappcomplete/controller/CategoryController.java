package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.CategoryDTO;
import hu.okrim.productreviewappcomplete.model.Category;
import hu.okrim.productreviewappcomplete.service.CategoryService;
import hu.okrim.productreviewappcomplete.specification.CategorySpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
        try {
            categoryService.deleteCategoryById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            if(ex.getMessage().contains("The DELETE statement conflicted with the SAME TABLE REFERENCE")) {
                errorMessage = "This category cannot be deleted as it is the parent category of other categories. Delete the child-categories first!";
            }
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        Category category = new Category(categoryDTO.getName(), categoryDTO.getDescription());
        if(categoryDTO.getParentCategory() != null) category.setParentCategory(categoryDTO.getParentCategory());
        categoryService.saveCategory(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Category >> searchBrands(@RequestParam(value = "searchText", required = false) String searchText,
                                                    @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                    @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                    @RequestParam("pageSize") Integer pageSize,
                                                    @RequestParam("pageNumber") Integer pageNumber,
                                                    @RequestParam("orderByColumn") String orderByColumn,
                                                    @RequestParam("orderByDirection") String orderByDirection ) {

        CategorySpecificationBuilder<Category> categoryCategorySpecificationBuilder = new CategorySpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "id" -> categoryCategorySpecificationBuilder.withId(searchText);
                case "name" -> categoryCategorySpecificationBuilder.withName(searchText);
                case "description" -> categoryCategorySpecificationBuilder.withDescription(searchText);
                case "parentCategory" -> categoryCategorySpecificationBuilder.withParentCategory(searchText);
                default -> {

                }
            }
        }
        else {
            if(quickFilterValues != null && !quickFilterValues.isEmpty()){
                // When searchColumn is not provided all fields are searched
                categoryCategorySpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<Category> specification = categoryCategorySpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<Category> categoryPage = categoryService.findAllBySpecification(specification, pageable);
        return ResponseEntity.ok(categoryPage);
    }
}
