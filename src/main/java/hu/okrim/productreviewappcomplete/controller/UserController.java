package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.dto.UserDTO;
import hu.okrim.productreviewappcomplete.mapper.UserMapper;
import hu.okrim.productreviewappcomplete.model.User;
import hu.okrim.productreviewappcomplete.service.CountryService;
import hu.okrim.productreviewappcomplete.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<String> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUser(id).getUsername(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> createUser(@RequestBody User user) {
        System.out.println("Received user:" + user);
        System.out.println("Username: " + user.getUsername());
        System.out.println("Country: " + user.getCountry());
        userService.saveUser(UserMapper.mapToUserDTO(user));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
