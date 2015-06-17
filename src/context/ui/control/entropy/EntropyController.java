package context.ui.control.entropy;

import context.app.ProjectManager;
import context.core.entity.*;

import context.core.task.entropy.EntropyTask;
import context.core.task.entropy.EntropyTaskInstance;
import context.ui.control.entropy.EntropyController;
import context.ui.control.entropy.EntropyNextStepsController;
import context.ui.control.workflow.basic2step.Basic2StepWorkflowController;
import context.ui.misc.NamingPolicy;
import javafx.beans.property.StringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author cchin6
 */
public class EntropyController extends Basic2StepWorkflowController {

    /**
     *
     */
    public EntropyController() {
        super();
        setTaskname(NamingPolicy.generateTaskName(EntropyTaskInstance.class));

        setTaskInstance(new EntropyTaskInstance(getTaskname()));
        super.postInitialize();
    }

    /**
     *
     * @param taskInstance
     */
    public EntropyController(EntropyTaskInstance taskInstance) {
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

        EntropyTaskInstance instance = (EntropyTaskInstance) getTaskInstance();
        StringProperty inputPath = basicInputViewController.getSelectedItemLabel().textProperty();
        StringProperty inputname = basicInputViewController.getSelectedCorpusName();
        CorpusData input = new CorpusData(inputname, inputPath);
        input.setId(basicInputViewController.getSelectedInput().getId());
        instance.setInput(input);

        final StringProperty outputPath = basicOutputViewController.getOutputDirTextField().textProperty();

        TabularData tabularData = new TabularData(NamingPolicy.generateTabularName(inputname.get(), outputPath.get(), instance), NamingPolicy.generateTabularPath(inputname.get(), outputPath.get(), instance));
        instance.setTabularOutput(tabularData, 0);

        System.out.println(instance);
        CTask task = new EntropyTask(this.getProgress(), this.getProgressMessage());
        task.setTaskInstance(instance);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                activeAllControls();
                nextStepsViewController = new EntropyNextStepsController(NamingPolicy.generateTaskName(EntropyTaskInstance.class).get());
                nextStepsViewController.setParent(EntropyController.this);
                nextStepsViewController.setOutputDir(outputPath.get());
                nextStepsViewController.setTabular(getTaskInstance().getTabularOutput(0));
                nextStepsViewController.init();
                System.out.println("result path=" + getTaskInstance().getTabularOutput(0).getPath());
                ProjectManager.getThisProject().addResult(getTaskInstance().getTabularOutput(0));
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
