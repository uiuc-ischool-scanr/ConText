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
package context.ui.control.stemming;

import context.app.ProjectManager;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.FileList;
import context.core.task.stemming.StemmingTask;
import context.core.task.stemming.StemmingTaskInstance;
import context.ui.control.workflow.basic2step.Basic2StepWorkflowController;
import context.ui.misc.FileHandler;
import context.ui.misc.NamingPolicy;
import javafx.beans.property.StringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class StemmingController extends Basic2StepWorkflowController {

    /**
     *
     */
    public StemmingController() {
        super();
//        try {
        setTaskname(NamingPolicy.generateTaskName(StemmingTaskInstance.class));
        setTaskInstance(new StemmingTaskInstance(getTaskname()));

//            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(StemmingConfigurationController.path));
//            Parent s2Content = (Parent) loader2.load();
//            configurationController = (StemmingConfigurationController) loader2.getController();
//            setStep2Content(s2Content);
        super.postInitialize();

//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
    }

    /**
     *
     * @param taskInstance
     */
    public StemmingController(StemmingTaskInstance taskInstance) {
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
//        StemmingConfigurationController configurationViewController = (StemmingConfigurationController) configurationController;
        StemmingTaskInstance instance = (StemmingTaskInstance) getTaskInstance();
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        CorpusData input = new CorpusData(inputname, inputPath);
        instance.setInput(input);

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();

        final String subdirectory = outputPath.get() + "/Stemming-Results/";
        FileHandler.createDirectory(subdirectory);
        FileList output = new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance), subdirectory);
        instance.setTextOutput(output);

        //TODO: set other parameters here
        System.out.println(instance);
        CTask task = new StemmingTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new StemmingNextStepsController(NamingPolicy.generateTaskName(StemmingTaskInstance.class).get());
                nextStepsViewController.setParent(StemmingController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                nextStepsViewController.setFilelist((FileList) getTaskInstance().getTextOutput());
                nextStepsViewController.init();
                ProjectManager.getThisProject().addData(getTaskInstance().getTextOutput());
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
