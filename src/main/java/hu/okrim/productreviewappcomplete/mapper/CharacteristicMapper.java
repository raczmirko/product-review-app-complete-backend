package hu.okrim.productreviewappcomplete.mapper;

import hu.okrim.productreviewappcomplete.dto.CharacteristicDTO;
import hu.okrim.productreviewappcomplete.model.Characteristic;

public class CharacteristicMapper {
    public static CharacteristicDTO mapToCharacteristicDTO (Characteristic characteristic){
        return new CharacteristicDTO(
                characteristic.getId(),
                characteristic.getName(),
                characteristic.getUnitOfMeasureName(),
                characteristic.getUnitOfMeasure(),
                characteristic.getDescription()
        );
    }
    public static Characteristic mapToCharacteristic (CharacteristicDTO characteristicDTO){
        return new Characteristic(
                null,
                characteristicDTO.getName(),
                characteristicDTO.getUnitOfMeasureName(),
                characteristicDTO.getUnitOfMeasure(),
                characteristicDTO.getDescription()
        );
    }
}
