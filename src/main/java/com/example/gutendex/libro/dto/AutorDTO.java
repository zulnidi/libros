package com.example.gutendex.libro.dto;

import com.example.gutendex.libro.model.Autor;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AutorDTO {
    private String nombre = "Desconocido";
    private String fechaNacimiento = "No disponible";
    private String fechaFallecimiento = "No disponible";

    // Método auxiliar para conversión desde la entidad
    public static AutorDTO fromEntity(Autor autor) {
        AutorDTO dto = new AutorDTO();
        if (autor != null) {
            dto.setNombre(autor.getNombre());
            dto.setFechaNacimiento(autor.getFechaNacimiento() != null ?
                    autor.getFechaNacimiento().toString() : "No disponible");
            dto.setFechaFallecimiento(autor.getFechaFallecimiento() != null ?
                    autor.getFechaFallecimiento().toString() : "No disponible");
        }
        return dto;
    }
}