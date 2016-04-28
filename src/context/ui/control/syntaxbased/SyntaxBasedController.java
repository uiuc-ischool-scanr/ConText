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
package context.ui.control.syntaxbased;

import context.app.ProjectManager;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.TabularData;
import context.core.task.syntaxbased.SyntaxBasedTask;
import context.core.task.syntaxbased.SyntaxBasedTaskInstance;
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
public class SyntaxBasedController extends BasicWorkflowController {

    /**
     *
     */
    public SyntaxBasedController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(SyntaxBasedTaskInstance.class));
            setTaskInstance(new SyntaxBasedTaskInstance(getTaskname()));

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(SyntaxBasedConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (SyntaxBasedConfigurationController) loader2.getController();

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
    public SyntaxBasedController(SyntaxBasedTaskInstance taskInstance) {
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

        SyntaxBasedConfigurationController confController = (SyntaxBasedConfigurationController) configurationController;
        SyntaxBasedTaskInstance instance = (SyntaxBasedTaskInstance) getTaskInstance();
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        CorpusData input = new CorpusData(inputname, inputPath);
        input.setId(basicInputViewController.getSelectedInput().getId());
//        instance.setInput((DataElement) basicInputViewController.getSelectedInput().clone());
        instance.setInput(input);
        instance.setFilterLabels(confController.getFilteredLabels());

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();
        instance.setOutputDir(outputPath.get());

//        CorpusData output = new CorpusData(NamingPolicy.generateOutputName(inputPath.get(), outputPath.get(), instance), outputPath);
//        instance.setTextOutput(output);
        TabularData tabularData = new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance),
                NamingPolicy.generateTabularPath(inputname.get()+"-SBN", outputPath.get(), instance, ".csv"));
        instance.setTabularOutput(tabularData, 0);
        
        /*final String subdirectory = outputPath.get()+"/SB-Results/";
        FileHandler.createDirectory(subdirectory);
        System.out.println("Created sub dir: "+subdirectory);
        FileList output=new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance),subdirectory);
        instance.setTextOutput(output);*/
        //TODO: set other parameters here

//        instance.setUnitOfAnalysis(confController.getUnitOfAnalysis());

        System.out.println(instance);
        CTask task = new SyntaxBasedTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new SyntaxBasedNextStepsController(NamingPolicy.generateTaskName(SyntaxBasedTaskInstance.class).get());
                nextStepsViewController.setParent(SyntaxBasedController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                nextStepsViewController.setTabular(getTaskInstance().getTabularOutput(0));
                //nextStepsViewController.setFilelist((FileList)getTaskInstance().getTextOutput());
                nextStepsViewController.init();
                ProjectManager.getThisProject().addResult(getTaskInstance().getTabularOutput(0));
                //ProjectManager.getThisProject().addData(getTaskInstance().getTextOutput());
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
