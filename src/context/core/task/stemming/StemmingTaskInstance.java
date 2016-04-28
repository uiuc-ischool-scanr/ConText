package context.core.task.stemming;

import context.core.entity.TaskInstance;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class StemmingTaskInstance extends TaskInstance {

    StanfordCoreNLP pipeline;

    /**
     *
     * @param name
     */
    public StemmingTaskInstance(StringProperty name) {
        super(name);
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
     * @param pipeline
     */
    public void setPipeline(StanfordCoreNLP pipeline) {
        this.pipeline = pipeline;
    }
}
