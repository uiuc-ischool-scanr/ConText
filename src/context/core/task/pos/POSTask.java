package context.core.task.pos;

import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.GenericTask;
import context.core.entity.TaskInstance;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.File;
import java.util.Properties;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class POSTask extends CTask {

    private static StanfordCoreNLP pipeline;

    static {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit");
        pipeline = new StanfordCoreNLP(props);

    }

    /**
     *
     * @param progress
     * @param progressMessage
     */
    public POSTask(DoubleProperty progress, StringProperty progressMessage) {
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
        task.progress(1, 20, "Starting Part of Speech Tagging process");

        POSTaskInstance ins = (POSTaskInstance) instance;
        ins.setPipeline(pipeline);
        CorpusData inputCorpus = (CorpusData) ins.getInput();
        task.progress(4, 20, "Loading " + inputCorpus.getPath().get());

        inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));

        task.progress(8, 20, inputCorpus.getFiles().size() + " files loaded");
        POSBody pb = new POSBody(ins);

        task.progress(10, 20, "Running Part of Speech Tagging...");
        //Run corpus statistics
        if (!pb.tagPOS()) {
            System.out.println("Error in detection");
            return instance;
        }

        //Write the output to CSV
        final String filepath = ins.getTabularOutput(0).getPath().get();
        task.progress(17, 20, "Saving results in " + filepath);
        pb.writeOutput(filepath);
        task.progress(20, 20, "Results saved successfully");
        task.done();
        return ins;
    }

}
