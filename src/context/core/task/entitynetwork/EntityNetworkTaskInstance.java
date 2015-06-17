package context.core.task.entitynetwork;

import context.core.entity.TaskInstance;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.Set;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class EntityNetworkTaskInstance extends TaskInstance {


    private AbstractSequenceClassifier<CoreLabel> classifier3;
    private AbstractSequenceClassifier<CoreLabel> classifier4;
    private AbstractSequenceClassifier<CoreLabel> classifier7;
    private int unitOfAnalysis; //1 -- sentence, 2 -- document
    private int distance;
    private StanfordCoreNLP pipeline;
    private String outputDir;
    
    private Set<String> filterLabels;
    
    /**
	 * @return the outputDir
	 */
	public synchronized String getOutputDir() {
		return outputDir;
	}

	/**
	 * @param outputDir the outputDir to set
	 */
	public synchronized void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

    /**
     *
     * @param name
     */
    public EntityNetworkTaskInstance(StringProperty name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public AbstractSequenceClassifier<CoreLabel> get3Classifier() {
        return classifier3;
    }
    
    /**
     *
     * @return
     */
    public AbstractSequenceClassifier<CoreLabel> get4Classifier() {
        return classifier4;
    }
    
    /**
     *
     * @return
     */
    public AbstractSequenceClassifier<CoreLabel> get7Classifier() {
        return classifier7;
    }
    
    /**
     *
     * @return
     */
    public int getDistance(){
    	return this.distance;
    }

    /**
     *
     * @return
     */
    public StanfordCoreNLP getPipeline() {
        return this.pipeline;
    }

    /**
     *
     * @return
     */
    public int getUnitOfAnalysis() {
    	return this.unitOfAnalysis;
    }

    /**
     *
     * @param classifier3
     */
    public void set3Classifier(AbstractSequenceClassifier<CoreLabel> classifier3) {
        this.classifier3 = classifier3;
    }

    /**
     *
     * @param classifier4
     */
    public void set4Classifier(AbstractSequenceClassifier<CoreLabel> classifier4) {
        this.classifier4 = classifier4;
    }

    /**
     *
     * @param classifier7
     */
    public void set7Classifier(AbstractSequenceClassifier<CoreLabel> classifier7) {
        this.classifier7 = classifier7;
    }
    
    /**
     *
     * @param distance
     */
    public void setDistance(int distance){
    	this.distance = distance;
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
     * @param unitOfAnalysis
     */
    public void setUnitOfAnalysis(int unitOfAnalysis){
    	this.unitOfAnalysis = unitOfAnalysis;
    }

	/**
	 * @return the filterLabels
	 */
	public Set<String> getFilterLabels() {
		return filterLabels;
	}

	/**
	 * @param filterLabels the filterLabels to set
	 */
	public void setFilterLabels(Set<String> filterLabels) {
		this.filterLabels = filterLabels;
	}
}
