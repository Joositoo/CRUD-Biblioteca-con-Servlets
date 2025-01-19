package org.example.servletbiblioteca.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.servletbiblioteca.Modelo.DAO;
import org.example.servletbiblioteca.Modelo.Ejemplar;
import org.example.servletbiblioteca.Modelo.Prestamo;
import org.example.servletbiblioteca.Modelo.Usuario;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "ControladorPrestamo", value = "/ControladorPrestamo")
public class ControladorPrestamo extends HttpServlet {
    static DAO<Prestamo, Integer> daoPrestamo = new DAO<Prestamo, Integer>(Prestamo.class, Integer.class);
    static DAO<Usuario, Integer> daoUsuario = new DAO<>(Usuario.class, Integer.class);
    static DAO<Ejemplar, Integer> daoEjemplar = new DAO<>(Ejemplar.class, Integer.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String opcion = request.getParameter("opcion");
        String idP = request.getParameter("id");
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        int idEjemplar = Integer.parseInt(request.getParameter("idEjemplar"));
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaDevol =LocalDate.now().plusDays(10);

        int id = (idP != null && !idP.isEmpty()) ? Integer.parseInt(idP) : 0;

        switch (opcion){
            case "crear prestamo":
                Prestamo prestamoCreado = creaPrestamo(idUsuario, idEjemplar, fechaInicio, fechaDevol);
                daoPrestamo.insert(prestamoCreado);
                prestamoCreado = buscaPrestamoCreado(idUsuario, idEjemplar);

                out.println("Préstamo registrado.");
                out.println(om.writeValueAsString(prestamoCreado));
                break;
            case "actualizar prestamo":
                break;
            case "eliminar prestamo":
                break;
            default:
                out.println("Error");
                break;
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String opcion = request.getParameter("opcion");
        String idP = request.getParameter("id");

        int id = (idP != null && !idP.isEmpty()) ? Integer.parseInt(idP) : 0;

        switch (opcion) {
            case "ver prestamo":
                Prestamo prestamo = daoPrestamo.selectById(id);
                out.println("Prestamo encontrado: \n" + om.writeValueAsString(prestamo));
                break;
            case "listar prestamos":
                List<Prestamo> prestamos = daoPrestamo.selectAll();

                out.println("Listado de préstamos: ");
                for (Prestamo p : prestamos) {
                    out.println(om.writeValueAsString(p));
                }
                break;
            default:
                out.println("Error");
        }
    }

    public Prestamo creaPrestamo(int idUsuario, int idEjemplar, LocalDate fechaInicio, LocalDate fechaDevol){
        Usuario usuario = daoUsuario.selectById(idUsuario);
        Ejemplar ejemplar = daoEjemplar.selectById(idEjemplar);

        return new Prestamo(usuario, ejemplar, fechaInicio, fechaDevol);
    }

    public Prestamo buscaPrestamoCreado(int idUsuario, int idEjemplar){
        Usuario usuario = daoUsuario.selectById(idUsuario);
        Ejemplar ejemplar = daoEjemplar.selectById(idEjemplar);
        Prestamo prestamo = null;

        List<Prestamo> listaPrestamos = daoPrestamo.selectAll();
        for (Prestamo p : listaPrestamos){
            if (p.getUsuario().equals(usuario) && p.getEjemplar().equals(ejemplar)){
                prestamo = p;
            }
        }
        return prestamo;
    }
}
