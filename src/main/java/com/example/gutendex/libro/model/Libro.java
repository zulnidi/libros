package com.example.gutendex.libro.model;


import jakarta.persistence.*;
import lombok.Data;

    @Entity
    @Data
    public class Libro {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String titulo;
        private String idioma;
        private Double numeroDescargas;

        @ManyToOne
        @JoinColumn(name = "autor_id")
        private Autor autor;
    }

