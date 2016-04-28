/*
 
 * Copyright (c) 2015 University of Illinois Board of Trustees, All rights reserved.   
 * Developed at GSLIS/ the iSchool, by Dr. Jana Diesner, Amirhossein Aleyasen,    
 * Chieh-Li Chin, Shubhanshu Mishra, Kiumars Soltani, and Liang Tao.     
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
package context.core.tokenizer;

import context.core.task.pos.POSTagger;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Aale
 */
public class Tokenizer {

    private static StanfordCoreNLP pipeline;

    static {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit");
        pipeline = new StanfordCoreNLP(props);
    }

    /**
     *
     * @param text
     * @param docId
     * @return
     */
    public static Map<String, CustomToken> tokenize(String text, String docId) {
        Map<String, CustomToken> customTokens = new LinkedHashMap<>();
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        int sentIndex = 0;
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            int index = 0;

            final List<CoreLabel> sent = sentence.get(TokensAnnotation.class);
            final List<TaggedWord> taggedWords = POSTagger.tag(sent, "en");
            for (TaggedWord token : taggedWords) {
                // this is the text of the token
                String word = token.word();
                // this is the POS tag of the token
                String pos = token.tag();
                // this is the NER label of the token
//                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                CustomToken ctoken = new CustomToken();
                ctoken.setWord(word);
                ctoken.setBeginPosition(token.beginPosition());
                ctoken.setEndPosition(token.endPosition());
                ctoken.setDocId(docId);
                ctoken.setSentenceIndex(sentIndex);
                ctoken.setMultiword(false);
                ctoken.setIndex(index);
                ctoken.setPos(pos);

                customTokens.put(word + "/" + docId + "/" + sentIndex + "/" + index, ctoken);
                index++;

            }
            sentIndex++;
        }
        return customTokens;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        String text = "Amir is a master student in Computer Science at UIUC. Shub is a PhD student in GSLIS at UIUC";
        final Map<String, CustomToken> tokens = Tokenizer.tokenize(text, "1");
        for (String key : tokens.keySet()) {
            System.out.println(key + "\t" + tokens.get(key));
        }

    }
}
