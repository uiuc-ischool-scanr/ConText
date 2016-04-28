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
package context.ui.control.codebooknetwork;

import context.app.ProjectManager;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.FileList;
import context.core.entity.TabularData;
import context.core.task.codebook.CodebookApplicationTask;
import context.core.task.codebook.CodebookApplicationTaskInstance;
import context.core.task.codebook.CodebookNetworkTaskInstance;
import context.ui.control.workflow.basic.BasicWorkflowController;
import context.ui.misc.FileHandler;
import context.ui.misc.NamingPolicy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.commons.io.FilenameUtils;
import org.openide.util.Exceptions;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class CodebookNetworkController extends BasicWorkflowController {

    /**
     *
     */
    public CodebookNetworkController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(CodebookNetworkTaskInstance.class));
            setTaskInstance(new CodebookApplicationTaskInstance(getTaskname()));

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(CodebookNetworkConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (CodebookNetworkConfigurationController) loader2.getController();

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
    public CodebookNetworkController(CodebookApplicationTaskInstance taskInstance) {
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
        CodebookApplicationTaskInstance instance = (CodebookApplicationTaskInstance) getTaskInstance();
        CodebookNetworkConfigurationController confController = (CodebookNetworkConfigurationController) configurationController;
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        CorpusData input = new CorpusData(inputname, inputPath);
        instance.setInput(input);

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();
        
        final String subdirectory = outputPath.get() + "/CB-Results/";
        FileHandler.createDirectory(subdirectory);
        System.out.println("Created sub dir: "+subdirectory);
        
        
        FileList output = new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance), subdirectory);
        instance.setTextOutput(output);
        
        StringProperty tabularName = NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance);
        StringProperty csvTabularPath = NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance);
        StringProperty gexfTabularPath = NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance, ".gexf");
        
        
        csvTabularPath.set(FilenameUtils.getFullPath(csvTabularPath.get()) + "CorpusNetwork.csv");
        gexfTabularPath.set(FilenameUtils.getFullPath(gexfTabularPath.get()) + "CorpusNetwork.gexf");
        System.out.println("CSV Path: "+csvTabularPath.get()+"\nGexF Path: "+gexfTabularPath.get());

        List<TabularData> td = new ArrayList<TabularData>();
        td.add(0, new TabularData(tabularName, csvTabularPath));
        td.add(1, new TabularData(new SimpleStringProperty(tabularName.get() + "-gexf"), gexfTabularPath));

        instance.setTabularOutput(td);

        instance.setCodebookFile(confController.getCodebookFile());
        instance.setDistance(confController.getDistance());
        instance.setIsDrop(confController.getCodebookMode());
        instance.setIsNormal(confController.getCodebookMethod());
        if (confController.getAggregationType() == 0) { // per document
            instance.setNetInputCorpus(false);
        } else { //per corpus
            instance.setNetInputCorpus(true);
        }
        instance.setNetOutputCSV(true);
        instance.setNetOutputGEXF(true);
        instance.setNetOutputType(confController.getNetworkType());
        instance.setNetwork(true);
        instance.setSeparator(confController.getUnitOfAnalysis());
        instance.setCustomTag(confController.getCustomTag());

        CTask task = new CodebookApplicationTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new CodebookNetworkNextStepsController(NamingPolicy.generateTaskName(CodebookApplicationTaskInstance.class).get());
                nextStepsViewController.setParent(CodebookNetworkController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                nextStepsViewController.setTabular(getTaskInstance().getTabularOutput(0));
                nextStepsViewController.setFilelist((FileList) getTaskInstance().getTextOutput());
                nextStepsViewController.init();
                ProjectManager.getThisProject().addResult(getTaskInstance().getTabularOutput(0));
                ProjectManager.getThisProject().addData(getTaskInstance().getTextOutput());
//                ProjectManager.getThisProject().addResult(getTaskInstance().getTabularOutput(1));
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
