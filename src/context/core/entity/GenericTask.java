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

import context.app.main.ContextFX;
import context.app.main.ContextFXController;
import javafx.concurrent.Task;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public abstract class GenericTask extends Task<TaskInstance> {
    
    /**
     *
     * @param message
     */
    public void progressMessage(String message) {
        updateMessage(message);
        ContextFX.appController.appendLog(message);
        //ContextFXController.appendLog(message);
        System.out.println(message);
    }
    
    /**
     *
     * @param l
     * @param l1
     * @param message
     */
    public void progress(long l, long l1, String message) {
        updateProgress(l, l1);
        progressMessage(message);
    }
    
    public void done() {
        progressMessage("");
    }
    
    /**
     *
     * @param l
     * @param l1
     */
    public void progress(long l, long l1) {
        updateProgress(l, l1);
    }
}
