package context.ui.control.parsetree;

import context.app.ProjectManager;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.TabularData;
import context.core.task.parsetree.ParseTreeNetworkTask;
import context.core.task.parsetree.ParseTreeNetworkTaskInstance;
import context.ui.control.workflow.basic.BasicWorkflowController;
import context.ui.misc.NamingPolicy;
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
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class ParseTreeController extends BasicWorkflowController {
    
    /**
     *
     */
    public ParseTreeController() {
        super();
        try {
            setTaskname(NamingPolicy.generateTaskName(ParseTreeNetworkTaskInstance.class));
            setTaskInstance(new ParseTreeNetworkTaskInstance(getTaskname()));
            
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource(ParseTreeConfigurationController.path));
            Parent s2Content = (Parent) loader2.load();
            configurationController = (ParseTreeConfigurationController) loader2.getController();
            
            setStep2Content(s2Content);
            
            super.postInitialize();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    /**
     *
     * @param taskInstance
     */
    public ParseTreeController(ParseTreeNetworkTaskInstance taskInstance) {
        this();
        initializeTaskToGUIControls(taskInstance);
    }
    
    /**
     *
     * @param event
     */
    @Override
    public void handleStep3RunButton(ActionEvent event) {
        if (!this.validateInputOutput()) {
            return;
        }
        
        ParseTreeConfigurationController confController = (ParseTreeConfigurationController) configurationController;
        ParseTreeNetworkTaskInstance instance = (ParseTreeNetworkTaskInstance) getTaskInstance();
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        CorpusData input = new CorpusData(inputname, inputPath);
        input.setId(basicInputViewController.getSelectedInput().getId());
//        instance.setInput((DataElement) basicInputViewController.getSelectedInput().clone());
        instance.setInput(input);
        instance.setSelectedTypes(confController.getFilteredLabels());
        instance.setAggregation(confController.getAggregationType());
        instance.setAdvance(confController.isAdvanced());
        
        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();
        instance.setOutputDir(outputPath.get());

//        CorpusData output = new CorpusData(NamingPolicy.generateOutputName(inputPath.get(), outputPath.get(), instance), outputPath);
//        instance.setTextOutput(output);
        TabularData tabularData = new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance),
                NamingPolicy.generateTabularPath(inputname.get() + "-PTN", outputPath.get(), instance, ".csv"));
        instance.setTabularOutput(tabularData, 0);

        /*final String subdirectory = outputPath.get()+"/SB-Results/";
         FileHandler.createDirectory(subdirectory);
         System.out.println("Created sub dir: "+subdirectory);
         FileList output=new FileList(NamingPolicy.generateOutputName(inputname.get(), outputPath.get(), instance),subdirectory);
         instance.setTextOutput(output);*/
        //TODO: set other parameters here
//        instance.setUnitOfAnalysis(confController.getUnitOfAnalysis());
        System.out.println(instance);
        CTask task = new ParseTreeNetworkTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new ParseTreeNextStepsController(NamingPolicy.generateTaskName(ParseTreeNetworkTaskInstance.class).get());
                nextStepsViewController.setParent(ParseTreeController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                nextStepsViewController.setTabular(getTaskInstance().getTabularOutput(0));
                //nextStepsViewController.setFilelist((FileList)getTaskInstance().getTextOutput());
                nextStepsViewController.init();
                ProjectManager.getThisProject().addResult(getTaskInstance().getTabularOutput(0));
                //ProjectManager.getThisProject().addData(getTaskInstance().getTextOutput());
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
