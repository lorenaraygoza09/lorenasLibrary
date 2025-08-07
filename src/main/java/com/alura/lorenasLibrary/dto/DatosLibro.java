package com.alura.lorenasLibrary.dto;

import com.alura.lorenasLibrary.model.Autor;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosLibro {
        @JsonAlias("title") private String titulo;
        @JsonAlias("authors") private List<DtoAutor> autor;
        @JsonAlias("languages")private List<String> lenguaje;
        @JsonAlias("download_count")private Long numeroDeDescargas;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<DtoAutor> getAutor() {
        return autor;
    }

    public void setAutor(List<DtoAutor> autor) {
        this.autor = autor;
    }

    public List<String> getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(List<String> lenguaje) {
        this.lenguaje = lenguaje;
    }

    public Long getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Long numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }
}
