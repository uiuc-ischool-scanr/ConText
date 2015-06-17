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
package context.ui.control.lexisnexis;

import context.ui.control.configuration.ConfigurationController;
import context.ui.misc.PropertiesUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class LexisNexisNetworkGenerationConfigurationController extends ConfigurationController implements Initializable {

    /**
     *
     */
    public static String path = PropertiesUtil.getFXMLPath(LexisNexisNetworkGenerationConfigurationController.class);
    @FXML
    private Font x2;
    @FXML
    private Font x3;
    @FXML
    private CheckBox LLCheckBox;
    @FXML
    private CheckBox OLCheckBox;
    @FXML
    private CheckBox PLCheckBox;
    @FXML
    private CheckBox SLCheckBox;
    @FXML
    private CheckBox SOCheckBox;
    @FXML
    private CheckBox POCheckBox;
    @FXML
    private CheckBox OOCheckBox;
    @FXML
    private CheckBox PPCheckBox;
    @FXML
    private CheckBox SSCheckBox;
    @FXML
    private CheckBox SPCheckBox;
    @FXML
    private TextField thresholdTextField;
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
     * @return
     */
    public TextField getThresholdTextField() {
        return thresholdTextField;
    }

    /**
     *
     * @return
     */
    public CheckBox getLLCheckBox() {
        return LLCheckBox;
    }

    /**
     *
     * @return
     */
    public CheckBox getOLCheckBox() {
        return OLCheckBox;
    }

    /**
     *
     * @return
     */
    public CheckBox getPLCheckBox() {
        return PLCheckBox;
    }

    /**
     *
     * @return
     */
    public CheckBox getSLCheckBox() {
        return SLCheckBox;
    }

    /**
     *
     * @return
     */
    public CheckBox getSOCheckBox() {
        return SOCheckBox;
    }

    /**
     *
     * @return
     */
    public CheckBox getPOCheckBox() {
        return POCheckBox;
    }

    /**
     *
     * @return
     */
    public CheckBox getOOCheckBox() {
        return OOCheckBox;
    }

    /**
     *
     * @return
     */
    public CheckBox getPPCheckBox() {
        return PPCheckBox;
    }

    /**
     *
     * @return
     */
    public CheckBox getSSCheckBox() {
        return SSCheckBox;
    }

    /**
     *
     * @return
     */
    public CheckBox getSPCheckBox() {
        return SPCheckBox;
    }

    /**
     * Initializes the controller class.
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        thresholdTextField.textProperty().set("75");
        LLCheckBox.selectedProperty().set(true);
        PPCheckBox.selectedProperty().set(true);
        SSCheckBox.selectedProperty().set(true);
        OOCheckBox.selectedProperty().set(true);
    }

}
