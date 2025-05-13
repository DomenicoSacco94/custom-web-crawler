package com.crawler.domains.occurrences.models;

import com.crawler.domains.regexp.models.Regexp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "occurrences")
public class Occurrence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "regexp_id", nullable = false)
    private Regexp regexp;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String surroundingText;

    @Column(columnDefinition = "TEXT")
    private String url;
}