    package org.example.session;

    import org.example.entities.Usuario;

    public class Session {
        private static Session instancia;
        private Usuario usuario;

        private Session() {

        }

        public static Session getInstance() {
            if(instancia == null) {
                instancia = new Session();
            }
            return instancia;
        }

        public void login(Usuario user) {
            if (user != null) {
                this.usuario = user;
                System.out.println("Usuario logueado: " + usuario.getNombre());
            } else {
                System.out.println("Error: Intento de login con usuario NULL");
            }
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void logout() {
            usuario = null;
        }
    }
