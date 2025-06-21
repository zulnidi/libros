package com.example.gutendex.libro.service;

import com.example.gutendex.libro.dto.LibroDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class GutendexService {
    private final RestTemplate restTemplate;

    @Value("${gutendex.api.url}")
    private String apiUrl;

    public GutendexService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String testConnection() {
        try {
            String testUrl = apiUrl + "/books?search=test";
            String response = restTemplate.getForObject(URI.create(testUrl), String.class);
            return "=== PRUEBA DE CONEXIÓN EXITOSA ===\n" +
                    "API: " + testUrl + "\n" +
                    "Respuesta: " + (response != null ? response.substring(0, Math.min(100, response.length())) : "OK");
        } catch (Exception e) {
            return "=== ERROR DE CONEXIÓN ===\n" +
                    "URL: " + apiUrl + "\n" +
                    "Error: " + e.getClass().getSimpleName() + "\n" +
                    "Mensaje: " + e.getMessage() + "\n\n" +
                    "SOLUCIONES:\n" +
                    "1. Verifica que la API esté disponible en tu navegador: " + apiUrl + "/books\n" +
                    "2. Revisa tu conexión a internet\n" +
                    "3. Verifica las dependencias en pom.xml";
        }
    }

    public List<LibroDTO> buscarLibrosPorTitulo(String titulo) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .path("/books")
                    .queryParam("search", URLEncoder.encode(titulo, StandardCharsets.UTF_8))
                    .build()
                    .toUriString();

            ResponseEntity<Map> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    Map.class
            );

            return procesarRespuesta(response.getBody());
        } catch (Exception e) {
            System.err.println("[ERROR] Al buscar libros: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<LibroDTO> obtenerLibrosPopulares(int cantidad) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .path("/books")
                    .queryParam("sort", "popular")
                    .build()
                    .toUriString();

            ResponseEntity<Map> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    Map.class
            );

            List<LibroDTO> libros = procesarRespuesta(response.getBody());
            return libros.subList(0, Math.min(cantidad, libros.size()));
        } catch (Exception e) {
            System.err.println("[ERROR] Al obtener libros populares: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<String> obtenerAutores() {
        try {
            String url = apiUrl + "/books";
            ResponseEntity<Map> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    Map.class
            );

            List<Map<String, Object>> resultados = (List<Map<String, Object>>) response.getBody().get("results");
            Set<String> autores = new TreeSet<>();

            for (Map<String, Object> libro : resultados) {
                List<Map<String, String>> autoresLibro = (List<Map<String, String>>) libro.get("authors");
                if (autoresLibro != null) {
                    for (Map<String, String> autor : autoresLibro) {
                        autores.add(autor.getOrDefault("name", "Anónimo"));
                    }
                }
            }
            return new ArrayList<>(autores);
        } catch (Exception e) {
            System.err.println("[ERROR] Al obtener autores: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<String> buscarAutoresVivosEnAnio(int anio) {
        try {
            String url = apiUrl + "/books";
            ResponseEntity<Map> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    Map.class
            );

            List<Map<String, Object>> resultados = (List<Map<String, Object>>) response.getBody().get("results");
            Set<String> autoresVivos = new TreeSet<>();

            for (Map<String, Object> libro : resultados) {
                List<Map<String, Object>> autores = (List<Map<String, Object>>) libro.get("authors");
                if (autores != null) {
                    for (Map<String, Object> autor : autores) {
                        try {
                            // Manejo correcto de los años (pueden venir como Integer o String)
                            Integer nacimiento = autor.get("birth_year") != null ?
                                    parseYear(autor.get("birth_year")) : null;
                            Integer fallecimiento = autor.get("death_year") != null ?
                                    parseYear(autor.get("death_year")) : null;

                            if (nacimiento != null && nacimiento <= anio &&
                                    (fallecimiento == null || fallecimiento >= anio)) {
                                autoresVivos.add((String) autor.getOrDefault("name", "Anónimo"));
                            }
                        } catch (Exception e) {
                            System.err.println("[ADVERTENCIA] Error procesando autor: " + e.getMessage());
                            continue;
                        }
                    }
                }
            }
            return new ArrayList<>(autoresVivos);
        } catch (Exception e) {
            System.err.println("[ERROR] Al buscar autores vivos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Método auxiliar para parsear años que pueden venir como Integer o String
    private Integer parseYear(Object yearData) {
        if (yearData == null) {
            return null;
        }
        try {
            if (yearData instanceof Integer) {
                return (Integer) yearData;
            } else if (yearData instanceof String) {
                return Integer.parseInt((String) yearData);
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public List<LibroDTO> buscarLibrosPorIdioma(String idioma) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .path("/books")
                    .queryParam("languages", idioma.toLowerCase())
                    .build()
                    .toUriString();

            ResponseEntity<Map> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    Map.class
            );

            return procesarRespuesta(response.getBody());
        } catch (Exception e) {
            System.err.println("[ERROR] Al buscar por idioma: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<LibroDTO> procesarRespuesta(Map<String, Object> response) {
        if (response == null || !response.containsKey("results")) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> resultados = (List<Map<String, Object>>) response.get("results");
        List<LibroDTO> libros = new ArrayList<>();

        for (Map<String, Object> libroData : resultados) {
            LibroDTO libro = new LibroDTO();
            libro.setTitulo((String) libroData.getOrDefault("title", "Sin título"));

            // Procesar autores
            List<Map<String, String>> autores = (List<Map<String, String>>) libroData.get("authors");
            if (autores != null && !autores.isEmpty()) {
                libro.setAutor(autores.get(0).getOrDefault("name", "Autor desconocido"));
            }

            // Procesar idiomas
            List<String> idiomas = (List<String>) libroData.get("languages");
            if (idiomas != null && !idiomas.isEmpty()) {
                libro.setIdioma(idiomas.get(0));
            }

            // Procesar descargas
            Object descargas = libroData.get("download_count");
            libro.setNumeroDescargas(descargas != null ? ((Number) descargas).doubleValue() : 0.0);

            libros.add(libro);
        }
        return libros;
    }
}