package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.Objects;

public class DataSource {
    public static ObservableList<TestFile> getAllFiles() {
        ObservableList<TestFile> files = FXCollections.observableArrayList();
        for (File file : Objects.requireNonNull(Main.mainDirectory.listFiles())) {
            files.add(new TestFile(file.getName(), 0.0, "N/A"));
        }
        return files;
    }
}
