package context.core.task.topicmodeling;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;

import java.io.File;
import java.util.List;

/**
 *
 * @author Aale
 */
public class TopicModelingBody {

    private TopicModelingTaskInstance instance;
    private CorpusData input;
    private TabularData[] tabularOutput;

    private int numTopics;
    private int numWordsPerTopic;
    private int numIterations;
    private int numOptInterval;
    private double sumAlpha;
    private String stopListPath;
    private List<FileData> CorpusFiles;
    private String result;
    private String log;
    private String[] allOuts;
    private boolean isLowercase;

    /**
     *
     * @param instance
     */
    public TopicModelingBody(TopicModelingTaskInstance instance) {
        this.instance = instance;
        this.input = (CorpusData) instance.getInput();
        // TODO Auto-generated method stub
        this.numTopics = instance.getNumTopics();
        this.numWordsPerTopic = instance.getNumWordsPerTopic();
        this.numIterations = instance.getNumIterations();
        this.numOptInterval=instance.getNumOptInterval();
        this.sumAlpha=instance.getSumAlpha();
        this.stopListPath = instance.getStopListLoc();
        this.CorpusFiles = this.input.getFiles();
        this.allOuts = new String[2];
        this.isLowercase = instance.getIsLowercase();
    }

    /**
     *
     * @return
     */
    public boolean ModelTopic() {
        try {
            MalletTopicModeling mtm = new MalletTopicModeling(numTopics, numWordsPerTopic,
            		numIterations,numOptInterval,sumAlpha, CorpusFiles, stopListPath, isLowercase);

            allOuts = mtm.topicModellingOutput();
        } catch (Exception e) {
        	e.printStackTrace();
            log += "Error in generating topics:" + e.getMessage() + " \n";
            return false;
        }
        return true;

    }

    /**
     *
     * @return
     */
    public String[] GetTopics() {
        return allOuts;
    }

    /**
     *
     * @param filepath1
     * @param filepath2
     */
    public void writeOutput(String filepath1, String filepath2, String filepath3, String filepath4) {
        
        this.writeWordsCsv(filepath1);
        this.writeProbsCsv(filepath2);
        this.writeWeightsCsv(filepath3);
        this.writeTokenCsv(filepath4);

    }

    /**
     *
     * @param filepath
     */
    public void writeWordsCsv(String filepath) {
        StringBuilder b = new StringBuilder();
        b.append("Topic,Weight,TopicMembers\n");//Average Square Fit,TopicMembers\n");
        b.append(allOuts[0]);
        
        // 2016.03 Add this code to delete existing file
        File toDelete = new File(filepath);
        	if (toDelete.exists()) {
        		toDelete.delete(); 
        	}
        //
        
        FileData.writeDataIntoFile(b.toString(), filepath);
    }

    /**
     *
     * @param filepath
     */
    public void writeProbsCsv(String filepath) {
        StringBuilder b = new StringBuilder();
        b.append("Document");
        for (int j = 0; j < numTopics; j++){
        	b.append(",Fit to Topic" + Integer.toString(j+1));
        }
        b.append("\n");
        b.append(allOuts[1]);
        
        // 2016.03 Add this code to delete existing file
        File toDelete = new File(filepath);
        	if (toDelete.exists()) {
        		toDelete.delete(); 
        	}
        //
        
        FileData.writeDataIntoFile(b.toString(), filepath);
    }
    
    /**
    *
    * @param filepath
    */
   public void writeWeightsCsv(String filepath) {
       StringBuilder b = new StringBuilder();
       b.append("Topic,Word,WordWeights\n");
       b.append(allOuts[2]);
       
       // 2016.03 Add this code to delete existing file
       File toDelete = new File(filepath);
       	if (toDelete.exists()) {
       		toDelete.delete(); 
       	}
       //
       
       FileData.writeDataIntoFile(b.toString(), filepath);
   }
    
   /**
   *
   * @param filepath
   */
  public void writeTokenCsv(String filepath) {
      StringBuilder b = new StringBuilder();
      b.append("LL/Token\n");
      b.append(allOuts[3]);
      
      // 2016.03 Add this code to delete existing file
      File toDelete = new File(filepath);
      	if (toDelete.exists()) {
      		toDelete.delete(); 
      	}
      //
      
      FileData.writeDataIntoFile(b.toString(), filepath);
  }
   
   

}
