/**
 * 
 */
package context.core.task.wordcloud;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

import cc.mallet.types.IDSorter;
import au.com.bytecode.opencsv.CSVReader;
import context.app.AppConfig;
import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.task.topicmodeling.MalletTopicModeling;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Shubhanshu
 *
 */
public class ClusteredWordCloud {

	/**
	 * 
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
    public int numOptInterval;
	public double sumAlpha;
    private int width;
    private int height;
    private int minFontSize;
    private WordCloudTaskInstance instance;
    private CorpusData input;
    private boolean isTff;
    private boolean isLowercase;
    /*private Boolean drop_num;
    private Boolean drop_pun;
    private Boolean keep_pou;
    */
    public class SentimentTopicWord{
    	String word;
    	int sentiment, topic;
    	double weight, fitVal;
    	public SentimentTopicWord(String word){
    		this.word = word;
    	}
    	public SentimentTopicWord(String word, int sentiment,
    			int topic, double weight, double fitVal){
    		this.word = word;
    		this.sentiment = sentiment;
    		this.topic = topic;
    		this.weight = weight;
    		this.fitVal = fitVal;
    		
    	}
    	
    	@Override
    	public String toString(){
    		Formatter out = new Formatter(new StringBuilder(), Locale.US);
    		out.format("{text: \"%s\",topic:%d,sentiment:%d,frequency:%.3f,fitVal:%.4f},",
    				word, topic, sentiment, weight, fitVal);
			return out.toString();
    		
    	}
    }
    
    private List<SentimentTopicWord> wordList;
    
    
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
        		if(this.isLowercase){
        			sentimentDict.put(line[0].toLowerCase(), Arrays.asList(line).subList(1, 4));
        		} else {
        			sentimentDict.put(line[0], Arrays.asList(line).subList(1, 4));
        		}
        	}
        	reader.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    
    private int getSentiment(String word){
    	String polarity = "";    	
    	if(sentimentDict.containsKey(word)){
    		polarity = sentimentDict.get(word).get(2);
    		switch (polarity) {
				case "positive":
					return 1;
				case "neutral":
					return 0;
				case "negative":
					return -1;
				default:
					return 2;
			}
    	}
		return 2;
    }
    
    private class TopicStruct {
        int id;
        double fitVal;
    };
    private TopicStruct[] topicData;
    
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
    
    
    
	public ClusteredWordCloud(WordCloudTaskInstance instance) {
		// TODO Auto-generated constructor stub
		this.instance = instance;
        init();
	}
	
	private void init() {
        this.input = (CorpusData) instance.getInput();
        this.toAggregate = new ArrayList<List<String[]>>();
        this.WordWeights = new ArrayList<String[]>();
        this.stopListLoc = instance.getStopListLoc();
        this.pipeline = instance.getPipeline();
        this.sentimentLoc = instance.getSentimentLoc();
        this.numIters = instance.getNumIters();
        this.numTopics = instance.getNumTopics();
        this.wordPerTopic = instance.getWordPerTopic();
        this.numOptInterval=instance.getNumOptInterval();
		this.sumAlpha=instance.getSumAlpha();
        this.width = instance.getWidth();
        this.height = instance.getHeight();
        this.minFontSize = instance.getMinFontSize();
        this.initSentimentDict();
        this.isLowercase = instance.getIsLowercase();
        /*this.drop_num = instance.isDropnum();
        this.drop_pun = instance.isDroppun();
        this.keep_pou = instance.isKeeppou();*/
    }
	
	public boolean genCloud() {
		List<FileData> files = input.getFiles();
		try {
            MalletTopicModeling mtm = new MalletTopicModeling(numTopics, wordPerTopic,
            		numIters,numOptInterval,sumAlpha, files, stopListLoc, isLowercase);
            ArrayList<TreeSet<IDSorter>> topicSortedWords = mtm.model.getSortedWords();
            wordList = new ArrayList<ClusteredWordCloud.SentimentTopicWord>();
            // Get weight of top words
            for (int i = 0; i < numTopics; i++) {
            	IDSorter topic = mtm.sortedTopics[i];
    			Iterator<IDSorter> iterator = topicSortedWords.get(topic.getID()).iterator();
    			int rank = 0;
    			while (iterator.hasNext() && rank < wordPerTopic) {
    				IDSorter idCountPair = iterator.next();
    				SentimentTopicWord w = new SentimentTopicWord(
    						(String)mtm.dataAlphabet.lookupObject(idCountPair.getID()));
    				w.weight = idCountPair.getWeight();
    				w.topic = i;
    				w.fitVal = topic.getWeight();
    				w.sentiment = getSentiment(w.word);
    				wordList.add(w); // Append word to the list
    				rank++;
    			}
    		}
        } catch (Exception e) {
        	e.printStackTrace();
            System.err.println("Error in generating word cloud:" + e.getMessage());
            return false;
        }
		return true;
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
   
   private String getJavaScript() {
       String lineSep = System.getProperty("line.separator");
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
       
       for(SentimentTopicWord w: wordList){
    	   jscriptCluster += "\t"+w.toString()+lineSep;
       }
       jscriptCluster += "];";
       return jscriptCluster;
   }
   
   
   private boolean writeJavaScript(String javascriptForHTML, String outputDirectory) {
       File D3WordCloudHTML = null;
   
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
       return true;
   }

}
