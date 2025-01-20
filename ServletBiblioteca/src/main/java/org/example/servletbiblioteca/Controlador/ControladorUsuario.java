package org.example.servletbiblioteca.Controlador;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Objects;

@WebServlet(name = "ControladorUsuario", value = "/ControladorUsuario")
public class ControladorUsuario extends HttpServlet {
    static DAO<Usuario, Integer> dao = new DAO<>(Usuario.class, Integer.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String opcion = req.getParameter("opcion");
        String dni = req.getParameter("dni");
        String nombre = req.getParameter("nombre");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String tipo = req.getParameter("tipo");
        String idU = req.getParameter("id");

        int id = (idU != null && !idU.isEmpty()) ? Integer.parseInt(idU) : 0;

        switch (opcion){
            case "crear":
                creaUsuario(dni, nombre, email, password, tipo);
                out.println("El usuario con dni '" +dni+ "' y nombre '" +nombre+"' ha sido registrado.");

                Usuario usuarioEncontrado = encuentraUsuario(email);
                out.println(om.writeValueAsString(usuarioEncontrado));
                break;
            case "borrar":
                Usuario usuario = dao.selectById(id);
                out.println(om.writeValueAsString(usuario));
                dao.delete(usuario);
                out.println("El usuario mostrado ha sido borrado.");
                break;
            case "actualizar":
                Usuario user = actualizaUsuario(id, dni, nombre, email, password, tipo);
                dao.update(user);
                out.println("Usuario modificado");

                Usuario userMod = dao.selectById(id);
                out.println(om.writeValueAsString(userMod));
                break;
            default:
                out.println("Error");
                break;
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        String opcion = req.getParameter("opcion");
        int id = Integer.parseInt(req.getParameter("id"));

        switch (opcion){
            case "ver usuario":
                Usuario usuario = verUsuario(id);

                String usuarioJson = om.writeValueAsString(usuario);
                out.println("Usuario encontrado: \n" + usuarioJson);
                break;
            case "listar usuarios":
                List<Usuario> listaUsuarios = verTodos();
                for (Usuario u : listaUsuarios) {
                    out.println(om.writeValueAsString(u));
                }
                break;
            default:
                out.println("Error");
                break;
        }
    }

    public void creaUsuario(String dni, String nombre, String email, String password, String tipo) throws JsonProcessingException {
        Usuario usuario = new Usuario(dni, nombre, email, password, tipo);
        dao.insert(usuario);
    }

    public Usuario encuentraUsuario(String email){
        Usuario usuario = null;
        List<Usuario> lista = dao.selectAll();
        for (Usuario u : lista){
            if (u.getEmail().equals(email)){
                usuario = u;
            }
        }
        return usuario;
    }

    public Usuario verUsuario(int id) throws JsonProcessingException {
        return dao.selectById(id);
    }

    public List<Usuario> verTodos(){
        return dao.selectAll();
    }

    public Usuario actualizaUsuario(int id, String dni, String nombre, String email, String password, String tipo) {
        Usuario usuario = dao.selectById(id);
        if (Objects.equals(dni, "")){
            dni = usuario.getDni();
        }
        if (Objects.equals(nombre, "")){
            nombre = usuario.getNombre();
        }
        if (Objects.equals(email, "")){
            email = usuario.getEmail();
        }
        if (Objects.equals(password, "")){
            password = usuario.getPassword();
        }
        if (Objects.equals(tipo, "")){
            tipo = usuario.getTipo();
        }
        usuario.setDni(dni);
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setTipo(tipo);

        return usuario;
    }
}
