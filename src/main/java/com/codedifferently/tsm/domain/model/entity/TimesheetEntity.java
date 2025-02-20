package com.codedifferently.tsm.domain.model.entity;

import lombok.*;

import java.util.Date;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "timesheets")
public class TimesheetEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timesheet_id")
    private Integer timesheet_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user_id;

    @Column(name = "created", nullable = false)
    private Date created;

    @Column(name = "updated", nullable = false)
    private Date updated;

    @Column(name = "week", nullable = false)
    private Date week;
    
}
