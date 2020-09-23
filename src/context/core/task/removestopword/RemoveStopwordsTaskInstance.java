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
package context.core.task.removestopword;

import context.core.entity.FileData;
import context.core.entity.TaskInstance;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kiumars Soltani
 */
public class RemoveStopwordsTaskInstance extends TaskInstance {

    private int removeType;
    private FileData stopwordFile;

    /**
     *
     * @param name
     */
    public RemoveStopwordsTaskInstance(StringProperty name) {
        super(name);
    }

    /**
     *
     * @param name
     * @param stopwordFile
     * @param removeType
     */
    public RemoveStopwordsTaskInstance(StringProperty name, FileData stopwordFile, int removeType) {
        super(name);
        this.stopwordFile = stopwordFile;
        this.removeType = removeType;
    }

    /**
     *
     * @return
     */
    public int getType() {
        return this.removeType;
    }

    /**
     *
     * @param removeType
     */
    public void setType(int removeType) {
        this.removeType = removeType;
    }

    /**
     *
     * @return
     */
    public FileData getStopwordFile() {
        return stopwordFile;
    }

    /**
     *
     * @param stopwordFile
     */
    public void setStopwordFile(FileData stopwordFile) {
        this.stopwordFile = stopwordFile;
    }

}
