package com.alura.lorenasLibrary.model;

import com.alura.lorenasLibrary.dto.DatosLibro;
import com.alura.lorenasLibrary.dto.DtoAutor;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "libros_autores",joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id"))
    private List<Autor> autores;

    private String lenguaje;
    private long numeroDeDescargas;


    public Libro(){}
    public Libro(DatosLibro datosLibro){
       this.titulo = datosLibro.getTitulo();
      //concatenar idiomas por comas
        if (datosLibro.getLenguaje() != null && !datosLibro.getLenguaje().isEmpty()){
            this.lenguaje = String.join(", ", datosLibro.getLenguaje());
        } else {
            this.lenguaje = "Idioma desconocido";
        }
       this.numeroDeDescargas = datosLibro.getNumeroDeDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(long numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autor) {
        this.autores = autor;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    @Override
    public String toString() {
        return "- Titulo: '" + titulo + '\'' +
                ", Autor/es: '" + autores + '\'' +
                ", Idioma: '" + lenguaje.toUpperCase() + '\'' +
                ", Numero de descargas: " + numeroDeDescargas;
    }
}
