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
package context.core.entity;

import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class FileList extends CorpusData {

    private File dir;

    /**
     *
     * @param name
     * @param dir
     */
    public FileList(StringProperty name, String dir) {
        super(name, new SimpleStringProperty(dir));
        this.dir = new File(dir);
    }

    /**
     *
     * @param name
     * @param dir
     */
    public FileList(StringProperty name, File dir) {
        super(name, new SimpleStringProperty(dir.getAbsolutePath()));
        this.dir = dir;
    }

    /**
     *
     * @return
     */
    public File getDir() {
        return dir;
    }

    /**
     *
     * @param dir
     */
    public void setDir(File dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
