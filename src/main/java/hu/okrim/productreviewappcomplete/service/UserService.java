package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.dto.UserDTO;
import hu.okrim.productreviewappcomplete.model.User;

import java.util.List;

public interface UserService {
    UserDTO getUser(Long id);
    UserDTO getUser(String username);
    void saveUser(UserDTO user);
    void deleteUser(Long id);
    List<User> getUsers();
}
