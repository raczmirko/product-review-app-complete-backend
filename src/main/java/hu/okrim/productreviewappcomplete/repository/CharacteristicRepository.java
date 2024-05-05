package hu.okrim.productreviewappcomplete.repository;

import hu.okrim.productreviewappcomplete.model.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {
}
