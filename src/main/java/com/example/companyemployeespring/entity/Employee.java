package com.example.companyemployeespring.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="employee")
@Data
@ToString(exclude = "company")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String surname;

    private String email;

    private String picName;

    @ManyToOne
    Company company;
}
