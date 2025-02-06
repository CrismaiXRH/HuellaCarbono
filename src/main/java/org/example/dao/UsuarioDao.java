package org.example.dao;

import org.example.entities.Usuario;
import org.example.session.Connection;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UsuarioDao extends Usuario {
    public ArrayList<Usuario> obtenerUsuarios() {
        Connection connection = Connection.getInstance();
        Session session = connection.getSession();
        session.beginTransaction();
        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) session.createQuery("from Usuario").list();
        session.getTransaction().commit();
        session.close();
        return usuarios;
    }
    public void insertarUsuario(Usuario user) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
    }
}
