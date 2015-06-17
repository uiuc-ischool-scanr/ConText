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

import context.app.main.ContextFXController;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class Project {

    StringProperty name;
    ObservableList<DataElement> data;
    ObservableList<DataElement> results;
    ObservableList<TaskInstance> tasks;
    String stopwordPath;

    /**
     *
     * @param name
     */
    public Project(StringProperty name) {
        this.name = name;
        data = FXCollections.observableArrayList();
        results = FXCollections.observableArrayList();
        tasks = FXCollections.observableArrayList();

        data.addListener(new ListChangeListener<DataElement>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends DataElement> change) {
                System.out.println("Data Changed: ");
                while (change.next()) {
                    if (change.wasAdded()) {
                        List added = change.getAddedSubList();
                        System.out.println("Added: " + added);
                    }
                    if (change.wasRemoved()) {
                        List removed = change.getRemoved();
                        System.out.println("Removed: " + removed);
                    }
                }
            }

        });

        results.addListener(new ListChangeListener<DataElement>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends DataElement> change) {
                System.out.println("Results Changed: ");
            }

        });

        tasks.addListener(new ListChangeListener<TaskInstance>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TaskInstance> change) {
                System.out.println("Tasks Changed: ");
            }

        });
    }

    /**
     *
     * @return
     */
    public ObservableList<DataElement> getData() {
        return data;
    }

    /**
     *
     * @param dataElement
     */
    public void addData(DataElement dataElement) {
        if (data == null) {
            data = FXCollections.observableArrayList();
        }
        data.add(dataElement);
        ContextFXController.addDataToTreeView(dataElement);
    }

    /**
     *
     * @return
     */
    public ObservableList<DataElement> getResults() {
        return results;
    }

    /**
     *
     * @param resultElement
     */
    public void addResult(DataElement resultElement) {
        if (results == null) {
            results = FXCollections.observableArrayList();
        }
        results.add(resultElement);
        ContextFXController.addResultToTreeView(resultElement);
    }

    /**
     *
     * @return
     */
    public ObservableList<TaskInstance> getTasks() {
        return tasks;
    }

    /**
     *
     * @param task
     */
    public void addTask(TaskInstance task) {
        if (tasks == null) {
            tasks = FXCollections.observableArrayList();
        }
        tasks.add(task);
        ContextFXController.addTaskToTreeView(task);
    }

    /**
     *
     * @param element
     */
    public void delete(ProjectElement element) {
        if (element instanceof DataElement) {
            DataElement d = (DataElement) element;
            if (data.contains(d)) {
                System.out.println(d.getName().get() + " deleted from data");
                data.remove(d);
            } else if (results.contains(d)) {
                System.out.println(d.getName().get() + " deleted from results");
                results.remove(d);
            }
        } else if (element instanceof TaskInstance) {
            TaskInstance t = (TaskInstance) element;
            if (tasks.contains(t)) {
                System.out.println(t.getName().get() + " deleted from tasks");
                tasks.remove(t);
            }
        }
    }

}
