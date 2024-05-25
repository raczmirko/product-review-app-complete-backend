package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.AspectDTO;
import hu.okrim.productreviewappcomplete.mapper.AspectMapper;
import hu.okrim.productreviewappcomplete.model.Aspect;
import hu.okrim.productreviewappcomplete.service.AspectService;
import hu.okrim.productreviewappcomplete.specification.AspectSpecificationBuilder;
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
@RequestMapping("/aspect")
public class AspectController {
    @Autowired
    AspectService aspectService;

    @GetMapping("/all")
    public ResponseEntity<List<Aspect>> getAspects() {
        List<Aspect> aspects = aspectService.findAll();
        if (aspects.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(aspects, HttpStatus.OK);
    }
    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deleteAspect(@PathVariable("id") Long id){
        try {
            aspectService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception ex) {
            String message = ex.getMessage();
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("multi-delete/{ids}")
    public ResponseEntity<?> deleteAspects(@PathVariable("ids") Long[] ids){
        try {
            for(Long id : ids) {
                aspectService.deleteById(id);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception ex) {
            String message = ex.getMessage();
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}/modify")
    public ResponseEntity<HttpStatus> modifyAspect(@PathVariable("id") Long id, @RequestBody AspectDTO aspectDTO){
        Aspect existingAspect = aspectService.findById(id);

        if (existingAspect == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingAspect.setName(aspectDTO.getName());
        existingAspect.setQuestion(aspectDTO.getQuestion());
        existingAspect.setCategory(aspectDTO.getCategory());

        aspectService.save(existingAspect);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createAspect(@RequestBody AspectDTO aspectDTO){
        aspectService.save(AspectMapper.mapToAspect(aspectDTO));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Aspect>> searchAspects(@RequestParam(value = "searchText", required = false) String searchText,
                                                    @RequestParam(value = "searchColumn", required = false) String searchColumn,
                                                    @RequestParam(value = "quickFilterValues", required = false) String quickFilterValues,
                                                    @RequestParam("pageSize") Integer pageSize,
                                                    @RequestParam("pageNumber") Integer pageNumber,
                                                    @RequestParam("orderByColumn") String orderByColumn,
                                                    @RequestParam("orderByDirection") String orderByDirection
    ) {
        AspectSpecificationBuilder<Aspect> brandAspectSpecificationBuilder = new AspectSpecificationBuilder<>();
        if (searchColumn != null) {
            switch (searchColumn) {
                case "id" -> brandAspectSpecificationBuilder.withId(searchText);
                case "name" -> brandAspectSpecificationBuilder.withName(searchText);
                case "question" -> brandAspectSpecificationBuilder.withQuestion(searchText);
                case "category" -> brandAspectSpecificationBuilder.withCategoryName(searchText);
                default -> {

                }
            }
        }
        else {
            if(quickFilterValues != null && !quickFilterValues.isEmpty()){
                // When searchColumn is not provided all fields are searched
                brandAspectSpecificationBuilder.withQuickFilterValues(List.of(quickFilterValues.split(",")));
            }
        }
        Specification<Aspect> specification = brandAspectSpecificationBuilder.build();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(orderByDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumn));
        Page<Aspect> aspectsPage = aspectService.findAllBySpecification(specification, pageable);
        return new ResponseEntity<>(aspectsPage, HttpStatus.OK);
    }
}
