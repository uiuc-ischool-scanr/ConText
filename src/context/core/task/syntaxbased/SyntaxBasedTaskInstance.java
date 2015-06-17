package context.core.task.syntaxbased;

import context.core.entity.TaskInstance;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class SyntaxBasedTaskInstance extends TaskInstance {

    private HashMap<List<String>, List<List<String>>> edgeMap;
    private StanfordCoreNLP pipeline;
    private StanfordCoreNLP POSpipeline;
    private int unitOfAnalysis; //1 -- sentence, 2 -- document
    private int distance;
    private boolean deepParse;
    private int timeout; // time in milliseconds before we move on from a documents
    private List<String> dependencyEdges;
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
    public SyntaxBasedTaskInstance(StringProperty name) {
        super(name);
    }

    /**
     *
     * @param dependencyEdges
     */
    public void setDependencyEdges(List<String> dependencyEdges) {
        this.dependencyEdges = dependencyEdges;
    }

    /**
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     *
     * @param edgeMap
     */
    public void setEdgeMap(HashMap<List<String>, List<List<String>>> edgeMap) {
        this.edgeMap = edgeMap;
    }

    /**
     *
     * @param deepParse
     */
    public void setDeepParse(boolean deepParse) {
        this.deepParse = deepParse;
    }

    /**
     *
     * @param distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     *
     * @param unitOfAnalysis
     */
    public void setUnitOfAnalysis(int unitOfAnalysis) {
        this.unitOfAnalysis = unitOfAnalysis;
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
    public boolean getDeepParse() {
        return this.deepParse;
    }

    /**
     *
     * @return
     */
    public int getDistance() {
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
     * @param POSpipeline
     */
    public void setPipelinePOS(StanfordCoreNLP POSpipeline) {
        this.POSpipeline = POSpipeline;
    }

    /**
     *
     * @return
     */
    public StanfordCoreNLP getPipelinePOS() {
        return this.POSpipeline;
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
     * @return
     */
    public HashMap<List<String>, List<List<String>>> getEdgeMap() {
        return this.edgeMap;
    }

    /**
     *
     * @return
     */
    public int getTimeout() {
        return this.timeout;
    }

    /**
     *
     * @return
     */
    public List<String> getDependencyEdges() {
        return dependencyEdges;
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
