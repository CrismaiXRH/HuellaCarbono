package org.example.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.dao.UsuarioDao;
import org.example.entities.Usuario;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController extends Controller implements Initializable {

    @FXML
    Button button;

    @FXML
    TextField textField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button registerButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void onOpen(Object input) throws IOException {

    }
    @FXML
    public Usuario recogerDatos() throws IOException{
        String usuarioIngresado = textField.getText();
        String contraseniaIngresada = passwordField.getText();
        if(usuarioIngresado.isEmpty() || contraseniaIngresada.isEmpty()){
            throw new IOException("Campos vacios");
        }
        UsuarioDao usuarioDao = new UsuarioDao();
        ArrayList<Usuario> usuarios = usuarioDao.obtenerUsuarios();
        for(Usuario user : usuarios){
            if (user.getNombre().equals(usuarioIngresado) && user.getContraseña().equals(contraseniaIngresada)){
                return user;
        }
    }
        return null;
    }

    @FXML
    void login() throws IOException {
        Usuario usuario = recogerDatos(); // Solo llamamos a recogerDatos() una vez

        if (usuario != null) {
            // 🔥 Guardamos el usuario en la sesión
            org.example.session.Session.getInstance().login(usuario);

            System.out.println("✅ Usuario logueado: " + usuario.getNombre());

            // Cambiamos de escena pasando el usuario
            App.currentController.changeScene(Scenes.INFORMATION, usuario);
        } else {
            System.out.println("❌ Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    public void gotoRegister() throws IOException {
        App.currentController.changeScene(Scenes.REGISTER, null);
    }
}

