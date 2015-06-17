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
package context.core.task.parsetree;

import context.core.textnets.Network;
import context.core.textnets.WordNode;
import context.core.tokenizer.CustomEdge;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Aale
 */
public class EdgeStream {

    List<CustomEdge> edges;
    String filename;

    /**
     *
     * @param filename
     */
    public EdgeStream(String filename) {
        this.edges = new ArrayList<>();
        this.filename = filename;
    }

    /**
     *
     * @param edge
     */
    public void addEdge(CustomEdge edge) {
        if (edges == null) {
            edges = new ArrayList<>();
        }
        edges.add(edge);
    }

    /**
     *
     * @param net
     */
    public void makeNetwork(Network net) {
        Set<String> allWords = new HashSet<String>();
        for (CustomEdge t : edges) {

            String w1 = fixCase(t.getWord1());
            w1 = removePOS(w1);
            String t1 = t.getType() + "-s";
            String w2 = fixCase(t.getWord2());
            w2 = removePOS(w2);
            String t2 = t.getType() + "-t";

            if (t.getType().equals("SP")) {
                t1 = "subject";
                t2 = "predicate";
            } else if (t.getType().equals("PO")) {
                t1 = "predicate";
                t2 = "object";
            }
            WordNode n1 = new WordNode(w1, t1);
            WordNode n2 = new WordNode(w2, t2);
            net.addEdge(n1, n2);
        }

        System.out.println("Finished Generating Network now off to printing files. ");
    }

    List<String> caseWords = Arrays.asList("this", "that", "these", "those", "the", "he", "she", "we", "you",
            "they", "their", "my", "our", "her", "his", "do", "did", "have", "has", "had", "doing", "what",
            "where", "when", "which", "it", "is");

    private String fixCase(String word) {
        if (caseWords.contains(word.toLowerCase())) {
            return word.toLowerCase();
        }
        return word;
    }

    private String removePOS(String w) {
        return w;
//        int lastIndex = w.lastIndexOf("/");
//        if (lastIndex != -1) {
//            return w.substring(0, lastIndex);
//        } else {
//            return w;
//        }
    }

}
