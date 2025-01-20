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

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String opcion = request.getParameter("opcion");
        String idP = request.getParameter("id");
        String idUsuario = request.getParameter("idUsuario");
        String idEjemplar = request.getParameter("idEjemplar");
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaDevol =LocalDate.now().plusDays(10);

        int id = (idP != null && !idP.isEmpty()) ? Integer.parseInt(idP) : 0;

        boolean existeUsuario = LoginUsuario.existeUsuario(email, password);
        if (existeUsuario) {
            switch (opcion){
                case "crear prestamo":
                    Prestamo prestamoCreado = creaPrestamo((Integer.parseInt(idUsuario)), (Integer.parseInt(idEjemplar)), fechaInicio, fechaDevol);
                    daoPrestamo.insert(prestamoCreado);
                    prestamoCreado = buscaPrestamoCreado((Integer.parseInt(idUsuario)), (Integer.parseInt(idEjemplar)));

                    out.println("Préstamo registrado.");
                    out.println(om.writeValueAsString(prestamoCreado));
                    break;
                case "actualizar prestamo":
                    Prestamo prestamo = actualizaPrestamo(id, Integer.parseInt(idUsuario), Integer.parseInt(idEjemplar));

                    out.println("El préstamo ha sido modificado.\n " + om.writeValueAsString(prestamo));
                    break;
                case "eliminar prestamo":
                    Prestamo prestamoAEliminar = daoPrestamo.selectById(id);
                    out.println(om.writeValueAsString(prestamoAEliminar));
                    daoPrestamo.delete(prestamoAEliminar);

                    out.println("El prestamo mostrado ha sido eliminado.");
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
        String idP = request.getParameter("id");

        int id = (idP != null && !idP.isEmpty()) ? Integer.parseInt(idP) : 0;

        boolean existeUsuario = LoginUsuario.existeUsuario(email, password);
        if (existeUsuario) {
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
        else {
            out.println("Usuario incorrecto");
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

    public Prestamo actualizaPrestamo(int id, int idUsuario, int idEjemplar){
        Prestamo prestamo = daoPrestamo.selectById(id);

        Usuario usuario = daoUsuario.selectById(idUsuario);
        Ejemplar ejemplar = daoEjemplar.selectById(idEjemplar);

        prestamo.setUsuario(usuario);
        prestamo.setEjemplar(ejemplar);
        prestamo.setFechaInicio(LocalDate.now());
        prestamo.setFechaDevolucion(LocalDate.now().plusDays(7));

        daoPrestamo.update(prestamo);

        return daoPrestamo.selectById(prestamo.getId());
    }
}
