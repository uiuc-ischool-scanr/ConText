package context.core.task.parsetree;

import context.core.entity.TaskInstance;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class ParseTreeNetworkTaskInstance extends TaskInstance {

    private HashMap<List<String>, List<List<String>>> edgeMap;
    private int aggregation; // 0 - per document 1- per corpus
    private String outputDir;
    private Set<String> selectedTypes;
    private boolean advance;

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
    public ParseTreeNetworkTaskInstance(StringProperty name) {
        super(name);
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
     * @param aggregation
     */
    public void setAggregation(int aggregation) {
        this.aggregation = aggregation;
    }

    /**
     *
     * @return
     */
    public int getAggregation() {
        return aggregation;
    }

    /**
     *
     * @return
     */
    public HashMap<List<String>, List<List<String>>> getEdgeMap() {
        return this.edgeMap;
    }

    /**
     * @return the filterLabels
     */
    public Set<String> getSelectedTypes() {
        return selectedTypes;
    }

    /**
     * @param filterLabels the filterLabels to set
     */
    public void setSelectedTypes(Set<String> filterLabels) {
        this.selectedTypes = filterLabels;
    }

    /**
     *
     * @param advance
     */
    public void setAdvance(boolean advance) {
        this.advance = advance;
    }

    /**
     *
     * @return
     */
    public boolean isAdvance() {
        return advance;
    }

}
