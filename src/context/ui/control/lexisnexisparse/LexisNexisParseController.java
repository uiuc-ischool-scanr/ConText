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
package context.ui.control.lexisnexisparse;

import context.app.ProjectManager;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.task.lexisnexis.LexisNexisParseTask;
import context.core.task.lexisnexis.LexisNexisParseTaskInstance;
import context.ui.control.workflow.basic2step.Basic2StepWorkflowController;
import context.ui.misc.NamingPolicy;
import javafx.beans.property.StringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class LexisNexisParseController extends Basic2StepWorkflowController {

    /**
     *
     */
    public LexisNexisParseController() {
        super();
        setTaskname(NamingPolicy.generateTaskName(LexisNexisParseTaskInstance.class));
        setTaskInstance(new LexisNexisParseTaskInstance(getTaskname()));
        super.postInitialize();
    }

    /**
     *
     * @param taskInstance
     */
    public LexisNexisParseController(LexisNexisParseTaskInstance taskInstance) {
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
        LexisNexisParseTaskInstance instance = (LexisNexisParseTaskInstance) getTaskInstance();
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        CorpusData input = new CorpusData(inputname, inputPath);

        instance.setInput(input);

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();
        CorpusData output = new CorpusData(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance), outputPath);
        instance.setTextOutput(output);

        CTask task = new LexisNexisParseTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new LexisNexisParseNextStepsController(NamingPolicy.generateTaskName(LexisNexisParseTaskInstance.class).get());
                nextStepsViewController.setParent(LexisNexisParseController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                nextStepsViewController.init();
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
