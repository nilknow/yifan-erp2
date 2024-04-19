package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query(nativeQuery = true,
    value = "select id from company where domain_prefix=:prefix")
    Long getCompanyIdByDomainPrefix(String prefix);
}
