package com.alura.lorenasLibrary.principal;

import com.alura.lorenasLibrary.dto.DtoAutor;
import com.alura.lorenasLibrary.model.Autor;
import com.alura.lorenasLibrary.dto.DatosLibro;
import com.alura.lorenasLibrary.model.DatosRespuesta;
import com.alura.lorenasLibrary.model.Libro;
import com.alura.lorenasLibrary.repository.AutorRepository;
import com.alura.lorenasLibrary.repository.LibroRepository;
import com.alura.lorenasLibrary.service.ConsumoApi;
import com.alura.lorenasLibrary.service.ConvierteDatos;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://gutendex.com";
    private final String URL_LIBROS = "/books?search=";
    private final String URL_AUTORES ="/books?author_year_start=";
    private final String URL_IDIOMA = "&languages=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repository;
    private AutorRepository autorRepository;

    private List<Libro> libros;
    private Optional<Libro> libroBuscado;

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.repository = repository;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \n
                    ****** BIENVENIDO A LA BIBLIOTECA DE LORENA ***** \n
                    Ingresa la opcion deseada:
                    1 - Buscar libros
                    2 - Buscar libros por titulo
                    3 - Mostrar libros registrados
                    4 - Mostrar autores registrados
                    5 - Mostrar autores vivos en determinado año
                    6 - Mostrar los 5 libros más descargados 
                    
                    0 - Salir
                    """;

            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;

                case 2:
                    buscarLibroPorTitulo();
                    break;

                case 3:
                    mostrarLibrosRegistrados();
                    break;

                case 4:
                    mostrarAutoresRegistrados();
                    break;

                case 5:
                    mostrarAutoresVivosEnXAño();
                    break;

                case 6:
                    mostrarTop5librosMasDescargados();
                    break;

                case 0:
                    System.out.println("Cerrando aplicación...");
                    break;

                default:
                    System.out.println("Ingresa una opción válida.");
            }
        }
    }

    private DatosLibro getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = scanner.nextLine();
        var libroCodificado = URLEncoder.encode(nombreLibro.trim(), StandardCharsets.UTF_8);
        var urlConsulta = URL_BASE + URL_LIBROS + libroCodificado;
        System.out.println("url consulta: " + urlConsulta);
        var json = consumoApi.obtenerDatos(urlConsulta);
        System.out.println("Json recibido" + json);
        if (json == null || json.isEmpty()) {
            throw new RuntimeException("El JSON es nulo o está vacío.");
        }

        DatosRespuesta respuesta = conversor.obtenerDatos(json, DatosRespuesta.class);

        if (respuesta.getResults() == null || respuesta.getResults().isEmpty()){
            throw new RuntimeException("No se encontraron resultados para ese libro.");
        }

        return respuesta.getResults().get(0);
    }

    private void buscarLibro(){
        DatosLibro datos = getDatosLibro();
        Libro libro = new Libro(datos);

        List<Autor> autoresFinales = new ArrayList<>();

        for (DtoAutor autorDatos : datos.getAutor()) {
            // Buscar si el autor ya existe en la base de datos
            Optional<Autor> autorExistente = autorRepository.findByName(autorDatos.getName());

            if (autorExistente.isPresent()) {
                autoresFinales.add(autorExistente.get());
            } else {
                Autor nuevoAutor = new Autor(
                        autorDatos.getName(),
                        autorDatos.getBirth_year(),
                        autorDatos.getDeath_year()
                );
                autoresFinales.add(autorRepository.save(nuevoAutor));
            }
        }

        libro.setAutores(autoresFinales);
        repository.save(libro); // Guardar el libro con autores existentes o nuevos
    }

    private void buscarLibroPorTitulo(){
        System.out.println("Ingresa el titulo del libro que deseas buscar");
        var nombreLibro = scanner.nextLine();
        this.libroBuscado = repository.findByTituloContainsIgnoreCase(nombreLibro);
        if (libroBuscado.isPresent()){
            System.out.println("El libro buscado es: " + libroBuscado.get());
        } else {
            System.out.println("El libro no fue encontrado.");
        }
    }

    private void mostrarLibrosRegistrados(){
        libros = repository.findAll();
        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println );
    }

    private void mostrarAutoresRegistrados() {
        libros = repository.findAllConAutores();
        libros.stream()
                .flatMap(libro -> libro.getAutores().stream())
                //.distinct()
                .sorted(Comparator.comparing(Autor::getName))
                .forEach(System.out::println);
    }

   /* private void mostrarAutoresVivosEnXAño() {
        System.out.println("Ingresa el año en el que deseas saber que autores estaban vivos");
        var añoAutor = scanner.nextInt();
        scanner.nextLine();
        libros = repository.findAllConAutores();
        libros.stream()
                .flatMap(libro -> libro.getAutores().stream())
                .filter(autor ->
                        autor.getBirth_year() != null &&
                                autor.getBirth_year() <= añoAutor &&
                                (autor.getDeath_year() == null || autor.getDeath_year() >= añoAutor)
                )
                .distinct()
                .forEach(autor -> System.out.println("Autor: " + autor.getName() + " || Año de nacimiento: " + autor.getBirth_year() + " || Año de muerte: " + autor.getDeath_year()));
    }
    */
    private void mostrarAutoresVivosEnXAño(){
        System.out.println("Ingresa el año en el que deseas saber qué autores estaban vivos");
        var añoAutor = scanner.nextInt();
        scanner.nextLine();
        List<Autor> autoresVivos = autorRepository.buscarAutoresVivosPorAnio(añoAutor);

        autoresVivos.forEach(autor ->
                System.out.println("Autor: " + autor.getName() + " || Año de nacimiento: " + autor.getBirth_year() + " || Año de muerte: " + autor.getDeath_year()));
    }

    private void mostrarTop5librosMasDescargados() {
        List<Libro> topLibros = repository.findTop5ByOrderByNumeroDeDescargasDesc();
        topLibros.forEach(libros ->
                System.out.println("- Titulo: " + libros.getTitulo() + " || Autor: " + libros.getAutores() + " || Numero de descargas: " + libros.getNumeroDeDescargas()));
    }

}
