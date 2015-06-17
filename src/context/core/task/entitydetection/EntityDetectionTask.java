package context.core.task.entitydetection;

import static context.app.AppConfig.getUserDirLoc;
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

    private static CRFClassifier classifier3;
    private static CRFClassifier classifier7;
    private static CRFClassifier classifier4;

    static {
        try {
            //TODO: for using STANFORD NER these should be uncomment!
            ContextFXController.appendLog("Loading classifier... (it takes times for first usage of entity detection process, please wait)");
            File f3 = new File(getUserDirLoc() + "/data/Classifiers/english.all.3class.distsim.crf.ser.gz");
            classifier3 = CRFClassifier.getClassifier(f3);
//            classifier3 = CRFClassifier.getClassifier("edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");
            ContextFXController.appendLog("Classfier #1 loaded.");
            File f7 = new File(getUserDirLoc() + "/data/Classifiers/english.muc.7class.distsim.crf.ser.gz");
            classifier7 = CRFClassifier.getClassifier(f7);
//            classifier7 = CRFClassifier.getClassifier("edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz");
            ContextFXController.appendLog("Classfier #2 loaded.");
            File f4 = new File(getUserDirLoc() + "/data/Classifiers/english.conll.4class.distsim.crf.ser.gz");
            classifier4 = CRFClassifier.getClassifier(f4);
//            classifier4 = CRFClassifier.getClassifier("edu/stanford/nlp/models/ner/english.conll.4class.distsim.crf.ser.gz");
            ContextFXController.appendLog("All classifiers loaded successfully.");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ClassCastException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

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

            ins.set3Classifier(classifier3);
            ins.set4Classifier(classifier4);
            ins.set7Classifier(classifier7);

            EntityDetectionBody edb = new EntityDetectionBody(ins);

            task.progress(10, 20, "Running Entity Detection...");
            //Run entity Detection
            if (!edb.detectEntities()) {
                System.out.println("Error in detection");
                return instance;
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
