package context.core.task.entitydetection;

import context.core.entity.TaskInstance;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class EntityDetectionTaskInstance extends TaskInstance {

    private AbstractSequenceClassifier<?> classifier3;
    private AbstractSequenceClassifier<?> classifier4;
    private AbstractSequenceClassifier<?> classifier7;
    int model;

    boolean innerXMLOutput;
    boolean htmlOutput;
    boolean slashTagsOutput;

    /**
     *
     * @param innerXMLOutput
     */
    public void setInnerXMLOutput(boolean innerXMLOutput) {
        this.innerXMLOutput = innerXMLOutput;
    }

    /**
     *
     * @param htmlOutput
     */
    public void setHtmlOutput(boolean htmlOutput) {
        this.htmlOutput = htmlOutput;
    }

    /**
     *
     * @param slashTagsOutput
     */
    public void setSlashTagsOutput(boolean slashTagsOutput) {
        this.slashTagsOutput = slashTagsOutput;
    }

    /**
     *
     * @return
     */
    public boolean isHtmlOutput() {
        return htmlOutput;
    }

    /**
     *
     * @return
     */
    public boolean isInnerXMLOutput() {
        return innerXMLOutput;
    }

    /**
     *
     * @return
     */
    public boolean isSlashTagsOutput() {
        return slashTagsOutput;
    }

    /**
     *
     * @return
     */
    public int getModel() {
        return model;
    }

    /**
     *
     * @param model
     */
    public void setModel(int model) {
        this.model = model;
    }

    /**
     *
     * @param name
     */
    public EntityDetectionTaskInstance(StringProperty name) {
        super(name);
    }

    /**
     *
     * @param classifier3
     */
    public void set3Classifier(AbstractSequenceClassifier<?> classifier3) {
        this.classifier3 = classifier3;
    }

    /**
     *
     * @return
     */
    public AbstractSequenceClassifier<?> get3Classifier() {
        return classifier3;
    }

    /**
     *
     * @return
     */
    public AbstractSequenceClassifier<?> get4Classifier() {
        return classifier4;
    }

    /**
     *
     * @param classifier4
     */
    public void set4Classifier(AbstractSequenceClassifier<?> classifier4) {
        this.classifier4 = classifier4;
    }

    /**
     *
     * @param classifier7
     */
    public void set7Classifier(AbstractSequenceClassifier<?> classifier7) {
        this.classifier7 = classifier7;
    }

    /**
     *
     * @return
     */
    public AbstractSequenceClassifier<?> get7Classifier() {
        return classifier7;
    }
}
