package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import sample.SpamTraining;

public class Main extends Application {
    static File mainDirectory;
    static SpamTraining train;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Assignment 1");
        primaryStage.setScene(new Scene(root, 800, 800));
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("./resources/data"));
        mainDirectory = directoryChooser.showDialog(primaryStage);

        File ham  = new File("resources/data/train/ham" );
        File ham2 = new File("resources/data/train/ham2");
        File spam = new File("resources/data/train/spam");
        ArrayList<File> hamFiles = new ArrayList<File>(Arrays.asList(ham, ham2));
        ArrayList<File> spamFiles = new ArrayList<File>(Collections.singletonList(spam));

        train = new SpamTraining();
        train.readFiles(hamFiles, spamFiles);
        train.train();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
