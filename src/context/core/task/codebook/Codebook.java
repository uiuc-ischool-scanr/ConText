package context.core.task.codebook;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.util.JavaIO;
import context.core.util.comparators.StringLengthComparator;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Kiumars Soltani
 *
 */
public class Codebook {

    private CodebookApplicationTaskInstance instance;
    private TObjectIntHashMap<String> cbMap;
    private Vector<Pair<String, String>> cbInfo;

    private CorpusData input;
    private CorpusData textOutput;
   /* private Boolean drop_num;
    private Boolean drop_pun;
    private Boolean keep_pou;*/

    /**
     *
     * @param instance
     */
    public Codebook(CodebookApplicationTaskInstance instance) {
        this.cbMap = new TObjectIntHashMap<String>();
        this.cbInfo = new Vector<Pair<String, String>>();
        this.instance = instance;
        init();
    }

    private void init() {
        this.input = (CorpusData) instance.getInput();
        this.textOutput = (CorpusData) instance.getTextOutput();
        /*this.drop_num = instance.isDropnum();
        this.drop_pun = instance.isDroppun();
        this.keep_pou = instance.isKeeppou();*/
    }

    /**
     *
     * @return
     */
    public boolean loadCodebook() {

        File nn = new File(instance.getCodebookFile());

        List<String> inn = new ArrayList<String>();
        if (JavaIO.readCSVFileIntoList(inn, nn, "[\n\r]", false) == 0) {
            return false;
        }

        String[] entities;
        int i = 0;
        for (String s : inn) {
            entities = s.split(",");
            if (entities.length != 3) {
                break;
            }
            cbMap.put(entities[0].toLowerCase().trim(), i);
            cbInfo.add(i, new ImmutablePair<String, String>(entities[1], entities[2]));
            i++;
        }

        return true;
    }

    private String findReplacement(String stemp) {
        if (instance.getIsDrop() == 1) {
            stemp = stemp.replaceAll("[^.,:;()?!\"\\s]", "");
            stemp = stemp.replaceAll("[ ]+", " ");
        } else if (instance.getIsDrop() == 2) {
            stemp = stemp.replaceAll("[^.,:;()?!\"\\s]", "`");
        }

        return stemp;

    }

    /**
     *
     * @return
     */
    public boolean applyCodebook() {
        //System.out.println("get all files");
        
        List<FileData> files = input.getFiles();

        //Make the patterns
        List<String> words = new ArrayList<>(this.cbMap.keySet());
        Collections.sort(words, new StringLengthComparator());
        Collections.reverse(words);

        StringBuffer sb = new StringBuffer();
        for (String word : words) {
            sb.append(("\\b(?i)" + word + "\\b|"));
        }

        //System.out.println("create word buffer");
        
        String regex = sb.substring(0, sb.length() - 1).toLowerCase();

        Pattern p = Pattern.compile(regex);

        try {
            for (FileData ff : files) {
                File f = ff.getFile();
                StringBuffer s = new StringBuffer();
                String content = JavaIO.readFile(f);
                /*if (drop_num) {
                    content.replaceAll("[0-9]", "");
                }
                if (drop_pun) {
                    if (keep_pou) {
                        content = content.replaceAll("[\\p{P}&&[^#]]+", " ");
                    } else {
                        content = content.replaceAll("\\p{P}", " ");
                    }
                }*/
                //System.out.println(content);
                Matcher m = p.matcher(content);
                String replc = "";
                StringBuffer stempb = new StringBuffer();
                String stemp = "";
                while (m.find()) {
                    stempb.setLength(0);
                    stemp = "";
                    int index = this.cbMap.get(m.group().toLowerCase());
                    //System.out.println(m.group() + "," + index);

                    if (instance.getIsNormal() == 0) {
                        replc = this.cbInfo.get(index).getLeft();
                    } else {
                        replc = this.cbInfo.get(index).getRight();
                    }
                    
                    m.appendReplacement(stempb, "");
                    stemp = this.findReplacement(stempb.toString());

                    s.append(stemp);
                    s.append(replc);
                }

                stempb.setLength(0);
                m.appendTail(stempb);
                s.append(this.findReplacement(stempb.toString()));

                String nameInputFileWithoutExtension = FilenameUtils.getBaseName(f.getName());
                String inputFileExtension = FilenameUtils.getExtension(f.getName());
                
                final String name = nameInputFileWithoutExtension + "-Cb." + inputFileExtension;


                int ii = textOutput.addFile(name);
                System.out.println("write codebook applied file " + name);
       
                textOutput.writeFile(ii, s.toString());                
                
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * @return
     */
    public Vector<Pair<String, String>> getCbInfo() {
        return this.cbInfo;
    }

}
