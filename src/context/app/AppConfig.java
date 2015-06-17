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

import context.core.entity.CTask;
import context.core.task.codebook.CodebookApplicationTask;
import context.core.task.corpusstat.CorpusStatTask;
import context.core.task.entitydetection.EntityDetectionTask;
import context.core.task.lexisnexis.LexisNexisNetworkGenerationTask;
import context.core.task.lexisnexis.LexisNexisParseTask;
import context.core.task.pos.POSTask;
import context.core.task.removestopword.RemoveStopwordsTask;
import context.core.task.sentiment.SentimentTask;
import context.core.task.syntaxbased.SyntaxBasedTask;
import context.core.task.topicmodeling.TopicModelingTask;
import context.core.task.wordcloud.WordCloudTask;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class AppConfig {

    private static Properties prop;

    /**
     *
     */
    public static String defaultStopwordFileLocation = getUserDirLoc() + "/data/Stoplists/stop.txt";

    /**
     *
     */
    public static String defaultDeleteListFileLocation = getUserDirLoc() + "/data/Stoplists/en.txt";

    /**
     *
     */
    public static String defaultSentimentFileLocation = getUserDirLoc() + "/data/Sentiment/sentiment_dic.csv";

    /**
     *
     */
    public static String defaultSentimentWordCloudFileLocation = getUserDirLoc() + "/data/Sentiment/sentiment.tff";

    /**
     *
     */
    public static String defaultOpenLocation = getUserDirLoc();

    private static String filePath = "/resources/config.properties";

    private static String userDirLoc;

    /**
     *
     * @return
     */
    public static String getUserDirLoc() {
        if (userDirLoc == null) {
            userDirLoc = getBestUserDirLoc();
        }
        return userDirLoc;
    }

    private static String getBestUserDirLoc() {
        String aPossibleFile = System.getProperty("user.dir") + "/data/Stoplists/stop.txt";
        if (fileExist(aPossibleFile)) {
            System.out.println("11111111111111111111");
            return System.getProperty("user.dir");
        } else {
            System.out.println("not exist (1) : " + aPossibleFile);
        }
        String userDirForMacDeveloper = getUserDirLocForMacDeveloper();
        aPossibleFile = userDirForMacDeveloper + "/data/Stoplists/stop.txt";
        if (fileExist(aPossibleFile)) {
            System.out.println("222222222222222222222");
            return userDirForMacDeveloper;
        } else {
            System.out.println("not exist (2) : " + aPossibleFile);
        }
        String userDirLocForInstaller = getUserDirLocForInstaller();
        aPossibleFile = userDirLocForInstaller + "/data/Stoplists/stop.txt";
        if (fileExist(aPossibleFile)) {
            System.out.println("33333333333333333333");
            return userDirLocForInstaller;
        } else {
            System.out.println("not exist (3) : " + aPossibleFile);
        }
        System.out.println("44444444444444444");
        return System.getProperty("user.dir");
    }

    private static String getUserDirLocForMacDeveloper() {
        String path = AppConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String result = null;
        int prefix_index = path.indexOf("Context-FX.jar");
        if (prefix_index != -1) {
            path = path.substring(0, prefix_index - 6);
            final File f = new File(path);
            result = f.getAbsolutePath();
        } else {
            System.out.println("Context-FX.jar not found!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        return result;
    }

    private static String getUserDirLocForInstaller() {
        String path = AppConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String result = null;
        int prefix_index = path.indexOf("Context-FX.jar");
        if (prefix_index != -1) {
            path = path.substring(0, prefix_index);
            final File f = new File(path);
            result = f.getAbsolutePath();
        } else {
            System.out.println("Context-FX.jar not found!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        return result;
    }

    private static boolean fileExist(String filepath) {
        File f = new File(filepath);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;

    }
    static List<Class<? extends CTask>> classes = Arrays.asList(CodebookApplicationTask.class,
            CorpusStatTask.class, EntityDetectionTask.class, LexisNexisNetworkGenerationTask.class,
            LexisNexisParseTask.class, POSTask.class, RemoveStopwordsTask.class, SentimentTask.class, SyntaxBasedTask.class,
            TopicModelingTask.class, WordCloudTask.class
    );

    /**
     *
     * @param taskname
     * @return
     */
    public static String getTaskLabel(String taskname) {
        String label = getProperty("task." + taskname + ".label");
        if (label != null) {
            return label;
        }
        return taskname;
    }

    /**
     *
     * @param taskname
     * @return
     */
    public static String getTaskHelpguideURL(String taskname) {
        return getProperty("task." + taskname + ".help.url");
    }

    /**
     *
     * @param taskname
     * @return
     */
    public static String getTaskAbbr(String taskname) {
        return getProperty("task." + taskname + ".abbr");
    }

    /**
     *
     * @param propety
     * @return
     */
    public static String getLabel(String propety) {
        return getProperty(propety);
    }

    /**
     *
     * @param propery
     * @return
     */
    public static String getProperty(String propery) {
        if (prop == null) {
            load();
        }
        propery = propery.toLowerCase();
        if (prop.containsKey(propery)) {
            return prop.getProperty(propery);
        } else {
            System.out.println("Properties file is not contain property=" + propery);
            return null;
        }
    }

    /**
     *
     */
    public static void load() {
        prop = new Properties();

        InputStream in = AppConfig.class.getResourceAsStream(filePath);
        System.out.println("properties file load from: " + filePath);
        try {
            //File file = new File(filePath);

            //load a properties file
            //prop.load(new FileInputStream(file));
            prop.load(in);

            //get the property value and print it out
//            this.table = prop.getProperty(PROP_TABLE);
//            String fieldsStr = prop.getProperty(PROB_FIELDS);
            System.out.println("properties loaded from " + filePath);
            System.out.println("Properties:" + prop.stringPropertyNames());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //WARNING: do not run this method, it override properties file!
    /**
     *
     */
    public static void save() {
        if (1 == 1) {
            return;
        }
        try {
            File file = new File(filePath);
            System.out.println("properties file:" + file.getAbsolutePath());
            FileWriter fwriter = new FileWriter(file);
//            prop.store(new FileOutputStream(file), "application configuration");
            for (Class c : classes) {

                String name = c.getSimpleName().substring(0, c.getSimpleName().length() - 4).toLowerCase();
                fwriter.write("# task " + name + " properties\n");
                fwriter.write("task." + name + ".label=" + name + "\n");
                fwriter.write("task." + name + ".abbr=" + name + "\n");
                fwriter.write("task." + name + ".help.url=" + "/resources/helpguide/" + name + ".html" + "\n");
                fwriter.write("\n");

            }
            fwriter.close();
            System.out.println("Properties file saved in " + filePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
//        save();
        System.out.println(getBestUserDirLoc());
    }
}
