package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.exception.EntityNotFoundException;
import hu.okrim.productreviewappcomplete.model.Characteristic;
import hu.okrim.productreviewappcomplete.repository.CharacteristicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacteristicServiceImpl implements CharacteristicService {
    @Autowired
    CharacteristicRepository characteristicRepository;

    @Override
    public List<Characteristic> findAll() {
        return characteristicRepository.findAll();
    }

    @Override
    public Characteristic findCharacteristicById(Long id) {
        return characteristicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Characteristic.class));
    }

    @Override
    public void deleteCharacteristicById(Long id) {
        characteristicRepository.deleteById(id);
    }

    @Override
    public void saveCharacteristic(Characteristic characteristic) {
        characteristicRepository.save(characteristic);
    }
}
