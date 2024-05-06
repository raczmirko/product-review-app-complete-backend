package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Characteristic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CharacteristicService {
    List<Characteristic> findAll ();
    Characteristic findCharacteristicById (Long id);
    void deleteCharacteristicById (Long id);
    void saveCharacteristic (Characteristic characteristic);
    Page<Characteristic> findAllBySpecification(Specification<Characteristic> specification, Pageable pageable);
}
