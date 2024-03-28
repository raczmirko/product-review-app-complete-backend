package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String userName;
    @Column(nullable = false, length = 1000)
    private String password;
    @ManyToOne
    @JoinColumn(name = "country", nullable = false)
    private Country country;
    @Column(name = "registered", nullable = false)
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

    public User(String userName, String password, Country country, Role role) {
        this.userName = userName;
        this.password = password;
        this.country = country;
        this.role = role;
        this.registrationDate = Instant.now();
        this.isActive = true;
    }

    public Long getId() {
        return id;
    }

    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
