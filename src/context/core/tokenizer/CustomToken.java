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
public class CustomToken {

    int beginPosition;
    int endPosition;
    int sentenceIndex;
    String docId;
    int index;
    String word;
    String tag;
    String pos;
    String ner;
    boolean multiword;

    /**
     *
     * @param beginPosition
     */
    public void setBeginPosition(int beginPosition) {
        this.beginPosition = beginPosition;
    }

    /**
     *
     * @param docId
     */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /**
     *
     * @param endPosition
     */
    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    /**
     *
     * @param multiword
     */
    public void setMultiword(boolean multiword) {
        this.multiword = multiword;
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
     * @param tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     *
     * @param ner
     */
    public void setNer(String ner) {
        this.ner = ner;
    }

    /**
     *
     * @param pos
     */
    public void setPos(String pos) {
        this.pos = pos;
    }

    /**
     *
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     *
     * @return
     */
    public boolean isMultiword() {
        return multiword;
    }

    /**
     *
     * @return
     */
    public int getBeginPosition() {
        return beginPosition;
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
    public int getEndPosition() {
        return endPosition;
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

    /**
     *
     * @return
     */
    public String getTag() {
        return tag;
    }

    /**
     *
     * @return
     */
    public String getNer() {
        return ner;
    }

    /**
     *
     * @return
     */
    public String getPos() {
        return pos;
    }

    /**
     *
     * @return
     */
    public String getWord() {
        return word;
    }

//    @Override
//    public String toString() {
//        return "CustomToken{" + "beginPosition=" + beginPosition + ", endPosition=" + endPosition + ", sentenceIndex=" + sentenceIndex + ", docId=" + docId + ", index=" + index + ", tag=" + tag + ", multiword=" + multiword + '}';
//    }
    @Override
    public String toString() {
        return word;
    }

}
