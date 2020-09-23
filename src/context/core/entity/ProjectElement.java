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
package context.core.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class ProjectElement {

    /**
     *
     */
    protected StringProperty name;

    /**
     *
     */
    protected StringProperty id;

    /**
     *
     * @param name
     */
    public ProjectElement(StringProperty name) {
        this.id = generateRandomId();
        this.name = name;
    }

    /**
     *
     * @return
     */
    public StringProperty getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(StringProperty name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public StringProperty getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(StringProperty id) {
        this.id = id;
    }

    @Override
    public String toString() {
        if (name == null || name.get().length() == 0) {
            return "DEFAULT-NAME";
        }
        return name.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ProjectElement)) {
            return false;
        }
        final ProjectElement other = (ProjectElement) obj;
        if (!Objects.equals(this.id.get(), other.id.get())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    static int Min = 1000;
    static int Max = 9999;

    private StringProperty generateRandomId() {
        DateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
        Date date = new Date();
        String id = dateFormat.format(date) + "" + (Min + (int) (Math.random() * ((Max - Min) + 1)));
        return new SimpleStringProperty(id);
    }

}
