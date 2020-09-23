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
package context.ui.control.entitynetwork;

import context.app.ProjectManager;
import context.app.Validation;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.FileList;
import context.core.entity.TabularData;
import context.core.task.entitynetwork.EntityNetworkTask;
import context.core.task.entitynetwork.EntityNetworkTaskInstance;
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
import org.thehecklers.monologfx.MonologFX;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 * @author cchin6 (add tabular and file list result)
 */
public class EntityNetworkController extends BasicWorkflowController {

    /**
     *
     */
    public EntityNetworkController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(EntityNetworkTaskInstance.class));
            setTaskInstance(new EntityNetworkTaskInstance(getTaskname()));

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(EntityNetworkConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (EntityNetworkConfigurationController) loader2.getController();

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
    public EntityNetworkController(EntityNetworkTaskInstance taskInstance) {
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
        EntityNetworkTaskInstance instance = (EntityNetworkTaskInstance) getTaskInstance();
        EntityNetworkConfigurationController confController = (EntityNetworkConfigurationController) configurationController;

        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        /*instance.setDropnum(basicInputViewController.isDropnum());
        instance.setDroppun(basicInputViewController.isDroppun());
        instance.setKeeppou(basicInputViewController.isKeeppou());*/
        CorpusData input = new CorpusData(inputname, inputPath);
        input.setId(basicInputViewController.getSelectedInput().getId());
//        instance.setInput((DataElement) basicInputViewController.getSelectedInput().clone());
        instance.setInput(input);
        try{
            instance.setDistance(confController.getDistance());
        }catch(Exception ex){
            MonologFX mono = Validation.buildWarningButton("Distance is not set, set Distance on Step 2", "Error");
            mono.show();
            Exceptions.printStackTrace(ex);
            return;
        }
        instance.setUnitOfAnalysis(confController.getUnitOfAnalysis());
        instance.setFilterLabels(confController.getFilteredLabels());

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();
        instance.setOutputDir(outputPath.get());

        //TabularData tabularData = new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance),
               // NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance));
        //instance.setTabularOutput(tabularData, 0);
        
        
        final TabularData oldTabularData = new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance),
                NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance));
                
        /*
        Niko
        create parent directory for the output output
        */
        final String subdirectory = outputPath.get()+"/Entity-Network/";
        FileHandler.createDirectory(subdirectory);
        //System.out.println("Created sub dir: "+subdirectory);
        FileList output=new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance),subdirectory);
        instance.setTextOutput(output);
        final String oldDir=instance.getOutputDir();
        instance.setOutputDir(subdirectory);
        outputPath.set(subdirectory);
        /*
        End Addition
        */
        
        TabularData newTabularData = new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance),
                NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance));
        
        instance.setTabularOutput(newTabularData, 0);               
        
        
        final String subdirectory2 = outputPath.get()+"/ED-Results/";
        FileHandler.createDirectory(subdirectory2);
        System.out.println("Created sub dir: "+subdirectory);
        output=new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance),subdirectory2);
        instance.setTextOutput(output);
        
        //TODO: set other parameters here

        //System.out.println(instance);
        CTask task = new EntityNetworkTask(this.getProgress(), this.getProgressMessage());
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
                nextStepsViewController = new EntityNetworkNextStepsController(NamingPolicy.generateTaskName(EntityNetworkTaskInstance.class).get());
                nextStepsViewController.setParent(EntityNetworkController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                nextStepsViewController.setTabular(getTaskInstance().getTabularOutput(0));
                nextStepsViewController.setFilelist((FileList) getTaskInstance().getTextOutput());
                nextStepsViewController.init();
                ProjectManager.getThisProject().addResult(getTaskInstance().getTabularOutput(0));
                ProjectManager.getThisProject().addData(getTaskInstance().getTextOutput());
                if (isNew()) {
                    ProjectManager.getThisProject().addTask(getTaskInstance());
                    setNew(false);
                }
                
                instance.setTabularOutput(oldTabularData, 0);                
                instance.setOutputDir(oldDir);
                basicOutputViewController.getOutputDirTextField().setText(oldDir);
                
                showNextStepPane(nextStepsViewController);
            }
        });
        getProgressBar().setVisible(true);
        deactiveAllControls();
        task.start();

        setTaskInstance(task.getTaskInstance());
    }

}
