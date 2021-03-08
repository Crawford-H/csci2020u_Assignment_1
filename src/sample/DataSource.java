package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

import java.io.IOException;
import java.util.*;

public class DataSource {
    public static ObservableList<TestFile> getAllFiles() throws IOException {
        File ham  = new File(Main.mainDirectory.getCanonicalPath() + "/train/ham" );
        File ham2 = new File(Main.mainDirectory.getCanonicalPath() + "/train/ham2");
        File spam = new File(Main.mainDirectory.getCanonicalPath() + "/train/spam");
        ArrayList<File> hamFiles = new ArrayList<>(Arrays.asList(ham, ham2));
        ArrayList<File> spamFiles = new ArrayList<>(Collections.singletonList(spam));
        SpamTraining train;
        train = new SpamTraining();
        train.readFiles(hamFiles, spamFiles);
        train.train();

        ObservableList<TestFile> files = FXCollections.observableArrayList();
        ArrayList<File> testFiles = new ArrayList<>(Arrays.asList(
                new File(Main.mainDirectory.getCanonicalPath() + "/test/ham" ),
                new File(Main.mainDirectory.getCanonicalPath() + "/test/spam")));
        Scanner fileScanner;
        for (File directory : testFiles) {
            String actualClass = (directory.getName().charAt(0) == 's') ? "spam" : "ham";

            for (File file : Objects.requireNonNull(directory.listFiles())) {
                double n = 0;
                fileScanner = new Scanner(file);
                while (fileScanner.hasNext()) {
                    String word = fileScanner.next();
                    if (train.getSpamProb(word) != null) {
                        if (train.getSpamProb(word) != 0) {
                            float prob = train.getSpamProb(word);
                            n += Math.log(1.0 - prob) - Math.log(prob);
                        }
                    }
                }
                double spamProbability = 1.0 / (1.0 + Math.pow(Math.E, n));
                files.add(new TestFile(file.getName(), spamProbability, actualClass));
            }
        }
        return files;
    }

    //calculate accuracy of spam detector
    public static float getAccuracy() throws IOException {
        float accuracy;
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

    //calculate precision of spam detector
    public static float getPrecision() throws IOException {
        float precision;
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
