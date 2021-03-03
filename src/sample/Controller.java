package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.scene.control.Button;
import java.awt.event.ActionEvent;

public class Controller {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    @FXML
    private TableView<TestFile> tableView;
    @FXML
    public Button directory;
    @FXML
    private TableColumn FileNameColumn;
    @FXML
    private TableColumn ActualClassColumn;
    @FXML
    private TableColumn SpamProbabilityColumn;

    private TableView<TestFile> file;


    public void display() {
        FileNameColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));
        ActualClassColumn.setCellValueFactory(new PropertyValueFactory<>("actualClass"));
        SpamProbabilityColumn.setCellValueFactory(new PropertyValueFactory<>("spamProbability"));
        tableView.setItems(DataSource.getAllFiles());
    }
    public void handle(ActionEvent actionEvent) {

    }

}
