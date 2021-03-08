package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;



public class Main extends Application {
    static File mainDirectory;
    static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Assignment 1");
        primaryStage.setScene(new Scene(root, 700, 600));
        Main.primaryStage = primaryStage;

        primaryStage.show();
    }

    public static void displayDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("./resources/data"));
        mainDirectory = directoryChooser.showDialog(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
