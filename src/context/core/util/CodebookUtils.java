package context.core.util;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author Kiumars Soltani
 *
 */
public class CodebookUtils {

    /**
     *
     */
    public static String log;

    /**
     *
     * @param file
     * @param content
     * @return
     */
    public static int write_string(File file, String content) {
        log = "";
        try {

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(content);
            bw.close();

        } catch (IOException e) {
            log += e.getMessage();
            return 0;
        }

        return 1;
    }

    /**
     *
     * @param input
     * @param type
     * @param delim
     * @return
     */
    public static Vector<String> getWords(String input, int type, String delim) { //1- sentence 2-paragraph 3- word 4- custom
        switch (type) {
            case 3:
                return CodebookUtils.make_words(input);
            case 1:
                return CodebookUtils.make_sentences(input);
            case 2:
                return CodebookUtils.make_paragraph(input);
            case 4:
                return CodebookUtils.make_custom_token(input, delim);
        }

        return null;
    }

    /**
     *
     * @param content
     * @return
     */
    public static Vector<String> make_words(String content) {

        Vector<String> return_list = new Vector<String>();

        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

//        System.out.println("contentInmakeWord1: "+ content);
        content = content.replaceAll("\\p{Cc}", " ");
        
//        System.out.println("contentInmakeWord2: "+ content);
        content = content.replaceAll("[^A-Za-z0-9 \\_ :;!\\?\\.,\'\"-]", " ");
        
//        System.out.println("contentInmakeWord3: "+ content);
        Annotation document = new Annotation(content);

        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                if (word != null) {
                    //System.out.println(word);
                    return_list.add(word);
                }
            }
        }

        return return_list;

    }

    /**
     *
     * @param content
     * @return
     */
    public static Vector<String> make_sentences(String content) {

        Vector<String> return_list = new Vector<String>();

        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        content = content.replaceAll("\\p{Cc}", " ");
        content = content.replaceAll("[^A-Za-z0-9 \\_ :;!\\?\\.,\'\"-]", " ");
        Annotation document = new Annotation(content);

        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                if (word != null) {
                    return_list.add(word);
                }
            }
            return_list.add(null);
        }

        return return_list;

    }

    /**
     *
     * @param content
     * @return
     */
    public static Vector<String> make_paragraph(String content) {

        String[] ss = content.split("\n");
        Vector<String> return_list = new Vector<String>();
        for (String s : ss) {
            return_list.addAll(make_words(s));
            return_list.add(null);
        }

        return return_list;
    }

    /**
     *
     * @param content
     * @param token
     * @return
     */
    public static Vector<String> make_custom_token(String content, String token) {

        String[] ss = content.split(token);
        Vector<String> return_list = new Vector<String>();
        for (String s : ss) {
            return_list.addAll(make_words(s));
            return_list.add(null);
        }

        return return_list;
    }

    /**
     *
     * @param input
     * @return
     */
    public static String read_file(File input) {
        log = "";
        String output = "";

        FileInputStream stream;
        try {
            stream = new FileInputStream(input);
            try {

                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                /* Instead of using default, pass in a decoder. */
                output = Charset.defaultCharset().decode(bb).toString();
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            // TODO: handle exception
            log += e.getMessage();
            return "";
        }

        return output;
    }

}
