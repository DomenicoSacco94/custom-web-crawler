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

    @ManyToOne
    @JoinColumn(name = "occurrence_id", nullable = false)
    private Occurrence occurrence;

    @Column(nullable = false)
    private String inferredText;

    @Column(nullable = false)
    private String consequences;
}