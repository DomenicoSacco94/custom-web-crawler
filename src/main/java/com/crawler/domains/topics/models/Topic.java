package com.crawler.domains.topics.models;

import com.crawler.domains.occurrences.regexp.models.Regexp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany
    @JoinColumn(name = "topic_id", referencedColumnName = "id")
    private List<Regexp> regexps;
}