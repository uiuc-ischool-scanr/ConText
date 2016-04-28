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
package context.ui.control.lexisnexis;

import context.app.ProjectManager;
import context.core.task.lexisnexis.LexisNexisNetworkGenerationTask;
import context.core.task.lexisnexis.LexisNexisNetworkGenerationTaskInstance;
import context.core.task.lexisnexis.MetadataType;
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
public class LexisNexisNetworkGenerationController extends BasicWorkflowController {

    /**
     *
     */
    public LexisNexisNetworkGenerationController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(LexisNexisNetworkGenerationTaskInstance.class));
            setTaskInstance(new LexisNexisNetworkGenerationTaskInstance(getTaskname()));

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(LexisNexisNetworkGenerationConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (LexisNexisNetworkGenerationConfigurationController) loader2.getController();

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
    public LexisNexisNetworkGenerationController(LexisNexisNetworkGenerationTaskInstance taskInstance) {
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
        LexisNexisNetworkGenerationConfigurationController confController = (LexisNexisNetworkGenerationConfigurationController) configurationController;
        LexisNexisNetworkGenerationTaskInstance instance = (LexisNexisNetworkGenerationTaskInstance) getTaskInstance();
        instance.setInputDirectory(basicInputViewController.getSelectedItemLabel().textProperty().get());
        final int threshold = Integer.parseInt(confController.getThresholdTextField().textProperty().get());
        instance.setThreshold(threshold);
        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        instance.setOutputDirectory(outputPath.get());
        if (confController.getLLCheckBox().isSelected()) {
            instance.addPair(MetadataType.LOCATION, MetadataType.LOCATION);
        }
        if (confController.getOLCheckBox().isSelected()) {
            instance.addPair(MetadataType.ORGANIZATION, MetadataType.LOCATION);
        }
        if (confController.getOOCheckBox().isSelected()) {
            instance.addPair(MetadataType.ORGANIZATION, MetadataType.ORGANIZATION);
        }
        if (confController.getPLCheckBox().isSelected()) {
            instance.addPair(MetadataType.PERSON, MetadataType.LOCATION);
        }
        if (confController.getPOCheckBox().isSelected()) {
            instance.addPair(MetadataType.PERSON, MetadataType.ORGANIZATION);
        }
        if (confController.getPPCheckBox().isSelected()) {
            instance.addPair(MetadataType.PERSON, MetadataType.PERSON);
        }
        if (confController.getSLCheckBox().isSelected()) {
            instance.addPair(MetadataType.SUBJECT, MetadataType.LOCATION);
        }
        if (confController.getSOCheckBox().isSelected()) {
            instance.addPair(MetadataType.SUBJECT, MetadataType.ORGANIZATION);
        }
        if (confController.getSPCheckBox().isSelected()) {
            instance.addPair(MetadataType.SUBJECT, MetadataType.PERSON);
        }
        if (confController.getSSCheckBox().isSelected()) {
            instance.addPair(MetadataType.SUBJECT, MetadataType.SUBJECT);
        }

        System.out.println(instance);
        LexisNexisNetworkGenerationTask task = new LexisNexisNetworkGenerationTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new LexisNexisNetworkGenerationNextStepsViewController(NamingPolicy.generateTaskName(LexisNexisNetworkGenerationTaskInstance.class).get());
                nextStepsViewController.setParent(LexisNexisNetworkGenerationController.this);
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
