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
package context.ui.control.stemming;

import context.ui.control.configuration.ConfigurationController;
import context.ui.misc.PropertiesUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class StemmingConfigurationController extends ConfigurationController implements Initializable {

    /**
     *
     */
    public static String path = PropertiesUtil.getFXMLPath(StemmingConfigurationController.class);
    @FXML
    private Font x1;
    @FXML
    private ToggleGroup MethodRadioBoxGroup;
    @FXML
    private ToggleGroup x2;
    @FXML
    private Label titleLabel;
    @FXML
    private RadioButton stemCapWordsRadioButton;
    @FXML
    private RadioButton donotStepCapWordRadioButton;
    @FXML
    private RadioButton ruleBasedRadioButton;
    @FXML
    private RadioButton dictionaryBasedRadioButton;

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
      ruleBasedRadioButton.selectedProperty().set(true);
      stemCapWordsRadioButton.selectedProperty().set(true);
    }

}
