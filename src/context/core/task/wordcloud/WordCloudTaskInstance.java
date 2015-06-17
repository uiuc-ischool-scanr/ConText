package context.core.task.wordcloud;


import context.core.entity.TaskInstance;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class WordCloudTaskInstance extends TaskInstance {

    private String stopListLoc;
    private StanfordCoreNLP pipeline;
    private String sentimentLoc;
    private Boolean isCluster;
    private int numIters;
    private int numTopics;
    private int wordPerTopic;
    private int width;
    private int height;
    private int minFontSize;

    /**
     *
     * @param name
     */
    public WordCloudTaskInstance(StringProperty name) {
        super(name);
    }

    // setters

    /**
     *
     * @param stopListLoc
     */
        public void setStopListLoc(String stopListLoc) {
        this.stopListLoc = stopListLoc;
    }

    /**
     *
     * @param pipeline
     */
    public void setPipeline(StanfordCoreNLP pipeline) {
        this.pipeline = pipeline;
    }

    /**
     *
     * @param sentimentLoc
     */
    public void setSentimentLoc(String sentimentLoc) {
        this.sentimentLoc = sentimentLoc;
    }

    /**
     *
     * @param isCluster
     */
    public void setClustering(Boolean isCluster) {
        this.isCluster = isCluster;
    }

    /**
     *
     * @param numIters
     */
    public void setNumIters(int numIters) {
        this.numIters = numIters;
    }

    /**
     *
     * @param numTopics
     */
    public void setNumTopics(int numTopics) {
        this.numTopics = numTopics;
    }

    /**
     *
     * @param wordPerTopic
     */
    public void setWordPerTopic(int wordPerTopic) {
        this.wordPerTopic = wordPerTopic;
    }

    /**
     *
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @param minFontSize
     */
    public void setMinFontSize(int minFontSize) {
        this.minFontSize = minFontSize;
    }

    // getters

    /**
     *
     * @return
     */
        public String getStopListLoc() {
        return stopListLoc;
    }

    /**
     *
     * @return
     */
    public StanfordCoreNLP getPipeline() {
        return pipeline;
    }

    /**
     *
     * @return
     */
    public String getSentimentLoc() {
        return sentimentLoc;
    }

    /**
     *
     * @return
     */
    public Boolean getClustering() {
        return isCluster;
    }

    /**
     *
     * @return
     */
    public int getNumIters() {
        return numIters;
    }

    /**
     *
     * @return
     */
    public int getNumTopics() {
        return numTopics;
    }

    /**
     *
     * @return
     */
    public int getWordPerTopic() {
        return wordPerTopic;
    }

    /**
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @return
     */
    public int getMinFontSize() {
        return minFontSize;
    }
}
