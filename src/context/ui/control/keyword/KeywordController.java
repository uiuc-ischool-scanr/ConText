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
package context.ui.control.keyword;

import context.app.ProjectManager;
import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.FileList;
import context.core.entity.TabularData;
import context.core.task.keyword.KeywordTask;
import context.core.task.keyword.KeywordTaskInstance;
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
 * @author Amirhossein Aleysain, Kiumars Soltani
 */
public class KeywordController extends BasicWorkflowController {

    /**
     *
     */
    public KeywordController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(KeywordTaskInstance.class));
            setTaskInstance(new KeywordTaskInstance(getTaskname()));

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(KeywordConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (KeywordConfigurationController) loader2.getController();

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
    public KeywordController(KeywordTaskInstance taskInstance) {
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
        KeywordConfigurationController confController = (KeywordConfigurationController) configurationController;
        KeywordTaskInstance instance = (KeywordTaskInstance) getTaskInstance();
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        /*instance.setDropnum(basicInputViewController.isDropnum());
        instance.setDroppun(basicInputViewController.isDroppun());
        instance.setKeeppou(basicInputViewController.isKeeppou());*/
        
        CorpusData input = new CorpusData(inputname, inputPath);
        instance.setInput(input);
        
        
        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();
        
        /*
        Niko
        create parent directory for the output output
        */
        final String subdirectory = outputPath.get()+"/Keyword-In-Context/";
        FileHandler.createDirectory(subdirectory);
        //System.out.println("Created sub dir: "+subdirectory);
        FileList output=new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance),subdirectory);
        final FileList oldOutput = (FileList) instance.getTextOutput();
        final String oldDir = outputPath.get();
        instance.setTextOutput(output);  
        outputPath.set(subdirectory);
        /*
        End Addition
        */
        
        
        final String subdirectory2 = outputPath.get() + "/KWIC-Results/";
        FileHandler.createDirectory(subdirectory2);
        output = new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance), subdirectory2);

        instance.setTextOutput(output);

        TabularData tabularData = new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance),
                NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance));
        instance.setTabularOutput(tabularData, 0);

        StringProperty keywordPath = confController.getKeywordFileTextField().textProperty();
        instance.setKeywordFile(new FileData(FileHandler.getFileNameProperty(keywordPath.get()), keywordPath));

        instance.setLeftBound(confController.getLeftBound());
        instance.setRightBound(confController.getRightBound());
        instance.setOmitCase(confController.isOmitCase());
        KeywordTask task = new KeywordTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new KeywordNextStepsController(NamingPolicy.generateTaskName(KeywordTaskInstance.class).get());
                nextStepsViewController.setParent(KeywordController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                nextStepsViewController.setFilelist((FileList) getTaskInstance().getTextOutput());
                nextStepsViewController.setTabular(getTaskInstance().getTabularOutput(0));
                nextStepsViewController.init();
                ProjectManager.getThisProject().addData(getTaskInstance().getTextOutput());
                ProjectManager.getThisProject().addResult(getTaskInstance().getTabularOutput(0));
                if (isNew()) {
                    ProjectManager.getThisProject().addTask(getTaskInstance());
                    setNew(false);
                }
                nextStepsViewController.setOutputDir(oldDir);
                showNextStepPane(nextStepsViewController);
            }
        });
        getProgressBar().setVisible(true);
        deactiveAllControls();
        task.start();

        setTaskInstance(task.getTaskInstance());

    }

}
