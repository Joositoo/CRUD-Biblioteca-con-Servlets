package org.example.servletbiblioteca.Controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.servletbiblioteca.Modelo.DAO;
import org.example.servletbiblioteca.Modelo.Usuario;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "LoginUsuario", value = "/LoginUsuario")
public class LoginUsuario extends HttpServlet {
    static DAO<Usuario, Integer> dao = new DAO<>(Usuario.class, Integer.class);

    public void doPOST(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        existeUsuario(email, password);
    }

    public static boolean existeUsuario(String email, String password) {
        List<Usuario> listaUsuarios = dao.selectAll();
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getEmail().equals(email) && usuario.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
