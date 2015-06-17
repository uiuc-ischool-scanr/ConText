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
package context.core.task.codebook;

import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.GenericTask;
import context.core.entity.TaskInstance;
import java.io.File;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Kiumars Soltani
 */
public class CodebookApplicationTask extends CTask {

    /**
     *
     * @param progress
     * @param progressMessage
     */
    public CodebookApplicationTask(DoubleProperty progress, StringProperty progressMessage) {
        super(progress, progressMessage);
    }

    /**
     *
     * @param instance
     * @param task
     * @return
     */
    @Override
    public TaskInstance run(TaskInstance instance, GenericTask task) {
        task.progress(1, 20, "Starting Codebook Application...");
        CodebookApplicationTaskInstance ins = (CodebookApplicationTaskInstance) instance;
        CorpusData inputCorpus = (CorpusData) ins.getInput();
        inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));
        Codebook cb = new Codebook(ins);

        task.progress(3, 20, "Loading Codebook...");
        //Load codebook
        if (!cb.loadCodebook()) {
            System.out.println("Error in loading the codebook");
            return instance;
        }
        task.progress(5, 20, "Codebook loaded");

        task.progress(6, 20, "Running Codebook Application...");
        //Run codebook
        if (!cb.applyCodebook()) {
            System.out.println("Error in applying codebook");
            return instance;
        }
        task.progress(13, 20, "Codebook Application successfully Done");

        System.out.println("output type " + ins.getNetOutputType());
        System.out.println("corpus ? " + ins.isNetInputCorpus());

        task.progress(14, 20, "Generating network...");
        if (ins.isNetwork()) {
            NetworkGeneration ng = new NetworkGeneration(cb, ins);
            if (!ng.applyNetwork()) {
                System.out.println("Error in applying network");
            }
        }
        task.progress(20, 20, "Network generated");
        task.done();
//        this.newline();
        return ins;
    }

//    public static void main(String[] args) {
//
//        String INPUTPATH = "C:\\Users\\Administrator\\workspace\\Context-FX\\data\\DarfurCorpus";
//        CorpusData input = new CorpusData(new SimpleStringProperty("input"), new SimpleStringProperty(INPUTPATH));
//        //input.addAllFiles(new File(INPUTPATH));
//
//        String OutputPath = "C:\\Users\\Administrator\\workspace\\Context-FX\\output";
//        CorpusData output = new CorpusData(new SimpleStringProperty("output"), new SimpleStringProperty(OutputPath));
//
//        //String[] headers = {"Network", "Label,Type,Label,Type,Weight"};
//        TabularData[] td = new TabularData[2];
//        td[0] = new TabularData(new SimpleStringProperty("csv"),
//                new SimpleStringProperty("C:\\Users\\Administrator\\workspace\\Context-FX\\output"));
//        td[1] = new TabularData(new SimpleStringProperty("gexf"),
//                new SimpleStringProperty("C:\\Users\\Administrator\\workspace\\Context-FX\\output"));
//
//        String codebookFileaddr = "C:\\Users\\Administrator\\workspace\\Context-FX\\data\\darfur_master_thes.csv";
//
//        CodebookApplicationTaskInstance cbinstance = new CodebookApplicationTaskInstance(new SimpleStringProperty("nn"));
//        cbinstance.setCodebookFile(codebookFileaddr);
//        cbinstance.setDistance(10);
//        cbinstance.setInput(input);
//        cbinstance.setIsDrop(0);
//        cbinstance.setIsNormal(0);
//        cbinstance.setNetInputCorpus(true);
//        cbinstance.setNetOutputCSV(true);
//        cbinstance.setNetOutputGEXF(true);
//        cbinstance.setNetOutputType(1);
//        cbinstance.setNetwork(true);
//        cbinstance.setSeparator(3);
//        cbinstance.setTabularOutput(td);
//        cbinstance.setTextOutput(output);
//
//        //CodebookApplicationTask cbtask = new CodebookApplicationTask();
//        //cbtask.run(cbinstance, null);
//    }
}
