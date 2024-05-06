package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.CharacteristicDTO;
import hu.okrim.productreviewappcomplete.mapper.CharacteristicMapper;
import hu.okrim.productreviewappcomplete.model.Characteristic;
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

import java.util.List;

@RestController
@RequestMapping("/characteristic")
public class CharacteristicsController {
    @Autowired
    CharacteristicService characteristicService;
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

    @PutMapping("/{id}/modify")
    public ResponseEntity<HttpStatus> modifyCharacteristic(@PathVariable("id") Long id, @RequestBody CharacteristicDTO characteristicDTO){
        Characteristic existingCharacteristic = characteristicService.findCharacteristicById(id);

        if (existingCharacteristic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingCharacteristic.setName(characteristicDTO.getName());
        existingCharacteristic.setUnitOfMeasureName(characteristicDTO.getUnitOfMeasureName());
        existingCharacteristic.setUnitOfMeasure(characteristicDTO.getUnitOfMeasure());
        existingCharacteristic.setDescription(characteristicDTO.getDescription());

        characteristicService.saveCharacteristic(existingCharacteristic);

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
    public ResponseEntity<Page<Characteristic >> searchCharacteristics(@RequestParam(value = "searchText", required = false) String searchText,
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
                case "categories" -> characteristicCharacteristicSpecificationBuilder.withCategoryName(searchText);
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
        return ResponseEntity.ok(characteristicPage);
    }
}
