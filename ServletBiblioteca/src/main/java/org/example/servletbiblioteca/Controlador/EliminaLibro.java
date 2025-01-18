package org.example.servletbiblioteca.Controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.servletbiblioteca.Modelo.DAO;
import org.example.servletbiblioteca.Modelo.Libro;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "EliminaLibro", value = "/EliminaLibro")
public class EliminaLibro extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO<Libro, Integer> dao = new DAO<>(Libro.class, Integer.class);
        Libro libro = null;

        String isbn = request.getParameter("isbn");

        List<Libro> listaLibros = dao.selectAll();

        for (Libro l : listaLibros) {
            if (l.getIsbn().equals(isbn)) {
                libro = l;
                break;
            }
        }

        if (libro != null) {
            dao.delete(libro);
            response.sendRedirect("verLibros.jsp");
        }
        else{
            request.setAttribute("noExisteLibro", "El libro con ISBN '" + isbn + "' no existe.");

            request.getRequestDispatcher("eliminaLibro.jsp").forward(request, response);
        }
    }
}
