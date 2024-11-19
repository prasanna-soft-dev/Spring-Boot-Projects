package com.bookstore.bookstore_using_spring_boot.Entity;

import jakarta.persistence.*; // For JPA annotations
import lombok.*; // For Lombok annotations

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for primary key
    private Long id;

    @Column(nullable = false, unique = true) // Ensures title is required and unique
    private String title;

    @Column(nullable = false) // Ensures author is required
    private String author;

    @Column(nullable = false) // Ensures description is required (unique is optional here)
    private String description;

    @Column(nullable = false) // Ensures genre is required
    private String genre;
}
