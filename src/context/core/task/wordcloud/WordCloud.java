package context.core.task.wordcloud;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import au.com.bytecode.opencsv.CSVReader;
import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.util.CorpusAggregator;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.IDSorter;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;
import context.app.AppConfig;
import context.core.task.pos.POSTagger;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 *
 * @author Aale
 */
public class WordCloud {

    /**
     * @param args
     */
    private List<List<String[]>> toAggregate;
    private String stopListLoc;
    private StanfordCoreNLP pipeline;
    private List<String[]> WordWeights;
    private String sentimentLoc;
    private Boolean isCluster;
    private int numIters;
    private int numTopics;
    private int wordPerTopic;
    private int width;
    private int height;
    private int minFontSize;
    private WordCloudTaskInstance instance;
    private CorpusData input;
    private boolean isTff;
    
    
    private static HashMap<String, List<String>> sentimentDict; 
    
    private void initSentimentDict(){
        try {
        	CSVReader reader = new CSVReader(new FileReader(sentimentLoc));
        	sentimentDict = new HashMap<String, List<String>>();
        	String[] line = reader.readNext(); // Read the header
        	/**
        	 * Row Format:
        	 * Word,POS,Stemmed,Priorpolarity,Type
        	 */
        	while((line = reader.readNext()) != null){	
        		sentimentDict.put(line[0], Arrays.asList(line).subList(1, 4));
        	}
        	reader.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    
    private class TopicStruct {

        int id;
        double fitVal;

        public int getIndexForId(TopicStruct[] topicData, int id) {
            for (int i = 0; i < topicData.length; i++) {
                if (topicData[i].id == id) {
                    return i;
                }
            }
            return -1;
        }

        public double getFitValForId(TopicStruct[] topicData, int id) {
            for (int i = 0; i < topicData.length; i++) {
                if (topicData[i].id == id) {
                    return topicData[i].fitVal;
                }
            }
            return 0.0;
        }
    };
    private TopicStruct[] topicData;

    /**
     *
     * @param instance
     */
    public WordCloud(WordCloudTaskInstance instance) {
        // TODO Auto-generated method stub
        this.instance = instance;
        init();
    }

    private void init() {

        this.input = (CorpusData) instance.getInput();
        toAggregate = new ArrayList<List<String[]>>();
        WordWeights = new ArrayList<String[]>();
        this.stopListLoc = instance.getStopListLoc();
        this.pipeline = instance.getPipeline();
        this.sentimentLoc = instance.getSentimentLoc();
        this.isCluster = instance.getClustering();
        this.numIters = instance.getNumIters();
        this.numTopics = instance.getNumTopics();
        this.wordPerTopic = instance.getWordPerTopic();
        this.width = instance.getWidth();
        this.height = instance.getHeight();
        this.minFontSize = instance.getMinFontSize();
        this.initSentimentDict();
        this.isTff = false;
        if(sentimentLoc.split("\\.")[0].toLowerCase() == "tff"){
        	this.isTff = true;
        }
    }

    /**
     *
     * @return
     */
    public boolean genCloud() {

        List<FileData> files = input.getFiles();
        int numTerms = 0;
        try {
            for (FileData ff : files) {
                File file = ff.getFile();
                String text;
                text = "";
                try {
                    text = readFile(file);
                    text = text.replaceAll("[^a-zA-Z ]", "");
                    text = text.toLowerCase();
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
                List<String[]> CorpusStatTags = new ArrayList<String[]>();
                try {
                    text = readFile(file);
                    text = text.replaceAll("\\p{Cc}", " ");
                    text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"-]", " ");
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
                            String[] entity = {word, pos, Integer.toString(1)};
                            if (!word.matches("^[a-zA-Z0-9]*$")) {
                                continue;
                            }
                            CorpusStatTags.add(entity);
                            numTerms++;
                        }
                    }
                    toAggregate.add(CorpusStatTags);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            List<String[]> CorpusStats = new CorpusAggregator().CorpusAggregate(toAggregate);
            HashMap<String, Integer> hmap = new HashMap<String, Integer>();
            for (String[] CorpusStat : CorpusStats) {
                if (null == hmap.get(CorpusStat[0])) {
                    hmap.put(CorpusStat[0], Integer.parseInt(CorpusStat[2]));
                } else {
                    hmap.put(CorpusStat[0], Math.max(Integer.parseInt(CorpusStat[2]), hmap.get(CorpusStat[0])));
                }
            }
            String[] WordArray = new String[hmap.size()];
            WordArray = hmap.keySet().toArray(WordArray);
            String[][] StatsArray = new String[WordArray.length][2];
            for (int i1 = 0; i1 < WordArray.length; i1++) {
                String[] temp = {WordArray[i1], Integer.toString(hmap.get(WordArray[i1]))};
                StatsArray[i1] = temp;
            }

            Comparator<String[]> compareStringArray = new Comparator<String[]>() {
                @Override
                public int compare(final String[] first, final String[] second) {
                    // here you should usually check that first and second
                    // a) are not null and b) have at least two items
                    // updated after comments: comparing Double, not Strings
                    // makes more sense, thanks Bart Kiers

                    return Double.valueOf(second[1]).compareTo(
                            Double.valueOf(first[1])
                    );

                }
            };
            Arrays.sort(StatsArray, compareStringArray);

            // Begin by importing documents from text to feature sequences
            ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

            // Pipes: lowercase, tokenize, remove stopwords, map to features
            // pipeList.add( new CharSequenceLowercase() );
            pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
            //pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false) );
            pipeList.add(new TokenSequenceRemoveStopwords(new File(stopListLoc), "UTF-8", false, false, false));
            //pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false) );
            pipeList.add(new TokenSequence2FeatureSequence());

            InstanceList instances = new InstanceList(new SerialPipes(pipeList));
            for (FileData ff : files) {
                File file = ff.getFile();
                Reader fileReader = null;
                try {
                    fileReader = new InputStreamReader(new FileInputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace(System.out);
                }
                instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                        3, 2, 1)); // data, label, name fields
            }

            // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
            //  Note that the first parameter is passed as the sum over topics, while
            //  the second is the parameter for a single dimension of the Dirichlet prior.
            ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

            model.addInstances(instances);

            // Use two parallel samplers, which each look at one half the corpus and combine
            // statistics after every iteration.
            model.setNumThreads(2);

            // Run the model for 50 iterations and stop (this is for testing only, 
            // for real applications, use 1000 to 2000 iterations)
            model.setNumIterations(numIters);
            //model.setSaveState(model.numIterations, ".\\Data\\StateSave.txt");
            //model.setSaveSerializedModel(model.numIterations, ".\\Data\\ModelSave.txt");
            try {
                model.estimate();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }

            // Show the words and topics in the first instance
            // The data alphabet maps word IDs to strings
            Alphabet dataAlphabet = instances.getDataAlphabet();

            FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
            LabelSequence topics = model.getData().get(0).topicSequence;

            Formatter out = new Formatter(new StringBuilder(), Locale.US);

            /*
             for (int position = 0; position < tokens.getLength(); position++) {
             out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
             }
             System.out.println(out);
             */
            // Estimate the topic distribution of the first instance, 
            //  given the current Gibbs state.
            List<String> Words = new ArrayList<String>();

            double[] topicDistribution = model.getTopicProbabilities(0);

            // Get an array of sorted sets of word ID/count pairs
            ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
            
            String SentimentString = null;
            String[] SentimentWords = null; 
            if(isTff){
            	File SentimentFile = new File(sentimentLoc);
                try {
                    SentimentString = readFile(SentimentFile);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                SentimentWords = SentimentString.split("\r");
                SentimentWords = SentimentWords[0].split("\n");
            }
            
            
            
            // Show top numTopics words in topics with proportions for the first document
            for (int topic = 0; topic < numTopics; topic++) {
                Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();

                out = new Formatter(new StringBuilder(), Locale.US);
                out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
                int rank = 0;

                while (iterator.hasNext() && rank < 2 * wordPerTopic) {
                    IDSorter idCountPair = iterator.next();
                    out.format("%s ", dataAlphabet.lookupObject(idCountPair.getID())); //, Math.round(idCountPair.getWeight()));
                    String[] temp = new String[5];
                    temp[2] = Integer.toString(topic);
                    temp[3] = String.valueOf(topicDistribution[topic]);
                    temp[4] = String.valueOf(2);
                    temp[0] = (String) dataAlphabet.lookupObject(idCountPair.getID());
                    for (String[] stat : StatsArray) {
                        if (0 == stat[0].compareTo(temp[0]) || 
                        		(stat[0].regionMatches(true, 0, temp[0], 0, 1)
                        				&& stat[0].regionMatches(1, temp[0], 1, stat[0].length() - 1)
                        				&& stat[0].length() == temp[0].length())) {
                            temp[0] = stat[0];
                            int found = 0;
                            for (String word : Words) {
                                if (0 == word.compareTo(temp[0])) {
                                    found = 1;
                                    break;
                                }
                            }
                            if (1 == found) {
                                continue;
                            }
                            String polarity = "";
                            if(isTff){
                            	// For backward compatibility
	                            if (SentimentString.contains('=' + temp[0] + " ")) {
	                                for (String SentimentWord : SentimentWords) {
	                                    if (SentimentWord.split("word1=")[1].split(" ")[0].equals(temp[0])) {
	                                        polarity = SentimentWord.split("priorpolarity=")[1].split(" ")[0];
	                                        if (polarity.equals("positive")) {
	                                            temp[4] = String.valueOf(1);
	                                        }
	                                        if (polarity.equals("neutral")) {
	                                            temp[4] = String.valueOf(0);
	                                        }
	                                        if (polarity.equals("negative")) {
	                                            temp[4] = String.valueOf(-1);
	                                        }
	                                    }
	                                }
	                            }
                            } else {
                            	if(sentimentDict.containsKey(temp[0])){
                            		polarity = sentimentDict.get(temp[0]).get(2);
                                    if (polarity.equals("positive")) {
                                        temp[4] = String.valueOf(1);
                                    }
                                    if (polarity.equals("neutral")) {
                                        temp[4] = String.valueOf(0);
                                    }
                                    if (polarity.equals("negative")) {
                                        temp[4] = String.valueOf(-1);
                                    }
                            	}
                            	
                            }
                            
                            temp[1] = Integer.toString(Integer.valueOf(stat[1]));
                            Words.add(temp[0]);
                            WordWeights.add(temp);
                            break;
                        }
                    }
                    rank++;
                }
                System.out.println(out);

            }

            Collections.sort(WordWeights, compareStringArray);

            topicData = new TopicStruct[numTopics];
            for (int i = 0; i < numTopics; i++) {
                topicData[i] = new TopicStruct();
                topicData[i].id = i;
                topicData[i].fitVal = topicDistribution[i];
            }

            Comparator<TopicStruct> compareTopicData = new Comparator<TopicStruct>() {
                @Override
                public int compare(final TopicStruct first, final TopicStruct second) {
                    System.out.println("Comparing " + first.fitVal + " " + second.fitVal);
                    return Double.compare(second.fitVal, first.fitVal);
                }
            };

            Arrays.sort(topicData, compareTopicData);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Finished cloud generation");

        return true;

    }

    private String getJavaScript() {

        if (!isCluster) {
            float maxMatch = Float.valueOf(WordWeights.get(0)[1]);
            //Adjust weights.
            String[] WordsForWordCloud = new String[WordWeights.size()];
            float[] WeightsForWordCloud = new float[WordWeights.size()];
            for (int wordIdx = 0; wordIdx < WordWeights.size(); wordIdx++) {
                WeightsForWordCloud[wordIdx] = Math.max((float) (1.0 + (0.8) * (Math.log10(Float.valueOf(WordWeights.get(wordIdx)[1]) / maxMatch))), ((float) minFontSize) / ((float) 100));
                WordsForWordCloud[wordIdx] = WordWeights.get(wordIdx)[0];
            }

            D3WordCloud cloud = new D3WordCloud(height, width, WordsForWordCloud, WeightsForWordCloud);
            String javascriptForHTML = cloud.getD3JavaScript();
            return javascriptForHTML;
        } else {
            String lineSep = System.getProperty("line.separator");
            int totalWords = 0;
            int topicCount[] = new int[numTopics];
            for (int i = 0; i < numTopics; i++) {
                topicCount[i] = wordPerTopic;
            }
            String jscriptCluster = "var item_count = " + Integer.toString(numTopics) + ";" + lineSep
                    + "var word_per_item = " + Integer.toString(wordPerTopic) + ";" + lineSep
                    + "var width = " + Integer.toString(width) + "," + lineSep
                    + "    height = " + Integer.toString(height) + "," + lineSep
                    + "\tfontSize = " + Integer.toString(minFontSize) + ";" + lineSep
                    + lineSep + "			var wordList = [" + lineSep;
            for (String[] WordWeight : WordWeights) {
                if (totalWords > (numTopics * wordPerTopic)) {
                    break;
                }
                if (topicCount[Integer.parseInt(WordWeight[2])] > 0) {
                    TopicStruct tStruct = new TopicStruct();
                    int topicId = tStruct.getIndexForId(topicData, Integer.parseInt(WordWeight[2]));
                    double fitVal = tStruct.getFitValForId(topicData, Integer.parseInt(WordWeight[2]));
                    jscriptCluster += "\t{text: \"" + WordWeight[0]
                    		+ "\",topic:" + Integer.toString(topicId)
                    		+ ",sentiment:" + WordWeight[4]
                    		+ ",frequency:" + WordWeight[1]
                    		+ ",fitVal:" + Double.toString(fitVal) + "}," + lineSep;
                    topicCount[Integer.parseInt(WordWeight[2])]--;
                    totalWords++;
                }

            }
            jscriptCluster += "];";
            return jscriptCluster;

        }
    }

    /**
     *
     * @param outputDirectory
     * @return
     */
    public boolean writeOutput(String outputDirectory) {
        //Write CSV
        return this.writeJavaScript(getJavaScript(), outputDirectory);
    }

    private boolean writeJavaScript(String javascriptForHTML, String outputDirectory) {
        File D3WordCloudHTML = null;
        if (!isCluster) {
            File dirFile = new File(outputDirectory + "/WordCloud");

            if (!dirFile.isDirectory()) {
                dirFile.mkdirs();
            }
            File file = new File(dirFile.getAbsolutePath() + "/WordCloud.html");
            File subDirFile = new File(dirFile.getAbsolutePath() + "/D3");
            if (!subDirFile.isDirectory()) {
                subDirFile.mkdirs();
            }
            File D3lib = new File(subDirFile.getAbsolutePath() + "/d3.v3.js");
            File D3Layout = new File(subDirFile.getAbsolutePath() + "/d3.layout.cloud.js");
            File D3libOrig = new File(AppConfig.getUserDirLoc() + "/data/WordClouds/D3/d3.v3.js");
            File D3LayoutOrig = new File(AppConfig.getUserDirLoc() + "/data/WordClouds/D3/d3.layout.cloud.js");
            try {
                FileUtils.copyFile(D3libOrig, D3lib);
                FileUtils.copyFile(D3LayoutOrig, D3Layout);
            } catch (IOException e2) {
                // TODO Auto-generated catch block

                e2.printStackTrace();
                return false;
            }

            // if file doesnt exists, then create it
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                    return false;
                }
            }

            FileWriter fw = null;
            try {
                fw = new FileWriter(file.getAbsoluteFile());
            } catch (IOException e) {
                e.printStackTrace(System.out);
                return false;
            }
            BufferedWriter bw = new BufferedWriter(fw);
            try {
                bw.write(javascriptForHTML);
            } catch (IOException e) {
                e.printStackTrace(System.out);
                return false;
            }
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace(System.out);
                return false;
            }
//            try {
//                Desktop userDesktop = Desktop.getDesktop();
//                userDesktop.open(file);
//            } catch (IOException e1) {
//                e1.printStackTrace(System.out);
//                return false;
//            }
        } else {
            File dirFile = new File(outputDirectory + "/WordCloud");

            if (!dirFile.isDirectory()) {
                dirFile.mkdirs();
            }
            File file = new File(dirFile.getAbsolutePath() + "/word_list.js");

            File D3lib = new File(dirFile.getAbsolutePath() + "/d3.v3.min.js");
            File D3Layout = new File(dirFile.getAbsolutePath() + "/d3.layout.cloud.js");
            File D3WordCloudJS = new File(dirFile.getAbsolutePath() + "/word_cloud.js");
            D3WordCloudHTML = new File(dirFile.getAbsolutePath() + "/wordCloud.html");
            File D3libOrig = new File(AppConfig.getUserDirLoc() + "/data/WordClouds/word_cloud/d3.v3.min.js");
            File D3LayoutOrig = new File(AppConfig.getUserDirLoc() + "/data/WordClouds/word_cloud/d3.layout.cloud.js");
            File D3WordCloudJSOrig = new File(AppConfig.getUserDirLoc() + "/data/WordClouds/word_cloud/word_cloud.js");
            File D3WordCloudHTMLOrig = new File(AppConfig.getUserDirLoc() + "/data/WordClouds/word_cloud/wordCloud.html");

            try {
                FileUtils.copyFile(D3libOrig, D3lib);
                FileUtils.copyFile(D3LayoutOrig, D3Layout);
                FileUtils.copyFile(D3WordCloudJSOrig, D3WordCloudJS);
                FileUtils.copyFile(D3WordCloudHTMLOrig, D3WordCloudHTML);
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
                return false;
            }

            // if file doesnt exists, then create it
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                    return false;
                }
            }

            FileWriter fw = null;
            try {
                fw = new FileWriter(file.getAbsoluteFile());
            } catch (IOException e) {
                e.printStackTrace(System.out);
                return false;
            }
            BufferedWriter bw = new BufferedWriter(fw);
            try {
                bw.write(javascriptForHTML);
            } catch (IOException e) {
                e.printStackTrace(System.out);
                return false;
            }
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace(System.out);
                return false;
            }
//            try {
//                Desktop userDesktop = Desktop.getDesktop();
//                userDesktop.open(D3WordCloudHTML);
//            } catch (IOException e1) {
//                e1.printStackTrace(System.out);
//                return false;
//            }

        }
        return true;
    }

    private static String readFile(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            return Charset.defaultCharset().decode(bb).toString();
        } finally {
            stream.close();
        }
    }

}
