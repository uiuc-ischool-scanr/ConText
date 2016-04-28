package context.core.task.entitynetwork;

import static context.app.AppConfig.getUserDirLoc;
import java.io.File;
import java.util.Properties;

import context.app.main.ContextFXController;
import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.GenericTask;
import context.core.entity.TaskInstance;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.IOException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import org.openide.util.Exceptions;

/**
 *
 * @author Aale
 */
public class EntityNetworkTask extends CTask {

    private static CRFClassifier classifier3;
    private static CRFClassifier classifier7;
    private static CRFClassifier classifier4;
    private static StanfordCoreNLP pipeline;

    static {
        try {
            ContextFXController.appendLog("Loading classifier... (it takes times for first usage of entity detection process, please wait)");
            File f3 = new File(getUserDirLoc() + "/data/Classifiers/english.all.3class.distsim.crf.ser.gz");
            classifier3 = CRFClassifier.getClassifier(f3);
            File f7 = new File(getUserDirLoc() + "/data/Classifiers/english.muc.7class.distsim.crf.ser.gz");
            classifier7 = CRFClassifier.getClassifier(f7);
            File f4 = new File(getUserDirLoc() + "/data/Classifiers/english.conll.4class.distsim.crf.ser.gz");
            classifier4 = CRFClassifier.getClassifier(f4);

//        classifier3 = CRFClassifier.getClassifierNoExceptions("edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");
            //	        ContextFXController.appendLog("Classfier #1 loaded.");
//        classifier7 = CRFClassifier.getClassifierNoExceptions("edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz");
            //	        ContextFXController.appendLog("Classfier #2 loaded.");
//        classifier4 = CRFClassifier.getClassifierNoExceptions("edu/stanford/nlp/models/ner/english.conll.4class.distsim.crf.ser.gz");
            ContextFXController.appendLog("All classifiers loaded successfully.");

            Properties props = new Properties();
            props.put("annotators", "tokenize, ssplit");
            pipeline = new StanfordCoreNLP(props);
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
        System.out.println("starting corpus statistics application");

        EntityNetworkTaskInstance ins = (EntityNetworkTaskInstance) instance;
        task.progress(3, 20, "Loading classifiers...");

        ins.set3Classifier(classifier3);
        ins.set4Classifier(classifier4);
        ins.set7Classifier(classifier7);
        ins.setPipeline(pipeline);
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
    }

}
