package com.example.gutendex.libro.repository;

import com.example.gutendex.libro.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Búsqueda flexible por título
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    // Búsqueda exacta por idioma
    List<Libro> findByIdioma(String idioma);

    // Búsqueda por idioma (case insensitive)
    @Query("SELECT l FROM Libro l WHERE LOWER(l.idioma) = LOWER(:idioma)")
    List<Libro> findByidiomaIgnoreCase(@Param("idioma") String idioma);

    // Verificación de existencia
    boolean existsByTituloAndAutorId(String titulo, Long autorId);

    // Búsqueda por autor
    List<Libro> findByAutorNombreContainingIgnoreCase(String nombreAutor);
}