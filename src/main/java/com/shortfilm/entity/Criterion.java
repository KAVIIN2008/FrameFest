
package com.shortfilm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "criteria")
public class Criterion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Criterion name is required")
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @NotNull(message = "Maximum score is required")
    @Positive(message = "Maximum score must be positive")
    @Column(nullable = false)
    private Integer maxScore;

    public Criterion() {
    }

    public Criterion(String name, String description, Integer maxScore) {
        this.name = name;
        this.description = description;
        this.maxScore = maxScore;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }
}