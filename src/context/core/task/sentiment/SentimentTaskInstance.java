package context.core.task.sentiment;

import context.core.entity.FileData;
import context.core.entity.TaskInstance;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class SentimentTaskInstance extends TaskInstance {

    private StanfordCoreNLP pipeline;
    private FileData sentimentFile;

    /**
     *
     * @param name
     */
    public SentimentTaskInstance(StringProperty name) {
        super(name);
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
     * @return
     */
    public StanfordCoreNLP getPipeline() {
        return pipeline;
    }

    /**
     *
     * @param sentimentFile
     */
    public void setSentimentFile(FileData sentimentFile) {
        this.sentimentFile = sentimentFile;
    }

    /**
     *
     * @return
     */
    public FileData getSentimentFile() {
        return sentimentFile;
    }
}
