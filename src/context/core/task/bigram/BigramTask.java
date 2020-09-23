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

package context.core.task.bigram;

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
public class BigramTask extends CTask {

    /**
     *
     * @param progress
     * @param progressMessage
     */
    public BigramTask(DoubleProperty progress, StringProperty progressMessage) {
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
        task.progress(1, 20, "Starting Bigram Application...");
        BigramApplicationTaskInstance ins = (BigramApplicationTaskInstance) instance;
        CorpusData inputCorpus = (CorpusData) ins.getInput();
        inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));

        BigramBody bb = new BigramBody(ins);
        task.progress(5, 20, "Codebook loaded");

        task.progress(6, 20, "Running Bigram Application...");
        //Run codebook
        if (!bb.getMutualInfo()) {
            System.out.println("Error in coallating bigrams");
            return instance;
        }
        task.progress(13, 20, "Bigram Application successfully Done");
        //final String filepath = (ins.getTextOutput().getPath().get()).replaceAll("/$", "") + ".csv";
        final String path = ins.getTabularOutput(0).getPath().get();//Modifyto save tabular result

        task.progress(17, 20, "Saving results in " + path);
        bb.writeOutput(path);
        
        task.progress(20, 20, "Bigram Task Complete");
        task.done();
//        this.newline();
        return ins;
    }

}
