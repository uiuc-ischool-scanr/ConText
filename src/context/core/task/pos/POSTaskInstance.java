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
package context.core.task.pos;

import context.core.entity.TaskInstance;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class POSTaskInstance extends TaskInstance {

    StanfordCoreNLP pipeline;
    String language;

    /**
     *
     * @param name
     */
    public POSTaskInstance(StringProperty name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public StanfordCoreNLP getPipeline() {
        return pipeline;
    }

    /**
     *
     * @param pipeline
     */
    public void setPipeline(StanfordCoreNLP pipeline) {
        this.pipeline = pipeline;
    }

    /**
     *
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     *
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }
}
