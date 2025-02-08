package org.example.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.entities.Actividad;
import org.example.entities.Usuario;
import org.example.services.HabitoServices;
import org.example.session.Session;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class HabitoController extends Controller implements Initializable {

    @FXML
    private ComboBox<String> comboBoxActividad;

    @FXML
    private TextField textFieldFrecuencia;

    @FXML
    private ComboBox<String> comboBoxTipo;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button saveButton;

    private final HabitoServices habitoServices = new HabitoServices();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarActividades();
        configurarTipos();
    }

    @FXML
    private void cargarActividades() {
        List<Actividad> actividades = habitoServices.obtenerTodasLasActividades();
        for (Actividad actividad : actividades) {
            comboBoxActividad.getItems().add(actividad.getNombre());
        }
    }

    @FXML
    private void configurarTipos() {
        comboBoxTipo.getItems().addAll("Diaria", "Semanal", "Mensual", "Anual");
    }

    @Override
    public void onOpen(Object input) throws IOException {
    }

    @FXML
    public void guardarHabito() {
        try {
            Usuario usuario = Session.getInstance().getUsuario();
            String actividadSeleccionada = comboBoxActividad.getValue();
            String frecuenciaTexto = textFieldFrecuencia.getText();
            String tipo = comboBoxTipo.getValue();
            LocalDate ultimaFecha = datePicker.getValue();

            habitoServices.guardarHabito(usuario, actividadSeleccionada, frecuenciaTexto, tipo, ultimaFecha);

            App.currentController.changeScene(Scenes.INFORMATION, Session.getInstance().getUsuario());

        } catch (Exception e) {
            System.out.println(" Error al guardar el hábito: " + e.getMessage());
        }
    }
}