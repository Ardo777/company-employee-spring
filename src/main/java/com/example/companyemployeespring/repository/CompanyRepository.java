package com.example.companyemployeespring.repository;

import com.example.companyemployeespring.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {


}
