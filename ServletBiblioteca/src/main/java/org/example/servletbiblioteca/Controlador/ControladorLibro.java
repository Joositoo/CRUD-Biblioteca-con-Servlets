package org.example.servletbiblioteca.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.servletbiblioteca.Modelo.DAO;
import org.example.servletbiblioteca.Modelo.Libro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "ControladorLibro", value = "/ControladorLibro")
public class ControladorLibro extends HttpServlet {
    static DAO<Libro, Integer> dao = new DAO<>(Libro.class, Integer.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String opcion = request.getParameter("opcion");
        String isbn = request.getParameter("isbn");
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");

        boolean existeUsuario = LoginUsuario.existeUsuario(email, password);
        if (existeUsuario) {
            switch (opcion) {
                case "crear libro":
                    Libro libroCreado = creaLibro(isbn, titulo, autor);

                    Libro encuentraLibroCreado = buscaLibro(libroCreado.getIsbn());
                    out.println("Libro registrado. \n" +om.writeValueAsString(encuentraLibroCreado));
                    break;
                case "actualizar libro":
                    Libro libroActualizado = actualizaLibro(isbn, titulo, autor);

                    dao.update(libroActualizado);

                    libroActualizado = buscaLibro(libroActualizado.getIsbn());
                    out.println("Libro actualizado. \n" +om.writeValueAsString(libroActualizado));
                    break;
                case "eliminar libro":
                    Libro libroEliminado = eliminaLibro(isbn);

                    out.println(om.writeValueAsString(libroEliminado));
                    out.println("El libro mostrado ha sido eliminado.");
                    break;
                default:
                    out.println("Error");
                    break;
            }
        }
        else {
            out.println("Usuario incorrecto");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String opcion = request.getParameter("opcion");
        String isbn = request.getParameter("isbn");

        boolean existeUsuario = LoginUsuario.existeUsuario(email, password);
        if (existeUsuario) {
            switch (opcion){
                case "ver libro":
                    Libro libroEncontrado = buscaLibro(isbn);

                    out.println("Libro encontrado: \n" +om.writeValueAsString(libroEncontrado));
                    break;
                case "listar libros":
                    out.println("Listado de libros: ");

                    List<Libro> listaLibros = dao.selectAll();
                    for (Libro libro : listaLibros) {
                        out.println(om.writeValueAsString(libro));
                    }
                    break;
                default:
                    out.println("error");
            }
        }
        else {
            out.println("Usuario incorrecto");
        }


    }

    public Libro creaLibro(String isbn, String titulo, String autor){
        Libro libro = new Libro(isbn, titulo, autor);
        dao.insert(libro);

        return libro;
    }

    public Libro actualizaLibro(String isbn, String titulo, String autor){
        Libro libroActualizado = buscaLibro(isbn);

        if (Objects.equals(titulo, "")){
            titulo = libroActualizado.getTitulo();
        }
        if (Objects.equals(autor, "")){
            autor = libroActualizado.getAutor();
        }

        libroActualizado.setTitulo(titulo);
        libroActualizado.setAutor(autor);

        return libroActualizado;
    }

    public Libro eliminaLibro(String isbn){
        Libro libroEliminar = buscaLibro(isbn);
        dao.delete(libroEliminar);

        return libroEliminar;
    }

    public Libro buscaLibro(String isbn) {
        Libro libro = null;
        List<Libro> listaLibros = dao.selectAll();

        for (Libro l : listaLibros) {
            if (l.getIsbn().equals(isbn)) {
                libro = l;
            }
        }

        return libro;
    }

}
