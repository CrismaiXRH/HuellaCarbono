package org.example.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class informationController extends Controller implements Initializable {

    @FXML
    TableView habitsTable;

    @FXML
    TableColumn frecuencyColumn;

    @FXML
    TableColumn tipeColumn;

    @FXML
    TableColumn lastdateColumn;

    @FXML
    Button consultarButton;

    @FXML
    Button addHabitButton;

    @FXML
    Button logoutButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void onOpen(Object input) throws IOException {

    }




}
