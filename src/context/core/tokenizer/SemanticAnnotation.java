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

import context.core.util.JavaIO;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.openide.util.Exceptions;

/**
 *
 * @author Aale
 */
public class SemanticAnnotation {

    private static StanfordCoreNLP pipeline;

    static {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, parse");
        pipeline = new StanfordCoreNLP(props);
    }

    /**
     *
     * @param text
     * @param docId
     * @return
     */
    public static Map<String, List<CustomToken>> tokenizeSPOStructure(String text, String docId) {
        Map<String, List<CustomToken>> sent_spo_map = new LinkedHashMap<>();
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        int sentIndex = 0;
        for (CoreMap sentence : sentences) {
            System.out.println("sent-" + sentIndex + ": " + sentence);
            final List<SPOStructure> spo_list = SPOExtractor.extractSPOs(sentence, docId, sentIndex);
            for (SPOStructure spo : spo_list) {
                System.out.println(spo);
            }
            System.out.println();
            sentIndex++;
        }
        return sent_spo_map;
    }

    /**
     *
     * @param text
     * @param docId
     * @return
     */
    public static Map<String, CustomEdge> tokenizeSPO(String text, String docId) {
        System.out.println("starting tokenizeSPO...");
        Map<String, CustomEdge> customEdges = new LinkedHashMap<>();
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        System.out.println("core annotation done, start analyzing results...");
        int sentIndex = 0;
        for (CoreMap sentence : sentences) {
//            System.out.println("sent-" + sentIndex + ": " + sentence);
            final List<SPOStructure> spo_list = SPOExtractor.extractSPOs(sentence, docId, sentIndex);
            customEdges.putAll(generateEdges(spo_list, docId, sentIndex));
//            for (SPOStructure spo : spo_list) {
//                System.out.println(spo);
//            }
//            System.out.println();
            sentIndex++;
        }
        System.out.println(customEdges);
        System.out.println("customEdge#" + customEdges.size());
        return customEdges;
    }

    /**
     *
     * @param text
     * @param docId
     * @return
     */
    public static Map<String, CustomEdge> tokenize(String text, String docId) {
        Map<String, CustomEdge> customEdges = new LinkedHashMap<>();
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        int sentIndex = 0;
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            int index = 0;

            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
//            System.out.println(dependencies);
            for (SemanticGraphEdge edge : dependencies.edgeListSorted()) {

                CustomEdge cedge = new CustomEdge();
                cedge.setDocId(docId);
                cedge.setSentenceIndex(sentIndex);
                cedge.setIndex(index);
                cedge.setWord1(removePOS(edge.getSource() + ""));
                cedge.setWord2(removePOS(edge.getTarget() + ""));
                cedge.setType(edge.getRelation() + "");
//                System.out.println(edge + " >d: " + edge.getDependent() + " >g: " + edge.getGovernor() + " > " + edge.getRelation() + "> " + edge.getSource() + " > " + edge.getTarget() + " >w: " + edge.getWeight());
                customEdges.put(cedge.getWord1() + "/" + cedge.getWord2() + "/" + cedge.getDocId() + "/" + cedge.getSentenceIndex(), cedge);
                index++;
            }

//            Collection<TypedDependency> deps = dependencies.typedDependencies();
//            for (TypedDependency typedDep : deps) {
//                GrammaticalRelation reln = typedDep.reln();
//                String type = reln.toString();
//                System.out.println("type=" + type + " >> " + typedDep);
//            }
//            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//            
            sentIndex++;
        }
        return customEdges;
    }

//    static String text = "In my lab, we develop and advance computational solutions that help people to better understand the interplay and co-evolution of information and socio-technical networks.";

    /**
     *
     * @param args
     */
        public static void main(String[] args) {
        String text = null;
        try {
            text = JavaIO.readFile(new File("data\\deep-parsing\\data.txt"));
//        String text = "Amir is a master student in Computer Science at UIUC. Shub is a PhD student in GSLIS at UIUC";
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

//        String text = "The cat eats a mouse. She goes to the university.";
//        DocumentPreprocessor dp = new DocumentPreprocessor(new StringReader(text));
//        int index = 1;
//        for (List sentence : dp) {
//            System.out.println(index+++ " :: ");
//            System.out.println(sentence);
//        }
        System.out.println();
        text = text.replace('\n', ' ');
        final Map<String, List<CustomToken>> tokens = SemanticAnnotation.tokenizeSPOStructure(text, "1");
        for (String key : tokens.keySet()) {
            System.out.print(key + "\nS-P-O: ");
            String str = "";
            for (CustomToken token : tokens.get(key)) {
                if (token == null || token.getWord() == null) {
                    str += "N/A";
                } else {
                    str += token.getWord();
                }
                str += " - ";
            }
            str = str.substring(0, str.length() - 3);
            System.out.println(str);
            System.out.println();
        }
    }

    /**
     *
     * @param args
     */
    public static void main2(String[] args) {
//        String text = "Amir is a master student in Computer Science at UIUC. Shub is a PhD student in GSLIS at UIUC";

        String text = "The cat eats a mouse. She goes to the university.";
        final Map<String, CustomEdge> tokens = SemanticAnnotation.tokenize(text, "1");
        for (String key : tokens.keySet()) {
            System.out.println(key + "\t" + tokens.get(key));
        }
    }

    /**
     *
     * @param word
     * @return
     */
    public static String removePOS(String word) {
        int lastDashIndex = word.lastIndexOf("-");
        if (lastDashIndex == -1) {
            return word;
        } else {
            return word.substring(0, lastDashIndex);
        }
    }

    private static Map<String, CustomEdge> generateEdges(List<SPOStructure> spo_list, String docId, int sentIndex) {
        Map<String, CustomEdge> customEdges = new LinkedHashMap<>();
        int index = 0;
        for (SPOStructure spo : spo_list) {
            for (CustomToken subj : spo.getSubjects()) {
                CustomEdge cedge = new CustomEdge();
                cedge.setDocId(docId);
                cedge.setSentenceIndex(sentIndex);
                cedge.setIndex(index);
                cedge.setWord1(subj.word);
                cedge.setWord2(spo.predicate.word);
                cedge.setType("SP");
//                System.out.println(edge + " >d: " + edge.getDependent() + " >g: " + edge.getGovernor() + " > " + edge.getRelation() + "> " + edge.getSource() + " > " + edge.getTarget() + " >w: " + edge.getWeight());
                customEdges.put(cedge.getWord1() + "/" + cedge.getWord2() + "/" + cedge.getDocId() + "/" + cedge.getSentenceIndex(), cedge);
                index++;
            }

            for (CustomToken obj : spo.getObjects()) {
                CustomEdge cedge = new CustomEdge();
                cedge.setDocId(docId);
                cedge.setSentenceIndex(sentIndex);
                cedge.setIndex(index);
                cedge.setWord1(spo.predicate.word);
                cedge.setWord2(obj.word);
                cedge.setType("PO");
//                System.out.println(edge + " >d: " + edge.getDependent() + " >g: " + edge.getGovernor() + " > " + edge.getRelation() + "> " + edge.getSource() + " > " + edge.getTarget() + " >w: " + edge.getWeight());
                customEdges.put(cedge.getWord1() + "/" + cedge.getWord2() + "/" + cedge.getDocId() + "/" + cedge.getSentenceIndex(), cedge);
                index++;
            }
        }

        return customEdges;
    }
}
