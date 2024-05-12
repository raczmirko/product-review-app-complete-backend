package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.CategoryHierarchyDTO;
import hu.okrim.productreviewappcomplete.dto.CharacteristicDTO;
import hu.okrim.productreviewappcomplete.mapper.CharacteristicMapper;
import hu.okrim.productreviewappcomplete.model.Category;
import hu.okrim.productreviewappcomplete.model.Characteristic;
import hu.okrim.productreviewappcomplete.service.CategoryService;
import hu.okrim.productreviewappcomplete.service.CharacteristicService;
import hu.okrim.productreviewappcomplete.specification.CharacteristicSpecificationBuilder;
import hu.okrim.productreviewappcomplete.util.SqlExceptionMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/characteristic")
public class CharacteristicsController {
    @Autowired
    CharacteristicService characteristicService;
    @Autowired
    CategoryService categoryService;
    @GetMapping("/all")
    public ResponseEntity<List<Characteristic>> getCharacteristics() {
        List<Characteristic> characteristics = characteristicService.findAll();
        if (characteristics.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(characteristics, HttpStatus.OK);
    }
    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteCharacteristic(@PathVariable("id") Long id){
        try {
            characteristicService.deleteCharacteristicById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String errorMessage = SqlExceptionMessageHandler.characteristicDeleteErrorMessage(ex);
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("multi-delete/{ids}")
    public ResponseEntity<?> deleteCharacteristics(@PathVariable("ids") Long[] ids){
        for(Long id : ids) {
            try {
                characteristicService.deleteCharacteristicById(id);
            } catch (Exception ex) {
                String errorMessage = SqlExceptionMessageHandler.characteristicDeleteErrorMessage(ex);
                return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/cascade-delete")
    public ResponseEntity<?> cascadeDeleteCharacteristic(@PathVariable("id") Long id){
        try {
            Characteristic foundCharacteristic = characteristicService.findCharacteristicById(id);
            Set<Category> categories = foundCharacteristic.getCategories();

            // Remove the characteristic from all categories
            for (Category category : categories) {
                category.getCharacteristics().remove(foundCharacteristic);
            }

            // Delete the characteristic
            characteristicService.deleteCharacteristicById(id);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            String errorMessage = SqlExceptionMessageHandler.characteristicDeleteErrorMessage(ex);
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}/modify")
    public ResponseEntity<?> modifyCharacteristic(@PathVariable("id") Long id, @RequestBody CharacteristicDTO characteristicDTO){
        Characteristic existingCharacteristic = characteristicService.findCharacteristicById(id);

        if (existingCharacteristic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            existingCharacteristic.setName(characteristicDTO.getName());
            existingCharacteristic.setUnitOfMeasureName(characteristicDTO.getUnitOfMeasureName());
            existingCharacteristic.setUnitOfMeasure(characteristicDTO.getUnitOfMeasure());
            existingCharacteristic.setDescription(characteristicDTO.getDescription());
            characteristicService.saveCharacteristic(existingCharacteristic);
        }
        catch (Exception ex) {
            String errorMessage = SqlExceptionMessageHandler.characteristicCreateErrorMessage(ex);
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCharacteristic(@RequestBody CharacteristicDTO characteristicDTO){
        Characteristic characteristic = CharacteristicMapper.mapToCharacteristic(characteristicDTO);
        try {
            characteristicService.saveCharacteristic(characteristic);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception ex) {
            String message = SqlExceptionMessageHandler.characteristicCreateErrorMessage(ex);
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Characteristic>> searchCharacteristics(@RequestParam(value = "searchText", required = false) String searchText,
                                                        @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                        @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam("pageNumber") Integer pageNumber,
                                                        @RequestParam("orderByColumn") String orderByColumn,
                                                        @RequestParam("orderByDirection") String orderByDirection ) {

        CharacteristicSpecificationBuilder<Characteristic> characteristicCharacteristicSpecificationBuilder = new CharacteristicSpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "id" -> characteristicCharacteristicSpecificationBuilder.withId(searchText);
                case "name" -> characteristicCharacteristicSpecificationBuilder.withName(searchText);
                case "description" -> characteristicCharacteristicSpecificationBuilder.withDescription(searchText);
                case "unitOfMeasure" -> characteristicCharacteristicSpecificationBuilder.withUnitOfMeasure(searchText);
                case "unitOfMeasureName" -> characteristicCharacteristicSpecificationBuilder.withUnitOfMeasureName(searchText);
                default -> {

                }
            }
        }
        else {
            if(quickFilterValues != null && !quickFilterValues.isEmpty()){
                // When searchColumn is not provided all fields are searched
                characteristicCharacteristicSpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<Characteristic> specification = characteristicCharacteristicSpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<Characteristic> characteristicPage = characteristicService.findAllBySpecification(specification, pageable);
        return new ResponseEntity<>(characteristicPage ,HttpStatus.OK);
    }

    @GetMapping("/{id}/list-characteristic-category-trees")
    public ResponseEntity<?> listCharacteristicCategories (@PathVariable("id") Long id) {
        Characteristic characteristic = characteristicService.findCharacteristicById(id);
        Set<Category> assignedCategories = characteristic.getCategories();
        List<CategoryHierarchyDTO> assignedCategoryHierarchy = new ArrayList<>();
        // Create the categoryHierarchy of each assigned category
        for(Category assignedCategory: assignedCategories){
            // Get all subcategories of current category
            List<Category> currentSubcategories = categoryService.findSubCategories(assignedCategory);
            // Iterate through each subcategory and get all further subcategories of each individual subcategory
            // Each tree branch is saved in a map identified by the currentSubcategory ID and has a list of child elements assigned
            HashMap<Long, List<Category>> subSubcategories = new HashMap<>();
            for (Category category : currentSubcategories) {
                // For each subcategory found create a list and save all subSubcategories of given subcategory
                List<Category> currentBranchSubcategories = categoryService.findSubCategories(category);
                subSubcategories.put(category.getId(), currentBranchSubcategories);
            }
            CategoryHierarchyDTO categoryHierarchyDTO = new CategoryHierarchyDTO();
            categoryHierarchyDTO.setCurrentCategory(assignedCategory);
            categoryHierarchyDTO.setCurrentSubcategories(currentSubcategories);
            categoryHierarchyDTO.setSubSubcategories(subSubcategories);
            assignedCategoryHierarchy.add(categoryHierarchyDTO);
        }
        return new ResponseEntity<>(assignedCategoryHierarchy, HttpStatus.OK);
    }

    @GetMapping("/{id}/available-characteristics")
    public ResponseEntity<Set<Characteristic>> getAvailableCharacteristics (@PathVariable("id") Long categoryId) {
        // Find the category by the provided ID
        Category category = categoryService.findCategoryById(categoryId);
        // Find all characteristics
        Set<Characteristic> availableCharacteristics = new HashSet<>(characteristicService.findAll());
        // Find all characteristics that are already assigned to the category or one of its subcategories
        Set<Characteristic> alreadyAssignedCharacteristics = new HashSet<>(getCharacteristicsOfCategoryAndAllSubcategories(category));
        // Remove already assigned characteristics from available ones
        availableCharacteristics.removeAll(alreadyAssignedCharacteristics);
        return new ResponseEntity<>(availableCharacteristics, HttpStatus.OK);
    }

    public Set<Characteristic> getCharacteristicsOfCategoryAndAllSubcategories(Category category) {
        // Create a list with the category and all subcategories
        ArrayList<Category> categories = new ArrayList<>(categoryService.findSubCategories(category));
        categories.add(category);
        // Create a set that stores the result characteristics
        Set<Characteristic> resultSet = new HashSet<>();
        // Iterate through the categories and add all of their characteristics to the result set
        for (Category c : categories) {
            resultSet.addAll(c.getCharacteristics());
        }
        return resultSet;
    }
}
