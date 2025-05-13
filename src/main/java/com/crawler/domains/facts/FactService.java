package com.crawler.domains.facts;

import com.crawler.domains.facts.models.FactDTO;
import com.crawler.domains.occurrences.models.OccurrenceDTO;

import java.util.List;

public interface FactService {
    List<FactDTO> getAllFacts();
    void extractFact(OccurrenceDTO occurrenceDTO);
}