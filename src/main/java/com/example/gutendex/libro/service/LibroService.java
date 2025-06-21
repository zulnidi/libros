package com.example.gutendex.libro.service;

import com.example.gutendex.libro.dto.LibroDTO;
import com.example.gutendex.libro.model.Libro;
import com.example.gutendex.libro.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {
    @Autowired
    private LibroRepository libroRepository;

    // Método para mostrar libros en consola
    public void mostrarLibrosEnConsola() {
        List<Libro> libros = libroRepository.findAll();

        if(libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos local.");
            return;
        }

        System.out.println("\n=== LIBROS REGISTRADOS ===");
        libros.forEach(libro -> {
            System.out.println("\nTítulo: " + libro.getTitulo());
            System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Descargas: " + libro.getNumeroDescargas());
        });
    }

    // Método para buscar por título y mostrar en consola
    public void buscarYMostrarPorTitulo(String titulo) {
        List<Libro> libros = libroRepository.findByTituloContainingIgnoreCase(titulo);

        if(libros.isEmpty()) {
            System.out.println("\nNo se encontraron libros con ese título en la base local.");
            return;
        }

        System.out.println("\n=== RESULTADOS DE BÚSQUEDA ===");
        libros.forEach(libro -> {
            System.out.println("\nTítulo: " + libro.getTitulo());
            System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
            System.out.println("Idioma: " + libro.getIdioma());
        });
    }

    // Método para filtrar por idioma y mostrar en consola
    public void mostrarLibrosPorIdioma(String idioma) {
        List<Libro> libros = libroRepository.findByIdioma(idioma.toLowerCase());

        if(libros.isEmpty()) {
            System.out.println("\nNo hay libros en el idioma " + idioma + " en la base local.");
            return;
        }

        System.out.println("\n=== LIBROS EN IDIOMA " + idioma.toUpperCase() + " ===");
        libros.forEach(libro -> {
            System.out.println("\nTítulo: " + libro.getTitulo());
            System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));
        });
    }

    // Métodos de persistencia (sin cambios)
    public boolean existePorTituloYAutor(String titulo, Long autorId) {
        return libroRepository.existsByTituloAndAutorId(titulo, autorId);
    }

    public Libro guardarLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    // Métodos adicionales para API (si los necesitas)
    public List<LibroDTO> listarTodosLibros() {
        return libroRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private LibroDTO convertirADTO(Libro libro) {
        LibroDTO dto = new LibroDTO();
        dto.setTitulo(libro.getTitulo());
        dto.setIdioma(libro.getIdioma());
        dto.setNumeroDescargas(libro.getNumeroDescargas());
        dto.setAutor(libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido");
        return dto;
    }
}