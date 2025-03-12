package com.crawler.domains.blacklist;

import com.crawler.domains.blacklist.models.BlacklistedIban;
import com.crawler.domains.blacklist.models.IbanProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlacklistedIbanRepository extends JpaRepository<BlacklistedIban, Long> {
    boolean existsByIban(String iban);
    List<IbanProjection> findAllBy();
}