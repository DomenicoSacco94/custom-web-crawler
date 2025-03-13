package com.crawler.domains.occurrences.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "regexp_occurrences")
public class Occurrence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pattern;

    @Column
    private String description;

    @Column(nullable = false)
    private String surroundingText;

    @Column
    private String url;
}