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

import context.app.AppConfig;
import context.ui.misc.FileHandler;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kiumars Soltani, Amirhossein Aleyasin
 */
public abstract class TaskInstance extends ProjectElement {

    /**
     *
     */
    protected DataElement input;

    /**
     *
     */
    protected DataElement textOutput;

    /**
     *
     */
    protected List<TabularData> tabularOutput;

    /**
     *
     * @param name
     */
    public TaskInstance(StringProperty name) {
        super(name);
        tabularOutput = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public DataElement getInput() {
        return this.input;
    }

    /**
     *
     * @param path
     */
    public void setInput(String path) {
        String filename = FileHandler.getFileName(path);
        this.input = new DataElement(new SimpleStringProperty(filename), new SimpleStringProperty(path));
    }

    /**
     *
     * @param input
     */
    public void setInput(DataElement input) {
        this.input = input;
    }

    /**
     *
     * @return
     */
    public DataElement getTextOutput() {
        return textOutput;
    }

    /**
     *
     * @return
     */
    public List<TabularData> getTabularOutput() {
        return tabularOutput;
    }

    /**
     *
     * @param index
     * @return
     */
    public TabularData getTabularOutput(int index) {
        if (this.tabularOutput == null) {
            System.out.println("tabularOutput is null");
            return null;
        }
        System.out.println("size=" + this.tabularOutput.size());
        if (index >= this.tabularOutput.size()) {
            System.out.println("size is less than index" + " index=" + index + " size=" + this.tabularOutput.size());
            return null;
        }
        return this.tabularOutput.get(index);
    }

    /**
     *
     * @param textOutput
     */
    public void setTextOutput(DataElement textOutput) {
        this.textOutput = textOutput;
    }

    /**
     *
     * @param path
     */
    public void setTextOutput(String path) {
        String filename = FileHandler.getFileName(path);
        this.textOutput = new DataElement(new SimpleStringProperty(filename), new SimpleStringProperty(path));
    }

    /**
     *
     * @param tabularOutput
     */
    public void setTabularOutput(List<TabularData> tabularOutput) {
        this.tabularOutput = tabularOutput;
    }

    /**
     *
     * @param tabularOutput
     * @param index
     */
    public void setTabularOutput(TabularData tabularOutput, int index) {
//        if (index >= this.tabularOutput.size()) {
//            return;
//        }
        this.tabularOutput.add(index, tabularOutput);
        System.out.println("tabular size=" + this.tabularOutput.size());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return AppConfig.getTaskLabel(this.getName().get());
    }

}
