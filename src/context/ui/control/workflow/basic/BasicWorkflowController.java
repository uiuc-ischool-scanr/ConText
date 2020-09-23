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
package context.ui.control.workflow.basic;

import context.app.AppConfig;
import context.app.Validation;
import context.core.entity.TaskInstance;
import context.ui.control.configuration.ConfigurationController;
import context.ui.control.input.BasicInputViewController;
import context.ui.control.output.BasicOutputViewController;
import context.ui.control.workflow.WorkflowController;
import context.ui.misc.PropertiesUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import org.openide.util.Exceptions;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class BasicWorkflowController extends WorkflowController implements Initializable {
    
    /**
     *
     */
    public static String path = PropertiesUtil.getFXMLPath(BasicWorkflowController.class);

    /**
     *
     */
    @FXML
    protected TabPane tabPane;

    /**
     *
     */
    @FXML
    protected Tab tab1;

    /**
     *
     */
    @FXML
    protected Tab tab2;

    /**
     *
     */
    @FXML
    protected Tab tab3;

    /**
     *
     */
    @FXML
    protected Insets x1;

    /**
     *
     */
    @FXML
    protected Button step1NextButton;

    /**
     *
     */
    @FXML
    protected Font x2;

    /**
     *
     */
    @FXML
    protected Font x3;

    /**
     *
     */
    @FXML
    protected Button step2BackButton;

    /**
     *
     */
    @FXML
    protected Button step2NextButton;

    /**
     *
     */
    @FXML
    protected Button step3BackButton;

    /**
     *
     */
    @FXML
    protected Button step3RunButton;
    
    /**
     *
     */
    @FXML
    protected AnchorPane pane1;

    /**
     *
     */
    @FXML
    protected AnchorPane pane2;

    /**
     *
     */
    @FXML
    protected AnchorPane pane3;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Insets x4;
    @FXML
    private Insets x5;
    @FXML
    private Insets x6;
    @FXML
    private Label progressLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private AnchorPane rootPane;
    
    /**
     *
     */
    protected ConfigurationController configurationController;
    
    /**
     *
     */
    public BasicWorkflowController() {
        setNew(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(BasicWorkflowController.path));
        loader.setRoot(this);
        loader.setController(this);
        try {
            pane = (AnchorPane) loader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        try {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource(BasicInputViewController.path));
            Parent s1Content = (Parent) loader1.load();
            basicInputViewController = (BasicInputViewController) loader1.getController();
            
            FXMLLoader loader3 = new FXMLLoader(getClass().getResource(BasicOutputViewController.path));
            Parent s3Content = (Parent) loader3.load();
            basicOutputViewController = (BasicOutputViewController) loader3.getController();
            
            setStep1Content(s1Content);
            setStep3Content(s3Content);
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    /**
     *
     */
    public void postInitialize() {
        basicInputViewController.setTitle(AppConfig.getTaskLabel(getTaskname().get()) + " : Input");
        configurationController.setTitle(AppConfig.getTaskLabel(getTaskname().get()) + " : Configuration");
        basicOutputViewController.setTitle(AppConfig.getTaskLabel(getTaskname().get()) + " : Output");
    }
    
    /**
     *
     * @param step1Content
     */
    public void setStep1Content(Parent step1Content) {
        this.step1Content = step1Content;
        pane1.getChildren().add(step1Content);
        boundToPane(step1Content);
    }
    
    /**
     *
     * @param step2Content
     */
    public void setStep2Content(Parent step2Content) {
        this.step2Content = step2Content;
        pane2.getChildren().add(step2Content);
        boundToPane(step2Content);
    }
    
    /**
     *
     * @param step3Content
     */
    public void setStep3Content(Parent step3Content) {
        this.step3Content = step3Content;
        pane3.getChildren().add(step3Content);
        boundToPane(step3Content);
    }
    
    /**
     *
     * @param nextStep
     */
    @Override
    public void showNextStepPane(Parent nextStep) {
        
        root = (AnchorPane) tabPane.getParent();
        if (root.getChildren() != null && root.getChildren().size() > 0) {
            mainPainContent = root.getChildren().get(0);
        }
        root.getChildren().clear();
        root.getChildren().add(nextStep);
    }
    
    /**
     *
     */
    @Override
    public void hideNextStepPane() {
        root.getChildren().clear();
        root.getChildren().add(mainPainContent);
        getProgressBar().setVisible(false);
        tabPane.getSelectionModel().select(tab2);
    }
    
    /**
     *
     * @param event
     */
    @FXML
    public void handleStep1NextButton(ActionEvent event) {
        if (!Validation.selectAnyItemInListView(basicInputViewController.getInputListView())) {
            return;
        }
        tabPane.getSelectionModel().select(tab2);
    }
    
    /**
     *
     * @param event
     */
    @FXML
    public void handleStep2BackButton(ActionEvent event) {
        tabPane.getSelectionModel().select(tab1);
    }
    
    /**
     *
     * @param event
     */
    @FXML
    public void handleStep2NextButton(ActionEvent event) {
        tabPane.getSelectionModel().select(tab3);
    }
    
    /**
     *
     * @param event
     */
    @FXML
    public void handleStep3BackButton(ActionEvent event) {
        tabPane.getSelectionModel().select(tab2);
    }
    
    /**
     *
     * @param event
     */
    @FXML
    public void handleStep3RunButton(ActionEvent event) {
        BasicOutputViewController conController = (BasicOutputViewController) basicOutputViewController;
        TaskInstance instance = (TaskInstance) getTaskInstance();
        /*instance.setDropnum(basicInputViewController.isDropnum());
        instance.setDroppun(basicInputViewController.isDroppun());
        instance.setKeeppou(basicInputViewController.isKeeppou());*/
        System.out.println("In BasicWorkflow");
    }
    
    /**
     *
     * @return
     */
    public ProgressBar getProgressBar() {
        return progressBar;
    }
    
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        progress = new SimpleDoubleProperty(0);
        progressMessage = new SimpleStringProperty();
//        progress.addListener(new ChangeListener<Object>() {
//            @Override
//            public void changed(ObservableValue<? extends Object> ov, Object t, Object t1) {
//                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + t.getClass() + " " + t1.getClass() + " " + t + " " + t1);
//            }
//        }
//        );
        progressBar = (ProgressBar) tab3.getContent().lookup("#progressBarId");
        progressLabel = (Label) tab3.getContent().lookup("#progressLabelId");
        progressBar.setProgress(0.6);
        if (!progressBar.progressProperty().isBound()) {
            progressBar.progressProperty().bind(progress);
        }
        if (!progressLabel.textProperty().isBound()) {
            progressLabel.textProperty().bind(progressMessage);
        }
    }
    
    /**
     *
     */
    @Override
    public void deactiveAllControls() {
        super.deactiveAllControls();
        step3RunButton.setText("Stop");
        step3BackButton.setDisable(true);
        step2BackButton.setDisable(true);
        step2NextButton.setDisable(true);
        step1NextButton.setDisable(true);
    }
    
    /**
     *
     */
    @Override
    public void activeAllControls() {
        super.activeAllControls();
        step3RunButton.setText("Run");
        step3BackButton.setDisable(false);
        step2BackButton.setDisable(false);
        step2NextButton.setDisable(false);
        step1NextButton.setDisable(false);
    }
    
}
