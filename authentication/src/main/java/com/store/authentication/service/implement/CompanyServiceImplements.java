package com.store.authentication.service.implement;

import com.store.authentication.domain.Company;
import com.store.authentication.repository.CompanyRepository;
import com.store.authentication.service.CompanyService;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImplements extends GenericServiceImplements<Company, Long, CompanyRepository>  implements CompanyService {
    public CompanyServiceImplements(CompanyRepository repository) {
        super(repository);
    }
}
