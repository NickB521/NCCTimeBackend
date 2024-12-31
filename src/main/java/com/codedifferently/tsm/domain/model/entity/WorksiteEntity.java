package com.codedifferently.tsm.domain.model.entity;

import lombok.*;
import jakarta.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "worksites")
public class WorksiteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worksite_id")
    private Integer id;

    @Column(name = "worksite_name", nullable = false)
    private String name;
}
