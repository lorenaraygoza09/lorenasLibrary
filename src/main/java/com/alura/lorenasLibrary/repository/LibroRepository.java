package com.alura.lorenasLibrary.repository;


import com.alura.lorenasLibrary.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    //buscar libro por titulo
    Optional<Libro> findByTituloContainsIgnoreCase(String nombreLibro);

    //buscar libros con autores
    @Query("SELECT DISTINCT l FROM Libro l JOIN FETCH l.autores")
    List<Libro> findAllConAutores();

    //buscando los 5 libros mas descargados
    List<Libro> findTop5ByOrderByNumeroDeDescargasDesc();

}
