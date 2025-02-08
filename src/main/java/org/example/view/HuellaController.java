package org.example.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.dao.ActividadDao;
import org.example.dao.HuellaDao;
import org.example.entities.Actividad;
import org.example.entities.Huella;
import org.example.entities.Usuario;
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
    private TextField valorTextField;

    @FXML
    private DatePicker fechaDatePicker;

    @FXML
    private Button saveButton;

    private final ActividadDao actividadDao = new ActividadDao();
    private final HuellaDao huellaDao = new HuellaDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarActividades();
        huellaComboBox.setOnAction(event -> actualizarValorTextField());
    }

    @FXML
    private void cargarActividades() {
        List<Actividad> actividades = actividadDao.obtenerTodasLasActividades();
        for (Actividad actividad : actividades) {
            huellaComboBox.getItems().add(actividad.getNombre());
        }
    }

    @FXML
    private void actualizarValorTextField() {
        String actividadSeleccionada = huellaComboBox.getValue();
        if (actividadSeleccionada != null) {
            Actividad actividad = actividadDao.obtenerActividad(actividadSeleccionada);
            if (actividad != null && actividad.getIdCategoria() != null) {
                valorTextField.setText(actividad.getIdCategoria().getUnidad());
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
            if (usuario == null) {
                throw new Exception("❌ No hay usuario logueado.");
            }

            String actividadSeleccionada = huellaComboBox.getValue();
            String cantidadTexto = cantidadTextField.getText();
            String valorTexto = valorTextField.getText();
            LocalDate fecha = fechaDatePicker.getValue();

            if (actividadSeleccionada == null || cantidadTexto.isEmpty() || valorTexto.isEmpty() || fecha == null) {
                throw new Exception("❌ Todos los campos deben estar completos.");
            }

            double cantidad = Double.parseDouble(cantidadTexto);
            if (cantidad <= 0) {
                throw new Exception("❌ La cantidad debe ser un número positivo.");
            }

            Actividad actividad = actividadDao.obtenerActividad(actividadSeleccionada);
            if (actividad == null) {
                throw new Exception("❌ Actividad no encontrada.");
            }

            // 🔹 Se inicializa correctamente `Huella`
            Huella nuevaHuella = new Huella(usuario, actividad, cantidad, valorTexto, fecha);

            huellaDao.guardarHuella(nuevaHuella);

            System.out.println("✅ Huella guardada exitosamente.");

            App.currentController.changeScene(Scenes.INFORMATION, Session.getInstance().getUsuario());

        } catch (Exception e) {
            System.out.println("❌ Error al guardar la huella: " + e.getMessage());
        }
    }
}