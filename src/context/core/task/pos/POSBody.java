/* 
 * Copyright (c) 2020 University of Illinois Board of Trustees, All rights reserved.   
 * Developed at the iSchool, by Dr. Jana Diesner, Chieh-Li Chin, 
 * Amirhossein Aleyasen, Shubhanshu Mishra, Kiumars Soltani, Liang Tao, 
 * Ming Jiang, Harathi Korrapati, Nikolaus Nova Parulian, and Lan Jiang..     
 *   
 * This program is free software; you can redistribute it and/or modify it under   
 * the terms of the GNU General Public License as published by the Free Software   
 * Foundation; either version 2 of the License, or any later version.   
 *    
 * This program is distributed in the hope that it will be useful, but WITHOUT   
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or    
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for   
 * more details.   
 *    
 * You should have received a copy of the GNU General Public License along with   
 * this program; if not, see <http://www.gnu.org/licenses>.   
 *
 */

package context.core.task.pos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.util.CorpusAggregator;
import context.core.util.JavaIO;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Aale
 */
public class POSBody {

    /**
     * @param args
     */
    private POSTaskInstance instance;
    private CorpusData input;
    private List<TabularData> tabularOutput;
    /*private Boolean drop_num;
    private Boolean drop_pun;
    private Boolean keep_pou;*/

    /**
     *
     */
    protected StanfordCoreNLP pipeline;
    private List<String[]> POStagsWithCount;

    /**
     *
     * @param instance
     */
    public POSBody(POSTaskInstance instance) {
		// TODO Auto-generated method stub

        // File[] Files, StanfordCoreNLP pipeline, String outputDir
        this.instance = instance;
        init();

    }

    private void init() {
        this.input = (CorpusData) instance.getInput();
        this.pipeline = instance.getPipeline();
        this.tabularOutput = instance.getTabularOutput();
        /*this.drop_num = instance.isDropnum();
        this.drop_pun = instance.isDroppun();
        this.keep_pou = instance.isKeeppou();*/
    }

    /**
     *
     * @return
     */
    public boolean tagPOS() {
        List<List<String[]>> toAggregate = new ArrayList<List<String[]>>();

        List<FileData> files = input.getFiles();
        try {
            for (FileData ff : files) {

                File file = ff.getFile();
                String text;
                List<String[]> POStags = new ArrayList<String[]>();
                try {
                    text = JavaIO.readFile(file);
                    if (instance.getLanguage().equals("en")) {
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
                        text = text.replaceAll("[^A-Za-z0-9 :;!\\?\\.,\'\"-]", " ");
                    }
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
                            String[] entity = {word, pos, Integer.toString(1)};
                            if (instance.getLanguage().equals("en")) {
                                if (!word.matches("^[a-zA-Z0-9]*$")) {
                                    continue;
                                }
                            }
                            POStags.add(entity);
                        }
                    }
                    
                    /*
                    Niko
                    add handler for writing pos result files
                    */
                    String inputNameWithoutExtension = FilenameUtils.getBaseName(ff.getFile().getName());
                    String inputExtension = FilenameUtils.getExtension(ff.getFile().getName());
                    String outputFile = inputNameWithoutExtension + "-POS." + inputExtension;
                    CorpusData output = (CorpusData) instance.getTextOutput();
                    int index = output.addFile(outputFile);
                    List<List<String[]>> aggregateTemp = new ArrayList<List<String[]>>();
                    aggregateTemp.add(POStags);
                    List<String[]> POStagsTemp = new CorpusAggregator().CorpusAggregate(aggregateTemp);
                    writeCsv(POStagsTemp, output.getFiles().get(index).getPath().get());
                    /*
                    End addition
                    */
                    
                    toAggregate.add(POStags);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            }
            POStagsWithCount = new CorpusAggregator().CorpusAggregate(toAggregate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * @param filepath
     */
    public void writeOutput(String filepath) {
        //Write CSV
        this.writeCsv(POStagsWithCount, filepath);
    }

    private void writeCsv(List<String[]> taggedPOS, String filepath) {
        System.out.println("POS Size=" + taggedPOS.size());
        StringBuffer sb = new StringBuffer();

        sb.append("Word,POS,Frequency\n");

        String toWrite = "";
        for (int i1 = 0; i1 < taggedPOS.size(); i1++) {
//            System.out.println(corpusStatsWithTFIDF.get(i1).length);

            toWrite = taggedPOS.get(i1)[0] + "," + taggedPOS.get(i1)[1] + "," + taggedPOS.get(i1)[2] + "\n";
            sb.append(toWrite);
        }        
        //System.out.println("size of string to write=" + sb.toString().length());
        //System.out.println("string to write=" + sb.toString());
        
        // 2016.03 Add this code to delete existing file
        File toDelete = new File(filepath);
        	if (toDelete.exists()) {
        		toDelete.delete(); 
        	}
        //
        
        FileData.writeDataIntoFile(sb.toString(), filepath);
    }

}
