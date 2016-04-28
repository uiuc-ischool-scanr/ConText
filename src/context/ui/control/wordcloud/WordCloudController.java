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
package context.ui.control.wordcloud;

import context.app.ProjectManager;
import context.core.entity.*;
import context.core.task.wordcloud.WordCloudTask;
import context.core.task.wordcloud.WordCloudTaskInstance;
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
public class WordCloudController extends BasicWorkflowController {

    /**
     *
     */
    public WordCloudController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(WordCloudTaskInstance.class));
            setTaskInstance(new WordCloudTaskInstance(getTaskname()));

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(WordCloudConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (WordCloudConfigurationController) loader2.getController();

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
    public WordCloudController(WordCloudTaskInstance taskInstance) {
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
        WordCloudConfigurationController confController = (WordCloudConfigurationController) configurationController;
        WordCloudTaskInstance instance = (WordCloudTaskInstance) getTaskInstance();
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        CorpusData input = new CorpusData(inputname, inputPath);
        instance.setInput(input);

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();
        CorpusData output = new CorpusData(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance),
                outputPath);
        instance.setTextOutput(output);
//        TabularData tabularData = new TabularData(NamingPolicy.generateTabularName(inputPath.get(), outputPath.get(), instance),
//                NamingPolicy.generateTabularPath(inputPath.get(), outputPath.get(), instance));
//        instance.setTabularOutput(tabularData, 0);
        instance.setStopListLoc(confController.getStopwordFile());
        instance.setSentimentLoc(confController.getSentimentFile());
        instance.setNumIters(confController.getNumberOfIterations());
        instance.setNumTopics(confController.getNumberOfTopics());
        instance.setWordPerTopic(confController.getNumberOfWordsPerTopics());
        instance.setSumAlpha(confController.getSumAlpha());
        instance.setNumOptInterval(confController.getNumberOfOptInterval());       
        
        instance.setClustering(confController.isClustering());
        instance.setHeight(confController.getCloudHeight());
        instance.setWidth(confController.getCloudWidth());
        instance.setMinFontSize(confController.getFontSize());
        instance.setIsLowercase(confController.getIsLowercase());
        CTask task = new WordCloudTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new WordCloudNextStepsController(NamingPolicy.generateTaskName(WordCloudTaskInstance.class).get());
                nextStepsViewController.setParent(WordCloudController.this);
                nextStepsViewController.setOutputDir(outputPath.get() + "/WordCloud/WordCloud.html");
                nextStepsViewController.init();
//                nextStepsViewController.setTabular(getTaskInstance().getTabularOutput(0));
//                ProjectManager.getThisProject().addResult(getTaskInstance().getTabularOutput(0));
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
