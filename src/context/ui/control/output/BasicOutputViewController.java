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
package context.ui.control.output;

import context.core.entity.FileList;
import context.ui.misc.FileHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class BasicOutputViewController extends AnchorPane implements Initializable {

    private static String lastLocation;
    /**
     * Initializes the controller class.
     */
    public static String path = "/context/ui/control/output/BasicOutputView.fxml";
    @FXML
    private TextField outputDirTextField;
    @FXML
    private Button browseButton;
    @FXML
    private Label titleLabel;

    /**
     *
     * @return
     */
    public TextField getOutputDirTextField() {
        return outputDirTextField;
    }

    /**
     *
     * @param title
     */
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
        if (lastLocation != null && lastLocation.length() > 0) {
            outputDirTextField.setText(lastLocation);
        }
    }

    @FXML
    private void handleBrowseButton(ActionEvent event) {
        FileList selectedDir = FileHandler.openDirectoryChooser("Select Corpus Directory...");
        if (selectedDir != null) {
            outputDirTextField.setText(selectedDir.getDir().getAbsolutePath());
            lastLocation = selectedDir.getDir().getAbsolutePath();
        }
    }

}
