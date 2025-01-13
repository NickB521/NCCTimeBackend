package com.codedifferently.tsm.domain.model.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "announcements")
public class AnnouncementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcement_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "worksite_id")
    private WorksiteEntity worksite;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "date_range", nullable = false)
    private Date dateRange;

    @Column(name = "approved")
    private Boolean approved;

}
