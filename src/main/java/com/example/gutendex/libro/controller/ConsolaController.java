package com.example.gutendex.libro.controller;

import com.example.gutendex.libro.dto.LibroDTO;
import com.example.gutendex.libro.service.GutendexService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsolaController {
    private final Scanner scanner;
    private final GutendexService gutendexService;

    public ConsolaController(GutendexService gutendexService) {
        this.gutendexService = gutendexService;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println(gutendexService.testConnection());

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    buscarLibroPorTitulo();
                    break;
                case "2":
                    listarLibrosPopulares();
                    break;
                case "3":
                    listarAutores();
                    break;
                case "4":
                    listarAutoresVivosEnAnio();
                    break;
                case "5":
                    listarLibrosPorIdioma();
                    break;
                case "6":
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Buscar libro por título");
        System.out.println("2. Listar libros populares");
        System.out.println("3. Listar autores");
        System.out.println("4. Listar autores vivos en un año");
        System.out.println("5. Listar libros por idioma");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void buscarLibroPorTitulo() {
        System.out.print("\nIngrese el título a buscar: ");
        String titulo = scanner.nextLine();

        List<LibroDTO> resultados = gutendexService.buscarLibrosPorTitulo(titulo);
        if (resultados.isEmpty()) {
            System.out.println("\nNo se encontraron resultados.");
        } else {
            System.out.println("\nResultados de búsqueda:");
            resultados.forEach(this::mostrarLibro);
        }
    }

    private void listarLibrosPopulares() {
        System.out.println("\nLibros más populares:");
        gutendexService.obtenerLibrosPopulares(10).forEach(this::mostrarLibro);
    }

    private void listarAutores() {
        System.out.println("\nListado de autores:");
        gutendexService.obtenerAutores().forEach(autor ->
                System.out.println("- " + autor));
    }

    private void listarAutoresVivosEnAnio() {
        System.out.print("\nIngrese el año: ");
        try {
            int anio = Integer.parseInt(scanner.nextLine());
            List<String> autores = gutendexService.buscarAutoresVivosEnAnio(anio);

            if (autores.isEmpty()) {
                System.out.println("\nNo se encontraron autores vivos en " + anio);
            } else {
                System.out.println("\nAutores vivos en " + anio + ":");
                autores.forEach(a -> System.out.println("- " + a));
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un año válido.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.print("\nIngrese el código de idioma (ej. es, en, fr): ");
        String idioma = scanner.nextLine();

        List<LibroDTO> libros = gutendexService.buscarLibrosPorIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("\nNo se encontraron libros en idioma '" + idioma + "'");
        } else {
            System.out.println("\nLibros en idioma '" + idioma + "':");
            libros.forEach(this::mostrarLibro);
        }
    }

    private void mostrarLibro(LibroDTO libro) {
        System.out.println("\nTítulo: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutor());
        System.out.println("Idioma: " + convertirIdioma(libro.getIdioma()));
        System.out.println("Descargas: " + String.format("%,.0f", libro.getNumeroDescargas()));
        System.out.println("-------------------");
    }

    private String convertirIdioma(String codigo) {
        return switch(codigo.toLowerCase()) {
            case "en" -> "Inglés";
            case "es" -> "Español";
            case "fr" -> "Francés";
            case "de" -> "Alemán";
            case "it" -> "Italiano";
            default -> codigo;
        };
    }
}