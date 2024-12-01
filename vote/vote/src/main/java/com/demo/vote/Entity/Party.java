package com.demo.vote.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//these are the parties that are going to receive the voting 
//this class is use to create a database for the parties and going to note the number of votes each party receives 
//and using this table the result will be determined

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

@Table(name = "parties")
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int voteReceived;
}
