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

@WebServlet(name = "EncuentraLibro", value = "/Encuentralibro")
public class EncuentraLibro extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (libro != null) {
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String libroJson = om.writeValueAsString(libro);
            out.println(libroJson);
            /*out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8' />");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0' />");
            out.println("<title>Lista de libros</title>");
            out.println("<link rel='stylesheet' href='style.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Libro encontrado</h2>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>isbn</th><th>Titulo</th><th>Autor</th><th>Json</th>");
            out.println("</tr>");
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String libroJson = om.writeValueAsString(libro);

            out.println("<tr><td>" + libro.getIsbn() + "</td><td>" + libro.getTitulo() + "</td><td>" + libro.getAutor() + "</td><td>"+libroJson+"</td></tr>");
            out.println("</table>");
            out.println("<br><br><a href='buscarLibro.jsp'><button>Volver atrás</button></a>");
            out.println("<br><br><a href='index.jsp'><button>Volver a la gestion</button></a>");
            out.println("</body>");
            out.println("</html>");*/
        }
        else{
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            //String libroJson = om.writeValueAsString("No se ha encontrado el libro");
            out.println(om.writeValueAsString("No se ha encontrado el libro"));

            //out.println();
            /*out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8' />");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0' />");
            out.println("<title>Lista de libros</title>");
            out.println("<link rel='stylesheet' href='style.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Libro encontrado</h2>");
            out.println("<p>Lo sentimos, el libro con isbn '" +isbn+"' no lo hemos encontrado.</p>");
            out.println("<br><br><a href='buscarLibro.jsp'><button>Volver atrás</button></a>");
            out.println("<br><br><a href='index.jsp'><button>Volver a la gestion</button></a>");
            out.println("</body>");
            out.println("</html>");*/
        }
    }
}
