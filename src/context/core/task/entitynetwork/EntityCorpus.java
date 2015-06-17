/**
 * 
 */
package context.core.task.entitynetwork;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.textnets.Corpus;
import context.core.textnets.Corpus.LABELTYPES;
import context.core.textnets.Network;
import context.core.textnets.Network.FileType;
import context.core.util.JavaIO;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 * @author Shubhanshu
 *
 */
public class EntityCorpus extends Corpus {

	/**
	 * 
	 */
	private EntityNetworkTaskInstance instance;
	private CorpusData input;
	private CorpusData output;
    private List<TabularData> tabularOutput;
	
	private AbstractSequenceClassifier<CoreLabel> classifier3;
    private AbstractSequenceClassifier<CoreLabel> classifier4;
    private AbstractSequenceClassifier<CoreLabel> classifier7;
	private StanfordCoreNLP pipeline;
	
    /**
     *
     * @param serializedClassifier
     */
    public EntityCorpus(String serializedClassifier) {
		// TODO Auto-generated constructor stub
		try {
			classifier7 = CRFClassifier.getClassifier(serializedClassifier);
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    /**
     *
     * @param instance
     */
    public EntityCorpus(EntityNetworkTaskInstance instance) {
		// TODO Auto-generated constructor stub
		super();
		this.instance = instance;
		this.input = (CorpusData) instance.getInput();
		this.output = (CorpusData) instance.getTextOutput();
        this.tabularOutput = instance.getTabularOutput();
        this.classifier3 = instance.get3Classifier();
        this.classifier4 = instance.get4Classifier();
        this.classifier7 = instance.get7Classifier();
        this.setLabels(classifier7.labels(), LABELTYPES.ENTITY);
        // TODO Add filter labels based on GUI Instance values.
        this.setFilterLabels(instance.getFilterLabels());
        this.setUnit(UNITOFANALYSIS.values()[instance.getUnitOfAnalysis()-1]);
        this.setWindowSize(instance.getDistance());
        System.err.println("Window Size: "+instance.getDistance()+", "+this.getWindowSize());
        this.setDocLevel(false);
		
		
	}
	
    /**
     *
     */
    public void genStreamsFromCorpus(){
		List<FileData> files = input.getFiles();
		for (FileData f:files){
			File file = f.getFile();
			String text = "";
			try {
				text = JavaIO.readFile(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			text = text.replaceAll("\\p{Cc}", " ");
            text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"-]", " ");
            String fileName = f.getName().getValue();
            EntityTextStream ets = getEntityTextStream(fileName, text);
            this.addStream(fileName, ets);
            System.err.println("Finished adding Text Stream: "+fileName);
            
            String htmlString = classifier7.classifyToString(text, "inlineXML", true);
            String inputNameWithoutExtension = FilenameUtils.getBaseName(f.getFile().getName());
            String inputExtension = FilenameUtils.getExtension(f.getFile().getName());
            String outputFile = inputNameWithoutExtension + "-ED." + inputExtension;
            int index = output.addFile(outputFile);
            output.writeFile(index, htmlString.toString());
            System.err.println("Finished Writing the Modified File: "+fileName);
            
		}
	}
	
    /**
     *
     * @param fileName
     * @param inText
     * @return
     */
    public EntityTextStream getEntityTextStream(String fileName, String inText){
    	EntityTextStream ets = new EntityTextStream(fileName);
    	List<List<CoreLabel>> out = classifier7.classify(inText);
    	//System.out.println(classifier7.labels());
    	for (List<CoreLabel> sentence : out) {
            for (int i = 0; i < sentence.size(); i++) {
            	CoreLabel word = sentence.get(i);
            	String wordStr = word.word();
            	String wordLbl = word.getString(CoreAnnotations.AnswerAnnotation.class);
            	if(!wordLbl.equals("O")){
            		int j = i+1;
                	String nextLbl = null;
                	CoreLabel nextWord;
            		String nextStr;
                	while(j<sentence.size()){
                		nextWord = sentence.get(j);
                		nextStr = nextWord.word();
                		nextLbl = nextWord.getString(CoreAnnotations.AnswerAnnotation.class);
                		if(!nextLbl.equals(wordLbl)){
                			break;
                		}
                		wordStr += "_"+nextStr;
                		j++;
                	}
                	i = j-1;
            	}
            	//System.out.print(wordStr+ '/' + wordLbl + ' ');
            	EntityToken e = new EntityToken(wordStr);
            	if(wordLbl.equals("O") || ! this.getFilterLabels().contains(wordLbl)){
            		wordLbl = null;
            	}
            	e.setLabel(wordLbl, LABELTYPES.ENTITY);
            	ets.addToken(e);
            }
            //System.out.println();
          }
    	//System.out.println("Done"); 
    	return ets;
    	
    }
	
    /**
     *
     * @param outputDir
     */
    public void saveNetworks(String outputDir){
		String fileName = "EntityNetwork";
		saveNetworks(fileName, outputDir, FileType.GRAPHML, LABELTYPES.ENTITY);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String text = "Values and Harassment Prevention.\r\n" + 
				"Dear Mr. Lay, I understand that Cindy Olson talked with you about the harassment prevention training scheduled over the next few weeks. In past classes, a senior-level business person has emphasized that harassment is not tolerated and is not consistent with Enrons values. For this set of training classes, the team asked if you could deliver that message via videotape. Cindy told me that your answer is \"yes.\" I understand that you are scheduled to record an unrelated video on Thursday morning, and that it might be possible to tape this message at the end of that session. Would that work with your schedule? If so, I will provide you with talking points and a script. If another time is better for you, let me know. Thanks for your help! Michelle Cash.";
		text += " "+text;
		text = text.replaceAll("\\p{Cc}", " ");
        text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"-]", " ");
		String serializedClassifier = "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";
		EntityCorpus ec = new EntityCorpus(serializedClassifier);
		EntityTextStream ets = ec.getEntityTextStream("", text);
		Network net = new Network();
		ets.makeNetwork(net, 7, LABELTYPES.ENTITY, UNITOFANALYSIS.SENTENCE);
		net.saveNet("outputNet", "./", FileType.GRAPHML);
		
		
		

	}

}
