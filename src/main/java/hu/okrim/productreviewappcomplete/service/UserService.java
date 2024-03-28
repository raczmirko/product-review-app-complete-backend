package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.User;

import java.util.List;

public interface UserService {
    User getUser(Long id);
    User getUser(String username);
    User saveUser(User user);
    void deleteUser(Long id);
    List<User> getUsers();
}
