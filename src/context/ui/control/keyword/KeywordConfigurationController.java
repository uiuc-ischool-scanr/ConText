/*
 
 * Copyright (c) 2020 University of Illinois Board of Trustees, All rights reserved.   
* Developed at the iSchool, by Dr. Jana Diesner, Chieh-Li Chin, 
* Amirhossein Aleyasen, Shubhanshu Mishra, Kiumars Soltani, Liang Tao, 
* Ming Jiang, Harathi Korrapati, Nikolaus Nova Parulian, and Lan Jiang.  
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
package context.ui.control.keyword;

import context.core.entity.FileData;
import context.ui.control.configuration.ConfigurationController;
import context.ui.misc.FileHandler;
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
public class KeywordConfigurationController extends ConfigurationController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public static String path = "/context/ui/control/keyword/KeywordConfigurationView.fxml";
    @FXML
    private Font x1;
    @FXML
    private Button browseButton;
    @FXML
    private Font x2;
    @FXML
    private Label titleLabel;
    @FXML
    private TextField leftBoundTextField;
    @FXML
    private TextField rightBoundTextField;
    @FXML
    private TextField keywordFileTextField;
    @FXML
    private CheckBox omit_case_checkbox;
    @FXML
    private Font x3;

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
        //keywordFileTextField.setText(AppConfig.defaultStopwordFileLocation);
        leftBoundTextField.setText(5 + "");
        rightBoundTextField.setText(5 + "");
    }

    /**
     *
     * @return
     */
    public TextField getKeywordFileTextField() {
        return keywordFileTextField;
    }

    /**
     *
     * @return
     */
    public Integer getLeftBound() {
        return Integer.parseInt(leftBoundTextField.getText());
    }

    /**
     *
     * @return
     */
    public Integer getRightBound() {
        return Integer.parseInt(rightBoundTextField.getText());
    }

    @FXML
    private void handleBrowseButton(ActionEvent event) {
        FileData file = FileHandler.openFileChooser("Open Keyword File...");
        if (file != null && file.getPath() != null) {
            keywordFileTextField.textProperty().set(file.getPath().get());
        }
    }
    
    public Boolean isOmitCase(){
        if(omit_case_checkbox.isSelected()){
            return true;
        }
        return false;
    }

}
