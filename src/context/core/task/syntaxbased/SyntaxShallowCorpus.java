/**
 *
 */
package context.core.task.syntaxbased;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.textnets.Corpus;
import context.core.textnets.Corpus.LABELTYPES;
import context.core.textnets.Corpus.UNITOFANALYSIS;
import context.core.textnets.Network;
import context.core.textnets.Network.FileType;
import context.core.tokenizer.CustomToken;
import context.core.tokenizer.Tokenizer;
import context.core.util.JavaIO;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Shubhanshu
 *
 */
public class SyntaxShallowCorpus extends Corpus {

    /**
     *
     */
    private SyntaxBasedTaskInstance instance;
    private CorpusData input;
    private List<TabularData> tabularOutput;

    private StanfordCoreNLP pipeline;

    /**
     *
     * @param serializedClassifier
     */
    public SyntaxShallowCorpus(String serializedClassifier) {
        // TODO Auto-generated constructor stub

    }

    /**
     *
     * @param instance
     */
    public SyntaxShallowCorpus(SyntaxBasedTaskInstance instance) {
        // TODO Auto-generated constructor stub
        super();
        this.instance = instance;
        this.input = (CorpusData) instance.getInput();
        this.tabularOutput = instance.getTabularOutput();
        this.setUnit(UNITOFANALYSIS.SENTENCE);
        this.setWindowSize(Integer.MAX_VALUE);
        this.setFilterLabels(instance.getFilterLabels());
        System.err.println("Window Size: " + instance.getDistance() + ", " + this.getWindowSize());
        this.setDocLevel(false);

    }

    /**
     *
     */
    public void genStreamsFromCorpus() {
    	System.err.println("Adding Text Streams: "+input.getFiles().size());
        List<FileData> files = input.getFiles();
        for (FileData ff : files) {
            File file = ff.getFile();
            String text = "";
            try {
                text = JavaIO.readFile(file);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            text = text.replaceAll("\\p{Cc}", " ");
            text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"-]", " ");
            String fileName = ff.getName().getValue();
            SyntaxShallowTextStream ets = getShallowParseTextStream(fileName, text);
            this.addStream(fileName, ets);
            System.err.println("Finished adding Text Stream: " + fileName);
        }
    }

    /**
     *
     * @param fileName
     * @param inText
     * @return
     */
    public SyntaxShallowTextStream getShallowParseTextStream(String fileName, String inText) {
        SyntaxShallowTextStream ets = new SyntaxShallowTextStream(fileName);
        final Map<String, CustomToken> tokens = Tokenizer.tokenize(inText, fileName);
        for (String key : tokens.keySet()) {
            final CustomToken ctoken = tokens.get(key);
            //System.out.println(key + "\t" + ctoken.getPos());
            SyntaxShallowToken e = new SyntaxShallowToken(ctoken.getWord());
            String label = ctoken.getPos();
            if(label.matches("\\p{Punct}") || ! this.getFilterLabels().contains(label)){
            	label = null;
            }
            e.setLabel(label, LABELTYPES.POS);
            ets.addToken(e);
        }
        return ets;

    }

    /**
     *
     * @param outputDir
     */
    public void saveNetworks(String outputDir) {
        String fileName = "POSNetwork";
        saveNetworks(fileName, outputDir, FileType.GRAPHML, LABELTYPES.POS);

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String text = "Values and Harassment Prevention.\r\n"
                + "Dear Mr. Lay, I understand that Cindy Olson talked with you about the harassment prevention training scheduled over the next few weeks. In past classes, a senior-level business person has emphasized that harassment is not tolerated and is not consistent with Enrons values. For this set of training classes, the team asked if you could deliver that message via videotape. Cindy told me that your answer is \"yes.\" I understand that you are scheduled to record an unrelated video on Thursday morning, and that it might be possible to tape this message at the end of that session. Would that work with your schedule? If so, I will provide you with talking points and a script. If another time is better for you, let me know. Thanks for your help! Michelle Cash.";
        text += " " + text;
        text = text.replaceAll("\\p{Cc}", " ");
        text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"-]", " ");
        String serializedClassifier = "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";
        SyntaxShallowCorpus ec = new SyntaxShallowCorpus(serializedClassifier);
        SyntaxShallowTextStream ets = ec.getShallowParseTextStream("", text);
        Network net = new Network();
        ets.makeNetwork(net, 7, LABELTYPES.POS, UNITOFANALYSIS.SENTENCE);
        net.saveNet("outputNet", "./", FileType.GRAPHML);

    }

}
