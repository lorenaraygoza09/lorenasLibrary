package com.alura.lorenasLibrary.repository;

import com.alura.lorenasLibrary.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByName(String name);

    //buscando autores vivos en x año
    @Query("SELECT a FROM Autor a WHERE a.birth_year <= :year AND (a.death_year IS NULL OR a.death_year > :year)")
    List<Autor> buscarAutoresVivosPorAnio(@Param("year") Integer year);

}
