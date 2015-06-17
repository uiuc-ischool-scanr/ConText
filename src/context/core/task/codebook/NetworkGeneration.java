package context.core.task.codebook;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.util.CodebookUtils;
import context.core.util.JavaIO;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.project.api.ProjectController;
import org.openide.util.Lookup;

/**
 *
 * @author Kiumars Soltani
 *
 */
public class NetworkGeneration {

    private Codebook cb;
    private CodebookApplicationTaskInstance instance;
    private Vector<Pair<String, String>> cbInfo;

    /**
     *
     * @param cb
     * @param instance
     */
    public NetworkGeneration(Codebook cb, CodebookApplicationTaskInstance instance) {
        this.cb = cb;
        this.instance = instance;
        this.cbInfo = this.cb.getCbInfo();
    }

    private TObjectIntHashMap<String> fillCodebookSet() {

        TObjectIntHashMap<String> codebookSet = new TObjectIntHashMap<String>();
        for (int i = 0; i < this.cbInfo.size(); i++) {
            Pair<String, String> ce = this.cbInfo.get(i);

            if (instance.getIsNormal() == 0) {
                codebookSet.put(ce.getLeft().toLowerCase(), i);
            } else { // == 1
                codebookSet.put(ce.getRight().toLowerCase(), i);
            }
        }

        return codebookSet;
    }

    private void makeNetwork(Vector<String> words, TIntObjectHashMap<Pair<String, String>> nodes,
            TObjectIntHashMap<Pair<Integer, Integer>> edges, TObjectIntHashMap<String> setCodebook) {
        String word = "";
        Integer wordIndex = null;
        int windowSize = instance.getDistance();

        /**
         * Start with an element in the words vector and seek ahead till the
         * window size. If you find an element then create an edge else move
         * ahead. Once all elements in windowSize are finished increment to the
         * next element in the words vector and repeat. TODO - Add feature for
         * skipping the seperator.
         */
        for (int i = 0; i < words.size() - windowSize; i++) {
            word = words.get(i);
            if (word == null) {
                continue;
            }
            word = word.toLowerCase();
            if (setCodebook.contains(word)) {
                wordIndex = i;
            } else {
                wordIndex = null;
            }
//            System.out.println("Source: " + word + ", " + wordIndex);
            int dist = 0;
            for (int j = 1; j + i < words.size() && dist <= windowSize && wordIndex != null; j++) {
                Integer targetIndex = i + j;
                String target = words.get(targetIndex);
                target = target.toLowerCase();
//                System.out.println("Target: (" + dist + ")" + target + ", " + targetIndex);
                if (target == null) {
//                    System.out.println("Seperator found and also breaking out of window: (" + dist + ")" + target + ", " + targetIndex);
                    break;
                }
                if (target.equals(".")) {
//                    System.out.println("Seperator found: (" + dist + ")" + target + ", " + targetIndex);
                    continue;
                }
//                System.out.println("No Seperator found: (" + dist + ")" + target + ", " + targetIndex);
                dist++;
                if (!setCodebook.contains(target)) {
                    continue;
                }

                int id1 = setCodebook.get(word);
                int id2 = setCodebook.get(target);
//                System.out.println("Edge: " + id1 + ", " + id2);
                if (id1 == id2) {
                    continue;
                }

                Pair<String, String> cb1 = this.cbInfo.get(id1);
                Pair<String, String> cb2 = this.cbInfo.get(id2);
//                System.out.println("Edge: " + cb1 + ", " + cb2);

                nodes.put(id1, new ImmutablePair<String, String>(cb1.getLeft(), cb1.getRight()));
                nodes.put(id2, new ImmutablePair<String, String>(cb2.getLeft(), cb2.getRight()));
//                System.out.println("Nodes: " + nodes);

                if (id1 <= id2) {
                    edges.adjustOrPutValue(new ImmutablePair<Integer, Integer>(id1, id2), 1, 1);
                } else {
                    edges.adjustOrPutValue(new ImmutablePair<Integer, Integer>(id2, id1), 1, 1);
                }
//                System.out.println("Added Edge: " + id1 + "," + id2);

            }
//            System.out.println("Exceeded window size: " + windowSize + ". Sliding window now.");
        }
        System.out.println("Finished Generating Network now off to printing files. ");

    }

    /**
     *
     * @param nodes
     * @param edges
     * @param csvFilepath
     * @param gexfFilepath
     */
    public void writeOutput(TIntObjectHashMap<Pair<String, String>> nodes,
            TObjectIntHashMap<Pair<Integer, Integer>> edges, String csvFilepath, String gexfFilepath) {

        //Write CSV
        if (instance.isNetOutputCSV()) {
            this.writeCsv(edges, csvFilepath);
        }

        //Write GEXF
        if (instance.isNetOutputGEXF()) {
            this.writeGexf(nodes, edges, gexfFilepath);
        }
    }

    private boolean writeGexf(TIntObjectHashMap<Pair<String, String>> nodes,
            TObjectIntHashMap<Pair<Integer, Integer>> edges, String filepath) {

        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
		//Workspace workspace = pc.getCurrentWorkspace();

        //Get a graph model - it exists because we have a workspace
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        final UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();

        //Create three nodes
        TIntObjectIterator<Pair<String, String>> node_it;
        for (node_it = nodes.iterator(); node_it.hasNext();) {
            node_it.advance();
            Node n0 = graphModel.factory().newNode(node_it.value().getLeft());
            n0.getAttributes().setValue("label", node_it.value().getLeft());
            if (instance.getNetOutputType() == 1) {
                n0.getAttributes().setValue("Type", node_it.value().getRight());
            }
            undirectedGraph.addNode(n0);

        }

        TObjectIntIterator<Pair<Integer, Integer>> edge_it;
        for (edge_it = edges.iterator(); edge_it.hasNext();) {
            edge_it.advance();
            Node s1 = undirectedGraph.getNode(nodes.get(edge_it.key().getLeft()).getLeft());
            Node s2 = undirectedGraph.getNode(nodes.get(edge_it.key().getRight()).getLeft());

            float weight = edge_it.value();
            Edge e0 = graphModel.factory().newEdge(s1, s2, weight, false);
            undirectedGraph.addEdge(e0);
        }

        //Export full graph
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try {
            ec.exportFile(new File(filepath));
        } catch (IOException ex) {
            return false;
        }

        return true;
    }

    private void writeCsv(TObjectIntHashMap<Pair<Integer, Integer>> edges, String filepath) {

        StringBuffer sb = new StringBuffer();

        if (instance.getNetOutputType() == 0) {
            sb.append("source,target,weight\n");
        } else {
            sb.append("source,source_type,target,target_type,weight\n");
        }

        TObjectIntIterator<Pair<Integer, Integer>> networkIt;
        for (networkIt = edges.iterator(); networkIt.hasNext();) {
            networkIt.advance();

            Pair<String, String> node1 = this.cbInfo.get(networkIt.key().getLeft());
            Pair<String, String> node2 = this.cbInfo.get(networkIt.key().getRight());

            if (instance.getNetOutputType() == 0) {
                sb.append(node1.getLeft() + "," + node2.getLeft() + "," + networkIt.value() + "\n");
            } else {
                sb.append(node1.getLeft() + "," + node1.getRight() + ","
                        + node2.getLeft() + "," + node2.getRight() + "," + networkIt.value() + "\n");
            }
        }

        FileData.writeDataIntoFile(sb.toString(), filepath);
    }

    /**
     *
     * @return
     */
    public boolean applyNetwork() {

        TObjectIntHashMap<String> setCodebook = this.fillCodebookSet();
        TIntObjectHashMap<Pair<String, String>> nodes = new TIntObjectHashMap<Pair<String, String>>();
        TObjectIntHashMap<Pair<Integer, Integer>> edges = new TObjectIntHashMap<Pair<Integer, Integer>>();

        CorpusData output = (CorpusData) this.instance.getTextOutput();
        List<FileData> files = output.getFiles();
        String path = FilenameUtils.getFullPath(files.get(0).getFile().getAbsolutePath());
        /**
         * The following line is to insure that the network files are not
         * written to the same folder as the Codebook applied corpus Very Dirty
         * Hack to get it working. Author: Shubhanshu Mishra
         *
         */
        path = path + "../";

        try {
            for (FileData f : files) {
                String content = JavaIO.readFile(f.getFile());
//                System.out.println("content before =======" + content);
//                content = codebookificationContent(content, setCodebook);
//                System.out.println("content after========" + content);
                if (content.isEmpty()) {
                    continue;
                }
                Vector<String> words = CodebookUtils.getWords(content, instance.getSeparator(), instance.getCustomTag());

                if (!instance.isNetInputCorpus()) {
                    nodes.clear();
                    edges.clear();
                    makeNetwork(words, nodes, edges, setCodebook);

                    String nameInputFileWithoutExtension = FilenameUtils.getBaseName(f.getFile().getName());
                    path = FilenameUtils.getFullPath(f.getFile().getAbsolutePath());
                    final String filepath = path + nameInputFileWithoutExtension + "-Network";
                    System.out.println("filepath (without extension)=" + filepath);
                    this.writeOutput(nodes, edges, filepath + ".csv", filepath + ".gexf");
                } else {
                    makeNetwork(words, nodes, edges, setCodebook);
                }

            }

            if (instance.isNetInputCorpus()) {
                this.writeOutput(nodes, edges, path + "CorpusNetwork.csv", path + "CorpusNetwork.gexf");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private String codebookificationContent(String content, TObjectIntHashMap<String> setCodebook) {
        TObjectIntIterator<String> cb_it;
//        content = content.toLowerCase();
        for (cb_it = setCodebook.iterator(); cb_it.hasNext();) {
            cb_it.advance();
            System.out.println(cb_it.key() + " " + cb_it.value());
            System.out.println("replaceUnderscore=" + replaceUnderscores(cb_it.key()));
            System.out.println("repalced by=" + cb_it.key().toLowerCase());
            content = content.replace(replaceUnderscores(cb_it.key()), cb_it.key().toLowerCase());
            System.out.println("updated content = " + content);
        }
        return content;

    }

    private CharSequence replaceUnderscores(String str) {
        return str.replace('_', ' ').toLowerCase();
    }

}
