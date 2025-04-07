package com.crawler.domains.facts.models;

import com.crawler.domains.occurrences.models.Occurrence;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "facts")
public class Fact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "occurrence_id", nullable = false)
    private Long occurrenceId;

    @Column(nullable = false)
    private String inferredText;

    @Column(name = "topic_id", nullable = false)
    private Long topicId;
}