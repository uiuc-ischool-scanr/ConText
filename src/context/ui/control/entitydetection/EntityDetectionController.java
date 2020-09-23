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
package context.ui.control.entitydetection;

import context.app.ProjectManager;
import context.core.entity.*;
import context.core.task.entitydetection.EntityDetectionTask;
import context.core.task.entitydetection.EntityDetectionTaskInstance;
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
public class EntityDetectionController extends BasicWorkflowController {

    /**
     *
     */
    public EntityDetectionController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(EntityDetectionTaskInstance.class));
            setTaskInstance(new EntityDetectionTaskInstance(getTaskname()));

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(EntityDetectionConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (EntityDetectionConfigurationController) loader2.getController();

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
    public EntityDetectionController(EntityDetectionTaskInstance taskInstance) {
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

        final EntityDetectionTaskInstance instance = (EntityDetectionTaskInstance) getTaskInstance();
        EntityDetectionConfigurationController confController = (EntityDetectionConfigurationController) configurationController;

        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        /*instance.setDropnum(basicInputViewController.isDropnum());
        instance.setDroppun(basicInputViewController.isDroppun());
        instance.setKeeppou(basicInputViewController.isKeeppou());*/
        CorpusData input = new CorpusData(inputname, inputPath);
        input.setId(basicInputViewController.getSelectedInput().getId());
        instance.setInput(input);

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();

        //        CorpusData output = new CorpusData(NamingPolicy.generateOutputName(inputPath.get(), outputPath.get(), instance), outputPath);
//        instance.setTextOutput(output);
        //Add output file list 
        final TabularData oldTabularData = new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance),
                NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance));
                
        /*
        Niko
        create parent directory for the output output
        */
        final String subdirectory = outputPath.get()+"/Entity-Detection/";
        FileHandler.createDirectory(subdirectory);
        //System.out.println("Created sub dir: "+subdirectory);
        FileList output=new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance),subdirectory);
        instance.setTextOutput(output);
        outputPath.set(subdirectory);
        /*
        End Addition
        */
        
        TabularData newTabularData = new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance),
                NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance));
        
        instance.setTabularOutput(newTabularData, 0);
       
        
        //Add output file list 
        final String subdirectory2 = outputPath.get() + "/ED-Annotated-Results/";
        FileHandler.createDirectory(subdirectory2);
        System.out.println("Created sub dir: " + subdirectory2);
        output = new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance), subdirectory2);
        instance.setTextOutput(output);

        //TODO 
        instance.setModel(confController.getModel());
        instance.setHtmlOutput(confController.isHTMLOutput());
        instance.setSlashTagsOutput(confController.isSlashTagsOutput());
        instance.setInnerXMLOutput(confController.isInnerXMLOutput());

        CTask task = new EntityDetectionTask(this.getProgress(), this.getProgressMessage());
        // Set waiting time for entity task to load thr library
        try {            
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new EntityDetectionNextStepsController(NamingPolicy.generateTaskName(EntityDetectionTaskInstance.class).get());
                nextStepsViewController.setParent(EntityDetectionController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                nextStepsViewController.setFilelist((FileList) instance.getTextOutput());
                if (instance.getModel() == 0) {
                    nextStepsViewController.setTextOutput(false);
                } else {
                    nextStepsViewController.setTextOutput(true);
                }
                nextStepsViewController.setTabular(getTaskInstance().getTabularOutput(0));
                //nextStepsViewController.setFilelist((FileList)getTaskInstance().getTextOutput());
                nextStepsViewController.init();
                ProjectManager.getThisProject().addResult(getTaskInstance().getTabularOutput(0));
                //ProjectManager.getThisProject().addData(getTaskInstance().getTextOutput());
                if (isNew()) {
                    ProjectManager.getThisProject().addTask(getTaskInstance());
                    setNew(false);
                }
                
                instance.setTabularOutput(oldTabularData, 0);
                
                showNextStepPane(nextStepsViewController);
            }
        });
        getProgressBar().setVisible(true);
        deactiveAllControls();
        task.start();
     
        setTaskInstance(task.getTaskInstance());        

    }

}
