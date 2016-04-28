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
package context.ui.control.sentiment;

import context.app.AppConfig;
import context.core.entity.FileData;
import context.ui.control.configuration.ConfigurationController;
import context.ui.misc.FileHandler;
import context.ui.misc.PropertiesUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class SentimentConfigurationController extends ConfigurationController implements Initializable {

    /**
     *
     */
    public static String path = PropertiesUtil.getFXMLPath(SentimentConfigurationController.class);

    @FXML
    private Font x1;
    @FXML
    private Button browseButton;
    @FXML
    private Font x2;
    @FXML
    private TextField sentimentFileTextField;
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
        sentimentFileTextField.setText(AppConfig.defaultSentimentFileLocation);
    }

    /**
     *
     * @return
     */
    public StringProperty getSentimentFile() {
        return sentimentFileTextField.textProperty();
    }

    @FXML
    private void handleBrowseButton(ActionEvent event) {
        FileData file = FileHandler.openFileChooser("Open Sentiment File...");
        if (file != null && file.getPath() != null) {
            sentimentFileTextField.textProperty().set(file.getPath().get());
        }
    }

}
