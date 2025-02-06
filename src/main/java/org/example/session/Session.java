package org.example.session;

import org.example.entities.Usuario;

public class Session {
    private static Session instancia;
    private static Usuario usuario;

    private Session() {

    }

    public static Session getInstance() {
        if(instancia == null) {
            instancia = new Session();
            instancia.login(usuario);
        }
        return instancia;
    }

    public void login(Usuario user) {
      usuario = user;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void logout() {
        usuario = null;
    }
}
