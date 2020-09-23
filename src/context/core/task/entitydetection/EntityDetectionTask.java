package context.core.task.entitydetection;

import static context.app.AppConfig.getUserDirLoc;
import context.app.main.ContextFX;
import context.app.main.ContextFXController;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.GenericTask;
import context.core.entity.TaskInstance;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import org.openide.util.Exceptions;

/**
 *
 * @author Aale
 */
public class EntityDetectionTask extends CTask {

    //Classifiers are loaded from EntityDetectionClassifier.java
	
    
    /**
     *
     * @param progress
     * @param progressMessage
     */
    public EntityDetectionTask(DoubleProperty progress, StringProperty progressMessage) {
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
        //throw new UnsupportedOperationException("Not supported yet.");
        task.progress(1, 20, "Starting Entity Detection process");

        EntityDetectionTaskInstance ins = (EntityDetectionTaskInstance) instance;
        // not sure if you want to change how we call up the file, this is how it was done
        // in the previous codebase.
        //ins.setStopWordFile(new File(System.getProperty("user.dir") + "/data/Stoplists/stop.txt"));
        CorpusData inputCorpus = (CorpusData) ins.getInput();

        task.progress(3, 20, "Loading " + ins.getInput().getPath().get());
        inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));

        task.progress(5, 20, inputCorpus.getFiles().size() + " files loaded");

        if (ins.getModel() == 0) { // Stanford NER
            task.progress(8, 20, "Loading classifiers...");

            ins.set3Classifier(EntityDetectionClassifier.getInstance().classifier3);
            ins.set4Classifier(EntityDetectionClassifier.getInstance().classifier4);
            ins.set7Classifier(EntityDetectionClassifier.getInstance().classifier7);

            EntityDetectionBody edb = new EntityDetectionBody(ins);

            task.progress(10, 20, "Running Entity Detection...");
            //Run entity Detection
            if (!edb.detectEntities()) {
                System.out.println("Error in detection");
                return ins;
            }

            //Write the output to CSV
            final String path = ins.getTabularOutput(0).getPath().get();
            task.progress(14, 20, "Saving results in " + path);
            edb.writeOutput(path);
        }
        task.progress(20, 20, "Results saved successfully");
        task.done();
        return ins;
    }

}
