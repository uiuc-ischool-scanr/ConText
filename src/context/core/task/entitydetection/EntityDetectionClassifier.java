package context.core.task.entitydetection;

import static context.app.AppConfig.getUserDirLoc;
import context.app.main.ContextFX;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.openide.util.Exceptions;

/**
 *
 * @author Aale
 */
public class EntityDetectionClassifier {

    private static EntityDetectionClassifier singleInstance = null;
    public  CRFClassifier classifier3;
    public  CRFClassifier classifier7;
    public  CRFClassifier classifier4;
    public  StanfordCoreNLP pipeline;
        
    public EntityDetectionClassifier() {            
        try {
            //TODO: for using STANFORD NER these should be uncomment!
            ContextFX.appController.appendLog("Loading classifier... (it takes times for first usage of entity detection process, please wait)");
            //ContextFXController.appendLog("Loading classifier... (it takes times for first usage of entity detection process, please wait)");
            File f3 = new File(getUserDirLoc() + "/data/Classifiers/english.all.3class.distsim.crf.ser.gz");
            classifier3 = CRFClassifier.getClassifier(f3);
//            classifier3 = CRFClassifier.getClassifier("edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");
            ContextFX.appController.appendLog("Classfier #1 loaded.");
            //ContextFXController.appendLog("Classfier #1 loaded.");
            File f7 = new File(getUserDirLoc() + "/data/Classifiers/english.muc.7class.distsim.crf.ser.gz");
            classifier7 = CRFClassifier.getClassifier(f7);
//            classifier7 = CRFClassifier.getClassifier("edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz");
            ContextFX.appController.appendLog("Classfier #2 loaded.");
            //ContextFXController.appendLog("Classfier #2 loaded.");
            File f4 = new File(getUserDirLoc() + "/data/Classifiers/english.conll.4class.distsim.crf.ser.gz");
            classifier4 = CRFClassifier.getClassifier(f4);
//            classifier4 = CRFClassifier.getClassifier("edu/stanford/nlp/models/ner/english.conll.4class.distsim.crf.ser.gz");
            ContextFX.appController.appendLog("All classifiers loaded successfully.");
            //ContextFXController.appendLog("All classifiers loaded successfully.");
            
            
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
    
    public static EntityDetectionClassifier getInstance() 
    { 
        if (singleInstance == null) 
            singleInstance = new EntityDetectionClassifier(); 
  
        return singleInstance; 
    } 
    
}
