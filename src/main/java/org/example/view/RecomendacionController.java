package org.example.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.App;
import org.example.entities.Categoria;
import org.example.entities.Habito;
import org.example.entities.Recomendacion;
import org.example.entities.Usuario;
import org.example.services.RecomendacionesServices;
import org.example.session.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class RecomendacionController extends Controller implements Initializable {

    @FXML
    private Button volverButton;

    @FXML
    private TableView<Recomendacion> recomendacionTable;

    @FXML
    private TableColumn<Recomendacion, String> descripcionColumn;

    @FXML
    private TableColumn<Recomendacion, BigDecimal> impactoColumn;

    private final RecomendacionesServices recomendacionesServices = new RecomendacionesServices();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        impactoColumn.setCellValueFactory(new PropertyValueFactory<>("impactoEstimado"));
    }

    @Override
    public void onOpen(Object input) throws IOException {
        if (input instanceof Usuario) {
            Usuario usuario = (Usuario) input;
            cargarRecomendacionesDelUsuario(usuario);
        }
    }

    private void cargarRecomendacionesDelUsuario(Usuario usuario) {
        try (org.hibernate.Session session = org.example.session.Connection.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();

            List<Habito> habitos = session.createQuery(
                    "SELECT h FROM Habito h JOIN FETCH h.idActividad a JOIN FETCH a.idCategoria WHERE h.idUsuario.id = :userId",
                    Habito.class
            ).setParameter("userId", usuario.getId()).getResultList();

            Set<Categoria> categorias = new HashSet<>();
            for (Habito habito : habitos) {
                categorias.add(habito.getIdActividad().getIdCategoria());
            }

            transaction.commit();

            List<Recomendacion> recomendaciones = recomendacionesServices.listarRecomendacionesPorCategorias(List.copyOf(categorias));

            recomendacionTable.getItems().setAll(recomendaciones);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void volver() throws IOException {
        App.currentController.changeScene(Scenes.INFORMATION, Session.getInstance().getUsuario());
    }
}