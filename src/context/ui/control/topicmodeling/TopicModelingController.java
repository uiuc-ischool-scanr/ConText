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
package context.ui.control.topicmodeling;

import context.app.ProjectManager;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.TabularData;
import context.core.task.topicmodeling.TopicModelingTask;
import context.core.task.topicmodeling.TopicModelingTaskInstance;
import context.ui.control.workflow.basic.BasicWorkflowController;
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
public class TopicModelingController extends BasicWorkflowController {

    /**
     *
     */
    public TopicModelingController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(TopicModelingTaskInstance.class));
            setTaskInstance(new TopicModelingTaskInstance(getTaskname()));

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(TopicModelingConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (TopicModelingConfigurationController) loader2.getController();

            setStep2Content(s2Content);

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     *
     * @param taskInstance
     */
    public TopicModelingController(TopicModelingTaskInstance taskInstance) {
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
        TopicModelingConfigurationController confController = (TopicModelingConfigurationController) configurationController;
        TopicModelingTaskInstance instance = (TopicModelingTaskInstance) getTaskInstance();
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        CorpusData input = new CorpusData(inputname, inputPath);
        instance.setInput(input);

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();
//        CorpusData output = new CorpusData(NamingPolicy.generateOutputName(inputPath.get(), outputPath.get(), instance),
//                outputPath);
//        instance.setTextOutput(output);

        TabularData tabularData1 = new TabularData(NamingPolicy.generateTabularName(inputname.get() + "-P", outputPath.get(), instance),
                NamingPolicy.generateTabularPath(inputname.get() + "-P", outputPath.get(), instance));
        instance.setTabularOutput(tabularData1, 0);

        TabularData tabularData2 = new TabularData(NamingPolicy.generateTabularName(inputname.get() + "-W", outputPath.get(), instance),
                NamingPolicy.generateTabularPath(inputname.get() + "-W", outputPath.get(), instance));
        instance.setTabularOutput(tabularData2, 1);

        instance.setNumIterations(confController.getNumberOfIterations());
        instance.setNumTopics(confController.getNumberOfTopics());
        instance.setNumWordsPerTopic(confController.getNumberOfWordsPerTopics());
        instance.setStopListLoc(confController.getStopwordFile());
        instance.setIsLowercase(confController.getIsLowercase());

        System.out.println(instance);
        CTask task = new TopicModelingTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new TopicModelingNextStepsController(NamingPolicy.generateTaskName(TopicModelingTaskInstance.class).get());
                nextStepsViewController.setParent(TopicModelingController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                System.out.println("injaaaaaaaaaaaaaaa");
                System.out.println(": " + getTaskInstance().getTabularOutput());
                nextStepsViewController.setTabulars(getTaskInstance().getTabularOutput());
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
