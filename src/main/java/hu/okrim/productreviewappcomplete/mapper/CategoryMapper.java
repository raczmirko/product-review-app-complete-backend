package hu.okrim.productreviewappcomplete.mapper;

import hu.okrim.productreviewappcomplete.dto.CategoryDTO;
import hu.okrim.productreviewappcomplete.model.Category;

public class CategoryMapper {
    public static CategoryDTO mapToCategoryDTO (Category category){
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getParentCategory(),
                category.getDescription()
        );
    }
    public static Category mapToCategory (CategoryDTO categoryDTO){
        return new Category(
                null,
                categoryDTO.getName(),
                categoryDTO.getParentCategory(),
                categoryDTO.getDescription()
        );
    }
}
