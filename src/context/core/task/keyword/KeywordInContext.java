package context.core.task.keyword;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.util.JavaIO;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kiumars Soltani This class will remove stop words defined by the user
 */
public class KeywordInContext {

    private CorpusData input;
    private CorpusData output;
    private TabularData tabular;

    private KeywordTaskInstance instance;
    private String replaceString;

    /**
     *
     * @param instance
     */
    public KeywordInContext(KeywordTaskInstance instance) {
        this.instance = instance;
        this.replaceString = "";
        init();
    }

    /**
     *
     */
    public void init() {
        this.replaceString = "```";
        this.input = (CorpusData) instance.getInput();
        this.output = (CorpusData) instance.getTextOutput();
        this.tabular = instance.getTabularOutput(0);
    }

    /**
     *
     * @return
     */
    public boolean removeOthers() {

        List<String> keywords = new ArrayList<String>();

        if (JavaIO.readCSVFileIntoList(keywords, instance.getKeywordFile().getFile(), "\n", true) == 0) {
            return false;
        }

        System.out.println("Read all the key words");
        Map<String, Integer> keywordMap = new HashMap<String, Integer>();
        try {
            for (FileData f : input.getFiles()) {
                StringBuffer s = new StringBuffer();
                String content = JavaIO.readFile(f.getFile());
                String[] words = content.split("\\W+");
                boolean[] mark = new boolean[words.length];
                for (int j = 0; j < words.length; j++) {
                    mark[j] = false;
                }
                for (int j = 0; j < words.length; j++) {
                    if (keywords.contains(words[j])) {
                        if (!keywordMap.containsKey(words[j])) {
                            keywordMap.put(words[j], 0);
                        }
                        Integer count = keywordMap.get(words[j]);
                        keywordMap.put(words[j], count + 1);
                        for (int i = 1; i < instance.getLeftBound(); i++) {
                            if (j - i >= 0) {
                                if (!keywords.contains(words[j - i])) {
                                    mark[j - i] = true;
                                }
                            } else {
                                break;
                            }
                        }
                        for (int i = 1; i < instance.getRightBound(); i++) {
                            if (j + i < words.length) {
                                if (!keywords.contains(words[j + i])) {
                                    mark[j + i] = true;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                for (int j = 0; j < words.length; j++) {
                    if (mark[j]) {
                        words[j] = replaceString;
                    }
                }

                for (int j = 0; j < words.length; j++) {
                    s.append(words[j]).append(" ");
                }

                String inputNameWithoutExtension = FilenameUtils.getBaseName(f.getFile().getName());
                String inputExtension = FilenameUtils.getExtension(f.getFile().getName());

                int index = output.addFile(inputNameWithoutExtension + "-KWIC." + inputExtension);
                output.writeFile(index, s.toString());
            }
            this.writeCsv(keywordMap, tabular.getPath().get());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void writeCsv(Map<String, Integer> keywordsMap, String filePath) {

        System.out.println("keywords#=" + keywordsMap.size());
        StringBuffer sb = new StringBuffer();
        sb.append("Keyword, Frequency\n");
        String toWrite = "";
        for (String key : keywordsMap.keySet()) {
            toWrite = key + "," + keywordsMap.get(key) + "\n";
            sb.append(toWrite);
        }
        
        
        // 2016.03 Add this code to delete existing file
        File toDelete = new File(filePath);
        	if (toDelete.exists()) {
        		toDelete.delete(); 
        	}
        //
        
        FileData.writeDataIntoFile(sb.toString(), filePath);
    }
}
