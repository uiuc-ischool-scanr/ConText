/* 
 * Copyright (c) 2020 University of Illinois Board of Trustees, All rights reserved.   
 * Developed at GSLIS/ the iSchool, by Dr. Jana Diesner, Chieh-Li Chin, Amirhossein Aleyasen, 
 * Shubhanshu Mishra, Kiumars Soltani, Liang Tao, Ming Jiang, Harathi Korrapati, 
 * Nikolaus Nova Parulian, and Lan Jiang.  
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

import context.app.main.ContextFX;
import context.app.main.ContextFXController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public abstract class CTask extends Service<TaskInstance> {
    
    DoubleProperty progress;
    StringProperty progressMessage;
    TaskInstance taskInstance;
    
    /**
     *
     * @param progress
     * @param progressMessage
     */
    public CTask(DoubleProperty progress, StringProperty progressMessage) {
        this.progress = new SimpleDoubleProperty(0);
        this.progressMessage = new SimpleStringProperty();
        progress.bind(this.progress);
        progressMessage.bind(this.progressMessage);
    }
    
    /**
     *
     * @return
     */
    public TaskInstance getTaskInstance() {
        return taskInstance;
    }
    
    /**
     *
     * @param taskInstance
     */
    public void setTaskInstance(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
    }
    
    /**
     *
     * @param instance
     * @param task
     * @return
     */
    public abstract TaskInstance run(TaskInstance instance, GenericTask task);
    
    /**
     *
     * @param message
     */
    public void trace(String message) {
        ContextFX.appController.appendLog(message);
        //ContextFXController.appendLog(message);
    }
    
    /**
     *
     * @param message
     */
    public void info(String message) {
        ContextFX.appController.appendLog(message);
        //ContextFXController.appendLog(message);
    }
    
    /**
     *
     * @param message
     */
    public void warning(String message) {
        ContextFX.appController.appendLog(message);
        //ContextFXController.appendLog(message);
    }
    
    /**
     *
     * @param message
     */
    public void error(String message) {
        ContextFX.appController.appendLog(message);
        //ContextFXController.appendLog(message);
    }
    
    /**
     *
     */
    public void newline() {
        ContextFX.appController.appendLog("\n");
        //ContextFXController.appendLog("\n");
    }
    
    /**
     *
     * @return
     */
    @Override
    protected Task<TaskInstance> createTask() {
        final TaskInstance instance = getTaskInstance();
        GenericTask task = new GenericTask() {
            @Override
            protected TaskInstance call() {
                return CTask.this.run(instance, this);
            }
        };
        
        if (!progress.isBound()) {
            progress.bind(this.progressProperty());
        }
        if (!progressMessage.isBound()) {
            progressMessage.bind(this.messageProperty());
        }
        return task;
    }
}
