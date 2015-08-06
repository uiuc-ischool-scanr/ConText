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
package context.app;

import context.app.main.ContextFX;
import context.app.main.ContextFXController;
import context.core.entity.FileList;
import context.core.entity.ProjectElement;
import context.core.entity.TabularData;
import context.core.entity.TaskInstance;
import context.core.task.codebook.CodebookApplicationTaskInstance;
import context.core.task.corpusstat.CorpusStatTaskInstance;
import context.core.task.entitydetection.EntityDetectionTaskInstance;
import context.core.task.lexisnexis.LexisNexisNetworkGenerationTaskInstance;
import context.core.task.lexisnexis.LexisNexisParseTaskInstance;
import context.core.task.pos.POSTaskInstance;
import context.core.task.removestopword.RemoveStopwordsTaskInstance;
import context.core.task.sentiment.SentimentTaskInstance;
import context.core.task.stemming.StemmingTaskInstance;
import context.core.task.syntaxbased.SyntaxBasedTaskInstance;
import context.core.task.topicmodeling.TopicModelingTaskInstance;
import context.core.task.wordcloud.WordCloudTaskInstance;
import context.ui.control.codebook.CodebookAppController;
import context.ui.control.corpusstat.CorpusStatController;
import context.ui.control.entitydetection.EntityDetectionController;
import context.ui.control.filelist.FileListViewerController;
import context.ui.control.lexisnexis.LexisNexisNetworkGenerationController;
import context.ui.control.lexisnexisparse.LexisNexisParseController;
import context.ui.control.pos.POSController;
import context.ui.control.removestopword.RemoveStopwordsController;
import context.ui.control.sentiment.SentimentController;
import context.ui.control.stemming.StemmingController;
import context.ui.control.syntaxbased.SyntaxBasedController;
import context.ui.control.tabular.TabularViewController;
import context.ui.control.topicmodeling.TopicModelingController;
import context.ui.control.web.WebViewController;
import context.ui.control.wordcloud.WordCloudController;
import context.ui.control.workflow.WorkflowController;
import context.ui.misc.NamingPolicy;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import org.openide.util.Exceptions;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class TabManager {

    private static Map<ProjectElement, Tab> openTabs = new HashMap<ProjectElement, Tab>();

    private static ProjectElement findInOpenTabs(Tab tab) {
        for (ProjectElement elem : openTabs.keySet()) {
            if (tab.equals(openTabs.get(elem))) {
                return elem;
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    public static Map<ProjectElement, Tab> getOpenTabs() {
        return openTabs;
    }

    /**
     *
     */
    public static void closeAllTabs() {
        for (Tab t : openTabs.values()) {
            closeTab(t);
        }
    }

    /**
     *
     * @param element
     * @param tabContent
     */
    public static void addTab(ProjectElement element, Parent tabContent) {
        final Tab tab = new Tab();
        String title = element.getName().get();
        if (element instanceof TaskInstance) {
            title = AppConfig.getTaskLabel(element.getName().get());
        }
        tab.setText(title);
        tab.setContent(tabContent);

        final ContextMenu contextMenu = new ContextMenu();

        MenuItem closeThis = new MenuItem("Close Tab");
        closeThis.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Close Tab");
                ContextFX.appController.getMainTabPane().getTabs().remove(ContextFX.appController.getMainTabPane().getSelectionModel().getSelectedItem());
            }
        });

        MenuItem closeAll = new MenuItem("Close All");
        closeAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Close All");
                ContextFX.appController.getMainTabPane().getTabs().clear();
            }
        });
        MenuItem closeOther = new MenuItem("Close Other");
        closeOther.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Close Other");
                final Tab selectedItem = ContextFX.appController.getMainTabPane().getSelectionModel().getSelectedItem();
                ContextFX.appController.getMainTabPane().getTabs();
                List<Tab> shouldClose = new ArrayList<>();
                for (Tab t : ContextFX.appController.getMainTabPane().getTabs()) {
                    if (!t.equals(selectedItem)) {
                        shouldClose.add(t);
                    }
                }
                ContextFX.appController.getMainTabPane().getTabs().removeAll(shouldClose);

            }
        });

        contextMenu.getItems().addAll(closeThis, closeAll, closeOther);
        tab.setContextMenu(contextMenu);

        tab.setOnClosed(new EventHandler<javafx.event.Event>() {
            @Override
            public void handle(javafx.event.Event e) {
                openTabs.remove(findInOpenTabs(tab));
            }
        });

        tab.setOnSelectionChanged(new EventHandler<javafx.event.Event>() {
            @Override
            public void handle(javafx.event.Event t) {
                if (tab.isSelected()) {
                    ProjectElement elem = findInOpenTabs(tab);
                    ContextFXController.selectNode(elem);
                    if (elem instanceof TaskInstance) {
                        StringProperty taskname = NamingPolicy.generateTaskName(elem.getClass());
                        final String url = AppConfig.getTaskHelpguideURL(taskname.get());
                        //ContextFXController.setHelpGuide(url);
                    }
                }
            }
        });
        openTabs.put(element, tab);
        ContextFX.appController.getMainTabPane().getTabs().add(tab);
        ContextFX.appController.getMainTabPane().getSelectionModel().select(tab);
    }

    /**
     *
     * @param tab
     */
    public static void selectTab(Tab tab) {
        ContextFX.appController.getMainTabPane().getSelectionModel().select(tab);
    }

    /**
     *
     * @param tabularData
     * @return
     */
    public static TabularViewController openTabularView(TabularData tabularData) {
        try {
            if (openTabs.containsKey(tabularData)) {
                selectTab(openTabs.get(tabularData));
            } else {
                FXMLLoader loader = new FXMLLoader(TabManager.class.getResource(TabularViewController.path));
                Parent root = (Parent) loader.load();
                TabularViewController controller = (TabularViewController) loader.getController();
                controller.setData(tabularData);
                TabManager.addTab(tabularData, root);
                return controller;
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    static List<String> acceptedExtensions = Arrays.asList("txt", "csv", "html", "htm", "xml", "dat");

    static boolean isAcceptedExtension(String file_path) {
        for (String ext : acceptedExtensions) {
            if (file_path.endsWith(ext)) {
                System.out.println("isAcceptedExtension:" + file_path + " :accepted");
                return true;
            }
        }
        System.out.println("isAcceptedExtension:" + file_path + " :rejected");
        return false;
    }

    /**
     *
     * @param f
     * @return
     */
    public static boolean isAcceptedFile(File f) {
        if (!f.isDirectory() && !(f.getName().equals("desktop.ini")) && !(f.getName().startsWith(".")) && isAcceptedExtension(f.getName())) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param fileList
     */
    public static void openFileListViewer(FileList fileList) {
        System.out.println("fileList:" + fileList.getDir().getAbsolutePath());
        try {
            if (openTabs.containsKey(fileList)) {
                selectTab(openTabs.get(fileList));
            } else {
                FXMLLoader loader = new FXMLLoader(TabManager.class.getResource(FileListViewerController.path));
                Parent root = (Parent) loader.load();
                FileListViewerController controller = (FileListViewerController) loader.getController();
                FileFilter ffilter = new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        if (TabManager.isAcceptedFile(f)) {
                            return true;
                        }
                        return false;
                    }
                };
                controller.setFileList(Arrays.asList(fileList.getDir().listFiles(ffilter)));
                TabManager.addTab(fileList, root);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     *
     * @param filepath
     */
    public static void openWebViewer(String filepath) {
        try {
//            if (openTabs.containsKey(fileList)) {
//                selectTab(openTabs.get(fileList));
//            } else {
            FXMLLoader loader = new FXMLLoader(TabManager.class.getResource(WebViewController.path));
            Parent root = (Parent) loader.load();
            WebViewController controller = (WebViewController) loader.getController();
            controller.setContent(filepath);
//            controller.reload();
            TabManager.addTab(new ProjectElement(new SimpleStringProperty("Word Cloud")), root);
//            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     *
     * @param task
     */
    public static void openTaskController(TaskInstance task) {
        if (openTabs.containsKey(task)) {
            selectTab(openTabs.get(task));
        } else {
            WorkflowController controller = null;
            if (task instanceof CodebookApplicationTaskInstance) {
                controller = new CodebookAppController((CodebookApplicationTaskInstance) task);

            } else if (task instanceof CorpusStatTaskInstance) {
                controller = new CorpusStatController((CorpusStatTaskInstance) task);

            } else if (task instanceof EntityDetectionTaskInstance) {
                controller = new EntityDetectionController((EntityDetectionTaskInstance) task);

            } else if (task instanceof LexisNexisNetworkGenerationTaskInstance) {
                controller = new LexisNexisNetworkGenerationController((LexisNexisNetworkGenerationTaskInstance) task);

            } else if (task instanceof LexisNexisParseTaskInstance) {
                controller = new LexisNexisParseController((LexisNexisParseTaskInstance) task);

            } else if (task instanceof POSTaskInstance) {
                controller = new POSController((POSTaskInstance) task);

            } else if (task instanceof RemoveStopwordsTaskInstance) {
                controller = new RemoveStopwordsController((RemoveStopwordsTaskInstance) task);

            } else if (task instanceof SentimentTaskInstance) {
                controller = new SentimentController((SentimentTaskInstance) task);

            } else if (task instanceof StemmingTaskInstance) {
                controller = new StemmingController((StemmingTaskInstance) task);

            } else if (task instanceof SyntaxBasedTaskInstance) {
                controller = new SyntaxBasedController((SyntaxBasedTaskInstance) task);

            } else if (task instanceof TopicModelingTaskInstance) {
                controller = new TopicModelingController((TopicModelingTaskInstance) task);

            } else if (task instanceof WordCloudTaskInstance) {
                controller = new WordCloudController((WordCloudTaskInstance) task);
            } else {
                System.out.println("obj is not task, class=" + task.getClass());
            }
            TabManager.addTab(task, controller);
        }
    }

    /**
     *
     * @param element
     */
    public static void updateName(ProjectElement element) {
        Tab tab = openTabs.get(element);
        if (tab != null) {
            tab.setText(element.getName().get());
        }
    }

    /**
     *
     * @param element
     */
    public static void delete(ProjectElement element) {
        Tab tab = openTabs.get(element);
        if (tab != null) {
            openTabs.remove(element);
            closeTab(tab);
        } else {
            System.out.println("tab is null (tab is not open)");
        }
    }

    private static void closeTab(Tab tab) {
        //        EventHandler<Event> handler = tab.getOnClosed();
//        if (null != handler) {
//            handler.handle(null);
//        } else {
        System.out.println("remove tab from tabPane " + tab.getText());
        tab.getTabPane().getTabs().remove(tab);
//        }
    }

    /**
     *
     * @param fxmlPath
     * @param title
     * @param event
     */
    public static void openNewWindow(String fxmlPath, String title, ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(TabManager.class.getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
//            stage.setScene(new Scene(root, 450, 450));
            stage.setScene(new Scene(root));

            stage.show();

            //hide this current window (if this is whant you want
//            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
