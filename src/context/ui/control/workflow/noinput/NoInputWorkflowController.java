/*
 
* Copyright (c) 2020 University of Illinois Board of Trustees, All rights reserved.   
* Developed at the iSchool, by Dr. Jana Diesner, Chieh-Li Chin, 
* Amirhossein Aleyasen, Shubhanshu Mishra, Kiumars Soltani, Liang Tao, 
* Ming Jiang, Harathi Korrapati, Nikolaus Nova Parulian, and Lan Jiang..     
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
package context.ui.control.workflow.noinput;

import context.app.AppConfig;
import context.ui.control.configuration.ConfigurationController;
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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.openide.util.Exceptions;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class NoInputWorkflowController extends WorkflowController implements Initializable {

    /**
     *
     */
    public static String path = PropertiesUtil.getFXMLPath(NoInputWorkflowController.class);

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
    protected Tab tab3;

    /**
     *
     */
    @FXML
    protected Button step1NextButton;

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
    protected AnchorPane pane3;
    @FXML
    private AnchorPane mainPane;
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
    public NoInputWorkflowController() {
        setNew(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(NoInputWorkflowController.path));
        loader.setRoot(this);
        loader.setController(this);
        try {
            pane = (AnchorPane) loader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        try {

            FXMLLoader loader3 = new FXMLLoader(getClass().getResource(BasicOutputViewController.path));
            Parent s3Content = (Parent) loader3.load();
            basicOutputViewController = (BasicOutputViewController) loader3.getController();

            setStep3Content(s3Content);

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     *
     */
    public void postInitialize() {
        configurationController.setTitle(AppConfig.getTaskLabel(getTaskname().get()) + " : Configuration");
        basicOutputViewController.setTitle(AppConfig.getTaskLabel(getTaskname().get()) + " : Output");
    }

    /**
     *
     * @param event
     */
    @FXML
    public void handleStep1NextButton(ActionEvent event) {
//        if (!Validation.selectAnyItemInListView(basicInputViewController.getInputListView())) {
//            return;
//        }
        tabPane.getSelectionModel().select(tab3);
    }

    /**
     *
     * @param event
     */
    @FXML
    public void handleStep3BackButton(ActionEvent event) {
        tabPane.getSelectionModel().select(tab1);
    }

    /**
     *
     * @param event
     */
    @FXML
    public void handleStep3RunButton(ActionEvent event) {
        System.out.println("In BasicWorkflow");
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
     * @param step3Content
     */
    public void setStep3Content(Parent step3Content) {
        this.step3Content = step3Content;
        pane3.getChildren().add(step3Content);
        boundToPane(step3Content);
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
     * @return
     */
    public Label getProgressLabel() {
        return progressLabel;
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
     * @param nextStep
     */
    @Override
    public void showNextStepPane(Parent nextStep) {

        root = (AnchorPane) tabPane.getParent();
        if (root == null) {
            return;
        }
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
        tabPane.getSelectionModel().select(tab1);
    }

    /**
     *
     */
    @Override
    public void deactiveAllControls() {
        super.deactiveAllControls();
        step3RunButton.setText("Stop");
        step3BackButton.setDisable(true);
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
        step1NextButton.setDisable(false);
    }

}
