/*
 
* Copyright (c) 2020 University of Illinois Board of Trustees, All rights reserved.   
* Developed at the iSchool, by Dr. Jana Diesner, Chieh-Li Chin, 
* Amirhossein Aleyasen, Shubhanshu Mishra, Kiumars Soltani, Liang Tao, 
* Ming Jiang, Harathi Korrapati, Nikolaus Nova Parulian, and Lan Jiang..     
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
package context.ui.control.filelist;

import context.core.util.CharsetDetector;
import context.core.util.JavaIO;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.openide.util.Exceptions;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class FileListViewerController extends AnchorPane implements Initializable {

    /**
     *
     */
    public static String path = "/context/ui/control/filelist/FileListViewer.fxml";

    private List<File> fileList;

    private int currentIndex;
    @FXML
    private Button goTofirstDocButton;
    @FXML
    private Insets x1;
    @FXML
    private Button goToPreviousDocButton;
    @FXML
    private Button goToNextDocButton;
    @FXML
    private Button goToLastDocButton;
    @FXML
    private WebView webView;
    @FXML
    private Label fileCounterLabel;

    static private String contentType;

    /**
     *
     * @return
     */
    public static String getContentType() {
        return contentType;
    }

    /**
     *
     * @return
     */
    public List<File> getFileList() {
        return fileList;
    }

    /**
     *
     * @param index
     */
    public void updateFileContentViewer(int index) {
        if ((fileList == null) && (fileList.size() == 0)) {
            return;
        }
        if (index < 0 || index >= fileList.size()) {
            return;
        }
        currentIndex = index;
        try {
            String path = fileList.get(index).toURI().toURL().toExternalForm();
            String abs_path = fileList.get(index).getAbsolutePath();

            fileCounterLabel.setText((index + 1) + " Out Of " + fileList.size() + " :" + fileList.get(index).getName());
            //System.out.println("filePath=" + path);
            final WebEngine webengine = webView.getEngine();
            String encoding = CharsetDetector.detectCharset(abs_path);
            if (abs_path.endsWith("html") || abs_path.endsWith("htm")) {
                contentType = "text/html";
                System.out.println("abs_path=" + abs_path);
                URL url = (new File(abs_path)).toURL();
                System.out.println("url=" + url);
                webengine.load(url.toExternalForm());
            } else {
                contentType = "text/plain";
                String content = "";
                try {
                    content = JavaIO.readFile(fileList.get(index));
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                webengine.loadContent(content, contentType);
            }
            final String utf_8 = "UTF-8";
            System.out.println("Detected Charset=" + encoding);
            if (encoding == null) {
                encoding = utf_8;
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(FileListViewerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param fileList
     */
    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
        updateFileContentViewer(0);

    }

    /**
     * Initializes the controller class.
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handleGoToFirstDocButton(ActionEvent event) {
        updateFileContentViewer(0);
    }

    @FXML
    private void handleGoToPreviousDocButton(ActionEvent event) {
        updateFileContentViewer(currentIndex - 1);
    }

    @FXML
    private void handleGoToNextDocButton(ActionEvent event) {
        updateFileContentViewer(currentIndex + 1);
    }

    @FXML
    private void handleGoToLastDocButton(ActionEvent event) {
        updateFileContentViewer(fileList.size() - 1);
    }

}
