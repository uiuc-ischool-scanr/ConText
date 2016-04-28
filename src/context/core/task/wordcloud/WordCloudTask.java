package context.core.task.wordcloud;

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
public class WordCloudTask extends CTask {

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
    public WordCloudTask(DoubleProperty progress, StringProperty progressMessage) {
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
        task.progress(1, 20, "Starting Word Cloud Process");

        WordCloudTaskInstance ins = (WordCloudTaskInstance) instance;
        ins.setPipeline(pipeline);
        CorpusData inputCorpus = (CorpusData) ins.getInput();
        task.progress(5, 20, "Loading " + ins.getInput().getPath().get());
        inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));
        task.progress(8, 20, inputCorpus.getFiles().size() + " files loaded");

        ClusteredWordCloud wc = new ClusteredWordCloud(ins);

        //Run corpus statistics
        task.progress(11, 20, "Running Word Cloud...");
        if (!wc.genCloud()) {
            System.out.println("Error in cloud generation");
            return instance;
        }

        //Write the output to CSV
        // Need the selected output File path name !!!
        final String path = ins.getTextOutput().getPath().get();
        task.progress(15, 20, "Saving result in " + path);
        if (!wc.writeOutput(path)) {
            System.out.println("Error in writing cloud to file");
            return instance;
        }
        task.progress(20, 20, "Results saved successfully");
        task.done();

        return ins;
    }

}
