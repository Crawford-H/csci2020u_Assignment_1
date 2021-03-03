package sample;

import java.io.File;
import java.util.*;

public class SpamTraining {
    //variables
    private final HashMap<String, Integer> trainHamFreq  = new HashMap<String, Integer>();//# of ham files containing W_i
    private final HashMap<String, Integer> trainSpamFreq = new HashMap<String, Integer>();//# of spam files containing W_i
    private final HashMap<String, Float>   trainHamProb  = new HashMap<String, Float>();  //Pr(W|H)
    private final HashMap<String, Float>   trainSpamProb = new HashMap<String, Float>();  //Pr(W|S)
    private int numHamFiles  = 0;
    private int numSpamFiles = 0;

    //getters and setters
    public HashMap<String, Float>   getHamProb()  { return this.trainHamProb;  }
    public HashMap<String, Float>   getSpamProb() { return this.trainSpamProb; }
    public HashMap<String, Integer> getHamFreq()  { return this.trainHamFreq;  }
    public HashMap<String, Integer> getSpamFreq() { return this.trainSpamFreq; }
    public int getNumHamFiles()  { return this.numHamFiles;  }
    public int getNumSpamFiles() { return this.numSpamFiles; }

    //read in words from spam and ham
    public void readFiles(ArrayList<File> hamFiles, ArrayList<File> spamFiles) {
        Scanner fileScanner = null;
        String word = "";

        //read ham files
        for (File directory : hamFiles){                //foreach directory
            for (File file : directory.listFiles()) {   //foreach file in directory
                try {
                    fileScanner = new Scanner(file);
                    numHamFiles++;
                    while (fileScanner.hasNext()) {
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
            for (File file : directory.listFiles()) {
                try {
                    fileScanner = new Scanner(file);
                    numSpamFiles++;
                    while (fileScanner.hasNext()) {
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
            trainSpamProb.put(word.getKey(), (float)((float)word.getValue() / (float)numSpamFiles));
    
        //calculate Pr(Wi|H)
        for (Map.Entry<String, Integer> word : trainHamFreq.entrySet()) 
            trainHamProb.put(word.getKey(), (float)((float)word.getValue() / (float)numHamFiles));

        //Pr(S|Wi)
        trainSpamProb.replaceAll((k, v) -> (float) (v / (v + trainHamProb.get(k)))); //use hashmap to hold Pr(S|W) now
    }


    public static void main(String[] args) {
        File ham = new File("resources/data/train/ham");
        File ham2 = new File("resources/data/train/ham2");
        File spam = new File("resources/data/train/spam");

        ArrayList<File> hamFiles = new ArrayList<File>(Arrays.asList(ham, ham2));
        ArrayList<File> spamFiles = new ArrayList<File>(Collections.singletonList(spam));

        SpamTraining train = new SpamTraining();
        train.readFiles(hamFiles, spamFiles);
        train.train();

        System.out.println(train.getSpamProb().values());
        System.out.println(train.getSpamProb().size());
    }
}