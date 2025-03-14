package com.crawler.domains.inferences;

import com.crawler.domains.inferences.models.Inference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InferenceRepository extends JpaRepository<Inference, Long> {
}