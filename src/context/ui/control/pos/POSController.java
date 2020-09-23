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
package context.ui.control.pos;

import context.app.ProjectManager;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.FileList;
import context.core.entity.TabularData;
import context.core.task.pos.POSTask;
import context.core.task.pos.POSTaskInstance;
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
public class POSController extends BasicWorkflowController {

    /**
     *
     */
    public POSController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(POSTaskInstance.class));
            setTaskInstance(new POSTaskInstance(getTaskname()));

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(POSConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (POSConfigurationController) loader2.getController();

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
    public POSController(POSTaskInstance taskInstance) {
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
         POSConfigurationController confController = (POSConfigurationController) configurationController;
        
        POSTaskInstance instance = (POSTaskInstance) getTaskInstance();
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

        /*
        Niko
        add handler for creating subdirectory output
        */
        final String subdirectory = outputPath.get()+"/POS-Results/";
        FileHandler.createDirectory(subdirectory);
        System.out.println("Created sub dir: "+subdirectory);
        FileList output=new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance),subdirectory);
        instance.setTextOutput(output);
        outputPath.set(subdirectory);
        /*
        End Addition
        */


        TabularData tabularData = new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance),
                NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance));
        instance.setTabularOutput(tabularData, 0);
        
        instance.setLanguage(confController.getLanguage());
                
        CTask task = new POSTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new POSNextStepsController(NamingPolicy.generateTaskName(POSTaskInstance.class).get());
                nextStepsViewController.setParent(POSController.this);
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
