package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.Characteristic;

public interface CharacteristicService {
    Characteristic findCharacteristicById (Long id);
    void deleteCharacteristicById (Long id);
    void saveCharacteristic (Characteristic characteristic);
}
