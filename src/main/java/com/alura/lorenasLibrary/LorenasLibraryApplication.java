package com.alura.lorenasLibrary;

import com.alura.lorenasLibrary.model.Autor;
import com.alura.lorenasLibrary.principal.Principal;
import com.alura.lorenasLibrary.repository.AutorRepository;
import com.alura.lorenasLibrary.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LorenasLibraryApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository repository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LorenasLibraryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository, autorRepository);
		principal.mostrarMenu();
	}
}
