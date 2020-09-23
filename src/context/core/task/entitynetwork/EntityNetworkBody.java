package context.core.task.entitynetwork;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.task.entitydetection.MultiWordEntities;
import context.core.task.syntaxbased.SyntacticNetwork;
import context.core.task.syntaxbased.SyntaxBasedTaskInstance;
import context.core.util.CorpusAggregator;
import context.core.util.ForAggregation;
import context.core.util.JavaIO;
import context.core.util.MyPair;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 *
 * @author Aale
 */
public class EntityNetworkBody {

	//EntityNetworkTask is not using EntityNetworkBody, but using EntityCorpus
	private AbstractSequenceClassifier<?> classifier3;
    private AbstractSequenceClassifier<?> classifier4;
    private AbstractSequenceClassifier<?> classifier7;
	private StanfordCoreNLP pipeline;
	private List<String[]> NetworkEdges;
	private HashSet<String[]> NodeHashSet;
	private EntityNetworkTaskInstance instance;
	private CorpusData input;
	private List<TabularData> tabularOutput;
	private List<List<String[]>> toAggregate;
	private List<List<List<String[]>>> entityTags;
	private List<String> EntitiesToKeepTrack;
	private int unitOfAnalysis;
	private long timeout;
	private int distance;
	private List<String[]> EntitiesWithOffset;
	
    /**
     *
     * @param instance
     */
    public EntityNetworkBody(EntityNetworkTaskInstance instance) {
		// TODO Auto-generated method stub

		this.instance = instance;
		init();
	}
	private void init(){

		this.EntitiesToKeepTrack = new ArrayList<String>();
		EntitiesToKeepTrack.add("PERSON");
		EntitiesToKeepTrack.add("ORGANIZATION");
		EntitiesToKeepTrack.add("MONEY");
		EntitiesToKeepTrack.add("LOCATION");
		String[] properNouns = new String[2];
		properNouns[0] = "NNP";
		properNouns[1] = "NNPS";
		String[] commonNouns = new String[2];
		commonNouns[0] = "NN";
		commonNouns[1] = "NNS";
		this.pipeline = instance.getPipeline();
		this.classifier3 = instance.get3Classifier();
        this.classifier4 = instance.get4Classifier();
        this.classifier7 = instance.get7Classifier();
		this.unitOfAnalysis = instance.getUnitOfAnalysis();
		this.distance = 7;
		this.unitOfAnalysis = 2;
		this.timeout = 120000;
		this.input = (CorpusData) instance.getInput();
		this.EntitiesWithOffset = new ArrayList<String[]>();
		
		this.tabularOutput = instance.getTabularOutput();
		NodeHashSet = new HashSet<String[]>();
		NetworkEdges = new ArrayList<String[]>();
		entityTags = new ArrayList<List<List<String[]>>>();
	}
	
    /**
     *
     * @return
     */
    public boolean genNetwork(){
		List<FileData> files = input.getFiles();
		toAggregate = new ArrayList<List<String[]>>();
		try{
			for (FileData ff:files){
				File file = ff.getFile();
				String text = JavaIO.readFile(file);
				text = text.replaceAll("\\p{Cc}", " ");
                text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"-]", " ");
				//text = text.replaceAll("[^\\x00-\\x7F]", "");
				Annotation document = new Annotation(text);
				pipeline.annotate(document);


				List<List<String[]>> DocPOSTags = new ArrayList<List<String[]>>();
				List<CoreMap> sentences = document.get(SentencesAnnotation.class);

				int placeInDoc = 0;
				for (CoreMap sentence : sentences) {
					List<String[]> sentPOStags = new ArrayList<String[]>();
					// traversing the words in the current sentence
					// a CoreLabel is a CoreMap with additional token-specific methods
					int placeInSent = 0;
					for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
						// this is the text of the token
						String word = token.get(TextAnnotation.class);
						// this is the POS tag of the token
						String pos = token.get(PartOfSpeechAnnotation.class);
						String[] entity = {word, pos, Integer.toString(placeInSent), Integer.toString(placeInDoc)};
						placeInSent++;
						placeInDoc++;
						if (!word.matches("^[a-zA-Z0-9]*$")) {
							continue;
						}
						if(!EntitiesToKeepTrack.contains(pos)){
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
				entityTags.add(DocPOSTags);
			}

			if(unitOfAnalysis ==2){
				for (List<List<String[]>> DocEntityTags: entityTags){
					String[] word;
					List<String[]> docAggregate = new ArrayList<String[]>();
					for (int overIndx = 0; overIndx < DocEntityTags.size(); overIndx ++){
						List<String[]> SentEntityTags = DocEntityTags.get(overIndx);
						for (int indx = 0; indx < SentEntityTags.size(); indx ++){
							word = SentEntityTags.get(indx);
							List<String[]> TempSentEntitytTags = SentEntityTags;
							int tempIndex = indx + 1;
							int tempOverIndex = overIndx;
							if (tempIndex >= SentEntityTags.size()){
								TempSentEntitytTags = null;
								Boolean breakCondition = false;
								while(TempSentEntitytTags == null || TempSentEntitytTags.size() == 0){
									if (tempOverIndex + 1 < DocEntityTags.size()){
										tempIndex = 0;
										tempOverIndex ++;
										TempSentEntitytTags = DocEntityTags.get(tempOverIndex);
									}
									else{
										breakCondition = true;
										break;
									}
								}
								if (breakCondition){
									break;
								}

							}
							String[] tempWord = null;
							try{
								tempWord = TempSentEntitytTags.get(tempIndex);
							} catch(Exception tempE){
								tempE.printStackTrace();
							}
							while (Integer.parseInt(tempWord[3]) - Integer.parseInt(word[3])<distance){
								String[] tempEntityEdge = new String[5];
								tempEntityEdge[0] = word[0];
								tempEntityEdge[1] = word[1];
								tempEntityEdge[2] = tempWord[0];
								tempEntityEdge[3] = tempWord[1];
								tempEntityEdge[4] = "1";
								docAggregate.add(tempEntityEdge);
								tempIndex ++;
								if (tempIndex >= TempSentEntitytTags.size()){
									TempSentEntitytTags = null;
									boolean breakCondition = false;
									while(TempSentEntitytTags == null || TempSentEntitytTags.size() == 0){
										if (tempOverIndex + 1 < DocEntityTags.size()){
											tempIndex = 0;
											tempOverIndex ++;
											TempSentEntitytTags = DocEntityTags.get(tempOverIndex);
										}
										else{
											breakCondition = true;
											break;
										}
									}
									if (breakCondition){
										break;
									}
								}
								try{
								tempWord = TempSentEntitytTags.get(tempIndex);
								}catch(Exception tempE){
									tempE.printStackTrace();
								}
							}
						}
					}
					toAggregate.add(docAggregate);
				}
				NetworkEdges = new CorpusAggregator().CorpusAggregate(toAggregate);
			}

		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
    /**
     *
     * @return
     */
    public String[][] getNetworkEdges(){
		String[][] NetworkEdgesArray = new String[NetworkEdges.size()][5];
		NetworkEdgesArray = NetworkEdges.toArray(NetworkEdgesArray);
		return NetworkEdgesArray;
	}

    /**
     *
     * @return
     */
    public String[][] getNetworkNodes(){
		String[][] NetworkNodes = new String[NodeHashSet.size()][2];
		NetworkNodes = NodeHashSet.toArray(NetworkNodes);
		return NetworkNodes;

	}

    /**
     *
     * @param filename
     * @return
     */
    public boolean extractGephiOutput(String filename){

		String[][] nodes_str = this.getNetworkNodes();
		String[][] edges_str = this.getNetworkEdges();

		File new_file = new File(filename);
		if(new_file.exists())
			new_file.delete();

		//Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		//Get a graph model - it exists because we have a workspace
		//GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
                //for java 8 into getGraphModel
                GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
		final DirectedGraph directedGraph = graphModel.getDirectedGraph();

		TObjectIntHashMap<String> nodes = new TObjectIntHashMap<String>();

		Vector<String> node_index = new Vector<String>();

		//Create the nodes
		for(String[] node_str : nodes_str){

			if (nodes.containsKey(node_str[0])){
				int index = nodes.get(node_str[0]);
				node_index.set(index, node_index.get(index) + "," + node_str[1]);
			}
			else
			{
				node_index.add(node_str[1]);
				nodes.put(node_str[0], node_index.size() - 1);
			}
		}

		for(TObjectIntIterator<String> node_it = nodes.iterator(); node_it.hasNext();){
			node_it.advance();	

			Node n0 = graphModel.factory().newNode(node_it.key());

                        /*
                        Niko
                        Change get attributes with index
                        */
			//n0.getAttributes().setValue("label", node_it.key());
			//n0.getAttributes().setValue("Type", node_index.get(node_it.value()));
                        n0.setAttribute("label", node_it.key());
                        n0.setAttribute("Type", node_index.get(node_it.value()));
			directedGraph.addNode(n0);
		}

		TObjectIntHashMap<MyPair<String, String> > edges = new TObjectIntHashMap<MyPair<String,String>>();

		for(String[] edge_str: edges_str){

			MyPair<String, String> edge = new MyPair(edge_str[0], edge_str[2]);

			int value = Integer.parseInt(edge_str[4]);
			edges.adjustOrPutValue(edge, value, value);			
		}

		TObjectIntIterator<MyPair<String, String> > edge_it;

		for(edge_it = edges.iterator(); edge_it.hasNext(); ){
			edge_it.advance();

			Node s1 = directedGraph.getNode(edge_it.key().getFirst());
			Node s2 = directedGraph.getNode(edge_it.key().getSecond());	
			if (s1 == null || s2 == null){
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
    public static boolean runUnit(SyntaxBasedTaskInstance instance, String output_address){


		SyntacticNetwork SN = new SyntacticNetwork(instance);
		SN.genNetwork();
		if (SN.extractGephiOutput(output_address))
			return false;


		return true;
	}

    /**
     *
     * @return
     */
    public boolean detectEntities() {

        List<FileData> files = input.getFiles();

        List<List<String[]>> toAggregate = new ArrayList<List<String[]>>();
        
        try {
            for (FileData ff : files) {

                File file = ff.getFile();
                String text;
                try {
                    text = JavaIO.readFile(file);
                    text = text.replaceAll("\\p{Cc}", " ");
                    text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"]", " ");


                    List<ForAggregation> longEntities3 = new ArrayList<ForAggregation>();
                    List<ForAggregation> longEntities4 = new ArrayList<ForAggregation>();
                    List<ForAggregation> longEntities7 = new ArrayList<ForAggregation>();
                    MultiWordEntities MWE3 = MultiWordEntityRecognition(classifier3, text);
                    MultiWordEntities MWE4 = MultiWordEntityRecognition(classifier4, text);
                    MultiWordEntities MWE7 = MultiWordEntityRecognition(classifier7, text);

                    longEntities3.addAll(MWE3.forAgg);
                    //longEntities4.addAll(MWE4.forAgg);
                    longEntities7.addAll(MWE7.forAgg);

                    HashMap<String, Integer[]> Entities = new HashMap<String, Integer[]>();

                    for (int entityIndex = 0; entityIndex < longEntities3.size(); entityIndex++) {
                        Integer[] offsetArray = {MWE3.startInd.get(entityIndex)};
                        Entities.put(longEntities3.get(entityIndex).toAggregate[0], offsetArray);
                        EntitiesWithOffset.add(longEntities3.get(entityIndex).toAggregate);
                    }
                    // The following code is to incorporate another NER library, but it was causing
                    // problems for large individual documents, so for now "longEntities4.addAll(MWE4.forAgg);" 
                    //has been commented out.  In the future,
                    // a check on file size may be done to switch between using the library or not.

                    for (int entityIndex = 0; entityIndex < longEntities4.size(); entityIndex++) {

                        if (Entities.containsKey(longEntities4.get(entityIndex).toAggregate[0]) && Arrays.asList(Entities.get(longEntities4.get(entityIndex).toAggregate[0])).contains(MWE4.startInd.get(entityIndex))) {
                            continue;
                        } else if (Entities.containsKey(longEntities4.get(entityIndex).toAggregate[0])) {
                            Integer[] numOfOcc = Entities.get(longEntities4.get(entityIndex).toAggregate[0]);
                            Integer[] offsetArray4 = new Integer[numOfOcc.length + 1];
                            offsetArray4 = Arrays.copyOf(Entities.get(longEntities4.get(entityIndex).toAggregate[0]), offsetArray4.length);
                            offsetArray4[offsetArray4.length - 1] = MWE4.startInd.get(entityIndex);
                            Entities.put(longEntities4.get(entityIndex).toAggregate[0], offsetArray4);
                            EntitiesWithOffset.add(longEntities4.get(entityIndex).toAggregate);
                        } else {
                            Integer[] offsetArray = {MWE4.startInd.get(entityIndex)};
                            Entities.put(longEntities4.get(entityIndex).toAggregate[0], offsetArray);
                            EntitiesWithOffset.add(longEntities4.get(entityIndex).toAggregate);
                        }

                    }

                    for (int entityIndex = 0; entityIndex < longEntities7.size(); entityIndex++) {

                        if (Entities.containsKey(longEntities7.get(entityIndex).toAggregate[0]) && Arrays.asList(Entities.get(longEntities7.get(entityIndex).toAggregate[0])).contains(MWE7.startInd.get(entityIndex))) {
                            continue;
                        } else if (Entities.containsKey(longEntities7.get(entityIndex).toAggregate[0])) {
                            Integer[] numOfOcc = Entities.get(longEntities7.get(entityIndex).toAggregate[0]);
                            Integer[] offsetArray7 = new Integer[numOfOcc.length + 1];
                            offsetArray7 = Arrays.copyOf(Entities.get(longEntities7.get(entityIndex).toAggregate[0]), offsetArray7.length);
                            offsetArray7[offsetArray7.length - 1] = MWE7.startInd.get(entityIndex);
                            Entities.put(longEntities7.get(entityIndex).toAggregate[0], offsetArray7);
                            EntitiesWithOffset.add(longEntities7.get(entityIndex).toAggregate);
                        } else {
                            Integer[] offsetArray = {MWE7.startInd.get(entityIndex)};
                            Entities.put(longEntities7.get(entityIndex).toAggregate[0], offsetArray);
                            EntitiesWithOffset.add(longEntities7.get(entityIndex).toAggregate);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private MultiWordEntities MultiWordEntityRecognition(AbstractSequenceClassifier<?> classifier, String inText) {

        List<ForAggregation> NamedEntities = new ArrayList<ForAggregation>();
        String htmlString = classifier.classifyToString(inText, "inlineXML", true);
        Pattern tags = Pattern.compile("<.+?>.+?</.+?>");
        Pattern tempTags = null;
        Matcher matcher = tags.matcher(htmlString);
        Matcher tempMatcher = null;
        List<Integer> startIndicies = new ArrayList<Integer>();
        HashMap<String, Integer> hashedNumOcc = new HashMap<String, Integer>();

        while (matcher.find()) {
            String name = (matcher.group().replaceAll("<.+?>", ""));
            /*
             if (name.split("\\s+").length<2){
             continue;
             }
             */
            String[] NamedEntity_array = {name, matcher.group().replaceAll("<", "").replaceAll(">.+", "")};

            if (hashedNumOcc.containsKey(name)) {
                hashedNumOcc.put(name, hashedNumOcc.get(name) + 1);
            } else {
                hashedNumOcc.put(name, 1);
            }
            ForAggregation NamedEntity = new ForAggregation(NamedEntity_array);
            try{
                startIndicies.add(findNthIndexOf(inText, name, hashedNumOcc.get(name)));
            }catch(IndexOutOfBoundsException ex){
                // next value
                continue;
            }
            if (null != NamedEntity) {
                NamedEntities.add(NamedEntity);
            }

        }
        MultiWordEntities toReturn = new MultiWordEntities(NamedEntities, startIndicies);
        return toReturn;
    }

    private int findNthIndexOf(String str, String needle, int occurence)
            throws IndexOutOfBoundsException {
        int index = -1;
        Pattern p = Pattern.compile(needle, Pattern.MULTILINE);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (--occurence == 0) {
                index = m.start();
                break;
            }
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return index;
    }

}
