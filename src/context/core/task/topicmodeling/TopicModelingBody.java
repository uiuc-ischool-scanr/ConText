package context.core.task.topicmodeling;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
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
    private String stopListPath;
    private List<FileData> CorpusFiles;
    private String result;
    private String log;
    private String[] allOuts;

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
        this.stopListPath = instance.getStopListLoc();
        this.CorpusFiles = this.input.getFiles();
        this.allOuts = new String[2];
    }

    /**
     *
     * @return
     */
    public boolean ModelTopic() {
        try {
            MalletTopicModeling mtm = new MalletTopicModeling();

            allOuts = mtm.topicModeling(numTopics, numWordsPerTopic, numIterations, CorpusFiles, stopListPath);
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
    public void writeOutput(String filepath1, String filepath2) {
        this.writeProbsCsv(filepath2);
        this.writeWordsCsv(filepath1);

    }

    /**
     *
     * @param filepath
     */
    public void writeWordsCsv(String filepath) {
        StringBuilder b = new StringBuilder();
        b.append("Topic,Average Fit,TopicMembers\n");//Average Square Fit,TopicMembers\n");
        b.append(allOuts[0]);
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
        FileData.writeDataIntoFile(b.toString(), filepath);
    }

}
