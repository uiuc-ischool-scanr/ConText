package context.core.task.removestopword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.util.JavaIO;

/**
 *
 * @author Kiumars Soltani This class will remove stop words defined by the user
 */
public class RemoveStopWord {

    private CorpusData input;
    private CorpusData output;

    private RemoveStopwordsTaskInstance instance;
    private String replaceString;

    /**
     *
     */
    public RemoveStopWord() {
        this.replaceString = "";
    }

    /**
     *
     * @param instance
     */
    public RemoveStopWord(RemoveStopwordsTaskInstance instance) {
        this.instance = instance;
        this.replaceString = "";
        init();
    }

    /**
     *
     */
    public void init() {
        if (instance.getType() == 1) {
            this.replaceString = "";
        } else {
            this.replaceString = "```";
        }

        this.input = (CorpusData) instance.getInput();
        this.output = (CorpusData) instance.getTextOutput();

    }

    /**
     *
     * @return
     */
    public boolean remove_stop_words() {

        List<String> stopwords = new ArrayList<String>();

        if (JavaIO.readCSVFileIntoList(stopwords, instance.getStopwordFile().getFile(), "\n", true) == 0) {
            return false;
        }

        Pattern p = generateStopwordsPattern(stopwords);

        System.out.println("Read all the stop words");

        try {
            for (FileData f : input.getFiles()) {
                String content = JavaIO.readFile(f.getFile());
                StringBuffer s = removeStringPattern(p, content);


                /*
                 StringBuffer sb = new StringBuffer();
                 PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<CoreLabel>(new FileReader(f.getFile()),
                 new CoreLabelTokenFactory(), "invertible=true");
                 for (String word; ptbt.hasNext();) {
                 CoreLabel label = (CoreLabel) ptbt.next();
                 //word = label.originalText();
                 word = label.word();
                 String word_lowercase = word.toLowerCase();
                 String before = label.getString(CoreAnnotations.BeforeAnnotation.class);
					
                 System.out.println(before);

                 if (stopwords.contains(word_lowercase)) {
                 sb.append(before + this.replaceString);
                 } else {
                 sb.append(before + word);
                 }
                 }
                 */
                String inputNameWithoutExtension = FilenameUtils.getBaseName(f.getFile().getName());
                String inputExtension = FilenameUtils.getExtension(f.getFile().getName());

                int index = output.addFile(inputNameWithoutExtension + "-RS." + inputExtension);
                output.writeFile(index, s.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * @param stopwords
     * @return
     */
    public Pattern generateStopwordsPattern(List<String> stopwords) {
        StringBuffer sb = new StringBuffer();
        for (String stopword : stopwords) {
            sb.append(("\\b(?i)" + stopword + "\\b|"));
        }
        String regex = sb.substring(0, sb.length() - 1).toLowerCase();
        Pattern p = Pattern.compile(regex);
        return p;
    }

    /**
     *
     * @param p
     * @param content
     * @return
     */
    public StringBuffer removeStringPattern(Pattern p, String content) {
        StringBuffer s = new StringBuffer();
        Matcher m = p.matcher(content);
        while (m.find()) {
            m.appendReplacement(s, replaceString);
        }
        m.appendTail(s);
        return s;
    }
}
