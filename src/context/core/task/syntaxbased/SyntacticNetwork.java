package context.core.task.syntaxbased;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.task.pos.POSTagger;
import context.core.util.CorpusAggregator;
import context.core.util.JavaIO;
import context.core.util.MyPair;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;

/**
 *
 * @author Aale
 */
public class SyntacticNetwork {

    /**
     * @param args
     */
    private StanfordCoreNLP pipeline;
    private StanfordCoreNLP POSpipeline;
    private List<String[]> NetworkEdges;
    private HashSet<String[]> NodeHashSet;
    private SyntaxBasedTaskInstance instance;
    private CorpusData input;
    private List<TabularData> tabularOutput;
    private List<List<String[]>> toAggregate;
    private List<List<List<String[]>>> POStags;
    private List<String> POStoKeepTrack;
    private HashMap<String, List<String>> graphComponents;
    private HashMap<List<String>, List<List<String>>> POSedgeMap;
    private List<String> dependencyEdges;
    private int unitOfAnalysis;
    private long timeout;
    private int distance;

    /**
     *
     * @param instance
     */
    public SyntacticNetwork(SyntaxBasedTaskInstance instance) {
        // TODO Auto-generated method stub

        this.instance = instance;
        init();
    }

    private void init() {

        POSedgeMap = instance.getEdgeMap();
        this.dependencyEdges = instance.getDependencyEdges();
        this.unitOfAnalysis = instance.getUnitOfAnalysis();
        this.timeout = instance.getTimeout();
        this.distance = instance.getDistance();
        this.input = (CorpusData) instance.getInput();
        this.pipeline = instance.getPipeline();
        this.POSpipeline = instance.getPipelinePOS();
        this.tabularOutput = instance.getTabularOutput();
        NodeHashSet = new HashSet<String[]>();
        NetworkEdges = new ArrayList<String[]>();
        POStags = new ArrayList<List<List<String[]>>>();

        graphComponents = new HashMap<String, List<String>>();
        List<String> properNouns = new ArrayList<String>();
        properNouns.add("NNP");
        properNouns.add("NNPS");
        List<String> commonNouns = new ArrayList<String>();
        commonNouns.add("NN");
        commonNouns.add("NNS");
        List<String> verbs = new ArrayList<String>();
        verbs.add("VB");
        verbs.add("VBD");
        verbs.add("VBZ");
        verbs.add("VBG");
        verbs.add("VBN");
        verbs.add("VBP");
        List<String> conjunctions = new ArrayList<String>();
        conjunctions.add("CC");
        List<String> numbers = new ArrayList<String>();
        numbers.add("CD");
        List<String> foreign = new ArrayList<String>();
        foreign.add("FW");
        List<String> modals = new ArrayList<String>();
        modals.add("MD");
        List<String> pronouns = new ArrayList<String>();
        pronouns.add("PRP");
        pronouns.add("PRP$");
        pronouns.add("WP");
        pronouns.add("WP$");
        List<String> adjectives = new ArrayList<String>();
        adjectives.add("JJ");
        adjectives.add("JJR");
        adjectives.add("JJS");
        List<String> symbols = new ArrayList<String>();
        symbols.add("SYM");
        List<String> interjections = new ArrayList<String>();
        interjections.add("UH");
        try {
            graphComponents.put("NN", commonNouns);
            graphComponents.put("NNS", commonNouns);
            graphComponents.put("NNP", properNouns);
            graphComponents.put("NNPS", properNouns);
            graphComponents.put("CC", conjunctions);
            graphComponents.put("CD", numbers);
            graphComponents.put("FW", foreign);
            graphComponents.put("MD", modals);
            graphComponents.put("PRP", pronouns);
            graphComponents.put("PRP$", pronouns);
            graphComponents.put("WP", pronouns);
            graphComponents.put("WP$", pronouns);
            graphComponents.put("VB", verbs);
            graphComponents.put("VBD", verbs);
            graphComponents.put("VBG", verbs);
            graphComponents.put("VBN", verbs);
            graphComponents.put("VBZ", verbs);
            graphComponents.put("VBP", verbs);
            graphComponents.put("JJ", adjectives);
            graphComponents.put("JJR", adjectives);
            graphComponents.put("JJS", adjectives);
            graphComponents.put("SYM", symbols);
            graphComponents.put("UH", interjections);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /////
        ///// Fixed params for Demo
        /////
        this.distance = 7;
        this.unitOfAnalysis = 2;
        this.timeout = 120000;

        this.POStoKeepTrack = new ArrayList<String>();
        POStoKeepTrack.add("NN");
        POStoKeepTrack.add("NNS");
        POStoKeepTrack.add("NNP");
        POStoKeepTrack.add("NNPS");

        POSedgeMap = new HashMap<List<String>, List<List<String>>>();
        List<List<String>> commonNounEdges = new ArrayList<List<String>>();
        commonNounEdges.add(properNouns);
        List<List<String>> properNounEdges = new ArrayList<List<String>>();
        properNounEdges.add(commonNouns);
        POSedgeMap.put(commonNouns, commonNounEdges);
        POSedgeMap.put(properNouns, properNounEdges);

        dependencyEdges = new ArrayList<String>();
        dependencyEdges.add("nsubj");
        dependencyEdges.add("dobj");
        /////
        ///// End fixed params
        /////

    }

    /**
     *
     * @return
     */
    public boolean genPOSNetwork() {
        List<FileData> files = input.getFiles();
        toAggregate = new ArrayList<List<String[]>>();
        try {
            for (FileData ff : files) {
                File file = ff.getFile();
                String text = JavaIO.readFile(file);
                text = text.replaceAll("\\p{Cc}", " ");
                text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"-]", " ");
                //text = text.replaceAll("[^\\x00-\\x7F]", "");
                Annotation document = new Annotation(text);
                POSpipeline.annotate(document);

                List<List<String[]>> DocPOSTags = new ArrayList<List<String[]>>();
                List<CoreMap> sentences = document.get(SentencesAnnotation.class);

                int placeInDoc = 0;
                for (CoreMap sentence : sentences) {
                    List<String[]> sentPOStags = new ArrayList<String[]>();
                    // traversing the words in the current sentence
                    // a CoreLabel is a CoreMap with additional token-specific methods
                    int placeInSent = 0;

                    final List<CoreLabel> sent = sentence.get(TokensAnnotation.class);
                    final List<TaggedWord> taggedWords = POSTagger.tag(sent, "en");
                    for (TaggedWord token : taggedWords) {
                        // this is the text of the token
                        String word = token.word();
                        // this is the POS tag of the token
                        String pos = token.tag();
                        String[] entity = {word, pos, Integer.toString(placeInSent), Integer.toString(placeInDoc)};
                        placeInSent++;
                        placeInDoc++;
                        if (!word.matches("^[a-zA-Z0-9]*$")) {
                            continue;
                        }
                        if (!POStoKeepTrack.contains(pos)) {
                            continue;
                        }
                        String[] hashNode = new String[2];
                        hashNode[0] = word;
                        hashNode[1] = pos;
                        NodeHashSet.add(hashNode);
                        sentPOStags.add(entity);
                    }
                    DocPOSTags.add(sentPOStags);
                }
                POStags.add(DocPOSTags);
            }

            if (unitOfAnalysis == 1) {
                for (List<List<String[]>> DocPOSTags : POStags) {
                    String[] word;
                    List<String[]> docAggregate = new ArrayList<String[]>();
                    for (int overIndx = 0; overIndx < DocPOSTags.size(); overIndx++) {
                        List<String[]> SentPOSTags = DocPOSTags.get(overIndx);
                        for (int indx = 0; indx < SentPOSTags.size(); indx++) {
                            word = SentPOSTags.get(indx);
                            List<String[]> TempSentPOSTags = SentPOSTags;
                            int tempIndex = indx + 1;
                            if (tempIndex >= SentPOSTags.size()) {
                                break;
                            }
                            String[] tempWord = null;
                            try {
                                tempWord = TempSentPOSTags.get(tempIndex);
                            } catch (Exception tempE) {
                                tempE.printStackTrace();
                            }
                            while (Integer.parseInt(tempWord[3]) - Integer.parseInt(word[3]) < distance) {
                                if (POSedgeMap.get(graphComponents.get(tempWord[1])).contains(graphComponents.get(word[1]))) {
                                    String[] tempPOSEdge = new String[5];
                                    tempPOSEdge[0] = word[0];
                                    tempPOSEdge[1] = word[1];
                                    tempPOSEdge[2] = tempWord[0];
                                    tempPOSEdge[3] = tempWord[1];
                                    tempPOSEdge[4] = "1";
                                    docAggregate.add(tempPOSEdge);
                                }
                                tempIndex++;
                                if (tempIndex >= TempSentPOSTags.size()) {
                                    break;
                                }
                                try {
                                    tempWord = TempSentPOSTags.get(tempIndex);
                                } catch (Exception tempE) {
                                    tempE.printStackTrace();
                                }
                            }
                        }
                    }
                    toAggregate.add(docAggregate);
                }
                NetworkEdges = new CorpusAggregator().CorpusAggregate(toAggregate);
            }

            if (unitOfAnalysis == 2) {
                for (List<List<String[]>> DocPOSTags : POStags) {
                    String[] word;
                    List<String[]> docAggregate = new ArrayList<String[]>();
                    for (int overIndx = 0; overIndx < DocPOSTags.size(); overIndx++) {
                        List<String[]> SentPOSTags = DocPOSTags.get(overIndx);
                        for (int indx = 0; indx < SentPOSTags.size(); indx++) {
                            word = SentPOSTags.get(indx);
                            List<String[]> TempSentPOSTags = SentPOSTags;
                            int tempIndex = indx + 1;
                            int tempOverIndex = overIndx;
                            if (tempIndex >= SentPOSTags.size()) {
                                TempSentPOSTags = null;
                                Boolean breakCondition = false;
                                while (TempSentPOSTags == null || TempSentPOSTags.size() == 0) {
                                    if (tempOverIndex + 1 < DocPOSTags.size()) {
                                        tempIndex = 0;
                                        tempOverIndex++;
                                        TempSentPOSTags = DocPOSTags.get(tempOverIndex);
                                    } else {
                                        breakCondition = true;
                                        break;
                                    }
                                }
                                if (breakCondition) {
                                    break;
                                }

                            }
                            String[] tempWord = null;
                            try {
                                tempWord = TempSentPOSTags.get(tempIndex);
                            } catch (Exception tempE) {
                                tempE.printStackTrace();
                            }
                            while (Integer.parseInt(tempWord[3]) - Integer.parseInt(word[3]) < distance) {
                                if (POSedgeMap.get(graphComponents.get(tempWord[1])).contains(graphComponents.get(word[1]))) {
                                    String[] tempPOSEdge = new String[5];
                                    tempPOSEdge[0] = word[0];
                                    tempPOSEdge[1] = word[1];
                                    tempPOSEdge[2] = tempWord[0];
                                    tempPOSEdge[3] = tempWord[1];
                                    tempPOSEdge[4] = "1";
                                    docAggregate.add(tempPOSEdge);
                                }
                                tempIndex++;
                                if (tempIndex >= TempSentPOSTags.size()) {
                                    TempSentPOSTags = null;
                                    boolean breakCondition = false;
                                    while (TempSentPOSTags == null || TempSentPOSTags.size() == 0) {
                                        if (tempOverIndex + 1 < DocPOSTags.size()) {
                                            tempIndex = 0;
                                            tempOverIndex++;
                                            TempSentPOSTags = DocPOSTags.get(tempOverIndex);
                                        } else {
                                            breakCondition = true;
                                            break;
                                        }
                                    }
                                    if (breakCondition) {
                                        break;
                                    }
                                }
                                try {
                                    tempWord = TempSentPOSTags.get(tempIndex);
                                } catch (Exception tempE) {
                                    tempE.printStackTrace();
                                }
                            }
                        }
                    }
                    toAggregate.add(docAggregate);
                }
                NetworkEdges = new CorpusAggregator().CorpusAggregate(toAggregate);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public boolean genNetwork() {

        toAggregate = new ArrayList<List<String[]>>();
        List<FileData> files = input.getFiles();
        try {
            for (FileData ff : files) {

                File file = ff.getFile();
                final File finalFile = file;
                System.out.println(file.getName());
                Thread myThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        genFileSyntax(finalFile);
                    }
                });
                myThread.start();
                long endTimeMillis = System.currentTimeMillis() + timeout;
                while (myThread.isAlive()) {
                    if (System.currentTimeMillis() > endTimeMillis) {
                        myThread.interrupt();
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException t) {
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        NetworkEdges = new CorpusAggregator().CorpusAggregate(toAggregate);
        return true;
    }

    private boolean genFileSyntax(File file) {
        try {
            String text = JavaIO.readFile(file);
            text = text.replaceAll("\\p{Cc}", " ");
            text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"-]", " ");
            // create an empty Annotation just with the given text
            Annotation document = new Annotation(text);

            // run all Annotators on this text
            pipeline.annotate(document);

            // these are all the sentences in this document
            // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
            List<CoreMap> sentences = document.get(SentencesAnnotation.class);

            for (CoreMap sentence : sentences) {
                if (Thread.interrupted()) {
                    return false;
                }
                Tree tree = sentence.get(TreeAnnotation.class);
                //System.out.println(dependencies.getAllNodesByWordPattern("it"));
                TreebankLanguagePack tlp = new PennTreebankLanguagePack();
                GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
                if (tree == null) {
                    continue;
                }
                GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
                Collection tdl = gs.typedDependenciesCollapsed();
                Iterator iter1 = tdl.iterator();
                String typedDep = "";
                List<String[]> tempDependencies = new ArrayList<String[]>();
                while (iter1.hasNext()) {
                    typedDep = iter1.next().toString();

                    if (typedDep.trim().split("\\(")[0].equals("nsubj") && dependencyEdges.contains("nsubj")) {
                        String[] tempDependency = new String[5];
                        tempDependency[0] = typedDep.trim().split("\\(")[1].split(",")[1].trim().split("-")[0];
                        tempDependency[1] = "";
                        tempDependency[2] = typedDep.trim().split("\\(")[1].split(",")[0].trim().split("-")[0];
                        tempDependency[3] = "";
                        tempDependency[4] = Integer.toString(1);
                        tempDependencies.add(tempDependency);

                    }
                    if (typedDep.trim().split("\\(")[0].equals("nsubjpass") && dependencyEdges.contains("nsubjpass")) {
                        String[] tempDependency = new String[5];
                        tempDependency[0] = typedDep.trim().split("\\(")[1].split(",")[0].trim().split("-")[0];
                        tempDependency[1] = "";
                        tempDependency[2] = typedDep.trim().split("\\(")[1].split(",")[1].trim().split("-")[0];
                        tempDependency[3] = "";
                        tempDependency[4] = Integer.toString(1);
                        tempDependencies.add(tempDependency);

                    }
                    if (typedDep.trim().split("\\(")[0].equals("dobj") && dependencyEdges.contains("dobj")) {
                        String[] tempDependency = new String[5];
                        tempDependency[0] = typedDep.trim().split("\\(")[1].split(",")[0].trim().split("-")[0];
                        tempDependency[1] = "";
                        tempDependency[2] = typedDep.trim().split("\\(")[1].split(",")[1].trim().split("-")[0];
                        tempDependency[3] = "";
                        tempDependency[4] = Integer.toString(1);
                        tempDependencies.add(tempDependency);
                    }
                    if (typedDep.trim().split("\\(")[0].equals("iobj") && dependencyEdges.contains("iobj")) {
                        String[] tempDependency = new String[5];
                        tempDependency[0] = typedDep.trim().split("\\(")[1].split(",")[0].trim().split("-")[0];
                        tempDependency[1] = "";
                        tempDependency[2] = typedDep.trim().split("\\(")[1].split(",")[1].trim().split("-")[0];
                        tempDependency[3] = "";
                        tempDependency[4] = Integer.toString(1);
                        tempDependencies.add(tempDependency);
                    }

                }

                final List<CoreLabel> sent = sentence.get(TokensAnnotation.class);
                final List<TaggedWord> taggedWords = POSTagger.tag(sent, "en");
                for (TaggedWord token : taggedWords) {
                    // this is the text of the token
                    String word = token.word();
                    // this is the POS tag of the token
                    String pos = token.tag();
                    for (String[] tempDep : tempDependencies) {
                        if (tempDep[0].equals(word)) {
                            tempDep[1] = pos;
                            String[] hashNode = new String[2];
                            hashNode[0] = word;
                            hashNode[1] = pos;
                            NodeHashSet.add(hashNode);
                        }
                        if (tempDep[2].equals(word)) {
                            tempDep[3] = pos;
                            String[] hashNode = new String[2];
                            hashNode[0] = word;
                            hashNode[1] = pos;
                            NodeHashSet.add(hashNode);
                        }
                    }
                }

                toAggregate.add(tempDependencies);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public String[][] getNetworkEdges() {
        String[][] NetworkEdgesArray = new String[NetworkEdges.size()][5];
        NetworkEdgesArray = NetworkEdges.toArray(NetworkEdgesArray);
        return NetworkEdgesArray;
    }

    /**
     *
     * @return
     */
    public String[][] getNetworkNodes() {
        String[][] NetworkNodes = new String[NodeHashSet.size()][2];
        NetworkNodes = NodeHashSet.toArray(NetworkNodes);
        return NetworkNodes;

    }

    /**
     *
     * @param filename
     * @return
     */
    public boolean extractGephiOutput(String filename) {

        String[][] nodes_str = this.getNetworkNodes();
        String[][] edges_str = this.getNetworkEdges();

        File new_file = new File(filename);
        if (new_file.exists()) {
            new_file.delete();
        }

        //Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Get a graph model - it exists because we have a workspace
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        final DirectedGraph directedGraph = graphModel.getDirectedGraph();

        TObjectIntHashMap<String> nodes = new TObjectIntHashMap<String>();

        Vector<String> node_index = new Vector<String>();

        //Create the nodes
        for (String[] node_str : nodes_str) {

            if (nodes.containsKey(node_str[0])) {
                int index = nodes.get(node_str[0]);
                node_index.set(index, node_index.get(index) + "," + node_str[1]);
            } else {
                node_index.add(node_str[1]);
                nodes.put(node_str[0], node_index.size() - 1);
            }
        }

        for (TObjectIntIterator<String> node_it = nodes.iterator(); node_it.hasNext();) {
            node_it.advance();

            Node n0 = graphModel.factory().newNode(node_it.key());

            n0.getAttributes().setValue("label", node_it.key());
            n0.getAttributes().setValue("Type", node_index.get(node_it.value()));
            directedGraph.addNode(n0);
        }

        TObjectIntHashMap<MyPair<String, String>> edges = new TObjectIntHashMap<MyPair<String, String>>();

        for (String[] edge_str : edges_str) {

            MyPair<String, String> edge = new MyPair(edge_str[0], edge_str[2]);

            int value = Integer.parseInt(edge_str[4]);
            edges.adjustOrPutValue(edge, value, value);
        }

        TObjectIntIterator<MyPair<String, String>> edge_it;

        for (edge_it = edges.iterator(); edge_it.hasNext();) {
            edge_it.advance();

            Node s1 = directedGraph.getNode(edge_it.key().getFirst());
            Node s2 = directedGraph.getNode(edge_it.key().getSecond());
            if (s1 == null || s2 == null) {
                continue;
            }

            int weight = edge_it.value();

            Edge e0 = graphModel.factory().newEdge(s1, s2, weight, true);
            directedGraph.addEdge(e0);

        }

        //Export full graph
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        try {
            ec.exportFile(new_file);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     *
     * @param instance
     * @param output_address
     * @return
     */
    public static boolean runUnit(SyntaxBasedTaskInstance instance, String output_address) {

        SyntacticNetwork SN = new SyntacticNetwork(instance);
        SN.genNetwork();
        if (SN.extractGephiOutput(output_address)) {
            return false;
        }

        return true;
    }

}
