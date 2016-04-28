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
package context.ui.control.workflow;

import context.app.Validation;
import context.core.entity.DataElement;
import context.core.entity.TabularData;
import context.core.entity.TaskInstance;
import context.ui.control.input.BasicInputViewController;
import context.ui.control.nextsteps.NextStepsViewController;
import context.ui.control.output.BasicOutputViewController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class WorkflowController extends AnchorPane {

    /**
     *
     */
    protected Parent step1Content;

    /**
     *
     */
    protected Parent step2Content;

    /**
     *
     */
    protected Parent step3Content;

    private boolean newController;

    /**
     *
     */
    protected DoubleProperty progress;

    /**
     *
     */
    protected StringProperty progressMessage;

    /**
     *
     */
    protected Pane pane;
    private TaskInstance taskInstance;

    /**
     *
     */
    protected BasicInputViewController basicInputViewController;

    /**
     *
     */
    protected BasicOutputViewController basicOutputViewController;
    private StringProperty taskname = new SimpleStringProperty();

    /**
     *
     */
    protected NextStepsViewController nextStepsViewController;

    /**
     *
     */
    protected Node mainPainContent;

    /**
     *
     */
    protected AnchorPane root;

    /**
     *
     * @return
     */
    public boolean isNew() {
        return newController;
    }

    /**
     *
     * @param newController
     */
    public void setNew(boolean newController) {
        this.newController = newController;
    }

    /**
     *
     * @param taskInstance
     */
    protected void initializeTaskToGUIControls(TaskInstance taskInstance) {
        setNew(false);
        setTaskInstance(taskInstance);
        setInputSelectedCorpus(taskInstance.getInput());
        setTabularOutputDirectory(taskInstance.getTabularOutput(0));
        setTextOutputDirectory(taskInstance.getTextOutput());
    }

    /**
     *
     * @return
     */
    public boolean validateInputOutput() {
        if (basicInputViewController == null || basicInputViewController.getInputListView() == null) {
            return true;
        }
        if (!Validation.selectAnyItemInListView(basicInputViewController.getInputListView())) {
            return false;
        }
        if (!Validation.nonEmptyOutputTextfield(basicOutputViewController.getOutputDirTextField())) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param input
     */
    public void setInputSelectedCorpus(DataElement input) {
        System.out.println("input id=" + input.getId());

        for (DataElement d : basicInputViewController.getInputListView().getItems()) {
            System.out.println(">" + d.getId());
        }
        int index = basicInputViewController.getInputListView().getItems().indexOf(input);
        System.out.println("index of listview=" + index);
        basicInputViewController.getInputListView().getSelectionModel().select(input);
        basicInputViewController.getInputListView().getFocusModel().focus(index);
    }

    /**
     *
     * @param textOutput
     */
    public void setTextOutputDirectory(DataElement textOutput) {
        if (textOutput == null || textOutput.getPath() == null) {
            return;
        }
        String dirPath = FilenameUtils.getFullPathNoEndSeparator(textOutput.getPath().get());
        basicOutputViewController.getOutputDirTextField().textProperty().set(dirPath);
    }

    /**
     *
     * @param tabularOutput
     */
    public void setTabularOutputDirectory(TabularData tabularOutput) {
        if (tabularOutput == null || tabularOutput.getPath() == null) {
            return;
        }
        String dirPath = FilenameUtils.getFullPathNoEndSeparator(tabularOutput.getPath().get());
        basicOutputViewController.getOutputDirTextField().textProperty().set(dirPath);
    }

    /**
     *
     * @param taskname
     */
    public void setTaskname(StringProperty taskname) {
        this.taskname = taskname;
    }

    /**
     *
     * @return
     */
    public StringProperty getTaskname() {
        return taskname;
    }

    /**
     *
     * @return
     */
    public TaskInstance getTaskInstance() {
        return taskInstance;
    }

    /**
     *
     * @param taskInstance
     */
    public void setTaskInstance(TaskInstance taskInstance) {
        this.taskInstance = taskInstance;
    }

    /**
     *
     * @return
     */
    public Parent getStep1Content() {
        return step1Content;
    }

    /**
     *
     * @return
     */
    public StringProperty getProgressMessage() {
        return progressMessage;
    }

    /**
     *
     * @param progressMessage
     */
    public void setProgressMessage(StringProperty progressMessage) {
        this.progressMessage = progressMessage;
    }

    /**
     *
     * @return
     */
    public DoubleProperty getProgress() {
        return progress;
    }

    /**
     *
     * @param progress
     */
    public void setProgress(DoubleProperty progress) {
        this.progress = progress;
    }

    /**
     *
     * @param content
     */
    protected void boundToPane(Parent content) {
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

    /**
     *
     * @return
     */
    public Parent getStep2Content() {
        return step2Content;
    }

    /**
     *
     * @return
     */
    public Parent getStep3Content() {
        return step3Content;
    }

    /**
     *
     * @param nextStep
     */
    public void showNextStepPane(Parent nextStep) {
    }

    /**
     *
     */
    public void hideNextStepPane() {
    }

    /**
     *
     */
    public void deactiveAllControls() {
        step1Content.setDisable(true);
        if (step2Content != null) {
            step2Content.setDisable(true);
        }
        step3Content.setDisable(true);
    }

    /**
     *
     */
    public void activeAllControls() {
        step1Content.setDisable(false);
        if (step2Content != null) {
            step2Content.setDisable(false);
        }
        step3Content.setDisable(false);
    }
}
