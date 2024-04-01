package hu.okrim.productreviewappcomplete.controller;

import hu.okrim.productreviewappcomplete.security.SecurityConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityController {
    @GetMapping("/session-second")
    public ResponseEntity<Integer> getSessionDuration(){
        Integer timeout = SecurityConstants.TOKEN_EXPIRATION / 1000;
        return new ResponseEntity<>(timeout, HttpStatus.OK);
    }
}
