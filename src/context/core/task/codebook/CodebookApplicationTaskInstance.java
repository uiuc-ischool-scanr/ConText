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
package context.core.task.codebook;

import context.core.entity.TaskInstance;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kiumars Soltani
 */
public class CodebookApplicationTaskInstance extends TaskInstance {

    //For the codebook
    private int isNormal; //0 - normal 1- not normal(replace with type)
    private int isDrop; //0 - no drop 1 - drop with "" 2- drop with placeholder
    private String codebookFile;

    //For network generation
    private boolean isNetwork;
    private int netOutputType; //0- node,node 1- node,type,node,type
    private boolean netOutputCSV;
    private boolean netOutputGEXF;
    private boolean netInputCorpus;
    private int distance;
    private int separator; // 1- sentence 2- paragraph 3-words 4-custom
    private String customTag;

    /**
     *
     * @param name
     */
    public CodebookApplicationTaskInstance(StringProperty name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public int getIsNormal() {
        return isNormal;
    }

    /**
     *
     * @param isNormal
     */
    public void setIsNormal(int isNormal) {
        this.isNormal = isNormal;
    }

    /**
     *
     * @return
     */
    public int getIsDrop() {
        return isDrop;
    }

    /**
     *
     * @param isDrop
     */
    public void setIsDrop(int isDrop) {
        this.isDrop = isDrop;
    }

    /**
     *
     * @return
     */
    public String getCodebookFile() {
        return codebookFile;
    }

    /**
     *
     * @param codebookFile
     */
    public void setCodebookFile(String codebookFile) {
        this.codebookFile = codebookFile;
    }

    /**
     *
     * @return
     */
    public int getNetOutputType() {
        return netOutputType;
    }

    /**
     *
     * @param netOutputType
     */
    public void setNetOutputType(int netOutputType) {
        this.netOutputType = netOutputType;
    }

    /**
     *
     * @return
     */
    public boolean isNetOutputCSV() {
        return netOutputCSV;
    }

    /**
     *
     * @param netOutputCSV
     */
    public void setNetOutputCSV(boolean netOutputCSV) {
        this.netOutputCSV = netOutputCSV;
    }

    /**
     *
     * @return
     */
    public boolean isNetOutputGEXF() {
        return netOutputGEXF;
    }

    /**
     *
     * @param netOutputGEXF
     */
    public void setNetOutputGEXF(boolean netOutputGEXF) {
        this.netOutputGEXF = netOutputGEXF;
    }

    /**
     *
     * @return
     */
    public boolean isNetInputCorpus() {
        return netInputCorpus;
    }

    /**
     *
     * @param netInputCorpus
     */
    public void setNetInputCorpus(boolean netInputCorpus) {
        this.netInputCorpus = netInputCorpus;
    }

    /**
     *
     * @return
     */
    public int getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     */
    public int getSeparator() {
        return separator;
    }

    /**
     *
     * @param separator
     */
    public void setSeparator(int separator) {
        this.separator = separator;
    }

    /**
     *
     * @return
     */
    public String getCustomTag() {
        return customTag;
    }

    /**
     *
     * @param customTag
     */
    public void setCustomTag(String customTag) {
        this.customTag = customTag;
    }

    /**
     *
     * @return
     */
    public boolean isNetwork() {
        return isNetwork;
    }

    /**
     *
     * @param isNetwork
     */
    public void setNetwork(boolean isNetwork) {
        this.isNetwork = isNetwork;
    }
}
