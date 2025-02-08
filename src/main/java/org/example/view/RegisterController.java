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

public class RegisterController extends Controller implements Initializable {

    @FXML
    Button button;

    @FXML
    TextField textFieldcorreo;

    @FXML
    TextField textFieldusuario;

    @FXML
    PasswordField passwordField;

    @FXML
    PasswordField confirmPassword;

    private final UserServices userServices = new UserServices();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void onOpen(Object input) throws IOException {

    }

    @FXML
    public Usuario recogerDatos() throws IOException {
        String correoIngresado = textFieldcorreo.getText().trim();
        String usuarioIngresado = textFieldusuario.getText().trim();
        String contraseniaIngresada = passwordField.getText();
        String contraseniaConfirmada = confirmPassword.getText();

        return userServices.registrarUsuario(correoIngresado, usuarioIngresado, contraseniaIngresada, contraseniaConfirmada);
    }

    @FXML
    public void botonRegistar() {
        try {
            Usuario nuevoUsuario = recogerDatos();
            System.out.println("Usuario registrado con éxito");
            App.currentController.changeScene(Scenes.LOGIN, null);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}