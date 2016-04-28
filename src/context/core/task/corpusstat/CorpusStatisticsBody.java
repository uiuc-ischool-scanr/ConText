package context.core.task.corpusstat;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.task.pos.POSTagger;
import context.core.util.CorpusAggregator;
import context.core.util.ForAggregationNoCase;
import context.core.util.JavaIO;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.util.CoreMap;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Aale
 */
public class CorpusStatisticsBody {

    /**
     * @param args
     */
    private CorpusStatTaskInstance instance;
    private CorpusData input;
    private List<TabularData> tabularOutput;

    private StanfordCoreNLP pipeline;
    private List<String[]> corpusStatsWithTFIDF;
    private File stopFile;

    /**
     *
     * @param instance
     */
    public CorpusStatisticsBody(CorpusStatTaskInstance instance) {
        // TODO Auto-generated method stub

        this.instance = instance;
        init();

    }

    private void init() {

        this.input = (CorpusData) instance.getInput();
        this.tabularOutput = instance.getTabularOutput();
        this.pipeline = instance.getPipeline();
    }

    /**
     *
     * @param includePOS
     * @return
     */
    public boolean RunCorpusStatistics(boolean includePOS) {
        if (includePOS) {
            return RunCorpusStatisticsWithPOS();
        } else {
            return RunCorpusStatisticsWithoutPOS();
        }
    }

    /**
     *
     * @return
     */
    public boolean RunCorpusStatisticsWithPOS() {
        System.out.println("CorpusStat with POS");

        List<List<String[]>> toAggregate = new ArrayList<List<String[]>>();
        int numTerms = 0;

        List<FileData> files = input.getFiles();
        try {
            for (FileData ff : files) {

                File file = ff.getFile();
                String text;
                List<String[]> CorpusStatTags = new ArrayList<String[]>();
                try {
                    text = JavaIO.readFile(file);
                    Annotation document = new Annotation(text);
                    pipeline.annotate(document);

                    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

                    for (CoreMap sentence : sentences) {
                        // traversing the words in the current sentence
                        // a CoreLabel is a CoreMap with additional token-specific methods
                        final List<CoreLabel> sent = sentence.get(TokensAnnotation.class);
                        final List<TaggedWord> taggedWords = POSTagger.tag(sent, instance.getLanguage());
                        for (TaggedWord token : taggedWords) {
                            // this is the text of the token
                            String word = token.word();
                            // this is the POS tag of the token
                            String pos = token.tag();
                            boolean val = pos.contains("NN");
                            if (!val) {
                                if (word == "The") {
                                    System.out.println("Que?");
                                }
                                word = word.toLowerCase();
                            }
                            String[] entity = {word, pos, file.getName(), Integer.toString(1)};
                            if (!word.matches("[a-zA-Z0-9_@#]*|:\\)|:-\\)|:\\(|:-\\(|:\\/|:-\\/|:\\\\|:-\\\\|:p|:-p|;\\)|;-\\)|:>|:->")) {
                                    continue;
                            }
                            CorpusStatTags.add(entity);
                            numTerms++;
                        }
                    }
                        for (String retval: text.split(" ")){
    						//System.out.println("processing " + retval);
    						if (retval.matches(":\\)|:-\\)|:\\(|:-\\(|:\\/|:-\\/|:\\\\|:-\\\\|:p|:-p|;\\)|;-\\)|:>|:->")) {
    						String[] entity = {retval, "A", file.getName(), Integer.toString(1)};
    						System.out.println("pass:" + numTerms);
                            CorpusStatTags.add(entity);
                            numTerms++;
                            //System.out.println("Printing words after corpusstattags:"+retval);
                            }
    						else
    						{
    						System.out.println("regex dint match " +retval);
    						}
    						}
                    toAggregate.add(CorpusStatTags);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            }
            List<String[]> CorpusStatsByFile = new CorpusAggregator().CorpusAggregateNoCase(toAggregate);
            HashMap<ForAggregationNoCase, Integer[]> CorpusStats = new HashMap<ForAggregationNoCase, Integer[]>();
            for (String[] CorpusStatByFile : CorpusStatsByFile) {
                String[] identifierForRatio = {CorpusStatByFile[0], CorpusStatByFile[1]};
                ForAggregationNoCase identifierForAggregate = new ForAggregationNoCase(identifierForRatio);
                if (!(null == CorpusStats.get(identifierForAggregate))) {
                    Integer currNumDocs = ((Integer[]) CorpusStats.get(identifierForAggregate))[0];
                    Integer[] NumDocsAndTotalCount = {currNumDocs + 1, ((Integer[]) CorpusStats.get(identifierForAggregate))[1] + Integer.parseInt(CorpusStatByFile[3])};
                    CorpusStats.put(identifierForAggregate, NumDocsAndTotalCount.clone());
                } else {
                    Integer[] NumDocsAndTotalCount = {1, Integer.parseInt(CorpusStatByFile[3])};
                    CorpusStats.put(identifierForAggregate, NumDocsAndTotalCount);
                }
            }
            List<String[]> corpusStatsWithTFIDFList = new ArrayList<String[]>();
            Iterator<ForAggregationNoCase> it = CorpusStats.keySet().iterator();
            while (it.hasNext()) {
                String[] currentCorpusStatWithTFIDF = new String[5];
                ForAggregationNoCase next = it.next();
                currentCorpusStatWithTFIDF[0] = next.toAggregate[0];
                currentCorpusStatWithTFIDF[4] = next.toAggregate[1];
                currentCorpusStatWithTFIDF[1] = Integer.toString(CorpusStats.get(next)[1]);
                currentCorpusStatWithTFIDF[3] = Float.toString(((float) CorpusStats.get(next)[0]) / ((float) files.size()));

                float tf = Float.parseFloat(currentCorpusStatWithTFIDF[1]) / (Float.MIN_VALUE + numTerms);
                float idf = (float) Math.log10(((float) files.size()) / (Float.MIN_VALUE + ((float) CorpusStats.get(next)[0])));
                currentCorpusStatWithTFIDF[2] = Float.toString((tf * idf));

                corpusStatsWithTFIDFList.add(currentCorpusStatWithTFIDF);
            }

            this.corpusStatsWithTFIDF = corpusStatsWithTFIDFList;
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
    public boolean RunCorpusStatisticsWithoutPOS() {
        System.out.println("CorpusStat without POS");

        List<List<String[]>> toAggregate = new ArrayList<List<String[]>>();
        int numTerms = 0;

        List<FileData> files = input.getFiles();
        try {
            for (FileData ff : files) {

                File file = ff.getFile();
                String text;
                List<String[]> CorpusStatTags = new ArrayList<String[]>();
                try {
                    text = JavaIO.readFile(file);
                    List<String> words = getTokens(text);
                    for (String word : words) {

                        String[] entity = {word, "A", file.getName(), Integer.toString(1)};

                        if (!word.matches("[a-zA-Z0-9_@#]*|:\\)|:\\(|:\\/|:\\\\|:p|;\\)|;-\\)")) {

                            continue;
                        }
                        System.out.println("pass:" + numTerms);
                        CorpusStatTags.add(entity);
                        numTerms++;
                    }
					for (String retval: text.split(" ")){
						//System.out.println("processing " + retval);
						if (retval.matches(":\\)|:\\(|:\\/|:\\\\|:p|;\\)|;-\\)")) {
						String[] entity = {retval, "A", file.getName(), Integer.toString(1)};
						System.out.println("pass:" + numTerms);
                        CorpusStatTags.add(entity);
                        numTerms++;
                        //System.out.println("Printing words after corpusstattags:"+retval);
                        }
						else
						{
						System.out.println("regex dint match " +retval);
						}
						}
                    toAggregate.add(CorpusStatTags);
                    //System.out.println("CorpusStatTags" + CorpusStatTags.size());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            }
            System.out.println("toAggregate" + toAggregate.size());
            List<String[]> CorpusStatsByFile = new CorpusAggregator().CorpusAggregateNoCase(toAggregate);
            HashMap<ForAggregationNoCase, Integer[]> CorpusStats = new HashMap<ForAggregationNoCase, Integer[]>();
            for (String[] CorpusStatByFile : CorpusStatsByFile) {
                String[] identifierForRatio = {CorpusStatByFile[0], CorpusStatByFile[1]};
                ForAggregationNoCase identifierForAggregate = new ForAggregationNoCase(identifierForRatio);
                if (!(null == CorpusStats.get(identifierForAggregate))) {
                    Integer currNumDocs = ((Integer[]) CorpusStats.get(identifierForAggregate))[0];
                    Integer[] NumDocsAndTotalCount = {currNumDocs + 1, ((Integer[]) CorpusStats.get(identifierForAggregate))[1] + Integer.parseInt(CorpusStatByFile[3])};
                    CorpusStats.put(identifierForAggregate, NumDocsAndTotalCount.clone());
                } else {
                    Integer[] NumDocsAndTotalCount = {1, Integer.parseInt(CorpusStatByFile[3])};
                    CorpusStats.put(identifierForAggregate, NumDocsAndTotalCount);
                }
            }
            List<String[]> corpusStatsWithTFIDFList = new ArrayList<String[]>();
            Iterator<ForAggregationNoCase> it = CorpusStats.keySet().iterator();
            while (it.hasNext()) {
                String[] currentCorpusStatWithTFIDF = new String[5];
                ForAggregationNoCase next = it.next();
                currentCorpusStatWithTFIDF[0] = next.toAggregate[0];
                currentCorpusStatWithTFIDF[4] = next.toAggregate[1];
                currentCorpusStatWithTFIDF[1] = Integer.toString(CorpusStats.get(next)[1]);
                currentCorpusStatWithTFIDF[3] = Float.toString(((float) CorpusStats.get(next)[0]) / ((float) files.size()));

                float tf = Float.parseFloat(currentCorpusStatWithTFIDF[1]) / (Float.MIN_VALUE + numTerms);
                float idf = (float) Math.log10(((float) files.size()) / (Float.MIN_VALUE + ((float) CorpusStats.get(next)[0])));
                currentCorpusStatWithTFIDF[2] = Float.toString((tf * idf));

                corpusStatsWithTFIDFList.add(currentCorpusStatWithTFIDF);
            }

            this.corpusStatsWithTFIDF = corpusStatsWithTFIDFList;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    /**
     *
     * @param filepath
     * @param includePOS
     */
    public void writeOutput(String filepath, boolean includePOS) {
        //Write CSV
        this.writeCsv(corpusStatsWithTFIDF, filepath, includePOS);
    }

    private void writeCsv(List<String[]> CorpusStatsWithTFIDF, String filepath, boolean includePOS) {

        StringBuffer sb = new StringBuffer();
        if (includePOS) {
            sb.append("Term, Frequency, TF*IDF, Ratio of texts occurring in, Part of speech" + "\n");
        } else {
            sb.append("Term, Frequency, TF*IDF, Ratio of texts occurring in" + "\n");

        }

        String toWrite = "";
        for (int i1 = 0; i1 < CorpusStatsWithTFIDF.size(); i1++) {
            if (includePOS) {
                toWrite = CorpusStatsWithTFIDF.get(i1)[0] + "," + CorpusStatsWithTFIDF.get(i1)[1] + "," + CorpusStatsWithTFIDF.get(i1)[2] + "," + CorpusStatsWithTFIDF.get(i1)[3] + "," + CorpusStatsWithTFIDF.get(i1)[4] + "\n";
            } else {
                toWrite = CorpusStatsWithTFIDF.get(i1)[0] + "," + CorpusStatsWithTFIDF.get(i1)[1] + "," + CorpusStatsWithTFIDF.get(i1)[2] + "," + CorpusStatsWithTFIDF.get(i1)[3] + "\n";
            }
            sb.append(toWrite);
        }
        System.out.println("in writecsv before writeDataIntoFile");
        
        // 2016.03 Add this code to delete existing file
        File toDelete = new File(filepath);
        	if (toDelete.exists()) {
        		toDelete.delete(); 
        	}
        //
        	
        FileData.writeDataIntoFile(sb.toString(), filepath);

    }

    private List<String> getTokens(String text) {
        List<String> tokens = new ArrayList();
        PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer(new StringReader(text),
                new CoreLabelTokenFactory(), "");
        for (CoreLabel label; ptbt.hasNext();) {
            label = ptbt.next();
            tokens.add(label.originalText());
        }
        System.out.println("Tokens#:" + tokens.size());
        return tokens;
    }

}
