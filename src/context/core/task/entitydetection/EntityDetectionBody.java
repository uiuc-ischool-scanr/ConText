package context.core.task.entitydetection;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.util.CorpusAggregator;
import context.core.util.ForAggregation;
import context.core.util.JavaIO;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Aale
 */
public class EntityDetectionBody {

    /**
     * @param args
     *
     */
    private EntityDetectionTaskInstance instance;
    private CorpusData input;
    private List<TabularData> tabularOutput;
    /*private Boolean drop_num;
    private Boolean drop_pun;
    private Boolean keep_pou;*/
    private AbstractSequenceClassifier<?> classifier3;
    private AbstractSequenceClassifier<?> classifier4;
    private AbstractSequenceClassifier<?> classifier7;
//    private File stopFile;

    List<String[]> entitiesWithCount;

    /**
     *
     * @param instance
     */
    public EntityDetectionBody(EntityDetectionTaskInstance instance) {
        // TODO Auto-generated method stub

        this.instance = instance;
        init();

    }

    private void init() {

        this.input = (CorpusData) instance.getInput();
        this.tabularOutput = instance.getTabularOutput();
        /*this.drop_num = instance.isDropnum();
        this.drop_pun = instance.isDroppun();
        this.keep_pou = instance.isKeeppou();*/
        this.classifier3 = instance.get3Classifier();
        this.classifier4 = instance.get4Classifier();
        this.classifier7 = instance.get7Classifier();
        System.out.println("init done successfully");
    }

    /**
     *
     * @return
     */
    public boolean detectEntities() {
        System.out.println("start detectEntities...");
        List<FileData> files = input.getFiles();

        List<List<String[]>> toAggregate = new ArrayList<List<String[]>>();
        try {
            for (FileData ff : files) {

                File file = ff.getFile();
                String text;
                try {
                    text = JavaIO.readFile(file);
                    text = text.replaceAll("\\p{Cc}", " ");
                    /*if (drop_num) {
                        text = text.replaceAll("[0-9]", " ");
                    }
                    if (drop_pun) {
                        if (keep_pou) {
                            text = text.replaceAll("[\\p{P}&&[^#]]+"," ");
                        } else {
                            text = text.replaceAll("\\p{P}", " ");
                        }
                    }*/
                    text = text.replaceAll("[^A-Za-z0-9 :;!$%\\?\\.,\'\"-]", " ");
                    
                    List<String[]> longEntities = new ArrayList<String[]>();

                    //List<ForAggregation> longEntities3 = new ArrayList<ForAggregation>();
                    //List<ForAggregation> longEntities4 = new ArrayList<ForAggregation>();
                    List<ForAggregation> longEntities7 = new ArrayList<ForAggregation>();
                    //MultiWordEntities MWE3 = MultiWordEntityRecognition(classifier3, text);
                    //MultiWordEntities MWE4 = MultiWordEntityRecognition(classifier4, text);
                    MultiWordEntities MWE7 = MultiWordEntityRecognition(classifier7, text);

                    //longEntities3.addAll(MWE3.forAgg);
                    //longEntities4.addAll(MWE4.forAgg);
                    longEntities7.addAll(MWE7.forAgg);

                    HashMap<String, Integer[]> Entities = new HashMap<String, Integer[]>();

                    for (int entityIndex = 0; entityIndex < longEntities7.size(); entityIndex++) {
                    Integer[] offsetArray = {MWE7.startInd.get(entityIndex)};
                    Entities.put(longEntities7.get(entityIndex).toAggregate[0], offsetArray);
                    longEntities.add(longEntities7.get(entityIndex).toAggregate);
	                }
	                    
                    /*for (int entityIndex = 0; entityIndex < longEntities3.size(); entityIndex++) {
                        Integer[] offsetArray = {MWE3.startInd.get(entityIndex)};
                        Entities.put(longEntities3.get(entityIndex).toAggregate[0], offsetArray);
                        longEntities.add(longEntities3.get(entityIndex).toAggregate);
                    }*/
                    // The following code is to incorporate another NER library, but it was causing
                    // problems for large individual documents, so for now "longEntities4.addAll(MWE4.forAgg);" 
                    //has been commented out.  In the future,
                    // a check on file size may be done to switch between using the library or not.

                    /*for (int entityIndex = 0; entityIndex < longEntities4.size(); entityIndex++) {

                        if (Entities.containsKey(longEntities4.get(entityIndex).toAggregate[0]) && Arrays.asList(Entities.get(longEntities4.get(entityIndex).toAggregate[0])).contains(MWE4.startInd.get(entityIndex))) {
                            continue;
                        } else if (Entities.containsKey(longEntities4.get(entityIndex).toAggregate[0])) {
                            Integer[] numOfOcc = Entities.get(longEntities4.get(entityIndex).toAggregate[0]);
                            Integer[] offsetArray4 = new Integer[numOfOcc.length + 1];
                            offsetArray4 = Arrays.copyOf(Entities.get(longEntities4.get(entityIndex).toAggregate[0]), offsetArray4.length);
                            offsetArray4[offsetArray4.length - 1] = MWE4.startInd.get(entityIndex);
                            Entities.put(longEntities4.get(entityIndex).toAggregate[0], offsetArray4);
                            
                            longEntities.add(longEntities4.get(entityIndex).toAggregate);
                        } else {
                            Integer[] offsetArray = {MWE4.startInd.get(entityIndex)};
                            Entities.put(longEntities4.get(entityIndex).toAggregate[0], offsetArray);
                           
                            longEntities.add(longEntities4.get(entityIndex).toAggregate);
                        }

                    }*/

                    /*for (int entityIndex = 0; entityIndex < longEntities7.size(); entityIndex++) {

                        if (Entities.containsKey(longEntities7.get(entityIndex).toAggregate[0]) && Arrays.asList(Entities.get(longEntities7.get(entityIndex).toAggregate[0])).contains(MWE7.startInd.get(entityIndex))) {
                            continue;
                        } else if (Entities.containsKey(longEntities7.get(entityIndex).toAggregate[0])) {
                            Integer[] numOfOcc = Entities.get(longEntities7.get(entityIndex).toAggregate[0]);
                            Integer[] offsetArray7 = new Integer[numOfOcc.length + 1];
                            offsetArray7 = Arrays.copyOf(Entities.get(longEntities7.get(entityIndex).toAggregate[0]), offsetArray7.length);
                            offsetArray7[offsetArray7.length - 1] = MWE7.startInd.get(entityIndex);
                            Entities.put(longEntities7.get(entityIndex).toAggregate[0], offsetArray7);
                            
                            longEntities.add(longEntities7.get(entityIndex).toAggregate);
                        } 
                         else {
                            Integer[] offsetArray = {MWE7.startInd.get(entityIndex)};
                            Entities.put(longEntities7.get(entityIndex).toAggregate[0], offsetArray);
                            
                            longEntities.add(longEntities7.get(entityIndex).toAggregate);
                        }
                    }*/

                    toAggregate.add(longEntities);
                    
                    
                    //Niko
                    //Add code for entity detection results
                    
                    /*String htmlString3 = classifier3.classifyToString(text, "inlineXML", true);
                    String htmlString4 = classifier4.classifyToString(text, "inlineXML", true);*/
                    String htmlString = classifier7.classifyToString(text, "inlineXML", true);
                    
                    
                    String inputNameWithoutExtension = FilenameUtils.getBaseName(ff.getFile().getName());
                    String inputExtension = FilenameUtils.getExtension(ff.getFile().getName());
                    String outputFile = inputNameWithoutExtension + "-ED." + inputExtension;
                    CorpusData output = (CorpusData) instance.getTextOutput();                    
                    int index = output.addFile(outputFile);
                    output.writeFile(index, htmlString.toString());
                    
                    //End Addition
                                        
                    
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            //List<String[]> EntitiesToBeRemoved = new ArrayList<String[]>();
            List<String[]> entitiesWithCount = new CorpusAggregator().CorpusAggregate(toAggregate);
            
				for (int i1 = 0; i1 < entitiesWithCount.size(); i1++) {
				/*String findStr = entitiesWithCount.get(i1)[0];
				int count = 0;
				for (FileData ff : files) {
                File file = ff.getFile();
                String text;
                text = JavaIO.readFile(file);
                text = text.replaceAll("\\p{Cc}", " ");
                text = text.replaceAll("[^A-Za-z0-9 :;!%$\\?\\.,\'\"-]", " ");
				String str = text;
				int lastIndex = 0;
				while(lastIndex != -1){
				lastIndex = str.indexOf(findStr,lastIndex);
					if(lastIndex != -1){
					count ++;
					lastIndex += findStr.length();
					}
				}
			    }*/
				//entitiesWithCount.get(i1)[2] = count + "";
				//out count is double counting
				System.out.println("word: " + entitiesWithCount.get(i1)[0]+ " frequency: "+entitiesWithCount.get(i1)[2]);
				}
            this.entitiesWithCount = entitiesWithCount;			
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
            String[] NamedEntity_array = {name.trim().replaceAll(" +", " "), matcher.group().replaceAll("<", "").replaceAll(">.+", "")};

            if (hashedNumOcc.containsKey(name)) {
                hashedNumOcc.put(name, hashedNumOcc.get(name) + 1);
            } else {
                hashedNumOcc.put(name, 1);
            }
            ForAggregation NamedEntity = new ForAggregation(NamedEntity_array);
            startIndicies.add(findNthIndexOf(inText, name, hashedNumOcc.get(name)));
            if (null != NamedEntity) {
                NamedEntities.add(NamedEntity);
            }

        }
        MultiWordEntities toReturn = new MultiWordEntities(NamedEntities, startIndicies);
        return toReturn;
    }

    private int findNthIndexOf(String str, String needle, int occurence){
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
//            throw new IndexOutOfBoundsException();
            return 0; 
        }
        return index;
    }

    /**
     *
     * @param filepath
     */
    public void writeOutput(String filepath) {
        //Write CSV
        this.writeCsv(entitiesWithCount, filepath);
    }

    /**
     *
     * @param entitiesWithCount
     * @param filePath
     */
    public static void writeCsv(List<String[]> entitiesWithCount, String filePath) {

        System.out.println("size of entitiesWithCount=" + entitiesWithCount.size());
        StringBuffer sb = new StringBuffer();
        sb.append("Term, Entity, Frequency\n");
        String toWrite = "";
        for (int i1 = 0; i1 < entitiesWithCount.size(); i1++) {
            //toWrite = entitiesWithCount.get(i1)[0].replaceAll("[^A-Za-z0-9\\. ]", "_") + "," + entitiesWithCount.get(i1)[1] + "," + (entitiesWithCount.get(i1)[2]) + "\n";
        	String term_csv = entitiesWithCount.get(i1)[0].replaceAll("[^A-Za-z0-9 :;!%$\\?\\.\'\"-]", ""); //replace "," 
        	toWrite = term_csv + "," + entitiesWithCount.get(i1)[1] + "," + (entitiesWithCount.get(i1)[2]) + "\n";
        	
        	sb.append(toWrite);
        }
        
        // 2016.03 Add this code to delete existing file
        File toDelete = new File(filePath);
        	if (toDelete.exists()) {
        		toDelete.delete(); 
        	}
        
        FileData.writeDataIntoFile(sb.toString(), filePath);
    }
}
