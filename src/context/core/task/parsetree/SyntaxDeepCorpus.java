/**
 *
 */
package context.core.task.parsetree;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.textnets.Network;
import context.core.textnets.Network.FileType;
import context.core.tokenizer.CustomEdge;
import context.core.tokenizer.SemanticAnnotation;
import context.core.util.JavaIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Shubhanshu
 *
 */
public class SyntaxDeepCorpus {

    /**
     *
     */
    private ParseTreeNetworkTaskInstance instance;
    private CorpusData input;
    private List<TabularData> tabularOutput;
    private HashMap<String, EdgeStream> streams;
    private boolean docLevel;
    private String tabularOutPath = "";
    private Set<String> selectedTypes;
    private boolean advanced;

    /**
     *
     * @param instance
     */
    public SyntaxDeepCorpus(ParseTreeNetworkTaskInstance instance) {
        // TODO Auto-generated constructor stub
        super();
        this.instance = instance;
        this.input = (CorpusData) instance.getInput();
        this.tabularOutput = instance.getTabularOutput();
        this.streams = new HashMap<>();
        this.selectedTypes = instance.getSelectedTypes();
        this.advanced = instance.isAdvance();
        if (instance.getAggregation() == 0) { // 0 - per document 1- per corpus
            this.setDocLevel(true);
        } else {
            this.setDocLevel(false);
        }
//        this.setUnit(UNITOFANALYSIS.SENTENCE);
        //        this.setWindowSize(Integer.MAX_VALUE);
        //        this.setFilterLabels(instance.getFilterLabels());
        //        System.err.println("Window Size: " + instance.getDistance() + ", " + this.getWindowSize());
        //        this.setDocLevel(false);

    }

    /**
     *
     * @param docLevel
     */
    public void setDocLevel(boolean docLevel) {
        this.docLevel = docLevel;
    }

    /**
     *
     * @param fileName
     * @param es
     */
    public void addStream(String fileName, EdgeStream es) {
        if (streams == null) {
            streams = new HashMap<>();
        }
        this.streams.put(fileName, es);
    }

    /**
     * @return the docLevel
     */
    public boolean isDocLevel() {
        return docLevel;
    }

    /**
     *
     */
    public void genStreamsFromCorpus() {

        System.err.println("Adding Text Streams: " + input.getFiles().size());
        List<FileData> files = input.getFiles();
        for (FileData ff : files) {
            System.out.println("Reading file: " + ff.getFile().getAbsolutePath());
            File file = ff.getFile();
            String text = "";
            try {
                text = JavaIO.readFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            text = text.replaceAll("\\p{Cc}", " ");
            text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"-]", " ");
            String fileName = ff.getName().getValue();
            final EdgeStream edgestream = getEdges(fileName, text);
            this.addStream(fileName, edgestream);
            System.err.println("Finished adding Text Stream: " + fileName);
        }
    }

    /**
     *
     * @param fileName
     * @param inText
     * @return
     */
    public EdgeStream getEdges(String fileName, String inText) {
        EdgeStream estr = new EdgeStream(fileName);
        Map<String, CustomEdge> tokens = null;
        if (advanced) {
            tokens = SemanticAnnotation.tokenize(inText, fileName);
            for (String key : tokens.keySet()) {
                final CustomEdge cedge = tokens.get(key);
                //TODO: more filtering here if needed.
                String type = cedge.getType();
//                cedge.setWord1(cedge.getWord1().toLowerCase());
//                cedge.setWord2(cedge.getWord2().toLowerCase());
                String prefix_type = getPrefix(type);
                if (this.selectedTypes.contains(prefix_type)) {
                    estr.addEdge(cedge);
                }
            }
        } else {
            tokens = SemanticAnnotation.tokenizeSPO(inText, fileName);
            for (String key : tokens.keySet()) {
                final CustomEdge cedge = tokens.get(key);
//                cedge.setWord1(cedge.getWord1().toLowerCase());
//                cedge.setWord2(cedge.getWord2().toLowerCase());
                estr.addEdge(cedge);
            }
        }
        return estr;

    }

    /**
     *
     * @param outputDir
     */
    public void saveNetworks(String outputDir) {
        String fileName = "SemanticNetwork";
        saveNetworks(fileName, outputDir, FileType.GRAPHML);

    }

    /**
     *
     * @param fileName
     * @param outputDir
     * @param ft
     */
    public void saveNetworks(String fileName, String outputDir, FileType ft) {
        if (docLevel) {
            for (String key : streams.keySet()) {
                Network net = new Network();
                net.setEdgeTablePath(this.tabularOutPath + "_" + key + ".csv");
                EdgeStream t = streams.get(key);
                t.makeNetwork(net);
                net.saveNet(fileName + "_" + key, outputDir, ft);
            }
        } else {
            Network net = new Network();
            net.setEdgeTablePath(this.tabularOutPath);
            for (String key : streams.keySet()) {
                EdgeStream t = streams.get(key);
                t.makeNetwork(net);
            }
            net.saveNet(fileName, outputDir, ft);
        }
    }

    /**
     * @return the tabularOutPath
     */
    public synchronized String getTabularOutPath() {
        return tabularOutPath;
    }

    /**
     * @param tabularOutPath the tabularOutPath to set
     */
    public synchronized void setTabularOutPath(String tabularOutPath) {
        this.tabularOutPath = tabularOutPath;
    }

    private static String getPrefix(String type) {
        int lastUnderlineIndex = type.lastIndexOf("_");
        if (lastUnderlineIndex == -1) {
            return type;
        } else {
            return type.substring(0, lastUnderlineIndex);
        }
    }
}
