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
package context.ui.control.nextsteps;

import context.app.AppConfig;
import context.app.TabManager;
import context.core.entity.FileList;
import context.core.entity.TabularData;
import context.ui.control.tabular.TabularViewController;
import context.ui.control.workflow.WorkflowController;
import context.ui.misc.FileHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class NextStepsViewController extends AnchorPane implements Initializable {

    /**
     *
     */
    public static String path = "/context/ui/control/nextsteps/NextStepsView.fxml";

    /**
     *
     */
    @FXML
    protected Font x1;

    private WorkflowController parent;

    /**
     *
     */
    @FXML
    protected VBox vbox;

    /**
     *
     */
    @FXML
    protected AnchorPane root;

    /**
     *
     */
    @FXML
    protected Label messageLabel;

    private String outputDir;
    private List<TabularData> tabularData;
    private FileList filelist;
    private Integer sortedColumnIndex;

    boolean textOutput;

    /**
     *
     * @param textOutput
     */
    public void setTextOutput(boolean textOutput) {
        this.textOutput = textOutput;
    }

    /**
     *
     * @return
     */
    public boolean isTextOutput() {
        return textOutput;
    }

    /**
     *
     * @param taskname
     */
    public NextStepsViewController(String taskname) {
        tabularData = new ArrayList<>();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(NextStepsViewController.path));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            AnchorPane.setTopAnchor(this, 0.0);
            AnchorPane.setLeftAnchor(this, 0.0);
            AnchorPane.setBottomAnchor(this, 0.0);
            AnchorPane.setRightAnchor(this, 0.0);
            messageLabel.setText(AppConfig.getTaskLabel(taskname) + " Sucessfully Done.");
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     *
     */
    public void init() {
    }

    /**
     *
     * @param text
     * @param event
     */
    public void addNextStepItem(String text, EventHandler<? super MouseEvent> event) {
        Label label = new Label(text);
        label.setOnMouseClicked(event);
        final ImageView imageView = new ImageView("resources/Actions-go-next-view-icon48.png");
        imageView.setFitHeight(22);
        imageView.setFitWidth(22);
        label.setGraphic(imageView);
        label.getStyleClass().add("nextstepsLabel");
        vbox.getChildren().add(label);
    }

    /**
     *
     * @param filelist
     */
    public void setFilelist(FileList filelist) {
        this.filelist = filelist;
    }

    /**
     *
     * @return
     */
    public FileList getFilelist() {
        return filelist;
    }

    /**
     *
     */
    public void addOpenWebViewNextStepItem() {
        addNextStepItem(AppConfig.getLabel("nextstep.webview.itemlist.label"), new EventHandler() {
            @Override
            public void handle(Event t) {
                TabManager.openWebViewer(getOutputDir());
            }
        });
    }

    /**
     *
     */
    public void addOpenTabularViewNextStepItem() {
        addOpenTabularViewNextStepItem("nextstep.tabularview.itemlist.label", 0, true);
    }

    /**
     *
     * @param roundDoubles
     */
    public void addOpenTabularViewNextStepItem(boolean roundDoubles) {
        addOpenTabularViewNextStepItem("nextstep.tabularview.itemlist.label", 0, roundDoubles);
    }

    /**
     *
     * @param label
     * @param index
     * @param roundDoubles
     */
    public void addOpenTabularViewNextStepItem(String label, int index, final boolean roundDoubles) {
        final TabularData td = getTabular(index);
        addNextStepItem(AppConfig.getLabel(label), new EventHandler() {
            @Override
            public void handle(Event t) {
                TabularViewController controller = TabManager.openTabularView(td);
                if (roundDoubles && controller != null) {
                    controller.setRoundDoubles(roundDoubles);
                }
                if (sortedColumnIndex != null && controller != null) {
                    controller.setSortedColumn(sortedColumnIndex);
                }

            }
        });
    }

    /**
     *
     */
    public void addOpenFileListViewNextStepItem() {
        addNextStepItem(AppConfig.getLabel("nextstep.filelistview.itemlist.label"), new EventHandler() {
            @Override
            public void handle(Event t) {
                TabManager.openFileListViewer(getFilelist());
            }
        });
    }

    /**
     *
     * @return
     */
    public TabularData getTabular() {
        return tabularData.get(0);
    }

    /**
     *
     * @param index
     * @return
     */
    public TabularData getTabular(int index) {
        return tabularData.get(index);
    }

    /**
     *
     * @return
     */
    public List<TabularData> getTabulars() {
        return tabularData;
    }

    /**
     *
     */
    public void addOpenOutputNextStepItem() {
        addNextStepItem(AppConfig.getLabel("nextstep.directory.itemlist.label"), new EventHandler() {
            @Override
            public void handle(Event t) {
                openOutputDirectory();
            }
        });
    }

    /**
     *
     */
    public void addReRunTaskNextStepItem() {
        addNextStepItem(AppConfig.getLabel("nextstep.rerun.itemlist.label"), new EventHandler() {
            @Override
            public void handle(Event t) {
                reRunTheTask();
            }
        });
    }

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     *
     * @param tabularData
     */
    public void setTabular(TabularData tabularData) {
        setTabular(tabularData, 0);
    }

    /**
     *
     * @param tabular
     * @param index
     */
    public void setTabular(TabularData tabular, int index) {
//        System.out.println("tabularrrrrrrrrr=" + index + " " + tabular);
        if (tabularData.size() < index) {
            System.out.println("set to =" + index);
            this.tabularData.set(index, tabular);
        } else {
            System.out.println("add to=" + index);
            this.tabularData.add(index, tabular);

        }
    }

    /**
     *
     * @param tabulars
     */
    public void setTabulars(List<TabularData> tabulars) {
//        System.out.println("tabularsss=" + tabulars);
        this.tabularData = tabulars;
    }

    /**
     *
     * @param tabularData
     * @param sortedColumnIndex
     */
    public void setTabularBySortColumn(TabularData tabularData, int sortedColumnIndex) {
        setTabular(tabularData);
        this.sortedColumnIndex = sortedColumnIndex;
    }

    /**
     *
     * @return
     */
    public String getOutputDir() {
        return outputDir;
    }

    /**
     *
     * @param outputDir
     */
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    /**
     *
     * @param parent
     */
    public void setParent(WorkflowController parent) {
        this.parent = parent;
    }

    /**
     *
     */
    public void openOutputDirectory() {
        FileHandler.openExternalDirectory(outputDir);
    }

    /**
     *
     */
    public void reRunTheTask() {
        parent.hideNextStepPane();
    }

}
