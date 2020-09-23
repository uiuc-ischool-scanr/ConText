/*
 * Copyright (c) 2020 University of Illinois Board of Trustees, All rights reserved.   
* Developed at the iSchool, by Dr. Jana Diesner, Chieh-Li Chin, 
* Amirhossein Aleyasen, Shubhanshu Mishra, Kiumars Soltani, Liang Tao, 
* Ming Jiang, Harathi Korrapati, Nikolaus Nova Parulian, and Lan Jiang.
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
package context.ui.control.sentiment;

import context.app.ProjectManager;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.TabularData;
import context.core.task.sentiment.SentimentTask;
import context.core.task.sentiment.SentimentTaskInstance;
import context.ui.control.workflow.basic.BasicWorkflowController;
import context.ui.misc.FileHandler;
import context.ui.misc.NamingPolicy;
import java.io.IOException;
import javafx.beans.property.StringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.openide.util.Exceptions;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class SentimentController extends BasicWorkflowController {

    /**
     *
     */
    public SentimentController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(SentimentTaskInstance.class));
            setTaskInstance(new SentimentTaskInstance(getTaskname()));

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(SentimentConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (SentimentConfigurationController) loader2.getController();

            setStep2Content(s2Content);
            super.postInitialize();

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     *
     * @param taskInstance
     */
    public SentimentController(SentimentTaskInstance taskInstance) {
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
        SentimentConfigurationController confController = (SentimentConfigurationController) configurationController;
        SentimentTaskInstance instance = (SentimentTaskInstance) getTaskInstance();
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        /*instance.setDropnum(basicInputViewController.isDropnum());
        instance.setDroppun(basicInputViewController.isDroppun());
        instance.setKeeppou(basicInputViewController.isKeeppou());*/
        CorpusData input = new CorpusData(inputname, inputPath);
        instance.setInput(input);

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();
//        CorpusData output = new CorpusData(NamingPolicy.generateOutputName(inputPath.get(), outputPath.get(), instance), outputPath);
//        instance.setTextOutput(output);

        TabularData tabularData = new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance),
                NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance));
        instance.setTabularOutput(tabularData, 0);

        StringProperty stopwordPath = confController.getSentimentFile();
        instance.setSentimentFile(new FileData(FileHandler.getFileNameProperty(stopwordPath.get()), stopwordPath));

        CTask task = new SentimentTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new SentimentNextStepsController(NamingPolicy.generateTaskName(SentimentTaskInstance.class).get());
                nextStepsViewController.setParent(SentimentController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                nextStepsViewController.setTabular(getTaskInstance().getTabularOutput(0));
                nextStepsViewController.init();
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
