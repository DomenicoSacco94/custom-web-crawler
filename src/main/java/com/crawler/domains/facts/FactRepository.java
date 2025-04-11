package com.crawler.domains.facts;

import com.crawler.domains.facts.models.Fact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactRepository extends JpaRepository<Fact, Long> {
}