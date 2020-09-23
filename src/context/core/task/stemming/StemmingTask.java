package context.core.task.stemming;

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
public class StemmingTask extends CTask {

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
    public StemmingTask(DoubleProperty progress, StringProperty progressMessage) {
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
        task.progress(1, 20, "Starting Lemmatization process");

        StemmingTaskInstance ins = (StemmingTaskInstance) instance;

        ins.setPipeline(pipeline);
        CorpusData inputCorpus = (CorpusData) ins.getInput();
        task.progress(4, 20, "Loading " + inputCorpus.getPath().get());
        inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));
        task.progress(8, 20, inputCorpus.getFiles().size() + " files loaded");
        StemmingBody sb = new StemmingBody(ins);

        task.progress(11, 20, "Running Lemmatization...");
        //Run entity Detection
        if (!sb.StemText()) {
            System.out.println("Error in detection");
            return instance;
        }
        task.progress(20, 20, "Lemmatization successfully done");

        task.done();
        return ins;

    }

}
