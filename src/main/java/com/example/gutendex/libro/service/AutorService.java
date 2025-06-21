package com.example.gutendex.libro.service;

import com.example.gutendex.libro.model.Autor;
import com.example.gutendex.libro.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    // Método para mostrar autores en consola (Opción 3 del menú)
    public void mostrarAutoresEnConsola() {
        List<Autor> autores = autorRepository.findAll();

        if(autores.isEmpty()) {
            System.out.println("\nNo hay autores registrados en el sistema.");
            return;
        }

        System.out.println("\n=== LISTA DE AUTORES ===");
        autores.forEach(autor -> {
            System.out.println("\nNombre: " + autor.getNombre());
            System.out.println("Nacimiento: " +
                    (autor.getFechaNacimiento() != null ? autor.getFechaNacimiento() : "No disponible"));
            System.out.println("Fallecimiento: " +
                    (autor.getFechaFallecimiento() != null ? autor.getFechaFallecimiento() : "No disponible"));
        });
        System.out.println("\nTotal de autores: " + autores.size());
    }

    // Método para mostrar autores vivos en un año (Opción 4 del menú)
    public void mostrarAutoresVivosEnAnio(int anio) {
        List<Autor> autores = autorRepository.findAutoresVivosEnAnio(anio);

        if(autores.isEmpty()) {
            System.out.println("\nNo hay autores vivos registrados en el año " + anio);
            return;
        }

        System.out.println("\n=== AUTORES VIVOS EN " + anio + " ===");
        autores.forEach(autor -> {
            System.out.println("\nNombre: " + autor.getNombre());
            System.out.println("Periodo de vida: " +
                    (autor.getFechaNacimiento() != null ? autor.getFechaNacimiento().getYear() : "?") + " - " +
                    (autor.getFechaFallecimiento() != null ? autor.getFechaFallecimiento().getYear() : "Presente"));
        });
    }

    // Métodos auxiliares para persistencia
    public Optional<Autor> buscarPorNombre(String nombre) {
        return autorRepository.findByNombre(nombre);
    }

    public Autor guardarAutor(Autor autor) {
        return autorRepository.save(autor);
    }

    // Método para verificar existencia (útil para evitar duplicados)
    public boolean existeAutor(String nombre) {
        return autorRepository.findByNombre(nombre).isPresent();
    }
}