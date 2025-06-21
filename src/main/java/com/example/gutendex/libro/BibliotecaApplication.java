package com.example.gutendex.libro;

import com.example.gutendex.libro.controller.ConsolaController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApplication implements CommandLineRunner {

	private final ConsolaController consolaController;

	public BibliotecaApplication(ConsolaController consolaController) {
		this.consolaController = consolaController;
	}

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		consolaController.iniciar();
	}
}