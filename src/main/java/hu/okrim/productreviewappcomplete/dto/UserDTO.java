package hu.okrim.productreviewappcomplete.dto;

import hu.okrim.productreviewappcomplete.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private CountryDTO country;
    private Instant registrationDate;
    private RoleDTO role;
    private Boolean isActive;
}
