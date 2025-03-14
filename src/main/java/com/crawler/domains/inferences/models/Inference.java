package com.crawler.domains.inferences.models;

import com.crawler.domains.occurrences.models.Occurrence;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inferences")
public class Inference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "occurrence_id", nullable = false)
    private Occurrence occurrence;

    @Column(nullable = false)
    private String inferredText;
}