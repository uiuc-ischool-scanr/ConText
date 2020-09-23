package context.ui.control.csvparser;

import context.app.ProjectManager;
import context.core.entity.*;
import context.core.task.csvparser.CsvParserTask;
import context.core.task.csvparser.CsvParserTaskInstance;
import context.ui.control.csvparser.CsvParserConfigurationController;
import context.ui.control.csvparser.CsvParserController;
import context.ui.control.csvparser.CsvParserNextStepsController;
import context.ui.control.workflow.basic.BasicWorkflowController;
import context.ui.misc.NamingPolicy;
import context.ui.misc.FileHandler;

import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import org.openide.util.Exceptions;

/**
 *
 * @author Aale
 */
public class CsvParserController extends BasicWorkflowController {

    /**
     *
     */
    public CsvParserController(){
		super();
		try{
			setTaskname(NamingPolicy.generateTaskName(CsvParserTaskInstance.class));
			setTaskInstance(new CsvParserTaskInstance(getTaskname()));
			
			FXMLLoader loader2 = new FXMLLoader(getClass().getResource(CsvParserConfigurationController.path));
			Parent s2Content=(Parent)loader2.load();
			configurationController=(CsvParserConfigurationController) loader2.getController();
			
			setStep2Content(s2Content);
			
			super.postInitialize();
			
			
			
		}catch (IOException ex){
			Exceptions.printStackTrace(ex);
		}
	}
	
    /**
     *
     * @param taskInstance
     */
    public CsvParserController(CsvParserTaskInstance taskInstance){
		this();
		initializeTaskToGUIControls(taskInstance);
	}
	
    /**
     *
     * @param event
     */
    public void handleStep1NextButton(ActionEvent event){
		super.handleStep1NextButton(event);
		
		//Set Input Folder for ConfigurationController
		CsvParserConfigurationController confController = (CsvParserConfigurationController) configurationController;
		
		StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
		//StringProperty inputname=basicInputViewController.getSelectedCorpusName();
		
		
		confController.setInputPath(inputPath.getValue());
		
		/*CsvParserTaskInstance instance = (CsvParserTaskInstance) getTaskInstance();
		CsvParserConfigurationController confController = (CsvParserConfigurationController) configurationController;
		
		StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        CorpusData input = new CorpusData(inputname, inputPath);
        input.setId(basicInputViewController.getSelectedInput().getId());

        instance.setInput(input);*/
		
	}
	
    /**
     *
     * @param event
     */
    @Override
	public void handleStep3RunButton(ActionEvent event){
		if(!this.validateInputOutput()){
			return;
		}
		
		CsvParserTaskInstance instance = (CsvParserTaskInstance) getTaskInstance();
		CsvParserConfigurationController confController = (CsvParserConfigurationController) configurationController;
		
		StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        CorpusData input = new CorpusData(inputname, inputPath);
        /* instance.setDropnum(basicInputViewController.isDropnum());
        instance.setDroppun(basicInputViewController.isDroppun());
        instance.setKeeppou(basicInputViewController.isKeeppou()); */
        input.setId(basicInputViewController.getSelectedInput().getId());

        instance.setInput(input);
        
        instance.setCsvSeparatorIndex(confController.getCsvSeparatorIndex());
        instance.setCsvSeparatorCustomText(confController.getCsvSeparatorCustomText());        
        instance.setCsvSeparatorChar(confController.getCsvSeparatorChar());
        instance.setCsvColumnList(confController.getCsvColumnList());
        instance.setGroupByColumnIndex(confController.getGroupByColumnIndex());
        instance.setTextColumnIndex(confController.getTextColumnIndex());
        instance.setCsvQuoteChar(confController.getCsvQuoteChar());
        instance.setGroupThreshold(confController.getGroupThreshold());
        
        final StringProperty outputPath=basicOutputViewController.getOutputDirTextField().textProperty();
        instance.setOutputDir(outputPath.get());
        
        final String subdirectory=outputPath.get()+"/PC-Results/";
        FileHandler.createDirectory(subdirectory);
        System.out.println("Created sub dir: "+subdirectory);
        
        FileList output = new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance),subdirectory);
        instance.setTextOutput(output);
		
        CTask task = new CsvParserTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new CsvParserNextStepsController(NamingPolicy.generateTaskName(CsvParserTaskInstance.class).get());
                nextStepsViewController.setParent(CsvParserController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                
                nextStepsViewController.setFilelist((FileList) getTaskInstance().getTextOutput());
                nextStepsViewController.init();
                ProjectManager.getThisProject().addResult(getTaskInstance().getTabularOutput(0));
                ProjectManager.getThisProject().addData(getTaskInstance().getTextOutput());
                if (isNew()) {
                    ProjectManager.getThisProject().addTask(getTaskInstance());
                    setNew(false);
                }
                showNextStepPane(nextStepsViewController);
            }
        });
        getProgressBar().setVisible(true);
        deactiveAllControls();
        task.start();

        setTaskInstance(task.getTaskInstance());
        
	}
}
