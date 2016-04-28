package context.core.task.stemming;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Aale
 */
public class StemmingBody {

    /**
     * @param args
     */
    String Result;

    private StemmingTaskInstance instance;
    private CorpusData input;
    private CorpusData output;
    private StanfordCoreNLP pipeline;

    /**
     *
     * @param instance
     */
    public StemmingBody(StemmingTaskInstance instance) {
        this.instance = instance;
        init();
    }

    private void init() {

        this.input = (CorpusData) instance.getInput();
        this.output = (CorpusData) instance.getTextOutput();
        this.pipeline = instance.getPipeline();

    }

    /**
     *
     * @return
     */
    public boolean StemText() {
        // TODO Auto-generated method stub
        System.out.println("Begin of StemText");
        List<FileData> files = input.getFiles();
        System.out.println("file list size=" + files.size());
        try {
            for (FileData file : files) {
                System.out.println("processing " + file.getPath());
                String text = "";
                text = file.readFileIntoString();

                text = text.replaceAll("\\p{Cc}", " ");
                text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"-]", " ");
                // create an empty Annotation just with the given text
                Annotation document = new Annotation(text);
                // run all Annotators on this text
                pipeline.annotate(document);
                // these are all the sentences in this document
                // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
                List<CoreMap> sentences = document.get(SentencesAnnotation.class);
                String lemmatizedText = "";
                for (CoreMap sentence : sentences) {

                    final List<CoreLabel> sent = sentence.get(TokensAnnotation.class);
                    final List<TaggedWord> taggedWords = LemmaTagger.lemmatize(sent, "en");
                    for (TaggedWord token : taggedWords) {
                        // this is the text of the token
                        String word = token.word();
                        // this is the lemma tag of the token
                        String lemma = token.tag();
                        lemmatizedText += lemma + " ";
                    }
                }
                String inputNameWithoutExtension = FilenameUtils.getBaseName(file.getFile().getName());
                String inputExtension = FilenameUtils.getExtension(file.getFile().getName());
                final String name = inputNameWithoutExtension + "-ST." + inputExtension;

                int index = output.addFile(name);
                System.out.println("write file " + name);
                output.writeFile(index, lemmatizedText);

                //index++;
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
    public String getResult() {
        return Result;
    }

}
