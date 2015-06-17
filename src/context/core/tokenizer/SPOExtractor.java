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
//
//import edu.stanford.nlp.trees.semgraph.SemanticGraph;
//import edu.stanford.nlp.trees.semgraph.SemanticGraphCoreAnnotations;
//import edu.stanford.nlp.trees.semgraph.SemanticGraphEdge;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Aale
 */
public class SPOExtractor {

    static List<SPOStructure> extractSPOs(CoreMap sentence, String docId, int sentIndex) {
        // traversing the words in the current sentence
        // a CoreLabel is a CoreMap with additional token-specific methods
        int index = 0;
        Map<String, CustomEdge> customEdges = new LinkedHashMap<>();
        SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
        for (SemanticGraphEdge edge : dependencies.edgeListSorted()) {
            CustomEdge cedge = new CustomEdge();
            cedge.setDocId(docId);
            cedge.setSentenceIndex(sentIndex);
            cedge.setIndex(index);
            cedge.setWord1(edge.getSource().originalText());
            cedge.setWord2(edge.getTarget().originalText());
            cedge.setType(edge.getRelation() + "");
            customEdges.put(cedge.getWord1() + "/" + cedge.getWord2() + "/" + cedge.getDocId() + "/" + cedge.getSentenceIndex(), cedge);
        }
        Collection<String> verbs = extractVerbs(customEdges.values());
        List<SPOStructure> spos_list = new ArrayList<>();
        for (String v : verbs) {
            SPOStructure spo = new SPOStructure();
            for (CustomEdge cedge : customEdges.values()) {
                if (cedge.getType().equals("nsubj") && cedge.getWord1().equals(v)) {
                    CustomToken subject = new CustomToken();
                    String expandedSubject = expandNoun(cedge.getWord2(), customEdges.values());
                    subject.setWord(expandedSubject);
                    spo.addSubject(subject);
                } else if (cedge.getType().equals("dobj") && cedge.getWord1().equals(v)) {
                    CustomToken object = new CustomToken();
                    String expandedObject = expandNoun(cedge.getWord2(), customEdges.values());
                    object.setWord(expandedObject);
                    spo.addObject(object);
                }
            }
            if (spo.getObjects().isEmpty()) {
                for (CustomEdge cedge : customEdges.values()) {
                    if (cedge.getType().contains("prep") && cedge.getWord1().equals(v)) {
                        CustomToken object = new CustomToken();
                        String expandedObject = expandNoun(cedge.getWord2(), customEdges.values());
                        object.setWord(expandedObject);
                        spo.addObject(object);
                        break;
                    }
                }
            }
            if (spo.getObjects().size() > 0 && spo.getSubjects().size() > 0) {
                CustomToken predicate = new CustomToken();
                predicate.setWord(v);
                spo.setPredicate(predicate);
                spos_list.add(spo);
            }
        }
        return spos_list;
    }

    private static Collection<String> extractVerbs(Collection<CustomEdge> relations) {
        Set<String> verbs = new LinkedHashSet<>();
        for (CustomEdge rel : relations) {
            if (rel.getType().equals("nsubj")) {
                verbs.add(rel.getWord1());
            }
        }
        return verbs;
    }

    private static String expandNoun(String word, Collection<CustomEdge> relations) {
        String expanded = word;
        for (CustomEdge rel : relations) {
            if (rel.getType().equals("amod") && rel.getWord1().equals(word)) {
                expanded = rel.getWord2() + " " + expanded;
            }
            if (rel.getType().equals("nn") && rel.getWord1().equals(word)) {
                expanded = rel.getWord2() + " " + expanded;
            }
        }
        return expanded;
    }

}
