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
package context.ui.control.pos;

import context.ui.control.configuration.ConfigurationController;
import context.ui.misc.PropertiesUtil;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class POSConfigurationController extends ConfigurationController implements Initializable {

    private static Map<String, String> language_labels = new LinkedHashMap<String, String>();

    static {
        language_labels.put("English", "en");
        language_labels.put("Arabic", "ar");
        language_labels.put("Chinese", "ch");
        language_labels.put("French", "fr");
        language_labels.put("German", "de");
    }

    /**
     *
     */
    public static String path = PropertiesUtil.getFXMLPath(POSConfigurationController.class);
    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Label languageLabel;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final ObservableList<String> languageList = FXCollections.observableArrayList(
                language_labels.keySet()
        );
        languageComboBox.setItems(languageList);
        languageComboBox.getSelectionModel().select(0);

    }

    /**
     *
     * @return
     */
    public String getLanguage() {
        return language_labels.get(languageComboBox.getSelectionModel().getSelectedItem());
    }

}
