package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.dto.UserDTO;
import hu.okrim.productreviewappcomplete.model.User;

import java.util.List;

public interface UserService {
    User getUser(Long id);
    User getUser(String username);
    void saveUser(User user);
    void deleteUser(Long id);
    List<User> getUsers();
}
