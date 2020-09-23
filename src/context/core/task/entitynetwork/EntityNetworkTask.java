package context.core.task.entitynetwork;

import static context.app.AppConfig.getUserDirLoc;
import context.app.Validation;
import context.app.main.ContextFX;
import java.io.File;
import java.util.Properties;

import context.app.main.ContextFXController;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.GenericTask;
import context.core.entity.TaskInstance;
import context.core.task.entitydetection.EntityDetectionClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.IOException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import org.openide.util.Exceptions;
import org.thehecklers.monologfx.MonologFX;

/**
 *
 * @author Aale
 */
public class EntityNetworkTask extends CTask {

    

    /**
     *
     * @param progress
     * @param progressMessage
     */
    public EntityNetworkTask(DoubleProperty progress, StringProperty progressMessage) {
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
        
        /*
        Notes:
        Add try catch on this multithreading method because
        there is no trace of error if exception happened in one of the function
        be hard to trace error 
        */
        
        try{        
            System.out.println("starting corpus statistics application");

            EntityNetworkTaskInstance ins = (EntityNetworkTaskInstance) instance;
            task.progress(3, 20, "Loading classifiers...");

            if(ins == null){
                return instance;
            }
            
            ins.set3Classifier(EntityDetectionClassifier.getInstance().classifier3);
            ins.set4Classifier(EntityDetectionClassifier.getInstance().classifier4);
            ins.set7Classifier(EntityDetectionClassifier.getInstance().classifier7);
            ins.setPipeline(EntityDetectionClassifier.getInstance().pipeline);
            CorpusData inputCorpus = (CorpusData) ins.getInput();
            inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));
            task.progress(5, 20, "Initializing Data Structures...");
            //EntityNetworkBody enb = new EntityNetworkBody(ins);
            EntityCorpus ec = new EntityCorpus(ins);
            ec.setTabularOutPath(ins.getTabularOutput(0).getPath().get());

            System.out.println("Files:" + inputCorpus.getFiles());

            final String path = ins.getOutputDir();
            System.out.println("Output Directory:" + path);
            //Run corpus statistics

            task.progress(7, 20, "Detecting Entities...");
            /*if (!enb.detectEntities()) {
             System.out.println("Error in detection");
             return instance;
             }*/

            ec.genStreamsFromCorpus();

            task.progress(13, 20, "Generating Network...");
            /*if (!enb.genNetwork()) {
             System.out.println("Error in generation");
             return instance;
             }*/
            ec.saveNetworks(path);

            //Write the output to CSV
            // Need the selected output File path name !!!
            System.out.println("process done");

            /* task.progress(16, 20, "Producing Gephi Files of Network...");
             enb.extractGephiOutput(filpath);*/
            task.progress(20, 20, "Done.");
            System.out.println("writing done");

            return ins;
        }catch(Exception ex){            
            Exceptions.printStackTrace(ex);            
            task.progress(20, 20, "Error on Entity Network process");
            // Return Null if there is error
            return instance;
        }        
    }

}
