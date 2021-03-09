package sample;

import java.io.File;
import java.util.*;

public class SpamTraining {
                                                                                //variables
    private final HashMap<String, Integer> trainHamFreq  = new HashMap<>();     //# of ham files containing W_i
    private final HashMap<String, Integer> trainSpamFreq = new HashMap<>();     //# of spam files containing W_i
    private final HashMap<String, Float>   trainHamProb  = new HashMap<>();     //Pr(W|H)
    private final HashMap<String, Float>   trainSpamProb = new HashMap<>();     //Pr(W|S) and Pr(S|W)
    private int numHamFiles  = 0;
    private int numSpamFiles = 0;
                                                                                //getters and setters
    public HashMap<String, Float>   getHamProb()  { return this.trainHamProb;  }
    public HashMap<String, Float>   getSpamProb() { return this.trainSpamProb; }
    public HashMap<String, Integer> getHamFreq()  { return this.trainHamFreq;  }
    public HashMap<String, Integer> getSpamFreq() { return this.trainSpamFreq; }
    public Float getSpamProb(String key) { return this.trainSpamProb.get(key); }
    public int getNumHamFiles()  { return this.numHamFiles;  }
    public int getNumSpamFiles() { return this.numSpamFiles; }

                                                                                //read in words from spam and ham
    public void readFiles(ArrayList<File> hamFiles, ArrayList<File> spamFiles) {
        Scanner fileScanner;
        String word;
                                                                                //read ham files
        for (File directory : hamFiles) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {   //foreach file in directory
                try {
                    fileScanner = new Scanner(file);
                    numHamFiles++;
                    while (fileScanner.hasNext()) {                             //add each word to map and count occurrences
                        word = fileScanner.next();
                        if (!trainHamFreq.containsKey(word))
                            trainHamFreq.put(word, 1);
                        else
                            trainHamFreq.replace(word, trainHamFreq.get(word) + 1);
                        trainSpamFreq.putIfAbsent(word, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
                                                                                //read spam files
        for (File directory : spamFiles) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                try {
                    fileScanner = new Scanner(file);
                    numSpamFiles++;
                    while (fileScanner.hasNext()) {                             //add each word to map and count occurrences
                        word = fileScanner.next();
                        if (!trainSpamFreq.containsKey(word))
                            trainSpamFreq.put(word, 1);
                        else
                            trainSpamFreq.replace(word, trainSpamFreq.get(word) + 1);
                        trainHamFreq.putIfAbsent(word, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
        }
    }

                                                                                //find probability of spam
    public void train() {
                                                                                //calculate Pr(Wi|S)
        for (Map.Entry<String, Integer> word : trainSpamFreq.entrySet()) 
            trainSpamProb.put(word.getKey(), ((float)word.getValue() / (float)numSpamFiles));
                                                                                //calculate Pr(Wi|H)
        for (Map.Entry<String, Integer> word : trainHamFreq.entrySet()) 
            trainHamProb.put(word.getKey(), ((float)word.getValue() / (float)numHamFiles));
                                                                                //Pr(S|Wi)
        trainSpamProb.replaceAll((k, v) -> (v / (v + trainHamProb.get(k))));    //use hashmap to hold Pr(S|W) now

                                                                                //correct probability for rare words to improve accuracy and precision:
        trainSpamProb.replaceAll((k, v) ->                                      //Pr'(S|W) = (s*Pr(S) + n*Pr(S|W)) / (s + n)
            ((3.0f * 0.5f + trainSpamFreq.get(k) * v) / (3.0f + trainSpamFreq.get(k)))
        );
    }
}