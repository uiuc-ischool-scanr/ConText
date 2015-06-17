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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aale
 */
public class SPOStructure {

    List<CustomToken> subjects;
    List<CustomToken> objects;
    CustomToken predicate;

    /**
     *
     */
    public SPOStructure() {
        subjects = new ArrayList<CustomToken>();
        objects = new ArrayList<CustomToken>();
    }

    /**
     *
     * @param subjects
     */
    public void setSubjects(List<CustomToken> subjects) {
        this.subjects = subjects;
    }

    /**
     *
     * @return
     */
    public List<CustomToken> getSubjects() {
        return subjects;
    }

    /**
     *
     * @param objects
     */
    public void setObjects(List<CustomToken> objects) {
        this.objects = objects;
    }

    /**
     *
     * @return
     */
    public List<CustomToken> getObjects() {
        return objects;
    }

    /**
     *
     * @param predicate
     */
    public void setPredicate(CustomToken predicate) {
        this.predicate = predicate;
    }

    /**
     *
     * @return
     */
    public CustomToken getPredicate() {
        return predicate;
    }

    /**
     *
     * @param subject
     */
    public void addSubject(CustomToken subject) {
        if (subjects == null) {
            subjects = new ArrayList<>();
        }
        subjects.add(subject);
    }

    /**
     *
     * @param object
     */
    public void addObject(CustomToken object) {
        if (objects == null) {
            objects = new ArrayList<>();
        }
        objects.add(object);
    }

    @Override
    public String toString() {
        return "{" + "subjects=" + subjects + ", predicate=" + predicate + ", objects=" + objects + '}';
    }

}
