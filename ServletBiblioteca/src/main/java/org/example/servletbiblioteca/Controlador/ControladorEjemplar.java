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
import org.example.servletbiblioteca.Modelo.Libro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "ControladorEjemplar", value = "/ControladorEjemplar")
public class ControladorEjemplar extends HttpServlet {
    static DAO<Ejemplar, Integer> daoEjemplar = new DAO<>(Ejemplar.class, Integer.class);
    static DAO<Libro, Integer> daoLibro = new DAO<>(Libro.class, Integer.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String opcion = request.getParameter("opcion");

        Integer id = null;
        String idParam = request.getParameter("id");
        if(idParam != null && !idParam.isEmpty()){
            id = Integer.valueOf(idParam);
        }

        String isbn = request.getParameter("isbn");
        String estado = request.getParameter("estado");

        boolean existeUsuario = LoginUsuario.existeUsuario(email, password);
        if (existeUsuario) {
            switch (opcion) {
                case "crear ejemplar":
                    boolean ejCreado = creaEjemplar(isbn, estado);
                    if (ejCreado){
                        out.println("El ejemplar con isbn '" +isbn+ "' y estado '" +estado+ "' se ha registrado.");
                    }
                    else {
                        out.println("No se ha podido registrar el ejemplar (estado incorrecto o isbn no se encuentra)");
                    }
                    break;
                case "modificar ejemplar":
                    if (actualizaEjemplar(id, isbn, estado)){
                        out.println("El ejemplar con id '"  +id+"' ha sido modificado");
                        out.println(om.writeValueAsString(daoEjemplar.selectById(id)));
                        break;
                    }
                    else{
                        out.println("No se ha podido modificar el ejemplar (isbn o estado incorrectos).");
                    }
                    break;
                case "eliminar ejemplar":
                    Ejemplar ejemplar = daoEjemplar.selectById(id);
                    out.println(om.writeValueAsString(ejemplar));
                    daoEjemplar.delete(ejemplar);

                    out.println("El ejemplar mostrado ha sido eliminado.");
                    break;
                default:
                    out.println("Error");
                    break;
            }
        }
        else{
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
        String idE = request.getParameter("id");

        int id = (idE != null && !idE.isEmpty()) ? Integer.parseInt(idE) : 0;

        boolean existeUsuario = LoginUsuario.existeUsuario(email, password);
        if (existeUsuario) {
            switch (opcion) {
                case "ver ejemplar":
                    Ejemplar ejemplar = leeEjemplar(id);

                    out.println("Ejemplar encontrado:");
                    out.println(om.writeValueAsString(ejemplar));
                    break;
                case "listar ejemplares":
                    List<Ejemplar>listaEjemplares = daoEjemplar.selectAll();
                    List<Ejemplar>listaDispoibles = new ArrayList<Ejemplar>();

                    out.println("Lista de ejemplares: \n");
                    for (Ejemplar e: listaEjemplares){
                        out.println(om.writeValueAsString(e));

                        if (e.getEstado().equals("Disponible")){
                            listaDispoibles.add(e);
                        }
                    }

                    out.println("Listado de ejemplares con estado 'Disponible: \n'");
                    for (Ejemplar e: listaDispoibles){
                        out.println(om.writeValueAsString(e));
                    }

                    break;
                default:
                    out.println("Error");
            }
        }
        else{
            out.println("Usuario incorrecto");
        }
    }

    public boolean creaEjemplar(String isbn, String estado) {
        Libro libro = null;
        List<Libro> listaLibros = daoLibro.selectAll();
        for (Libro l : listaLibros) {
            if (l.getIsbn().equals(isbn)) {
                libro = l;
            }
        }

        if (libro != null) {
            Ejemplar ejemplar = new Ejemplar(libro, estado);

            if (ejemplar.getEstado().equals("Prestado") || ejemplar.getEstado().equals("Disponible") ||
                    ejemplar.getEstado().equals("Dañado")) {
                daoEjemplar.insert(ejemplar);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public Ejemplar buscaEjemplar(String isbn){
        Ejemplar ejemplar = null;
        List<Ejemplar> listaEjemplars = daoEjemplar.selectAll();
        for (Ejemplar e : listaEjemplars) {
            if (e.getIsbn().equals(isbn)) {
                ejemplar = e;
                break;
            }
        }
        return ejemplar;
    }

    public Ejemplar leeEjemplar(int id){
        return daoEjemplar.selectById(id);
    }

    public boolean actualizaEjemplar(int id, String isbn, String estado) {
        Libro libro = null;
        List<Libro> listaLibros = daoLibro.selectAll();
        for (Libro l : listaLibros) {
            if (l.getIsbn().equals(isbn)) {
                libro = l;
            }
        }

        if (libro != null) {
            Ejemplar ejemplar = daoEjemplar.selectById(id);

            if (Objects.equals(estado, "Prestado") || Objects.equals(estado, "Disponible") ||
                    Objects.equals(estado, "Dañado")){
                ejemplar.setIsbn(libro);
                ejemplar.setEstado(estado);
                daoEjemplar.update(ejemplar);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
}
