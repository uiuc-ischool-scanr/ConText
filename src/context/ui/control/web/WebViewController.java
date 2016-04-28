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
package context.ui.control.web;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import org.openide.util.Exceptions;

/**
 * FXML Controller class
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class WebViewController extends AnchorPane implements Initializable {

    /**
     *
     */
    public static String path = "/context/ui/control/web/WebView.fxml";

    @FXML
    private Label titleLabel;
    @FXML
    private WebView webView;

    /**
     *
     * @return
     */
    public WebView getWebView() {
        return webView;
    }

    /**
     *
     */
    public void reload() {
        if (webView.getEngine() != null) {
            webView.getEngine().reload();
        }
    }

    /**
     *
     * @return
     */
    public Label getTitleLabel() {
        return titleLabel;
    }

    /**
     *
     * @param path
     */
    public void setContent(String path) {
        try {
            System.out.println("path=" + path);
            final String toExternalForm = new File(path).toURI().toURL().toExternalForm();
            System.out.println("externalForm=" + toExternalForm);
            webView.getEngine().load(toExternalForm);

        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Initializes the controller class.
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webView.setCache(false);
    }

}
