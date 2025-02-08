package org.example.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.dao.ActividadDao;
import org.example.dao.HabitoDao;
import org.example.entities.Actividad;
import org.example.entities.Habito;
import org.example.entities.Usuario;
import org.example.session.Connection;
import org.example.session.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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

    private final ActividadDao actividadDao = new ActividadDao();
    private final HabitoDao habitoDao = new HabitoDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarActividades();
        configurarTipos();
    }

    @FXML
    private void cargarActividades() {
        List<Actividad> actividades = actividadDao.obtenerTodasLasActividades();
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
            if (usuario == null) {
                throw new Exception("❌ No hay usuario logueado.");
            }

            String actividadSeleccionada = comboBoxActividad.getValue();
            String frecuenciaTexto = textFieldFrecuencia.getText();
            String tipo = comboBoxTipo.getValue();
            LocalDate ultimaFecha = datePicker.getValue();

            if (actividadSeleccionada == null || frecuenciaTexto.isEmpty() || tipo == null || ultimaFecha == null) {
                throw new Exception("❌ Todos los campos deben estar completos.");
            }

            int frecuencia = Integer.parseInt(frecuenciaTexto);
            if (frecuencia <= 0) {
                throw new Exception("❌ La frecuencia debe ser un número positivo.");
            }

            Actividad actividad = actividadDao.obtenerActividad(actividadSeleccionada);
            if (actividad == null) {
                throw new Exception("❌ Actividad no encontrada.");
            }

            // 🔹 Se inicializa correctamente `Habito`
            Habito nuevoHabito = new Habito(usuario, actividad, frecuencia, tipo, ultimaFecha);

            habitoDao.guardarHabito(nuevoHabito);

            System.out.println("✅ Hábito guardado exitosamente.");

            App.currentController.changeScene(Scenes.INFORMATION, Session.getInstance().getUsuario());

        } catch (Exception e) {
            System.out.println("❌ Error al guardar el hábito: " + e.getMessage());
        }
    }
}
