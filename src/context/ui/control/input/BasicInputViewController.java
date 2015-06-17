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
package context.ui.control.input;

import context.app.ProjectManager;
import context.core.entity.DataElement;
import context.core.entity.FileList;
import context.ui.misc.FileHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class BasicInputViewController extends AnchorPane implements Initializable {

    /**
     *
     */
    public static String path = "/context/ui/control/input/BasicInputView.fxml";

    @FXML
    private ListView<DataElement> inputListView;
    @FXML
    private Button importButton;
    @FXML
    private Label selectedItemLabel;

    @FXML
    private Label titleLabel;

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /**
     *
     * @return
     */
    public ListView<DataElement> getInputListView() {
        return inputListView;
    }

    /**
     *
     * @return
     */
    public DataElement getSelectedInput() {
        return getInputListView().getSelectionModel().getSelectedItem();
    }

    /**
     *
     * @return
     */
    public Label getSelectedItemLabel() {
        return selectedItemLabel;
    }

    /**
     * Initializes the controller class.
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        if (this.getParent() instanceof WorkflowController) {
//            System.out.println("set title");
//            WorkflowController parent = (WorkflowController) this.getParent();
//            titleLabel.setText(AppConfig.getTaskLabel(parent.getTaskname().get()));
//        } else {
//            System.out.println("its not instance of workflow");
//        }
        this.inputListView.setItems(ProjectManager.getThisProject().getData());

        inputListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<DataElement>() {
                    public void changed(ObservableValue<? extends DataElement> ov,
                            DataElement old_val, DataElement new_val) {
                        selectedItemLabel.setText(new_val.getPath().get());
                    }
                });
    }

    @FXML
    private void handleImportButton(ActionEvent event) {
        FileList selectedDir = FileHandler.openDirectoryChooser("Select Corpus Directory...");
        if (selectedDir != null) {
            ProjectManager.getThisProject().addData(selectedDir);
            inputListView.getSelectionModel().selectLast();
        }
    }

    /**
     *
     * @return
     */
    public StringProperty getSelectedCorpusName() {
        return this.getInputListView().getSelectionModel().selectedItemProperty().getValue().getName();
    }

}
