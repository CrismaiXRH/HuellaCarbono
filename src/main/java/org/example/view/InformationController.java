package org.example.view;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.example.App;
import org.example.entities.Actividad;
import org.example.entities.Habito;
import org.example.entities.Huella;
import org.example.entities.Usuario;
import org.example.services.HabitoServices;
import org.example.services.HuellaServices;
import org.example.session.Session;
import org.hibernate.Hibernate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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
    TableColumn<Habito, Void> deleteHabitoColumn;

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
    TableColumn<Huella, Void> deleteHuellaColumn;

    @FXML
    Button addFootprintButton;

    @FXML
    ComboBox<String> footprintComboBox;

    @FXML
    LineChart<String, Number> footprintChart;

    @FXML
    ComboBox<String> periodComboBox;

    @FXML
    Button exportButton;

    private final HuellaServices huellaServices = new HuellaServices();
    private final HabitoServices habitoServices = new HabitoServices();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        periodComboBox.getItems().addAll("Diario", "Semanal", "Mensual");
        periodComboBox.setOnAction(event -> actualizarGraficoImpacto());
        addDeleteButtonToHabitsTable();
        addDeleteButtonToFootprintsTable();
    }

    @Override
    public void onOpen(Object input) throws IOException {
        if (input instanceof Usuario) {
            Usuario usuario = (Usuario) input;
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
        ObservableList<Habito> habitos = FXCollections.observableArrayList(habitoServices.listarHabitosPorUsuario(usuario.getId()));
        System.out.println("Hábitos cargados: " + habitos);

        habitNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdActividad().getNombre()));
        frecuencyColumn.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));
        tipeColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        lastdateColumn.setCellValueFactory(new PropertyValueFactory<>("ultimaFecha"));

        habitsTable.setItems(habitos);
    }

    @FXML
    public void cargarHuellasDelUsuario(Usuario usuario) {
        ObservableList<Huella> huellas = FXCollections.observableArrayList(huellaServices.obtenerHuellasDelUsuarioConActividad(usuario.getId()));
        System.out.println("Huellas cargadas: " + huellas);

        for (Huella huella : huellas) {
            Actividad actividad = huella.getIdActividad();
            if (actividad != null) {
                Hibernate.initialize(actividad.getIdCategoria());
            }
        }

        footprintNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdActividad().getNombre()));
        footprintDateColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        footprintValueColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        footprintUnitColumn.setCellValueFactory(new PropertyValueFactory<>("unidad"));

        footprintsTable.setItems(huellas);
    }

    @FXML
    public void actualizarGraficoImpacto() {
        String periodoSeleccionado = periodComboBox.getValue();
        LocalDate hoy = LocalDate.now();
        LocalDate inicio = null;
        LocalDate fin = hoy;

        switch (periodoSeleccionado) {
            case "Diario":
                inicio = hoy.minusDays(1);
                break;
            case "Semanal":
                inicio = hoy.minusWeeks(1);
                break;
            case "Mensual":
                inicio = hoy.minusMonths(1);
                break;
        }

        List<Huella> huellas = huellaServices.obtenerHuellasPorPeriodo(inicio, fin);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Impacto Ambiental");

        for (Huella huella : huellas) {
            BigDecimal impacto = calcularImpactoHuella(huella);
            System.out.println("Fecha: " + huella.getFecha() + ", Impacto: " + impacto);
            series.getData().add(new XYChart.Data<>(huella.getFecha().toString(), impacto));
        }

        footprintChart.getData().clear();
        footprintChart.getData().add(series);
    }

    private BigDecimal calcularImpactoHuella(Huella huella) {
        BigDecimal valorHuella = huella.getValor();
        BigDecimal factorEmision = huella.getIdActividad().getIdCategoria().getFactorEmision();
        return valorHuella.multiply(factorEmision);
    }

    public void añadirHuella() throws IOException {
        App.currentController.changeScene(Scenes.FOOTPRINT, null);
    }

    public void verRecomendaciones() throws IOException {
        App.currentController.changeScene(Scenes.RECOMENDATION, Session.getInstance().getUsuario());
    }

    @FXML
    public void generarReportePDF() {
        Usuario usuario = Session.getInstance().getUsuario();
        List<Huella> huellas = huellaServices.obtenerHuellasDelUsuarioConActividad(usuario.getId());

        String dest = "reporte_huella_carbono.pdf";
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();

            document.add(new Paragraph("Reporte de Huella de Carbono"));
            document.add(new Paragraph("Usuario: " + usuario.getNombre()));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Fecha");
            table.addCell("Valor");
            table.addCell("Actividad");
            table.addCell("Recomendaciones");

            for (Huella huella : huellas) {
                table.addCell(huella.getFecha().toString());
                table.addCell(huella.getValor().toString());
                table.addCell(huella.getIdActividad().getNombre());

                PdfPCell cell = new PdfPCell(new Paragraph("Recomendaciones..."));
                cell.setNoWrap(false);
                cell.setFixedHeight(60f);
                cell.setVerticalAlignment(Element.ALIGN_TOP);
                cell.setPadding(5f);

                Paragraph paragraph = new Paragraph("Recomendaciones sobre cómo reducir la huella de carbono, por ejemplo, usar transporte público o comer productos orgánicos.");
                paragraph.setLeading(10f);
                paragraph.setAlignment(Element.ALIGN_LEFT);

                cell.addElement(paragraph);
                table.addCell(cell);
            }

            document.add(table);
            document.close();
            System.out.println("Reporte PDF generado en: " + dest);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addDeleteButtonToHabitsTable() {
        Callback<TableColumn<Habito, Void>, TableCell<Habito, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Habito, Void> call(final TableColumn<Habito, Void> param) {
                final TableCell<Habito, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Habito habito = getTableView().getItems().get(getIndex());
                            habitoServices.eliminarHabito(habito);
                            habitsTable.getItems().remove(habito);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        deleteHabitoColumn.setCellFactory(cellFactory);
    }

    private void addDeleteButtonToFootprintsTable() {
        Callback<TableColumn<Huella, Void>, TableCell<Huella, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Huella, Void> call(final TableColumn<Huella, Void> param) {
                final TableCell<Huella, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Huella huella = getTableView().getItems().get(getIndex());
                            huellaServices.eliminarHuella(huella);
                            footprintsTable.getItems().remove(huella);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        deleteHuellaColumn.setCellFactory(cellFactory);
    }
}