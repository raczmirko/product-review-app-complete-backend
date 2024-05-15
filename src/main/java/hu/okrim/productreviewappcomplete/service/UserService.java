package hu.okrim.productreviewappcomplete.service;

import hu.okrim.productreviewappcomplete.model.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    User findById(String username);
    void save(User user);
    void deleteById(Long id);
    List<User> findAll();
    String getUserRole(String username);
}
