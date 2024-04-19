package com.nilknow.yifanerp2.service;

import com.nilknow.yifanerp2.repository.CompanyRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {
    @Resource
    private CompanyRepository companyRepository;

    public Long getCompanyIdByDomainPrefix(String prefix) {
        return companyRepository.getCompanyIdByDomainPrefix(prefix);
    }
}
