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

import java.util.Arrays;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class ContextFX extends Application {

    /**
     *
     */
    public static ContextFXController appController;

    /**
     *
     */
    public static void initialize() {

    }

    /**
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        initialize();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ContextFX.fxml"));

        Parent root = (Parent) loader.load();
        //root.getStylesheets().add("style-default.css");
        appController = (ContextFXController) loader.getController();
        appController.setStageAndSetupListeners(stage);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("ConText 2.0");//From August 2015, it becomes ConText 1.1 //Jan 2016 - 1.2.X // July 2018 - 1.2.1 // April 2020 - 2.0
        stage.getIcons().add(new Image("resources/context-blue.png"));
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Thread.setDefaultUncaughtExceptionHandler(ContextFX::handleException);
        launch(args);
    }

    /*
    public static void handleException(Thread t, Throwable e) {
        System.out.println("Unhandled Exception: " + e);
        if (e instanceof IndexOutOfBoundsException || e instanceof ArrayIndexOutOfBoundsException) {
            boolean isUpdateCachedBoundsBug = Arrays.stream(e.getStackTrace()).anyMatch(
                    s -> s.getClassName().startsWith("javafx.scene.Parent") && "updateCachedBounds".equals(s.getMethodName()));
            if (isUpdateCachedBoundsBug) {
                System.out.println("Detected an AIOBE or IOBE from the updateCachedBounds bug:");
                e.printStackTrace();
                //t.stop();
            } else {
                System.out.println("Detected an AIOBE or IOBE that is not an updateCachedBounds bug: " + e);
                e.printStackTrace();
                //t.stop();
            }
        } else {
            e.printStackTrace();
            //System.out.println("Unhandled Exception: " + e);
        }
    }
    */

}
