package hu.okrim.productreviewappcomplete.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant date;

    @Column(nullable = false)
    private String user;

    @Column(nullable = false)
    private String dmlType;

    @Column(nullable = false)
    private String table;

    @Column(nullable = false)
    private String description;
}
