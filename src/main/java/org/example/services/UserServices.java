package org.example.services;

import org.example.dao.UsuarioDao;
import org.example.entities.Usuario;
import org.example.session.Session;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class UserServices {
    private final UsuarioDao usuarioDao = new UsuarioDao();

    public boolean logear(Usuario usuario) {
        usuarioDao.insertarUsuario(usuario);
        Session.getInstance().login(usuario);
        return true;
    }

    public Usuario validarCredenciales(String nombre, String contrasena) {
        if (nombre == null || nombre.isEmpty() || contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario y la contraseña no pueden estar vacíos");
        }

        List<Usuario> usuarios = usuarioDao.obtenerUsuarios();
        for (Usuario user : usuarios) {
            if (user.getNombre().equals(nombre) && user.getContraseña().equals(contrasena)) {
                return user;
            }
        }
        return null;
    }

    public Usuario registrarUsuario(String correo, String nombre, String contrasena, String contrasenaConfirmada) throws IOException {
        if (nombre.isEmpty() || contrasena.isEmpty() || correo.isEmpty() || contrasenaConfirmada.isEmpty()) {
            throw new IOException("Todos los campos son obligatorios");
        }
        if (!correo.contains("@") || !correo.contains(".")) {
            throw new IOException("El correo ingresado no es válido");
        }
        if (!contrasena.equals(contrasenaConfirmada)) {
            throw new IOException("Las contraseñas no coinciden");
        }

        List<Usuario> usuarios = usuarioDao.obtenerUsuarios();
        for (Usuario user : usuarios) {
            if (user.getNombre().equals(nombre)) {
                throw new IOException("El usuario ya existe");
            }
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail(correo);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setContraseña(contrasena);
        nuevoUsuario.setFechaRegistro(LocalDate.now());

        usuarioDao.insertarUsuario(nuevoUsuario);
        return nuevoUsuario;
    }
}