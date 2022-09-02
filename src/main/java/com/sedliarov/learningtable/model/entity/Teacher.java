package com.sedliarov.learningtable.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "teahers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Teacher{

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String secondName;

    private boolean isAdmin;

}
