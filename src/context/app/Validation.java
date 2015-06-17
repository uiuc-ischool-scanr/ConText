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

import context.core.entity.DataElement;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.thehecklers.monologfx.MonologFX;
import org.thehecklers.monologfx.MonologFXBuilder;
import org.thehecklers.monologfx.MonologFXButton;
import org.thehecklers.monologfx.MonologFXButtonBuilder;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class Validation extends Application {

    static MonologFXButton cancelButton = MonologFXButtonBuilder.create()
            .cancelButton(true)
            //.icon("dialog_cancel.png")
            .type(MonologFXButton.Type.CANCEL)
            .build();

    static MonologFXButton okButton = MonologFXButtonBuilder.create()
            .defaultButton(true)
            //.icon("dialog_apply.png")
            .type(MonologFXButton.Type.OK)
            .build();

    /**
     *
     * @param message
     * @param title
     * @return
     */
    public static MonologFX buildWarningButton(String message, String title) {
        MonologFX mono = MonologFXBuilder.create()
                .modal(true)
                .message(message)
                .titleText(title)
                .button(okButton)
                //.button(cancelButton)
                .buttonAlignment(MonologFX.ButtonAlignment.CENTER)
                .build();
        return mono;
    }

    /**
     *
     * @param inputListView
     * @return
     */
    public static boolean selectAnyItemInListView(ListView<DataElement> inputListView) {
        if (inputListView.getSelectionModel().isEmpty()) {
            MonologFX mono = buildWarningButton(AppConfig.getLabel("validation.input.listview.empty.message"), AppConfig.getLabel("validation.input.listview.empty.title"));
            MonologFXButton.Type retval = mono.show();
            return false;
        }
        return true;
    }

    /**
     *
     * @param textField
     * @return
     */
    public static boolean nonEmptyOutputTextfield(TextField textField) {
        if (textField.getText() == null || textField.getText().length() == 0) {
            MonologFX mono = buildWarningButton(AppConfig.getLabel("validation.output.textfield.empty.message"), AppConfig.getLabel("validation.output.textfield.empty.title"));
            MonologFXButton.Type retval = mono.show();
            return false;
        }
        return true;
    }

    /**
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setMnemonicParsing(true);
        btn.setText("_Test Dialogs");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
//                MonologFXButton mlb = new MonologFXButton();
//                mlb.setDefaultButton(true);
//                mlb.setType(MonologFXButton.Type.OK);
//                
//                MonologFXButton mlb2 = new MonologFXButton();
//                mlb2.setCancelButton(true);
//                mlb2.setType(MonologFXButton.Type.CANCEL);
//
//                MonologFX mono = new MonologFX();
//                mono.setModal(true);
//                mono.setMessage("Welcome to MonologFX! Let's do this.");
//                mono.setTitleText("Important Announcement");
//                mono.addButton(mlb);
//                mono.addButton(mlb2);
//                mono.setDisplayTime(5);

                // "Conventional dialog (non-timed)
                MonologFX mono = MonologFXBuilder.create()
                        .modal(true)
                        .message("Welcome to MonologFX! Please feel free to try it out and share your thoughts.")
                        .titleText("Important Announcement")
                        .button(okButton)
                        .button(cancelButton)
                        .buttonAlignment(MonologFX.ButtonAlignment.CENTER)
                        .build();

                // Show the dialog!
                MonologFXButton.Type retval = mono.show();
                System.out.println("Return value=" + retval);

                // Testing a timed dialog
                okButton = MonologFXButtonBuilder.create()
                        .defaultButton(true)
                        .cancelButton(true)
                        .type(MonologFXButton.Type.OK)
                        .build();

                mono = MonologFXBuilder.create()
                        .message("This is a timed dialog. Watch it appear and disappear before your eyes!")
                        .titleText("Now you see it...")
                        .button(okButton)
                        .buttonAlignment(MonologFX.ButtonAlignment.CENTER)
                        .displayTime(5)
                        .build();

                // Show the dialog!
                retval = mono.show();
                System.out.println("Return value=" + retval);
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("MonologFX Test Application");
        primaryStage.setScene(scene);
        primaryStage.show();
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
        launch(args);
    }

}
