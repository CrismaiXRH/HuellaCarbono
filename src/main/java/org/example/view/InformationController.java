package org.example.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.App;
import org.example.dao.HabitoDao;
import org.example.dao.HuellaDao;
import org.example.entities.Habito;
import org.example.entities.Huella;
import org.example.entities.Usuario;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InformationController extends Controller implements Initializable {

    @FXML
    TableView<Habito> habitsTable;

    @FXML
    TableColumn<Habito, String> habitNameColumn;

    @FXML
    TableColumn<Habito, String> frecuencyColumn;

    @FXML
    TableColumn<Habito, String> tipeColumn;

    @FXML
    TableColumn<Habito, String> lastdateColumn;

    @FXML
    Button consultarButton;

    @FXML
    Button addHabitButton;

    @FXML
    TableView<Huella> footprintsTable;

    @FXML
    TableColumn<Huella, String> footprintNameColumn;

    @FXML
    TableColumn<Huella, String> footprintDateColumn;

    @FXML
    TableColumn<Huella, String> footprintValueColumn;

    @FXML
    TableColumn<Huella, String> footprintUnitColumn;

    @FXML
    Button addFootprintButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void onOpen(Object input) throws IOException {
        if (input instanceof Usuario) {
            Usuario usuario = (Usuario) input; // ✅ Casting manual
            cargarHabitosDelUsuario(usuario);
            cargarHuellasDelUsuario(usuario);
        }
    }

    @FXML
    public void añadirHabito() throws IOException {
        App.currentController.changeScene(Scenes.HABIT, null);
    }

    @FXML
    public void cargarHabitosDelUsuario(Usuario usuario) {
        HabitoDao habitoDao = new HabitoDao();
        ObservableList<Habito> habitos = FXCollections.observableArrayList(habitoDao.listarHabitosPorUsuario(usuario.getId()));
        System.out.println("Hábitos cargados: " + habitos);

        // Enlazar columnas con atributos de la entidad Habito
        habitNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdActividad().getNombre()));
        frecuencyColumn.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));
        tipeColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        lastdateColumn.setCellValueFactory(new PropertyValueFactory<>("ultimaFecha"));

        // Agregar los hábitos a la tabla
        habitsTable.setItems(habitos);
    }

    @FXML
    public void cargarHuellasDelUsuario(Usuario usuario) {
        HuellaDao huellaDao = new HuellaDao();
        ObservableList<Huella> huellas = FXCollections.observableArrayList(huellaDao.listarHuellasPorUsuario(usuario.getId()));
        System.out.println("Huellas cargadas: " + huellas);

        // Enlazar columnas con atributos de la entidad Huella
        footprintNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdActividad().getNombre()));
        footprintDateColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        footprintValueColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        footprintUnitColumn.setCellValueFactory(new PropertyValueFactory<>("unidad"));

        // Agregar las huellas a la tabla
        footprintsTable.setItems(huellas);
    }

    public void añadirHuella() throws IOException {
        App.currentController.changeScene(Scenes.FOOTPRINT, null);
    }
}