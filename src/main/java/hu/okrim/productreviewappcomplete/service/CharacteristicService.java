package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Characteristic;

import java.util.List;

public interface CharacteristicService {
    List<Characteristic> findAll ();
    Characteristic findCharacteristicById (Long id);
    void deleteCharacteristicById (Long id);
    void saveCharacteristic (Characteristic characteristic);
}
