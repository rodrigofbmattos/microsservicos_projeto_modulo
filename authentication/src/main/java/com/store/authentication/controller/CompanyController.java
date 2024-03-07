package com.store.authentication.controller;

import com.store.authentication.domain.Company;
import com.store.authentication.service.CompanyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/company")
public class CompanyController extends GenericController<Company> {
    public CompanyController(CompanyService service) {
        super(service);
    }
}
