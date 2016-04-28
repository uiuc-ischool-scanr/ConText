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
package context.ui.control.removestopword;

import context.app.AppConfig;
import context.core.entity.FileData;
import context.ui.control.configuration.ConfigurationController;
import context.ui.misc.FileHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class RemoveStopwordsConfigurationController extends ConfigurationController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static String path = "/context/ui/control/removestopword/RemoveStopwordsConfigurationView.fxml";
    @FXML
    private Font x1;
    @FXML
    private TextField stopwordFileTextField;
    @FXML
    private Button browseButton;
    @FXML
    private RadioButton dropRadioButton;
    @FXML
    private ToggleGroup MethodRadioBoxGroup;
    @FXML
    private RadioButton DropAndInsertRadioButton;
    @FXML
    private Font x2;
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
        stopwordFileTextField.setText(AppConfig.defaultStopwordFileLocation);
        DropAndInsertRadioButton.selectedProperty().set(true);
    }
    
    /**
     *
     * @return
     */
    public TextField getStopwordFileTextField() {
        return stopwordFileTextField;
    }
    
    /**
     *
     * @return
     */
    public RadioButton getDropAndInsertRadioButton() {
        return DropAndInsertRadioButton;
    }
    
    /**
     *
     * @return
     */
    public RadioButton getDropRadioButton() {
        return dropRadioButton;
    }
    
    @FXML
    private void handleBrowseButton(ActionEvent event) {
        FileData file = FileHandler.openFileChooser("Open Stopword File...");
        if (file != null && file.getPath() != null) {
            stopwordFileTextField.textProperty().set(file.getPath().get());
        }
    }
    
}
