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

import context.app.AppConfig;
import context.core.entity.FileData;
import context.ui.control.configuration.ConfigurationController;
import context.ui.misc.FileHandler;
import context.ui.misc.PropertiesUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class CodebookNetworkConfigurationController extends ConfigurationController implements Initializable {

    /**
     *
     */
    public static String path = PropertiesUtil.getFXMLPath(CodebookNetworkConfigurationController.class);
    @FXML
    private Button browseButton;
    @FXML
    private TextField codebookFileTextField;
//    @FXML
//    private ComboBox<String> codebookMethodComboBox;
//    @FXML
//    private ComboBox<String> codebookModeComboBox;
    @FXML
    private ComboBox<String> networkTypeComboBox;
    @FXML
    private ComboBox<String> aggregationComboBox;
    @FXML
    private ComboBox<String> unitOfAnalysisComboBox;
    @FXML
    private TextField customTagTextField;
    @FXML
    private TextField distanceTextField;
    @FXML
    private Label titleLabel;

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
//        ObservableList<String> codebookMethodOptions
//                = FXCollections.observableArrayList(
//                        "Normalization",
//                        "Entity Class Encoding"
//                );
//        codebookMethodComboBox.getItems().addAll(codebookMethodOptions);
//        codebookMethodComboBox.getSelectionModel().select(0);
//        ObservableList<String> codebookModeOptions
//                = FXCollections.observableArrayList(
//                        "Replace and Insert", //0
//                        "Positive Filter", //1
//                        "Positive Filter with Placeholder" //2
//                );
//        codebookModeComboBox.getItems().addAll(codebookModeOptions);
//        codebookModeComboBox.getSelectionModel().select(2);
        ObservableList<String> networkTypeOptions
                = FXCollections.observableArrayList(
                        "One-mode Network",
                        "Multi-mode Network"
                );
        networkTypeComboBox.getItems().addAll(networkTypeOptions);
        networkTypeComboBox.getSelectionModel().select(0);
        ObservableList<String> aggregationOptions
                = FXCollections.observableArrayList(
                        "Per Document",
                        "Per Corpus"
                );
        aggregationComboBox.getItems().addAll(aggregationOptions);
        aggregationComboBox.getSelectionModel().select(1);
        ObservableList<String> unitOfAnalysisOptions
                = FXCollections.observableArrayList(
                        "Sentence",
                        "Paragraph",
                        "Text",
                        "Custom Tag"
                );
        unitOfAnalysisComboBox.getItems().addAll(unitOfAnalysisOptions);
        unitOfAnalysisComboBox.getSelectionModel().select(2);
        distanceTextField.setText(AppConfig.getProperty("task.lexisnexisparse.distance"));
        customTagTextField.setVisible(false);
    }

    /**
     *
     * @return
     */
    public String getCustomTag() {
        return customTagTextField.textProperty().get();
    }

    /**
     *
     * @return
     */
    public int getUnitOfAnalysis() { // 1-sentence 2- paragraph 3-text 4-custom tag
        return unitOfAnalysisComboBox.getSelectionModel().getSelectedIndex() + 1;
    }

    /**
     *
     * @return
     */
    public int getCodebookMode() { // 0- replace and Insert 1- positive filter 2- with placeholder
//        return codebookModeComboBox.getSelectionModel().getSelectedIndex();
        return 2;
    }

    /**
     *
     * @return
     */
    public int getCodebookMethod() { // 0 - normalization 1- entity class encoding
//        return codebookMethodComboBox.getSelectionModel().getSelectedIndex();
        return 0;
    }

    /**
     *
     * @return
     */
    public int getNetworkType() { // 0 - one-mode 1- multi-mode
        return networkTypeComboBox.getSelectionModel().getSelectedIndex();
    }

    /**
     *
     * @return
     */
    public int getAggregationType() { // 0 - per document 1- per corpus
        return aggregationComboBox.getSelectionModel().getSelectedIndex();
    }

    /**
     *
     * @return
     */
    public String getCodebookFile() {
        return codebookFileTextField.textProperty().get();
    }

    /**
     *
     * @return
     */
    public int getDistance() {
        return Integer.parseInt(distanceTextField.textProperty().get());
    }

    @FXML
    private void handleBrowseButton(ActionEvent event) {
        FileData file = FileHandler.openFileChooser("Open Codebook File...");
        if (file != null && file.getPath() != null) {
            codebookFileTextField.textProperty().set(file.getPath().get());
        }
    }

    @FXML
    private void handleUnitOfAnalysisComboBox(ActionEvent event) {
        if (unitOfAnalysisComboBox.getSelectionModel().getSelectedIndex() == 3) {
            customTagTextField.setVisible(true);
            customTagTextField.requestFocus();
        } else {
            customTagTextField.setVisible(false);
        }
    }

}
