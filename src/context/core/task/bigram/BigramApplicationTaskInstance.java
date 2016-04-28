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
package context.core.task.bigram;

import context.core.entity.TaskInstance;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kiumars Soltani
 * @author Ming Jiang
 */
public class BigramApplicationTaskInstance extends TaskInstance {

    //For the codebook
	//StanfordCoreNLP pipeline;

    /**
     *
     * @param name
     */
    
    public BigramApplicationTaskInstance(StringProperty name) {
        super(name);
    }
    
//    public void setPipeline(StanfordCoreNLP pipeline) {
//        this.pipeline = pipeline;
//    }
//
//    public StanfordCoreNLP getPipeline() {
//        return pipeline;
//    }
    
}
