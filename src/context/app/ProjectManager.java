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
package context.app;

import context.core.entity.Project;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class ProjectManager {

    /**
     *
     */
    public static ProjectManager instance = new ProjectManager();
    ObservableList<Project> projects;
    Project currentProject;

    /**
     *
     */
    public ProjectManager() {
        System.out.println("Initialize ProjectManager...");
        currentProject = new Project(new SimpleStringProperty("My Project"));
        projects = FXCollections.observableArrayList();

        projects.addListener(new ListChangeListener<Project>() {

            @Override
            public void onChanged(ListChangeListener.Change<? extends Project> change) {
                System.out.println("Projects Changed:" + change);
            }

        });
    }

    /**
     *
     * @return
     */
    public ObservableList<Project> getProjects() {
        return projects;
    }

    /**
     *
     * @param project
     */
    public void addProject(Project project) {
        if (projects == null) {
            projects = FXCollections.observableArrayList();
        }
        projects.add(project);
    }

    /**
     *
     * @param index
     * @return
     */
    public Project getProject(int index) {
        return projects.get(index);
    }

    /**
     *
     * @return
     */
    public Project getCurrentProject() {
        return currentProject;
    }

    /**
     *
     * @param currentProject
     */
    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    /**
     *
     * @return
     */
    public static Project getThisProject() {
        return instance.getCurrentProject();
    }

}
