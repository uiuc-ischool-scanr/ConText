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
package context.ui.control.entitydetection;

import context.ui.control.configuration.ConfigurationController;
import context.ui.misc.PropertiesUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class EntityDetectionConfigurationController extends ConfigurationController implements Initializable {

    /**
     *
     */
    public static String path = PropertiesUtil.getFXMLPath(EntityDetectionConfigurationController.class);

    @FXML
    private ComboBox<String> modelComboBox;
    @FXML
    private Font x1;
    @FXML
    private RadioButton slashTagsRadioButton;
    @FXML
    private RadioButton innerXMLRadioButton;
    @FXML
    private RadioButton htmlRadioButton;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> modelOptions
                = FXCollections.observableArrayList(
                        "Stanford NER" //0
//                        ,"iSchool Entity Extractor" //1
//                        ,"ConText Model 2", //2
//                        "ConText Model 3", //3
//                        "ConText Model 4" //4
                );
        modelComboBox.getItems().addAll(modelOptions);
        modelComboBox.getSelectionModel().select(0);
        innerXMLRadioButton.setSelected(true);
    }

    /**
     *
     * @return
     */
    public int getModel() {
        return modelComboBox.getSelectionModel().getSelectedIndex();
    }

    /**
     *
     * @return
     */
    public boolean isInnerXMLOutput() {
        return innerXMLRadioButton.isSelected();
    }

    /**
     *
     * @return
     */
    public boolean isHTMLOutput() {
        return htmlRadioButton.isSelected();
    }

    /**
     *
     * @return
     */
    public boolean isSlashTagsOutput() {
        return slashTagsRadioButton.isSelected();
    }

}
