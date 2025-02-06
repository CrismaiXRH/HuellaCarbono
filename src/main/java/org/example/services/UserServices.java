package org.example.services;

//En services van las validaciones

import org.example.dao.UsuarioDao;
import org.example.entities.Usuario;
import org.example.session.Session;

public class UserServices {
    public boolean logear(Usuario usuario){
        UsuarioDao usuarioDao = new UsuarioDao();
        usuarioDao.insertarUsuario(usuario);
        Session.getInstance().login(usuario);
        return true;
    }
}
