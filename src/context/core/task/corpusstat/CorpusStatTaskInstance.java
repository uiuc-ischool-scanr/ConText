package context.core.task.corpusstat;

import context.core.entity.TaskInstance;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class CorpusStatTaskInstance extends TaskInstance {

    StanfordCoreNLP pipeline;
    String language;
    Boolean includePOS;

    /**
     *
     * @param name
     */
    public CorpusStatTaskInstance(StringProperty name) {
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
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     *
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     *
     * @return
     */
    public Boolean isIncludePOS() {
        return includePOS;
    }

    /**
     *
     * @param includePOS
     */
    public void setIncludePOS(Boolean includePOS) {
        this.includePOS = includePOS;
    }

}
