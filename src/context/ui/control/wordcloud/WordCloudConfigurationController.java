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
package context.ui.control.wordcloud;

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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class WordCloudConfigurationController extends ConfigurationController implements Initializable {

    /**
     *
     */
    public static String path = PropertiesUtil.getFXMLPath(WordCloudConfigurationController.class);

    @FXML
    private Font x1;
    @FXML
    private Font x2;
    @FXML
    private Label titleLabel;
    @FXML
    private ToggleGroup cloudTypeToggleGroup;
    @FXML
    private Color x3;
    @FXML
    private TextField stopwordFileTextField;
    @FXML
    private TextField numberOfTopicsTextField;
    @FXML
    private TextField wordsPerTopicTextField;
    @FXML
    private TextField numberOfIterationTextField;
    @FXML
    private RadioButton singleTypeRadioButton;
    @FXML
    private RadioButton clusteredTypeRadioButton;
    @FXML
    private TextField widthTextField;
    @FXML
    private TextField heightTextField;
    @FXML
    private TextField minFontSizeTextField;
    @FXML
    private Button browsestopwordsButton;
    @FXML
    private TextField sentimentListTextField;
    @FXML
    private Button browseSentimentListButton;

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
        sentimentListTextField.setText(AppConfig.defaultSentimentFileLocation);
        numberOfTopicsTextField.setText(AppConfig.getProperty("task.wordcloud.numberoftopics"));
        wordsPerTopicTextField.setText(AppConfig.getProperty("task.wordcloud.wordspertopic"));
        numberOfIterationTextField.setText(AppConfig.getProperty("task.wordcloud.numberofiterations"));
        widthTextField.setText(AppConfig.getProperty("task.wordcloud.width"));
        heightTextField.setText(AppConfig.getProperty("task.wordcloud.height"));
        minFontSizeTextField.setText(AppConfig.getProperty("task.wordcloud.fontsize"));
        clusteredTypeRadioButton.selectedProperty().set(true);
    }

    /**
     *
     * @return
     */
    public boolean isClustering() {
    	Toggle rd_cluster = cloudTypeToggleGroup.getSelectedToggle();
    	int i = cloudTypeToggleGroup.getToggles().indexOf(rd_cluster);
    	int j = cloudTypeToggleGroup.getToggles().indexOf(clusteredTypeRadioButton);
        if (i==j) {
            return true;
        } else {
            return false;
        }
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
    public String getSentimentFile() {
        return sentimentListTextField.textProperty().get();
    }

    /**
     *
     * @return
     */
    public int getNumberOfIterations() {
        return Integer.parseInt(numberOfIterationTextField.textProperty().get());
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
        return Integer.parseInt(wordsPerTopicTextField.textProperty().get());
    }

    /**
     *
     * @return
     */
    public int getCloudWidth() {
        return Integer.parseInt(widthTextField.textProperty().get());
    }

    /**
     *
     * @return
     */
    public int getCloudHeight() {
        return Integer.parseInt(heightTextField.textProperty().get());
    }

    /**
     *
     * @return
     */
    public int getFontSize() {
        return Integer.parseInt(minFontSizeTextField.textProperty().get());
    }

    @FXML
    private void handleBrowsestopwordsButton(ActionEvent event) {
        FileData file = FileHandler.openFileChooser("Open Stopwords File...");
        if (file != null && file.getPath() != null) {
            stopwordFileTextField.textProperty().set(file.getPath().get());
        }
    }

    @FXML
    private void handleBrowseSentimentListButton(ActionEvent event) {
        FileData file = FileHandler.openFileChooser("Open Sentiment File...");
        if (file != null && file.getPath() != null) {
            sentimentListTextField.textProperty().set(file.getPath().get());
        }
    }

}
