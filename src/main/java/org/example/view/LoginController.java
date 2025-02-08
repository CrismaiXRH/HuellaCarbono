package org.example.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.entities.Usuario;
import org.example.services.UserServices;

import java.io.IOException;
import java.net.URL;
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

    private final UserServices userServices = new UserServices();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void onOpen(Object input) throws IOException {

    }

    @FXML
    public Usuario recogerDatos() throws IOException {
        String usuarioIngresado = textField.getText();
        String contraseniaIngresada = passwordField.getText();
        if (usuarioIngresado.isEmpty() || contraseniaIngresada.isEmpty()) {
            throw new IOException("Campos vacíos");
        }
        return userServices.validarCredenciales(usuarioIngresado, contraseniaIngresada);
    }

    @FXML
    void login() throws IOException {
        Usuario usuario = recogerDatos();

        if (usuario != null) {
            org.example.session.Session.getInstance().login(usuario);

            System.out.println("Usuario logueado: " + usuario.getNombre());

            App.currentController.changeScene(Scenes.INFORMATION, usuario);
        } else {
            System.out.println("Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    public void gotoRegister() throws IOException {
        App.currentController.changeScene(Scenes.REGISTER, null);
    }
}