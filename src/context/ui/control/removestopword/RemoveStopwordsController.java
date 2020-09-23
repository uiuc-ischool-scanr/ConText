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
package context.ui.control.removestopword;

import context.app.ProjectManager;
import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.entity.FileList;
import context.core.task.removestopword.RemoveStopwordsTask;
import context.core.task.removestopword.RemoveStopwordsTaskInstance;
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
public class RemoveStopwordsController extends BasicWorkflowController {

    /**
     *
     */
    public RemoveStopwordsController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(RemoveStopwordsTaskInstance.class));
            setTaskInstance(new RemoveStopwordsTaskInstance(getTaskname()));

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(RemoveStopwordsConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (RemoveStopwordsConfigurationController) loader2.getController();

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
    public RemoveStopwordsController(RemoveStopwordsTaskInstance taskInstance) {
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
        RemoveStopwordsConfigurationController confController = (RemoveStopwordsConfigurationController) configurationController;
        RemoveStopwordsTaskInstance instance = (RemoveStopwordsTaskInstance) getTaskInstance();
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        /*instance.setDropnum(basicInputViewController.isDropnum());
        instance.setDroppun(basicInputViewController.isDroppun());
        instance.setKeeppou(basicInputViewController.isKeeppou());*/
        CorpusData input = new CorpusData(inputname, inputPath);
        instance.setInput(input);

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();
        final String subdirectory = outputPath.get() + "/RSW-Results/";
        FileHandler.createDirectory(subdirectory);
        FileList output = new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance), subdirectory);

        instance.setTextOutput(output);

        StringProperty stopwordPath = confController.getStopwordFileTextField().textProperty();
        instance.setStopwordFile(new FileData(FileHandler.getFileNameProperty(stopwordPath.get()), stopwordPath));

        if (confController.getDropRadioButton().isSelected()) {
            instance.setType(1);
        } else {
            instance.setType(2);
        }

        RemoveStopwordsTask task = new RemoveStopwordsTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new RemoveStopwordsNextStepsController(NamingPolicy.generateTaskName(RemoveStopwordsTaskInstance.class).get());
                nextStepsViewController.setParent(RemoveStopwordsController.this);
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
