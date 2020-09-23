/*
 
 * Copyright (c) 2020 University of Illinois Board of Trustees, All rights reserved.   
 * Developed at GSLIS/ the iSchool, by Dr. Jana Diesner, Chieh-Li Chin, Amirhossein Aleyasen, 
 * Shubhanshu Mishra, Kiumars Soltani, Liang Tao, Ming Jiang, Harathi Korrapati, 
 * Nikolaus Nova Parulian, and Lan Jiang.  
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
package context.app.main;

import context.app.AppConfig;
import context.app.ProjectManager;
import context.app.TabManager;
import context.core.entity.DataElement;
import context.core.entity.FileList;
import context.core.entity.ProjectElement;
import context.core.entity.TabularData;
import context.core.entity.TaskInstance;

import context.ui.control.bigram.BigramController;
import context.ui.control.codebook.CodebookAppController;
import context.ui.control.codebooknetwork.CodebookNetworkController;
import context.ui.control.corpusstat.CorpusStatController;
import context.ui.control.csvparser.CsvParserController;
import context.ui.control.entitydetection.EntityDetectionController;
import context.ui.control.entitynetwork.EntityNetworkController;
import context.ui.control.entropy.EntropyController;
import context.ui.control.keyword.KeywordController;
//import context.ui.control.lexisnexis.LexisNexisNetworkGenerationController;
//import context.ui.control.lexisnexisparse.LexisNexisParseController;
//import context.ui.control.parsetree.ParseTreeController;
import context.ui.control.pos.POSController;
import context.ui.control.removestopword.RemoveStopwordsController;
import context.ui.control.sentiment.SentimentController;
import context.ui.control.stemming.StemmingController;
import context.ui.control.syntaxbased.SyntaxBasedController;
import context.ui.control.topicmodeling.TopicModelingController;
import context.ui.control.wordcloud.WordCloudController;
import context.ui.control.workflow.WorkflowController;
import context.ui.misc.FileHandler;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
//import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class ContextFXController implements Initializable {

    private static Stage stage;

    private Label label;
    @FXML
    private TreeView<ProjectElement> resourceTreeView;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private WebView welcomeWebView;
    @FXML
    private WebView helpWebView;

    @FXML
    private SplitPane leftMainSplitPane;
    @FXML
    private Insets x2;
    @FXML
    private Button clearLogButton;
    @FXML
    private TextArea logTextArea;

    //@FXML
    //private MenuItem lexisNexisNetworkGenerationMenuItem;
    @FXML
    private MenuItem POSMenuItem;
    @FXML
    private MenuItem removeStopwordsMenuItem;
    @FXML
    private MenuItem stemmingMenuItem;
    @FXML
    private MenuItem corpusStatMenuItem;
    @FXML
    private MenuItem entityDetectionMenuItem;
    @FXML
    private MenuItem topicModelingMenuItem;
    @FXML
    private MenuItem sentimentAnalysisMenuItem;
    @FXML
    private MenuItem codebookApplicationMenuItem;
    //@FXML
    //private MenuItem parseLexisNexisMenuItem;
    //@FXML
    //private MenuItem lexisNexisNetworkGenerationMenuItem2;
    @FXML
    private MenuItem syntaxBasedMenuItem;
    @FXML
    private MenuItem deepParsingMenuItem;
    @FXML
    private MenuItem codebookNetworkGenrationMenuItem;
    @FXML
    private MenuItem helpContentMenuItem;
    @FXML
    private MenuItem onlineDocsMenuItem;
    @FXML
    private MenuItem reportIssueMenuItem;
    @FXML
    private MenuItem checkForUpdateMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private MenuItem wordCloudMenuItem;
    @FXML
    private MenuItem keywordMenuItem;
    @FXML
    private MenuItem bigramDetectionMenuItem;

    @FXML
    private MenuItem entityTypesNetworkGenerationMenuItem;
    @FXML
    private MenuItem geocodedNetworkMenuItem;
    @FXML
    private MenuItem communicatioNetworkGenerationMeunItem;
    @FXML
    private MenuItem communicationTopicNetworkGenerationMenuItem;
    @FXML
    private MenuItem underlyingNetworkGenerationMenuItem;
    @FXML
    private MenuItem networkComparisonMenuItem;
    @FXML
    private MenuItem gdeltNetworkMenuItem;

    @FXML
    private MenuItem entropyMenuItem;

    @FXML
    private MenuItem csvParserMenuItem;

    /**
     *
     * @return
     */
    public TabPane getMainTabPane() {
        return mainTabPane;
    }

    @FXML
    private Tab tab1;
    @FXML
    private Insets x1;
    @FXML
    private Button newProjectButton;
    @FXML
    private Button newFile;
    @FXML
    private Button saveFileButton;
    @FXML
    private MenuItem importTextCorpusMenuItem;

    private static StringProperty logs;

    /**
     *
     * @param log
     */
    public void appendLog(String log) {
        logTextArea.appendText(log + "\n");
    }

    /**
     *
     * @return
     */
    public Stage getStage() {
        return stage;
    }

    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    /*Remove right pane from the UI
     * public static void setHelpGuide(String path) {
     if (path != null) {
     helpWebView.getEngine().load(ContextFXController.class.getResource(path).toExternalForm());
     }
     }*/
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logs = new SimpleStringProperty();
        logTextArea.textProperty().bindBidirectional(logs);
        welcomeWebView.getEngine().load("https://docs.google.com/document/d/1GQlA9wZ8a4mjS6RdaERTkbKUWtbyAPcCxAu1aocPFvA/pub");

        //setHelpGuide("/resources/helpguide/HelpGuidePublic.html");
        leftMainSplitPane.setDividerPosition(0, 0.12);
        initializeResourceTreeView();
    }

    void setStageAndSetupListeners(Stage stage) {
        this.stage = stage;
    }

    private void handleSalamButton(ActionEvent event) {

    }

    /**
     *
     * @param element
     * @param index
     */
    public void addToTreeView(ProjectElement element, int index) {
        /*
        Niko
        add exception because this is probably the cause why ConText hanging
         */
        try {
            resourceTreeView.getRoot().getChildren().get(index).getChildren().add(new TreeItem<ProjectElement>(element));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param dataElement
     */
    public void addDataToTreeView(DataElement dataElement) {
        addToTreeView(dataElement, 0);
    }

    /**
     *
     * @param taskInstance
     */
    public void addTaskToTreeView(TaskInstance taskInstance) {
        addToTreeView(taskInstance, 1);
    }

    /**
     *
     * @param dataElement
     */
    public void addResultToTreeView(DataElement dataElement) {
        addToTreeView(dataElement, 2);
    }

    /**
     *
     * @param element
     */
    public void selectNode(ProjectElement element) {
        final TreeItem<ProjectElement> root = resourceTreeView.getRoot();
        for (TreeItem<ProjectElement> child : root.getChildren()) {
            for (TreeItem<ProjectElement> node : child.getChildren()) {
                if (node.getValue() != null && node.getValue().equals(element)) {
                    resourceTreeView.getSelectionModel().select(node);
                }
            }

        }
    }

    private void initializeResourceTreeView() {
        final ProjectElement rootNode = new ProjectElement(new SimpleStringProperty("My Project"));
        TreeItem<ProjectElement> treeRoot = new TreeItem<ProjectElement>(rootNode);
        final ProjectElement dataNode = new ProjectElement(new SimpleStringProperty("Data"));
        final ProjectElement processNode = new ProjectElement(new SimpleStringProperty("Processes"));
        final ProjectElement resultNode = new ProjectElement(new SimpleStringProperty("Results"));
        treeRoot.setExpanded(true);
        treeRoot.getChildren().addAll(Arrays.asList(
                new TreeItem<>(dataNode),
                new TreeItem<>(processNode),
                new TreeItem<>(resultNode)));

        resourceTreeView.setShowRoot(true);
        resourceTreeView.setRoot(treeRoot);

        treeRoot.getChildren().get(0).setExpanded(true);
        treeRoot.getChildren().get(1).setExpanded(true);
        treeRoot.getChildren().get(2).setExpanded(true);
        
        resourceTreeView.setEditable(true);
        
        resourceTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    ProjectElement item = resourceTreeView.getSelectionModel().getSelectedItem().getValue();
                    openProjectElement(item);
                }
            }
        });   
                
        resourceTreeView.setCellFactory(new Callback<TreeView<ProjectElement>, TreeCell<ProjectElement>>() {
            @Override
            public TreeCell<ProjectElement> call(TreeView<ProjectElement> p) {
                return new TreeCellContextMenuImpl();
            }
        });
        
        //  root.getChildren().add(treeView);
    }

    private void renameProjectElement(ProjectElement element, String newName) {
        element.setName(new SimpleStringProperty(newName));
        TabManager.updateName(element);
    }

    private void deleteProjectElement(ProjectElement element) {
        TabManager.delete(element);
        removeNode(resourceTreeView.getSelectionModel().getSelectedItem());
        ProjectManager.getThisProject().delete(element);
    }

    /**
     *
     * @param node
     */
    public void removeNode(TreeItem<ProjectElement> node) {
        if (node != null) {
            TreeItem<ProjectElement> parentNode = node.getParent();
            if (parentNode != null) {
                parentNode.getChildren().remove(node);
            }
        }
    }

    private void openProjectElement(ProjectElement element) {
        if (element instanceof FileList) {
            TabManager.openFileListViewer((FileList) element);
        } else if (element instanceof TabularData) {
            TabManager.openTabularView((TabularData) element);
        } else if (element instanceof TaskInstance) {
            TabManager.openTaskController((TaskInstance) element);
        } else {
            System.out.println("not supported project element, type=" + element.getClass());
        }
    }

    @FXML
    private void handleTab1Closed(Event event) {
    }

    @FXML
    private void handleNewProject(ActionEvent event) {
    }

    @FXML
    private void handleNewFile(ActionEvent event) {
    }

    @FXML
    private void handleSaveFile(ActionEvent event) {
    }

    @FXML
    private void handleImportTextCorpusMenuItem(ActionEvent event) {

        FileList selectedDir = FileHandler.openDirectoryChooser("Select Corpus Directory...");
        if (selectedDir != null) {
            ProjectManager.getThisProject().addData(selectedDir);
            TabManager.openFileListViewer(selectedDir);

        }
    }

    private void handleImportTextCorpusFileMenuItem(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text File...");
        fileChooser.showOpenDialog(stage);
    }

    /* @FXML
    private void handleLexisNexisNetworkGenerationMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new LexisNexisNetworkGenerationController());

    }*/
    private void handleGenericTaskMenuItem(WorkflowController controller) {
        TabManager.addTab(controller.getTaskInstance(), controller);
//        addTaskToTreeView(controller.getTaskInstance());
    }

    @FXML
    private void handleClearLogButton(ActionEvent event) {
        logTextArea.textProperty().set("");
    }

    @FXML
    private void handlePOSMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new POSController());
    }

    @FXML
    private void handleRemoveStopwordsMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new RemoveStopwordsController());
    }

    @FXML
    private void handleStemmingMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new StemmingController());
    }

    @FXML
    private void handleCorpusStatMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new CorpusStatController());
    }

    @FXML
    private void handleEntityDetectionMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new EntityDetectionController());
    }

    @FXML
    private void handleTopicModelingMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new TopicModelingController());
    }

    @FXML
    private void handleSentimentAnalysisMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new SentimentController());
    }

    @FXML
    private void handleCodebookApplicationMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new CodebookAppController());
    }

    /* @FXML
    private void handleParseLexisNexisMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new LexisNexisParseController());
    }
     */
    @FXML
    private void handleCsvParserMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new CsvParserController());
    }

    @FXML
    private void handleSyntaxBasedMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new SyntaxBasedController());
    }

    /*@FXML
	
	 * private void handleDeepParsingMenuItem(ActionEvent event) {
	 * handleGenericTaskMenuItem(new ParseTreeController()); }
	 */

    @FXML
    private void handleCodebookNetworkGenerationMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new CodebookNetworkController());
    }

    @FXML
    private void handleHelpContentMenuItem(ActionEvent event) {
        FileHandler.openWebpage(AppConfig.getProperty("help.helpcontent.url"));
    }

    @FXML
    private void handleOnlineDocsMenuItem(ActionEvent event) {
        FileHandler.openWebpage(AppConfig.getProperty("help.onlinedocs.url"));
    }

    @FXML
    private void handleReportIssueMenuItem(ActionEvent event) {
        FileHandler.openWebpage(AppConfig.getProperty("help.reportissue.email"));
    }

    @FXML
    private void handleCheckForUpdateMenuItem(ActionEvent event) {
    }

    @FXML
    private void handleAboutMenuItem(ActionEvent event) {
        FileHandler.openWebpage(AppConfig.getProperty("help.about.url"));
    }

    @FXML
    private void handleWordCloudMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new WordCloudController());
    }

    @FXML
    private void handleKeywordMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new KeywordController());
    }

    @FXML
    private void handleBigramDetectionMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new BigramController());
    }

    @FXML
    private void handleEntityTypesNetworkGenerationMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new EntityNetworkController());
    }

    @FXML
    private void handleGeocodedNetworkMenuItem(ActionEvent event) {
    }

    @FXML
    private void handleExitMenuItem(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleCloseAllMenuItem(ActionEvent event) {
        TabManager.closeAllTabs();
    }

    @FXML
    private void handleEntropyMenuItem(ActionEvent event) {
        handleGenericTaskMenuItem(new EntropyController());

    }

    private final class TreeCellContextMenuImpl extends TreeCell<ProjectElement> {

        private ContextMenu taskContextMenu = new ContextMenu();
        private ContextMenu dataContextMenu = new ContextMenu();

        public TreeCellContextMenuImpl() {
            //Task MenuItems
//            taskContextMenu.setMinWidth(200);
//            dataContextMenu.setMinWidth(200);
            MenuItem openMenuItem = new MenuItem("Open");
            MenuItem renameMenuItem = new MenuItem("Rename");
//            MenuItem duplicateTaskMenuItem = new MenuItem("Duplicate");
            MenuItem deleteMenuItem = new MenuItem("Delete");

            //Data MenuItems
//            MenuItem openDataMenuItem = new MenuItem("Open");
//            MenuItem renameDataMenuItem = new MenuItem("Rename");
            MenuItem locateDataMenuItem = new MenuItem("Open folder location");
//            MenuItem deleteDataMenuItem = new MenuItem("Delete");

            taskContextMenu.getItems().add(openMenuItem);
            taskContextMenu.getItems().add(new SeparatorMenuItem());
            taskContextMenu.getItems().add(renameMenuItem);
//            taskContextMenu.getItems().add(duplicateTaskMenuItem);
            taskContextMenu.getItems().add(deleteMenuItem);

            dataContextMenu.getItems().add(openMenuItem);
            dataContextMenu.getItems().add(new SeparatorMenuItem());
            dataContextMenu.getItems().add(renameMenuItem);
            dataContextMenu.getItems().add(locateDataMenuItem);
//            dataContextMenu.getItems().add(deleteMenuItem);

            openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    ProjectElement element = getTreeItem().getValue();
                    openProjectElement(element);
                }
            });

            renameMenuItem.setOnAction(new EventHandler() {
                @Override
                public void handle(Event t) {
                    ProjectElement element = getTreeItem().getValue();
//                    String newName = Dialogs.showInputDialog(stage, "Please enter new name:", "Rename " + element.getName().get(), "Rename", element.getName().get());
                    String newName = element.getName().get(); // TODO: fix it using another dialog lib
                    System.out.println("rename to " + newName);
                    renameProjectElement(element, newName);
                    setText(newName);
                }
            });

            deleteMenuItem.setOnAction(new EventHandler() {
                @Override
                public void handle(Event t) {
                    ProjectElement element = getTreeItem().getValue();
//                    Dialogs.DialogResponse response = Dialogs.showConfirmDialog(stage, "Do you want to contine?",
//                            "Delete " + element.getName().get(), "Delete");
//                    if (response.equals(Dialogs.DialogResponse.YES)) {
//                        deleteProjectElement(element);
//                    }
                    deleteProjectElement(element); //TODO : add new confirmation using another dialog lib
                }

            });

            locateDataMenuItem.setOnAction(new EventHandler() {
                @Override
                public void handle(Event t) {
                    if (getTreeItem().getValue() instanceof DataElement) {
                        DataElement d = (DataElement) getTreeItem().getValue();
                        String path = FileHandler.getDirOrParentDir(d.getPath().get());
                        if (path!=null){
                            FileHandler.openExternalDirectory(path);
                            System.out.println("Open external directory " + path);
                        }
                    }

                }
            });
        }

        @Override
        public void updateItem(ProjectElement item, boolean empty) {
            super.updateItem(item, empty);
            setText(getString());
            if (getTreeItem() != null && getTreeItem().getGraphic() != null) {
                setGraphic(getTreeItem().getGraphic());
            }
            // if the item is not empty and is a root...
            //     if (!empty/* && getTreeItem().getParent() == null*/) {
            if (item instanceof TaskInstance) {
                setContextMenu(taskContextMenu);
            } else if (item instanceof DataElement) {
                setContextMenu(dataContextMenu);
            }
            //     }
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
