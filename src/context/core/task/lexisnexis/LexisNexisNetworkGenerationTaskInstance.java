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
package context.core.task.lexisnexis;

import context.core.entity.TaskInstance;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class LexisNexisNetworkGenerationTaskInstance extends TaskInstance {

    private String inputDirectory;
    private String outputDirectory;
    private Integer threshold;
    private List<Pair<MetadataType, MetadataType>> types;

    /**
     *
     * @param name
     */
    public LexisNexisNetworkGenerationTaskInstance(StringProperty name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public String getInputDirectory() {
        return inputDirectory;
    }

    /**
     *
     * @param inputDirectory
     */
    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    /**
     *
     * @return
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     *
     * @param outputDirectory
     */
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     *
     * @return
     */
    public Integer getThreshold() {
        return threshold;
    }

    /**
     *
     * @param threshold
     */
    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    /**
     *
     * @return
     */
    public List<Pair<MetadataType, MetadataType>> getTypes() {
        return types;
    }

    /**
     *
     * @param types
     */
    public void setTypes(List<Pair<MetadataType, MetadataType>> types) {
        this.types = types;
    }

    /**
     *
     * @param first
     * @param second
     */
    public void addPair(MetadataType first, MetadataType second) {
        if (types == null) {
            types = new ArrayList<>();
        }
        types.add(new Pair<MetadataType, MetadataType>(first, second));
    }

    @Override
    public String toString() {
        return this.getName().get();
        //return "LexisNexisNetworkGenrationInstance{" + "inputDirectory=" + inputDirectory + ", outputDirectory=" + outputDirectory + ", threshold=" + threshold + '}';
    }

}
