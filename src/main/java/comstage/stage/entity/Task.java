package comstage.stage.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private String name;

    private String description;

    private String status;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date = LocalDateTime.now();




}