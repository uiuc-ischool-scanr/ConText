package context.core.task.parsetree;

import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.GenericTask;
import context.core.entity.TaskInstance;
import java.io.File;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class ParseTreeNetworkTask extends CTask {

    /**
     *
     * @param progress
     * @param progressMessage
     */
    public ParseTreeNetworkTask(DoubleProperty progress, StringProperty progressMessage) {
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
        System.out.println("starting Deep Parsing Network Generation");
        task.progress(1, 20, "Starting Deep Parsing Network Generation");
        ParseTreeNetworkTaskInstance ins = (ParseTreeNetworkTaskInstance) instance;
        CorpusData inputCorpus = (CorpusData) ins.getInput();
        task.progress(2, 20, "Loading " + inputCorpus.getPath().get());
        inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));
        task.progress(4, 20, inputCorpus.getFiles().size() + " files loaded");
//        SyntacticNetwork sn = new SyntacticNetwork(ins);
        SyntaxDeepCorpus ssc = new SyntaxDeepCorpus(ins);

        System.out.println("Files:" + inputCorpus.getFiles());

        ssc.setTabularOutPath(ins.getTabularOutput(0).getPath().get());
        
        final String path = ins.getOutputDir();
        System.out.println("Output Directory:" + path);

        task.progress(10, 20, "Deep Parsing Network Generation...");
        ssc.genStreamsFromCorpus();
        //Run corpus statistics
//        ins.setDeepParse(true); // JUST A PLACE HOLDER TO TEST THE CODE!!!! SHOULD BE USER DEFINED!!!
//        if (ins.getDeepParse()) {
//            if (!sn.genNetwork()) {
//                System.out.println("Error in deep parse detection");
//                return instance;
//            }
//
//        } else {
//            if (!sn.genPOSNetwork()) {
//                System.out.println("Error in pos parse detection");
//                return instance;
//            }
//        }
        task.progress(17, 20, "Saving network...");
        ssc.saveNetworks(path);
        //Write the output to CSV
        // Need the selected output File path name !!!
        System.out.println("process done");

//        sn.extractGephiOutput(filpath);
        System.out.println("writing done");
        
        task.progress(20, 20, "Deep Parsing Network Generation successfully done");

        return ins;
    }

}
