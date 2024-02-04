package com.example.companyemployeespring.controller;

import com.example.companyemployeespring.entity.Employee;
import com.example.companyemployeespring.repository.CompanyRepository;
import com.example.companyemployeespring.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.File;
import java.util.Optional;


@Controller

public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Value("${picture.upload.directory}")
    private String uploadDirectory;

    @GetMapping("/employees")
    public String employeesPage(ModelMap modelMap) {
        modelMap.addAttribute("employees", employeeRepository.findAll());
        return "employees";
    }

    @GetMapping("/employees/add")
    public String addEmployeesPage(ModelMap modelMap) {
        modelMap.addAttribute("companies", companyRepository.findAll());
        return "addEmployee";
    }

    @PostMapping("/employees/add")
    public String addEmployees(@ModelAttribute Employee employee,
                               @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            employee.setPicName(picName);
        }
        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable("id") int id) {
        employeeRepository.deleteById(id);
        return "redirect:/employees";
    }

    @GetMapping("/employees/update/{id}")
    public String updateEmployeePage(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<Employee> byId = employeeRepository.findById(id);
        if (byId.isPresent()) {
            modelMap.addAttribute("companies", companyRepository.findAll());
            modelMap.addAttribute("employee", byId.get());
        } else {
            return "redirect:/employees";
        }
        return "updateEmployee";
    }
    @PostMapping("/employees/update")
    public String updateEmployees(@ModelAttribute Employee employee,
                                  @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            employee.setPicName(picName);
        } else {
            Optional<Employee> fromDB = employeeRepository.findById(employee.getId());
            employee.setPicName(fromDB.get().getPicName());
        }
        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/employees/image/delete")
    public String deleteEmployeeImage(@RequestParam("id") int id) {
        Optional<Employee> byId = employeeRepository.findById(id);
        if (byId.isEmpty()) {
            return "redirect:/employees";
        } else {
            Employee employee = byId.get();
            String picName = employee.getPicName();
            if (picName != null) {
                employee.setPicName(null);
                employeeRepository.save(employee);
                File file = new File(uploadDirectory, picName);
                if (file.exists()) {
                    file.delete();
                }
            }
            return "redirect:/employees/update/" + employee.getId();
        }
    }

}
