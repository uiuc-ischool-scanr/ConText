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
package context.ui.control.topicmodeling;

import context.app.AppConfig;
import context.core.entity.FileData;
import context.ui.control.configuration.ConfigurationController;
import context.ui.misc.FileHandler;
import context.ui.misc.PropertiesUtil;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class TopicModelingConfigurationController extends ConfigurationController implements Initializable {

    /**
     *
     */
    public static String path = PropertiesUtil.getFXMLPath(TopicModelingConfigurationController.class);
    @FXML
    private Font x1;
    @FXML
    private TextField stopwordFileTextField;
    @FXML
    private Button browseButton;
    @FXML
    private Font x2;
    @FXML
    private TextField numberOfTopicsTextField;
    @FXML
    private TextField wordsPerTopicsTextField;
    @FXML
    private TextField numberOfIterationsTextField;
    @FXML
    private TextField numberOfOptIntervalTextField;
    @FXML
    private TextField sumOfAlphaTextField;
    @FXML
    private Label titleLabel;
    @FXML
    private CheckBox toLowercaseCheckBox;

    /**
     *
     * @param title
     */
    @Override
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stopwordFileTextField.setText(AppConfig.defaultStopwordFileLocation);
        numberOfTopicsTextField.setText(AppConfig.getProperty("task.topicmodeling.numberoftopics"));
        wordsPerTopicsTextField.setText(AppConfig.getProperty("task.topicmodeling.wordspertopic"));
        numberOfIterationsTextField.setText(AppConfig.getProperty("task.topicmodeling.numberofiterations"));
        numberOfOptIntervalTextField.setText(AppConfig.getProperty("task.topicmodeling.numoptinterval"));
        sumOfAlphaTextField.setText(AppConfig.getProperty("task.topicmodeling.sumalpha"));
        toLowercaseCheckBox.selectedProperty().set(true);
    }

    /**
     *
     * @return
     */
    public String getStopwordFile() {
        return stopwordFileTextField.textProperty().get();
    }

    /**
     *
     * @return
     */
    public int getNumberOfIterations() {
        return Integer.parseInt(numberOfIterationsTextField.textProperty().get());
    }

    /**
     *
     * @return
     */
    public int getNumberOfTopics() {
        return Integer.parseInt(numberOfTopicsTextField.textProperty().get());
    }

    /**
     *
     * @return
     */
    public int getNumberOfWordsPerTopics() {
        return Integer.parseInt(wordsPerTopicsTextField.textProperty().get());
    }
    
    /**
    *
    * @return
    */
    public int getNumberOfOptInterval(){
    	return Integer.parseInt(numberOfOptIntervalTextField.textProperty().get());
    }
    
    /**
    *
    * @return
    */
    public double getSumAlpha(){
    	return Double.parseDouble(sumOfAlphaTextField.textProperty().get());
    }

    @FXML
    private void handleBrowseButton(ActionEvent event) {
        FileData file = FileHandler.openFileChooser("Open Stopword File...");
        if (file != null && file.getPath() != null) {
            stopwordFileTextField.textProperty().set(file.getPath().get());
        }
    }
    public Boolean getIsLowercase() {
		return toLowercaseCheckBox.isSelected();
	}
}
