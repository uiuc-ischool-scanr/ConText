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
package context.core.task.removestopword;

import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.GenericTask;
import context.core.entity.TaskInstance;
import java.io.File;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kiumars Soltani
 */
public class RemoveStopwordsTask extends CTask {

    /**
     *
     * @param progress
     * @param progressMessage
     */
    public RemoveStopwordsTask(DoubleProperty progress, StringProperty progressMessage) {
        super(progress, progressMessage);
    }

    /**
     *
     * @param instance
     * @param task
     * @return
     */
    @Override
    public TaskInstance run(TaskInstance instance, GenericTask task) {
        task.progress(1, 20, "Starting Remove Stopwords process");

        RemoveStopwordsTaskInstance ins = (RemoveStopwordsTaskInstance) instance;
        CorpusData inputCorpus = (CorpusData) ins.getInput();
        task.progress(4, 20, "Loading " + inputCorpus.getPath().get());
        inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));
        task.progress(8, 20, inputCorpus.getFiles().size() + " files loaded");

        RemoveStopWord rsw = new RemoveStopWord(ins);
        task.progress(10, 20, "Running Remove Stopwords...");
        if (rsw.remove_stop_words()) {
            task.progress(20, 20, "Remove Stopwords successfully done");
            task.done();
            return ins;
        } else {
            System.out.println("Failed to run the job");
            return instance;
        }
    }

}
