package com.example.gutendex.libro.dto;

import lombok.Data;
import com.example.gutendex.libro.model.Libro;
@Data
public class LibroDTO {
    private String titulo = "Sin título";
    private String autor = "Autor desconocido";
    private String idioma = "Desconocido";
    private Double numeroDescargas = 0.0;

    // Método auxiliar para conversión desde la entidad
    public static LibroDTO fromEntity(Libro libro) {
        LibroDTO dto = new LibroDTO();
        if (libro != null) {
            dto.setTitulo(libro.getTitulo());
            dto.setIdioma(libro.getIdioma());
            dto.setNumeroDescargas(libro.getNumeroDescargas());
            if (libro.getAutor() != null) {
                dto.setAutor(libro.getAutor().getNombre());
            }
        }
        return dto;
    }
}