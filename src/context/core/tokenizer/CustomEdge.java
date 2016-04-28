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

/**
 *
 * @author Aale
 */
public class CustomEdge {

    int sentenceIndex;
    String docId;
    int index;
    String word1;
    String word2;
    String type;

    /**
     *
     * @param docId
     */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /**
     *
     * @param sentenceIndex
     */
    public void setSentenceIndex(int sentenceIndex) {
        this.sentenceIndex = sentenceIndex;
    }

    /**
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     *
     * @param word1
     */
    public void setWord1(String word1) {
        this.word1 = word1;
    }

    /**
     *
     * @param word2
     */
    public void setWord2(String word2) {
        this.word2 = word2;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getWord1() {
        return word1;
    }

    /**
     *
     * @return
     */
    public String getWord2() {
        return word2;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return
     */
    public String getDocId() {
        return docId;
    }

    /**
     *
     * @return
     */
    public int getSentenceIndex() {
        return sentenceIndex;
    }

    /**
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "CustomEdge{" + "sentenceIndex=" + sentenceIndex + ", docId=" + docId + ", index=" + index + ", word1=" + word1 + ", word2=" + word2 + ", type=" + type + '}';
    }

}
