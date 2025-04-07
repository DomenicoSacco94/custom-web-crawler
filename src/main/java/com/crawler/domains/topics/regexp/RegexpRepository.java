package com.crawler.domains.topics.regexp;

import com.crawler.domains.topics.regexp.models.Regexp;
import com.crawler.domains.topics.regexp.models.RegexpProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegexpRepository extends JpaRepository<Regexp, Long> {
    List<Regexp> findByPattern(String pattern);
    List<RegexpProjection> findAllBy();
    List<RegexpProjection> findAllByTopicId(Long topicId);
}