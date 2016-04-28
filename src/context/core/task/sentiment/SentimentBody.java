package context.core.task.sentiment;

/**
 * Revised by Ming Jiang Now can be used for N-grams;
 *
 */
import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.task.pos.POSTagger;
import context.core.util.CorpusAggregator;
import context.core.util.JavaIO;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Aale
 */
public class SentimentBody {

    private SentimentTaskInstance instance;
    private CorpusData input;
    private List<TabularData> tabularOutput;

    private FileData sentimentFile;
    private String log;
    private ArrayList<Formatter> allOuts;
    private List<String> ToCSV;// output for html parsing of sentiment
    private List<String> ToHumanCSV;// output for human readability to be displayed in ConText
    private StanfordCoreNLP pipeline;
    String SentimentString;

    /**
     *
     * @param instance
     */
    public SentimentBody(SentimentTaskInstance instance) {
        // TODO Auto-generated method stub
        this.instance = instance; // String path to a file containing the list of sentiment
        init();

    }

    private void init() {

        this.input = (CorpusData) instance.getInput();
        this.tabularOutput = instance.getTabularOutput();
        this.pipeline = instance.getPipeline();
        this.sentimentFile = instance.getSentimentFile();

    }

    /**
     *
     * @return @throws IOException
     */
    public boolean RunSentimentAnalysis() throws IOException {

        SentimentString = sentimentFile.readFileIntoString();
        SentimentString = SentimentString.toLowerCase();

        //read csv file (Sentiment_Dictionary)
        String[] SentimentWords = SentimentString.split("\n");

        HashMap sentiMap1 = new HashMap();
        Map<String, String[]> sentiMap_nwords = new HashMap<String, String[]>();
        Map<String, String[]> sentiMap_swords = new HashMap<String, String[]>();
        List<String> Ngrams = new ArrayList<String>();

        for (String line : SentimentWords) {
            if (line.contains("Word,POS")) {
                continue;
            }
            String[] line_temp = line.split(",");

            String line_word = line_temp[0];
            String line_pol = line_temp[3];

            String line_pos = "NA";
            String line_subj = "NA";

            if (!line_temp[1].matches("")) {
                line_pos = line_temp[1];
            }

            if (line_temp.length == 5 && !line_temp[4].matches("")) {
                line_subj = line_temp[4];
            }

            String[] line_val = {line_pol, line_subj};
            if (!line_pos.matches("NA")) {
                String[] line_key = {line_word, line_pos};
                SentimentObj SObj = new SentimentObj(line_key.clone());
                sentiMap1.put(SObj, line_val);
            } else if (line_word.contains(" ")) {
                String temp = line_word.replaceAll(" ", "_");
                sentiMap_nwords.put(temp, line_val);  //n-grams without completed info
                Ngrams.add(line_word);
            } else {
                sentiMap_swords.put(line_word, line_val);  //single-grams without completed info
            }

            //SentimentObj SObj = new SentimentObj(line_key.clone());
        }// using a hashmap to store the sentiment values of the words for faster access

        //String lenOfSentWord = (SentimentWords[0].split("word1=")[1].split(" ")[0]);
        ToCSV = new ArrayList<String>();
        ToHumanCSV = new ArrayList<String>();
        List<List<String[]>> toAggregate = new ArrayList<List<String[]>>();

        List<FileData> files = input.getFiles();
        try {
            for (FileData docData : files) {
                File documentFile = docData.getFile();
                List<String[]> SentimentTags = new ArrayList<String[]>();
                String documentString = null;
                try {
                    documentString = JavaIO.readFile(documentFile);

                    documentString = documentString.toLowerCase();

                    for (int j = 0; j < Ngrams.size(); j++) {
                        if (documentString.contains(Ngrams.get(j))) {
                            //Label = "T";
                            //Find_P.add(Ngrams.get(j).split(" "));
                            String temp = Ngrams.get(j).replaceAll(" ", "_");
                            documentString = documentString.replaceAll(Ngrams.get(j), temp);
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }

                Annotation document = new Annotation(documentString);

                pipeline.annotate(document);

                List<CoreMap> sentences = document.get(SentencesAnnotation.class);
                List<String[]> docWords = new ArrayList<String[]>();
                String[] wordPOS = new String[3];

                for (CoreMap sentence : sentences) {
                    // traversing the words in the current sentence
                    // a CoreLabel is a CoreMap with additional token-specific methods

                    final List<CoreLabel> sent = sentence.get(TokensAnnotation.class);
                    final List<TaggedWord> taggedWords = POSTagger.tag(sent, "en");
                    for (TaggedWord token : taggedWords) {
                        // this is the text of the token
                        String word = token.word();
                        // this is the POS tag of the token

                        if (word.matches("\\W")) {
                            if (!word.matches("[0-9a-zA-Z.;:\'\"]*")) {
                                continue;
                            }
                        }

                        if (word.matches("[;.\'\"]")) {
                            continue;
                        }

                        // this is the POS tag of the token
                        String pos = token.tag();

                        wordPOS[0] = word;
                        if (pos.contains("JJ")) {
                            wordPOS[1] = "adj";
                        } else if (pos.contains("NN")) {
                            wordPOS[1] = "noun";
                        } else if (pos.contains("VB")) {
                            wordPOS[1] = "verb";
                        } else if (pos.contains("RB")) {
                            wordPOS[1] = "adverb";
                        } else {
                            wordPOS[1] = "anypos";
                        }
                        wordPOS[2] = pos;
                        docWords.add(wordPOS.clone());

                    }
                }

                // documentString = documentString.replaceAll(",","&#44;");
                ArrayList<String[]> outputList = new ArrayList<String[]>();
                for (String[] word_and_pos : docWords) {
                    String[] word_temp = {word_and_pos[0], word_and_pos[1]};
                    SentimentObj word_pos_obj = new SentimentObj(word_temp);

                    //System.out.println (word_temp[0]);
                    if (sentiMap1.containsKey(word_pos_obj)) {
                        //System.out.println (word_temp[0]);
                        String polarity = ((String[]) sentiMap1.get(word_pos_obj))[0];
                        String color = "000000";
                        if (polarity.equals("positive")) {
                            color = "(0,230,0)";
                        }
                        if (polarity.equals("neutral")) {
                            color = "(0,0,230)";
                        }
                        if (polarity.equals("negative")) {
                            color = "(230,0,0)";
                        }
                        String[] sentiTag = {word_and_pos[0], word_and_pos[2], polarity, Integer.toString(1)};
                        SentimentTags.add(sentiTag.clone());
                        //System.out.println(SentimentTags.size());
                        String[] tempArray = {"<span style=\"color:rgb" + color + "\">" + String.valueOf(word_and_pos[0]) + " </span>", color};
                        outputList.add(tempArray);

                    } else if (sentiMap_swords.containsKey(word_temp[0])) {
                        //System.out.println (word_temp[0]);
                        String polarity = ((String[]) sentiMap_swords.get(word_temp[0]))[0];
                        String color = "000000";
                        if (polarity.equals("positive")) {
                            color = "(0,230,0)";
                        }
                        if (polarity.equals("neutral")) {
                            color = "(0,0,230)";
                        }
                        if (polarity.equals("negative")) {
                            color = "(230,0,0)";
                        }
                        String[] sentiTag = {word_and_pos[0], word_and_pos[2], polarity, Integer.toString(1)};
                        //System.out.println(SentimentTags.size());

                        SentimentTags.add(sentiTag.clone());
                        String[] tempArray = {"<span style=\"color:rgb" + color + "\">" + String.valueOf(word_and_pos[0]) + " </span>", color};
                        outputList.add(tempArray);

                        //System.out.println(SentimentTags.size());
                    } else if (sentiMap_nwords.containsKey(word_temp[0])) {
                        //System.out.println (word_temp[0]);
                        String polarity = ((String[]) sentiMap_nwords.get(word_temp[0]))[0];
                        String color = "000000";
                        if (polarity.equals("positive")) {
                            color = "(0,230,0)";
                        }
                        if (polarity.equals("neutral")) {
                            color = "(0,0,230)";
                        }
                        if (polarity.equals("negative")) {
                            color = "(230,0,0)";
                        }
                        String phrase = word_and_pos[0].replaceAll("_", " ");
                        String[] sentiTag = {phrase, word_and_pos[2], polarity, Integer.toString(1)};
                        //System.out.println(SentimentTags.size());

                        SentimentTags.add(sentiTag.clone());
                        String[] tempArray = {"<span style=\"color:rgb" + color + "\">" + String.valueOf(phrase) + " </span>", color};
                        outputList.add(tempArray);

                        //System.out.println(SentimentTags.size());
                    } else {
                        String[] tempArray = {word_and_pos[0], null};
                        outputList.add(tempArray);
                    }
                }

                if (SentimentTags.size() > 0) {
                    //System.out.println (SentimentTags.size());
                    toAggregate.add(SentimentTags);// Adding the list of words from text with sentiment
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        List<String[]> ToHumanCSVArray = new CorpusAggregator().CorpusAggregate(toAggregate); // collate the list of words/POSs with sentiment so that
        // each word-POS pair has the proper count.
        ToHumanCSV.add("Term,Part Of Speech,Sentiment,Frequency");
        for (String[] HumanArray : ToHumanCSVArray) {
            String HumanTemp = HumanArray[0] + "," + HumanArray[1] + "," + HumanArray[2] + "," + HumanArray[3];
            //System.out.println (HumanTemp);
            ToHumanCSV.add(HumanTemp);
        }

        return true;
    }

    /**
     *
     * @return
     */
    public List<String> GetCSV() {
        return ToCSV;
    }

    /**
     *
     * @return
     */
    public List<String> GetHumanCSV() {
        return ToHumanCSV;
    }

    /**
     *
     * @param filepath
     */
    public void writeOutput(String filepath) {
        this.writeCsv(GetHumanCSV(), filepath);
    }

    private void writeCsv(List<String> ToHumanCSV, String filepath) {
        StringBuffer sb = new StringBuffer();
        String toWrite = "";
        for (int i1 = 0; i1 < ToHumanCSV.size(); i1++) {
            toWrite = ToHumanCSV.get(i1) + "\n";
            sb.append(toWrite);
        }
        
        // 2016.03 Add this code to delete existing file
        File toDelete = new File(filepath);
        	if (toDelete.exists()) {
        		toDelete.delete(); 
        	}
        //
        
        FileData.writeDataIntoFile(sb.toString(), filepath);
    }

}
