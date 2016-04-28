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
import context.core.entity.TaskInstance;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class NamingPolicy {

//    public static StringProperty generateInputName(String inputPath) {
//        return new SimpleStringProperty(FileHandler.getFileName(inputPath));
//    }

    /**
     *
     * @param aTaskClass
     * @return
     */
    
    public static StringProperty generateTaskName(Class aTaskClass) {
        final String simpleName = aTaskClass.getSimpleName();
        final String taskname = simpleName.substring(0, simpleName.length() - 12);
        return new SimpleStringProperty(taskname);
    }

    /**
     *
     * @param inputname
     * @param outputPath
     * @param instance
     * @return
     */
    public static StringProperty generateOutputName(String inputname, String outputPath, TaskInstance instance) {
        String name = inputname + "-" + AppConfig.getTaskAbbr(generateTaskName(instance.getClass()).get());
        return new SimpleStringProperty(name);
    }

    /**
     *
     * @param inputname
     * @param outputPath
     * @param instance
     * @return
     */
    public static StringProperty generateTabularName(String inputname, String outputPath, TaskInstance instance) {
        String name = inputname + "-" + AppConfig.getTaskAbbr(generateTaskName(instance.getClass()).get());
        return new SimpleStringProperty(name);
    }

    /**
     *
     * @param inputName
     * @param outputPath
     * @param instance
     * @return
     */
    public static StringProperty generateTabularPath(String inputName, String outputPath, TaskInstance instance) {
        return generateTabularPath(inputName, outputPath, instance, ".csv");
    }

    /**
     *
     * @param inputName
     * @param outputPath
     * @param instance
     * @param extension
     * @return
     */
    public static StringProperty generateTabularPath(String inputName, String outputPath, TaskInstance instance, String extension) {
        System.out.println("generateTabularPath");
        System.out.println(outputPath);
        System.out.println(inputName);
        System.out.println(generateTaskName(instance.getClass()).get());
        String path = outputPath + "/" + inputName + "-" + generateTaskName(instance.getClass()).get() + extension;
        return new SimpleStringProperty(path);

    }

    /**
     *
     * @param aController
     * @return
     */
    public static String generateTapTitle(AnchorPane aController) {
        final String simpleName = aController.getClass().getSimpleName();
        return simpleName.substring(0, simpleName.length() - 10);
    }

}
