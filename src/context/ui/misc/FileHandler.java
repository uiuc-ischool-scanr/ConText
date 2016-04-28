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
package context.ui.misc;

import context.app.AppConfig;
import context.app.main.ContextFXController;
import context.core.entity.FileData;
import context.core.entity.FileList;
import context.core.util.JavaIO;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.openide.util.Exceptions;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class FileHandler {

    private static String lastDirectoryLocation;
    private static String lastFileLocation;

    /**
     *
     * @return
     */
    public static String getLastDirectoryLocation() {
        if (lastDirectoryLocation == null) {
            return AppConfig.defaultOpenLocation;
        }
        return lastDirectoryLocation;
    }

    /**
     *
     * @return
     */
    public static String getLastFileLocation() {
        if (lastFileLocation == null) {
            return AppConfig.defaultOpenLocation;
        }
        return lastFileLocation;
    }

    /**
     *
     * @param lastDirectoryLocation
     */
    public static void setLastDirectoryLocation(String lastDirectoryLocation) {
        FileHandler.lastDirectoryLocation = lastDirectoryLocation;
    }

    /**
     *
     * @param lastFileLocation
     */
    public static void setLastFileLocation(String lastFileLocation) {
        FileHandler.lastFileLocation = lastFileLocation;
    }

    /**
     *
     * @param message
     * @return
     */
    public static FileList openDirectoryChooser(String message) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle(message);

        dirChooser.setInitialDirectory(new File(getLastDirectoryLocation()));
        final File selectedDirectory = dirChooser.showDialog(ContextFXController.getStage());
        if (selectedDirectory != null) {
            selectedDirectory.getAbsolutePath();
//            File[] files = selectedDirectory.listFiles();
            FileList dir = new FileList(new SimpleStringProperty(selectedDirectory.getName()), selectedDirectory);
            setLastDirectoryLocation(selectedDirectory.getAbsolutePath());
            return dir;
        } else {
            System.out.println("Selected Directory is Null");
            return null;
        }
    }

    /**
     *
     * @param message
     * @return
     */
    public static FileData openFileChooser(String message) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(message);

        fileChooser.setInitialDirectory(new File(getLastFileLocation()));
        final File selectedFile = fileChooser.showOpenDialog(ContextFXController.getStage());
        if (selectedFile != null) {
            selectedFile.getAbsolutePath();
            FileData file = new FileData(new SimpleStringProperty(selectedFile.getName()), selectedFile);
            setLastFileLocation(selectedFile.getParent());
            return file;
        } else {
            System.out.println("Selected File is Null");
            return null;
        }
    }

    /**
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        return FilenameUtils.getBaseName(path);
    }

    /**
     *
     * @param path
     * @return
     */
    public static StringProperty getFileNameProperty(String path) {
        return new SimpleStringProperty(getFileName(path));
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        String test = "C:\\Data\\Dropbox\\Development\\Java-ws\\Context-FX\\data\\Corpus";
        System.out.println(getFileName(test));
    }

    /**
     *
     * @param path
     */
    public static void openExternalDirectory(String path) {
//        try {
//            Desktop.getDesktop().open(new File(path));
            JavaIO.openDirectory(new File(path));
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
    }

    /**
     *
     * @param path
     * @return
     */
    public static String getDirOrParentDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("The path not exist: " + path);
            return path;
        } else if (file.isDirectory()) {
            return path;
        } else if (file.isFile()) {
            return FilenameUtils.getFullPath(path);
        }
        return null;
    }

    /**
     *
     * @param url
     */
    public static void openWebpage(String url) {
        try {
            URI uri = new URI(url);
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(uri);
            }
        } catch (URISyntaxException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     *
     * @param subdirectory
     */
    public static void createDirectory(String subdirectory) {
        try {
            FileUtils.forceMkdir(new File(subdirectory));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
