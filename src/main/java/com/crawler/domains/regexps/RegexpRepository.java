package com.crawler.domains.regexps;

import com.crawler.domains.regexps.models.Regexp;
import com.crawler.domains.regexps.models.RegexpProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegexpRepository extends JpaRepository<Regexp, Long> {
    boolean existsByPattern(String pattern);
    List<Regexp> findByPattern(String pattern);
    List<RegexpProjection> findAllBy();
}