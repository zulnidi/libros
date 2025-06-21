package com.example.gutendex.libro.repository;

import com.example.gutendex.libro.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Búsqueda exacta por nombre
    Optional<Autor> findByNombre(String nombre);

    // Búsqueda flexible por nombre (para consola)
    List<Autor> findByNombreContainingIgnoreCase(String nombre);

    // Autores vivos en un año específico
    @Query("SELECT a FROM Autor a WHERE " +
            "(YEAR(a.fechaNacimiento) <= :anio AND " +
            "(a.fechaFallecimiento IS NULL OR YEAR(a.fechaFallecimiento) >= :anio))")
    List<Autor> findAutoresVivosEnAnio(@Param("anio") int anio);

    // Verificación rápida de existencia
    boolean existsByNombre(String nombre);
}