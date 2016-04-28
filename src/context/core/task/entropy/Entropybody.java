package context.core.task.entropy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.task.pos.POSTagger;
import context.core.util.CorpusAggregator;
import context.core.util.ForAggregationNoCase;
import context.core.util.JavaIO;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 *
 * @author Aale
 */
public class Entropybody {

    /**
     * @Author Ming Jiang
     */
//    StanfordCoreNLP pipeline;
//    
//    private List<String[]> corpusStatsWithTFIDF;
//    private File stopFile;
    Map<String, Integer> file_length = new HashMap<String, Integer>();

    private EntropyTaskInstance instance;
    private CorpusData input;
    private List<TabularData> tabularOutput;

    private StanfordCoreNLP pipeline;
    private List<String[]> corpusStatsWithTFIDF;
    Map<String, String[]> Files_entropy;

    /**
     *
     * @param instance
     */
    public Entropybody(EntropyTaskInstance instance) {
        // TODO Auto-generated method stub

        this.instance = instance;
        init();

    }

    private void init() {

        this.input = (CorpusData) instance.getInput();
        this.tabularOutput = instance.getTabularOutput();
        this.pipeline = instance.getPipeline();
    }

    /**
     *
     * @return
     */
    public boolean RunEntropyComputation(/*String folderpath*/) {
        // TODO Auto-generated method stub

        List<List<String[]>> toAggregate = new ArrayList<List<String[]>>();
        int numTerms = 0;

//        File folder = new File(folderpath);
//		File[] listOfFiles = folder.listFiles();
        List<FileData> files = input.getFiles();
        try {

//            for (int i = 0; i < listOfFiles.length; i++) {
//            	File file = listOfFiles[i];
            for (FileData ff : files) {

                File file = ff.getFile();
                int doc_length = 0;
                String text;
                List<String[]> CorpusStatTags = new ArrayList<String[]>();
                try {
                    text = JavaIO.readFile(file);
                    Annotation document = new Annotation(text);
                    pipeline.annotate(document);

                    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

                    for (CoreMap sentence : sentences) {
                        // traversing the words in the current sentence
                        // a CoreLabel is a CoreMap with additional token-specific methods

                        final List<CoreLabel> sent = sentence.get(TokensAnnotation.class);
                        final List<TaggedWord> taggedWords = POSTagger.tag(sent, "en");
                        for (TaggedWord token : taggedWords) {
                            // this is the text of the token
                            String word = token.word();
                            // this is the POS tag of the token
                            String pos = token.tag();
                            boolean val = pos.contains("NN");
                            if (!val) {
                                if (word == "The") {
                                    System.out.println("Que?");
                                }
//                            word = word.toLowerCase();

                            }

                            word = word.toLowerCase();

                            String[] entity = {word, pos, file.getName(), Integer.toString(1)};
                            if ("en".equals("en")) {
                                if (!word.matches("[a-zA-Z0-9]*")) {
                                    continue;
                                }
                            }
                            CorpusStatTags.add(entity);
                            doc_length++;
                            numTerms++;
                        }
                    }
                    toAggregate.add(CorpusStatTags);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                file_length.put(file.getName(), doc_length);
            }

            List<String[]> CorpusStatsByFile = new CorpusAggregator().CorpusAggregateNoCase(toAggregate);
            HashMap<ForAggregationNoCase, Integer[]> CorpusStats = new HashMap<ForAggregationNoCase, Integer[]>();
            for (String[] CorpusStatByFile : CorpusStatsByFile) {

                String[] identifierForRatio = {CorpusStatByFile[0]};

                ForAggregationNoCase identifierForAggregate = new ForAggregationNoCase(identifierForRatio);
                if (!(null == CorpusStats.get(identifierForAggregate))) {
                    Integer currNumDocs = ((Integer[]) CorpusStats.get(identifierForAggregate))[0];
                    Integer[] NumDocsAndTotalCount = {currNumDocs + 1, ((Integer[]) CorpusStats.get(identifierForAggregate))[1] + Integer.parseInt(CorpusStatByFile[3])};
                    CorpusStats.put(identifierForAggregate, NumDocsAndTotalCount.clone());
                } else {
                    Integer[] NumDocsAndTotalCount = {1, Integer.parseInt(CorpusStatByFile[3])};
                    CorpusStats.put(identifierForAggregate, NumDocsAndTotalCount);
                }
            }
            List<String[]> corpusStatsWithTFIDFList = new ArrayList<String[]>();
            Iterator<ForAggregationNoCase> it = CorpusStats.keySet().iterator();
            while (it.hasNext()) {
                String[] currentCorpusStatWithTFIDF = new String[3];
                ForAggregationNoCase next = it.next();

                currentCorpusStatWithTFIDF[0] = next.toAggregate[0];

                currentCorpusStatWithTFIDF[1] = Integer.toString(CorpusStats.get(next)[1]);   //tf

                float tf = Float.parseFloat(currentCorpusStatWithTFIDF[1]) / (Float.MIN_VALUE + numTerms);
                //               float idf = (float) Math.log10(((float) listOfFiles.length - 1) / (Float.MIN_VALUE + ((float) CorpusStats.get(next)[0])));
                float idf = (float) Math.log10(((float) files.size()) / (Float.MIN_VALUE + ((float) CorpusStats.get(next)[0])));
                currentCorpusStatWithTFIDF[2] = Float.toString((tf * idf));   //weight

                if (corpusStatsWithTFIDFList.contains(currentCorpusStatWithTFIDF[0])) {
                    System.out.println(currentCorpusStatWithTFIDF[0]);
                } else {
                    corpusStatsWithTFIDFList.add(currentCorpusStatWithTFIDF);
                }
            }

            this.corpusStatsWithTFIDF = corpusStatsWithTFIDFList;
            this.Files_entropy = this.Entropy(/*listOfFiles*/);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * @param x
     * @param base
     * @return
     */
    public Double log(Double x, int base) //Log function
    {
        return (Double) (Math.log(x) / Math.log(base));
    }

    /**
     *
     * @return
     */
    public Map<String, String[]> Entropy(/*File[] fileslist*/) {

        double total_weight = 0.0;
        Map<String, Double> corpus_weight = new HashMap<String, Double>();
        Map<String, String[]> files_entropy = new HashMap<String, String[]>();
        for (int i1 = 0; i1 < corpusStatsWithTFIDF.size(); i1++) {

            corpus_weight.put(corpusStatsWithTFIDF.get(i1)[0], Double.parseDouble(corpusStatsWithTFIDF.get(i1)[2]));
            total_weight += Double.parseDouble(corpusStatsWithTFIDF.get(i1)[2]);

        }

        List<FileData> files = input.getFiles();
        try {

//        	for (int i = 0; i < fileslist.length; i++) {
//            	File file = fileslist[i];
            for (FileData ff : files) {

                File file = ff.getFile();
                String text;
                Double H_X = 0.0;

                try {
                    text = JavaIO.readFile(file);
//                    Properties props = new Properties();
//            		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
//            		pipeline = new StanfordCoreNLP(props);
                    Annotation document = new Annotation(text);
                    pipeline.annotate(document);

                    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

                    for (CoreMap sentence : sentences) {
                        // traversing the words in the current sentence
                        // a CoreLabel is a CoreMap with additional token-specific methods
                        for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                            // this is the text of the token
                            Double prob_word = 0.0;
                            String word = token.get(TextAnnotation.class);
                            word = word.toLowerCase();
                            if (!word.matches("[a-zA-Z0-9]*")) {
                                continue;
                            }

                            prob_word = corpus_weight.get(word) / total_weight;
                            if (prob_word > 0) {
                                H_X += -1.0 * prob_word * this.log(prob_word, 2);
                            }

                            // System.out.println(word + " , " + prob_word + " , " + H_X);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

                int count_file_terms = file_length.get(file.getName());
                //H_X = H_X / Math.log10(count_file_terms);
                Double Smoothed_H_X = H_X / Math.log10(count_file_terms);

                String[] entropy_entity = new String[3];
                entropy_entity[0] = String.valueOf(count_file_terms);
                entropy_entity[1] = String.valueOf(H_X);
                entropy_entity[2] = String.valueOf(Smoothed_H_X);

                //System.out.println(file.getName() + " , " + count_file_terms + " , " + H_X + " , " + H_X / Math.log10(count_file_terms));
                files_entropy.put(file.getName(), entropy_entity);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return files_entropy;
    }

    /**
     *
     * @param filepath
     */
    public void writeOutput(String filepath) {
        //Write CSV
        this.writeCsv(Files_entropy, filepath);
    }

    private void writeCsv(Map<String, String[]> files_entropy, String filepath) {

        StringBuffer sb = new StringBuffer();
        sb.append("File_Name, File_length, Entropy, Normalized_Entropy" + "\n");

        String toWrite = "";
        for (String fkey : files_entropy.keySet()) {
            String[] temp = files_entropy.get(fkey);
            toWrite = fkey + "," + temp[0] + "," + temp[1] + "," + temp[2] + "\n";
            sb.append(toWrite);
        }
        System.out.println("in writecsv before writeDataIntoFile");
        
        // 2016.03 Add this code to delete existing file
        File toDelete = new File(filepath);
        	if (toDelete.exists()) {
        		toDelete.delete(); 
        	}
        //
        
        FileData.writeDataIntoFile(sb.toString(), filepath);

    }
}
