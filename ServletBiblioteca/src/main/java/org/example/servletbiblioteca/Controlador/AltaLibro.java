package org.example.servletbiblioteca.Controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.servletbiblioteca.Modelo.DAO;
import org.example.servletbiblioteca.Modelo.Libro;

import java.io.IOException;
import java.util.Objects;

@WebServlet(name = "AltaLibro", value = "/AltaLibro")
public class AltaLibro extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        DAO<Libro, Integer> dao = new DAO<>(Libro.class, Integer.class);

        String isbn = request.getParameter("isbn");
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");

        if (!isbn.matches("(978)[0-9]{9}")){
            request.setAttribute("isbnIncorrecto", "Asegúrate de que el isbn empieza por '978' y le siguen 9 dígitos.");

            request.getRequestDispatcher("creaLibro.jsp").forward(request, response);
        }
        if (Objects.equals(titulo, "") || Objects.equals(autor, "")){
            if (Objects.equals(titulo, "")){
                request.setAttribute("campoIncorrecto", "No dejes el campo vacío.");
            }
            if (Objects.equals(autor, "")){
                request.setAttribute("campoIncorrecto2", "No dejes el campo vacío.");
            }
            request.getRequestDispatcher("creaLibro.jsp").forward(request, response);
        }

        Libro libro = new Libro(isbn, titulo, autor);
        dao.insert(libro);

        response.sendRedirect("verLibros.jsp");

    }
}
