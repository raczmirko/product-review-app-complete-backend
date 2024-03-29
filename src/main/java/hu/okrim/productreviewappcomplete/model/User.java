package hu.okrim.productreviewappcomplete.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String username;
    @Column(nullable = false, length = 1000)
    private String password;
    @ManyToOne
    @JoinColumn(name = "country", nullable = false)
    private Country country;
    @Column(name = "registered", nullable = false)
    @JsonIgnore
    private Instant registrationDate;
    @ManyToOne
    @JoinColumn(name = "role", nullable = false)
    private Role role;
    @Column(nullable = false)
    private Boolean isActive;

    public User() {
        this.registrationDate = Instant.now();
        this.isActive = true;
    }
}
