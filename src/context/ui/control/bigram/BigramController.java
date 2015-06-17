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
package context.ui.control.bigram;

import context.app.ProjectManager;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.TabularData;
import context.core.task.bigram.BigramApplicationTaskInstance;
import context.core.task.bigram.BigramTask;
import context.ui.control.workflow.basic2step.Basic2StepWorkflowController;
import context.ui.misc.NamingPolicy;
import javafx.beans.property.StringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 * @author cchin6 - Change output from text files to tabular data, Nov 7, 2014
 */
public class BigramController extends Basic2StepWorkflowController {

    /**
     *
     */
    public BigramController() {
        super();
        setTaskname(NamingPolicy.generateTaskName(BigramApplicationTaskInstance.class));
        setTaskInstance(new BigramApplicationTaskInstance(getTaskname()));
        super.postInitialize();
    }

    /**
     *
     * @param taskInstance
     */
    public BigramController(BigramApplicationTaskInstance taskInstance) {
        this();
        initializeTaskToGUIControls(taskInstance);
    }

    /**
     *
     * @param event
     */
    @Override
    public void handleStep3RunButton(ActionEvent event) {
        if (!this.validateInputOutput()) {
            return;
        }

        BigramApplicationTaskInstance instance = (BigramApplicationTaskInstance) getTaskInstance();
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        CorpusData input = new CorpusData(inputname, inputPath);
        input.setId(basicInputViewController.getSelectedInput().getId());
        instance.setInput(input);

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();

        /* Output is now a tabular data - Nov 7, 2014
        final String subdirectory = outputPath.get() + "/Bigram-Results/";
        FileHandler.createDirectory(subdirectory);
        FileList output = new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance), subdirectory);
        instance.setTextOutput(output);*/
        
        //Add new output for tabular data - Nov 7, 2014
        TabularData tabularData=new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance),NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance));
		instance.setTabularOutput(tabularData,0);

       
        System.out.println(instance);
        CTask task = new BigramTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new BigramNextStepsController(NamingPolicy.generateTaskName(BigramApplicationTaskInstance.class).get());
                nextStepsViewController.setParent(BigramController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                nextStepsViewController.setTabular(getTaskInstance().getTabularOutput(0));
                nextStepsViewController.init();
                System.out.println("result path=" + getTaskInstance().getTabularOutput(0).getPath());
                ProjectManager.getThisProject().addResult(getTaskInstance().getTabularOutput(0));
                if (isNew()) {
                    ProjectManager.getThisProject().addTask(getTaskInstance());
                    setNew(false);
                }
                showNextStepPane(nextStepsViewController);
            }
        });
        getProgressBar().setVisible(true);
        deactiveAllControls();
        task.start();

        setTaskInstance(task.getTaskInstance());

    }

}
