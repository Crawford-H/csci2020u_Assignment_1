package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class DataSource {
    public static ObservableList<TestFile> getAllFiles() throws FileNotFoundException {
        ObservableList<TestFile> files = FXCollections.observableArrayList();
        double n = 0;
        Scanner fileScanner;
        String actualClass = (Main.mainDirectory.getName().charAt(0) == 's') ? "spam" : "ham";

        for (File file : Objects.requireNonNull(Main.mainDirectory.listFiles())) {
            n = 0;
            fileScanner = new Scanner(file);
            while (fileScanner.hasNext()) {
                String word = fileScanner.next();
                if (Main.train.getSpamProb(word) != null) {
                    if (Main.train.getSpamProb(word) != 0) {
                        float prob = Main.train.getSpamProb(word);
                        n += Math.log(1.0 - prob) - Math.log(prob);
                    }
                }
            }
            double spamProbability = 1.0 / (1.0 + Math.pow(Math.E, n));
            files.add(new TestFile(file.getName(), spamProbability, actualClass));
        }
        return files;
    }


    public static float getAccuracy() throws FileNotFoundException {
        float accuracy = 0.0f;
        int numCorrectGuesses = 0;
        for (TestFile file : getAllFiles()) {
            if (file.getActualClass().equals("ham") && file.getSpamProbability() < 0.5f)
                numCorrectGuesses++;
            else if (file.getActualClass().equals("spam") && file.getSpamProbability() >= 0.5f)
                numCorrectGuesses++;
        }
        accuracy = (float)numCorrectGuesses / (float)getAllFiles().size();
        return accuracy;
    }


    public static float getPrecision() throws FileNotFoundException {
        float precision = 0.0f;
        int numFalsePositives = 0;
        int numTruePositives = 0;
        for (TestFile file : getAllFiles()) {
            if (file.getActualClass().equals("spam") && file.getSpamProbability() >= 0.5) {
                numTruePositives++;
            }
            else if (file.getActualClass().equals("ham") && file.getSpamProbability() >= 0.5) {
                numFalsePositives ++;
            }
        }
        precision = (float)numTruePositives / (float)(numFalsePositives + numTruePositives);
        return precision;
    }
}
