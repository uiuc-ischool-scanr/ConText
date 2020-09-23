package context.core.task.bigram;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.util.CodebookUtils;
import context.core.util.DefaultValueHashMap;
import context.core.util.ForAggregation;
import context.core.util.JavaIO;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Aale
 */
public class BigramBody {

    private BigramApplicationTaskInstance instance;
    private CorpusData input;
    /*private Boolean drop_num;
    private Boolean drop_pun;
    private Boolean keep_pou;*/
    private DefaultValueHashMap baseWords;
    private DefaultValueHashMap bigrams;
    private int totalBigrams;

    /**
     *
     * @param instance
     */
    public BigramBody(BigramApplicationTaskInstance instance) {
        // TODO Auto-generated method stub
        this.instance = instance;
        init();
    }

    private void init() {

        this.input = (CorpusData) instance.getInput();
        /*this.drop_num = instance.isDropnum();
        this.drop_pun = instance.isDroppun();
        this.keep_pou = instance.isKeeppou();*/
        int[] defaultVal = {0, 0};
        totalBigrams = 0;
        baseWords = new DefaultValueHashMap(defaultVal);
        bigrams = new DefaultValueHashMap(0);
    }

    /**
     *
     * @return
     */
    public boolean getMutualInfo() {
        List<FileData> files = input.getFiles();
        try {
            for (FileData ff : files) {
                /*
                Niko add initialization for temporary vars
                 */
                int tempTotalBigrams = 0;
                int[] defaultVal = {0, 0};
                DefaultValueHashMap tempBaseWords = new DefaultValueHashMap(defaultVal);
                DefaultValueHashMap tempBigrams = new DefaultValueHashMap(0);

                /*
                End addition
                 */
                File file = ff.getFile();
                String fullText = JavaIO.readFile(file);
                List<ArrayList<String>> sentences = new ArrayList<ArrayList<String>>();
                ArrayList<String> tempSent = new ArrayList<String>();
                for (String text : CodebookUtils.make_sentences(fullText)) {
                    if (text == null) {
                        sentences.add((ArrayList<String>) tempSent.clone());
                        tempSent.clear();
                        continue;
                    }
                    tempSent.add(text);
                }

                for (ArrayList<String> listSentence : sentences) {
                    String sentence = "";
                    for (String s : listSentence) {
                        sentence += s + " ";
                    }
                    sentence = sentence.replaceAll("[^A-Za-z0-9_@#\\w ]", "");
                    /*if (drop_num) {
                        
                        sentence = sentence.replaceAll("[0-9]", "");
                    }
                    if (drop_pun) {
                        if (keep_pou) {
                            sentence = sentence.replaceAll("[\\p{P}&&[^#]]+", "");
                        } else {
                            sentence = sentence.replaceAll("\\p{P}", "");
                        }
                    }*/
                    sentence = sentence.replaceAll("LRB", "");
                    sentence = sentence.replaceAll("RRB", "");
                    String temp = "";
                    for (String word : sentence.split("\\s+")) {
                        if (temp.equals("")) {
                            temp = word;
                            continue;
                        }
                        totalBigrams++;
                        String[] keyStrings = {temp, word};
                        ForAggregation key = new ForAggregation(keyStrings);
                        bigrams.put(key, ((int) bigrams.get(key)) + 1);
                        int[] baseValTemp = {0, 0};
                        baseValTemp = ((int[]) baseWords.get(temp)).clone();
                        baseValTemp[0]++;
                        baseWords.put(temp, baseValTemp.clone());
                        int[] baseValWord = {0, 0};
                        baseValWord = ((int[]) baseWords.get(word)).clone();
                        baseValWord[1]++;
                        baseWords.put(word, baseValWord.clone());
                        temp = word;
                    }

                    /*
                    Niko add handler for one file
                     */
                    for (String word : sentence.split("\\s+")) {
                        if (temp.equals("")) {
                            temp = word;
                            continue;
                        }
                        tempTotalBigrams++;
                        String[] keyStrings = {temp, word};
                        ForAggregation key = new ForAggregation(keyStrings);
                        tempBigrams.put(key, ((int) tempBigrams.get(key)) + 1);
                        int[] baseValTemp = {0, 0};
                        baseValTemp = ((int[]) tempBaseWords.get(temp)).clone();
                        baseValTemp[0]++;
                        tempBaseWords.put(temp, baseValTemp.clone());
                        int[] baseValWord = {0, 0};
                        baseValWord = ((int[]) tempBaseWords.get(word)).clone();
                        baseValWord[1]++;
                        tempBaseWords.put(word, baseValWord.clone());
                        temp = word;
                    }
                    /*
                    End addition
                     */
                }
                /*
                    Niko
                    add handler for writing pos result files
                 */
                String inputNameWithoutExtension = FilenameUtils.getBaseName(ff.getFile().getName());
                String inputExtension = FilenameUtils.getExtension(ff.getFile().getName());
                String outputFile = inputNameWithoutExtension + "-BIGRAM." + inputExtension;
                CorpusData output = (CorpusData) instance.getTextOutput();
                int index = output.addFile(outputFile);
                writeCsv(tempBaseWords, tempBigrams, tempTotalBigrams, output.getFiles().get(index).getPath().get());
                /*
                    End addition
                 */

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * @param filepath
     */
    public void writeOutput(String filepath) {
        //Write CSV
        this.writeCsv(baseWords, bigrams, totalBigrams, filepath);
    }

    private void writeCsv(DefaultValueHashMap baseWords, DefaultValueHashMap bigrams, int totalBigrams, String filepath) {
        StringBuffer sb = new StringBuffer();

        //sb.append("Bigram,Frequency,Mutual Information\n");
        //Separate bigramstring to Word a & Word b -- by Ming Jiang
        sb.append("Word a,Word b,Frequency,Mutual Information\n");
        String toWrite = "";
        for (Object bigram : bigrams.keySet().toArray()) {
            String[] bigramStr = ((ForAggregation) bigram).toAggregate;
            try {
                int checker = ((int[]) baseWords.get("brave"))[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
            double check = (double) ((int[]) baseWords.get(bigramStr[0]))[0];
            double probX = ((double) ((int[]) baseWords.get(bigramStr[0]))[0]) / (double) totalBigrams;
            double probY = ((double) ((int[]) baseWords.get(bigramStr[1]))[1]) / (double) totalBigrams;
            double probXY = ((double) ((int) bigrams.get(bigram))) / ((double) totalBigrams);
            double mutualInfo = (double) (probXY * Math.log(probXY / (probX * probY)) / Math.log(2));
            //Separate bigramstring to Word a & Word b -- by Ming Jiang
            toWrite = bigramStr[0] + "," + bigramStr[1] + "," + Integer.toString((int) bigrams.get(bigram)) + "," + Double.toString(mutualInfo) + "\n";
            sb.append(toWrite);
        }
        //	        System.out.println("size of string to write=" + sb.toString().length());

        // 2016.03 Add this code to delete existing file
        File toDelete = new File(filepath);
        if (toDelete.exists()) {
            toDelete.delete();
        }

        FileData.writeDataIntoFile(sb.toString(), filepath);
    }

}
