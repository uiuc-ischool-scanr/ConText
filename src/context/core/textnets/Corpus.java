/**
 *
 */
package context.core.textnets;

import context.app.Validation;
import context.core.textnets.Network.FileType;
import java.util.HashMap;
import java.util.Set;
import org.openide.util.Exceptions;
import org.thehecklers.monologfx.MonologFX;

/**
 * @author Shubhanshu
 *
 */
public abstract class Corpus {

    /**
     *
     */
    public static enum LABELTYPES {

        /**
         *
         */
        ENTITY,
        /**
         *
         */
        CODEBOOK_LABEL,
        /**
         *
         */
        CODEBOOK_CLASS,
        /**
         *
         */
        POS,
        /**
         *
         */
        PARSETREE
    };

    /**
     *
     */
    public static enum UNITOFANALYSIS {

        /**
         *
         */
        SENTENCE,
        /**
         *
         */
        PARAGRAPH,
        /**
         *
         */
        TEXT,
        /**
         *
         */
        CUSTOM_TAG
    };

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     *
     */
    public static HashMap<LABELTYPES, Set<String>> labels;
    private Set<String> filterLabels;

    /**
     *
     */
    public String customTag = "";

    private UNITOFANALYSIS unit;

    private String tabularOutPath = "";
    private HashMap<String, TextStream> streams;

    private boolean docLevel;

    private int windowSize;

    /**
     *
     */
    public Corpus() {
        // TODO Auto-generated constructor stub
        this.streams = new HashMap<String, TextStream>();
        this.unit = UNITOFANALYSIS.SENTENCE;
        labels = new HashMap<Corpus.LABELTYPES, Set<String>>();

    }

    /**
     *
     * @param fileName
     * @param ts
     */
    public void addStream(String fileName, TextStream ts) {
        this.streams.put(fileName, ts);
    }

    /**
     * @return the customTag
     */
    public synchronized String getCustomTag() {
        return customTag;
    }

    /**
     * @return the filterLabels
     */
    public synchronized Set<String> getFilterLabels() {
        return filterLabels;
    }

    /**
     * @param l
     * @return the labels
     */
    public Set<String> getLabels(LABELTYPES l) {
        return labels.get(l);
    }

    /**
     * @return the streams
     */
    public HashMap<String, TextStream> getStreams() {
        return streams;
    }

    /**
     * @return the tabularOutPath
     */
    public synchronized String getTabularOutPath() {
        return tabularOutPath;
    }

    /**
     * @return the unit
     */
    public synchronized UNITOFANALYSIS getUnit() {
        return unit;
    }

    /**
     * @return the windowSize
     */
    public int getWindowSize() {
        return windowSize;
    }

    /**
     * @return the docLevel
     */
    public boolean isDocLevel() {
        return docLevel;
    }

    /**
     *
     * @param fileName
     * @param outputDir
     * @param ft
     * @param l
     */
    public void saveNetworks(String fileName, String outputDir, FileType ft,
            LABELTYPES l) {
        try {
            if (docLevel) {
                for (String key : streams.keySet()) {
                    Network net = new Network();
                    net.setEdgeTablePath(this.tabularOutPath + "_" + key + ".csv");
                    TextStream t = streams.get(key);
                    t.makeNetwork(net, windowSize, l, unit);
                    net.saveNet(fileName + "_" + key, outputDir, ft);
                }
            } else {
                Network net = new Network();
                net.setEdgeTablePath(this.tabularOutPath);
                for (String key : streams.keySet()) {
                    TextStream t = streams.get(key);
                    t.makeNetwork(net, windowSize, l, unit);
                }
                net.saveNet(fileName, outputDir, ft);
            }
        } catch (Exception ex) {
            MonologFX mono = Validation.buildWarningButton("Error on Entity Network process", "Error");
            mono.show();
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * @param customTag the customTag to set
     */
    public synchronized void setCustomTag(String customTag) {
        this.customTag = customTag;
    }

    /**
     * @param docLevel the docLevel to set
     */
    public void setDocLevel(boolean docLevel) {
        this.docLevel = docLevel;
    }

    /**
     * @param filterLabels the filterLabels to set
     */
    public synchronized void setFilterLabels(Set<String> filterLabels) {
        this.filterLabels = filterLabels;
    }

    /**
     * @param labels the labels to set
     * @param l
     */
    public void setLabels(Set<String> labelList, LABELTYPES l) {
        labels.put(l, labelList);
    }

    /**
     * @param streams the streams to set
     */
    public void setStreams(HashMap<String, TextStream> streams) {
        this.streams = streams;
    }

    /**
     * @param tabularOutPath the tabularOutPath to set
     */
    public synchronized void setTabularOutPath(String tabularOutPath) {
        this.tabularOutPath = tabularOutPath;
    }

    /**
     * @param unit the unit to set
     */
    public synchronized void setUnit(UNITOFANALYSIS unit) {
        this.unit = unit;
    }

    /**
     * @param windowSize the windowSize to set
     */
    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

}
