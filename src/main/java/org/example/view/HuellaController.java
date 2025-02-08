package org.example.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.entities.Actividad;
import org.example.entities.Categoria;
import org.example.entities.Usuario;
import org.example.services.HuellaServices;
import org.example.session.Session;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class HuellaController extends Controller implements Initializable {

    @FXML
    private ComboBox<String> huellaComboBox;

    @FXML
    private TextField cantidadTextField;

    @FXML
    private TextField valorTextField; // Este campo solo mostrará la unidad (kg, kWh, etc.)

    @FXML
    private DatePicker fechaDatePicker;

    @FXML
    private Button saveButton;

    private final HuellaServices huellaServices = new HuellaServices();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarActividades();
        huellaComboBox.setOnAction(event -> actualizarValorTextField());
    }

    @FXML
    private void cargarActividades() {
        List<Actividad> actividades = huellaServices.obtenerTodasLasActividades();
        for (Actividad actividad : actividades) {
            huellaComboBox.getItems().add(actividad.getNombre());
        }
    }

    public void actualizarValorTextField() {
        String actividadSeleccionada = huellaComboBox.getValue();
        if (actividadSeleccionada != null) {
            Actividad actividad = huellaServices.obtenerActividadConCategoria(actividadSeleccionada);
            if (actividad != null) {
                Categoria categoria = actividad.getIdCategoria();
                if (categoria != null) {
                    String unidad = categoria.getUnidad();
                    valorTextField.setText(unidad); // Solo muestra la unidad
                }
            }
        }
    }

    @Override
    public void onOpen(Object input) throws IOException {
    }

    @FXML
    public void guardarHuella() {
        try {
            Usuario usuario = Session.getInstance().getUsuario();
            String actividadSeleccionada = huellaComboBox.getValue();
            String cantidadTexto = cantidadTextField.getText();
            String unidad = valorTextField.getText(); // Solo unidad, no número
            LocalDate fecha = fechaDatePicker.getValue();

            huellaServices.guardarHuella(usuario, actividadSeleccionada, cantidadTexto, unidad, fecha);

            App.currentController.changeScene(Scenes.INFORMATION, Session.getInstance().getUsuario());

        } catch (Exception e) {
            System.out.println(" Error al guardar la huella: " + e.getMessage());
        }
    }
}