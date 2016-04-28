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
package context.core.task.lexisnexis;

import context.core.entity.CTask;
import context.core.entity.GenericTask;
import context.core.entity.TaskInstance;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class LexisNexisNetworkGenerationTask extends CTask {

    /**
     *
     * @param progress
     * @param progressMessage
     */
    public LexisNexisNetworkGenerationTask(DoubleProperty progress, StringProperty progressMessage) {
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
        info("LexisNexis network generation:");
        LexisNexisNetworkGenerationTaskInstance inst = (LexisNexisNetworkGenerationTaskInstance) instance;
        LxNxDataProvider lexisDataProvider = new LxNxDataProvider();
        info("Parsing and deduplication...");
        task.progressMessage("parsing...");
        task.progress(10, 100);
        lexisDataProvider.parseAndDeduplicate(inst.getInputDirectory(), null, null, null, null, null, null, false);
        List<LxNxMetadata> list = lexisDataProvider.getUniqueList();
        for (Pair<MetadataType, MetadataType> pair : inst.getTypes()) {
            info("Generating " + pair.getKey().getValue() + "-" + pair.getValue().getValue() + " network...");
            task.progressMessage("Generating " + pair.getKey().getValue() + "-" + pair.getValue().getValue() + " network...");
            task.progress(50, 100);
            lexisDataProvider.generateAggrMDNetwork(list, inst.getOutputDirectory(), pair.getKey(), pair.getValue(), inst.getThreshold());
        }
        task.progress(100, 100);
        task.progressMessage("Done.");
        info("LexisNexis network generation Done.");
        return instance;
    }

}
