package context.core.task.sentiment;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.GenericTask;
import context.core.entity.TaskInstance;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class SentimentTask extends CTask {

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
    public SentimentTask(DoubleProperty progress, StringProperty progressMessage) {
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
        task.progress(1, 20, "Starting Sentiment Analysis process");
        SentimentTaskInstance ins = (SentimentTaskInstance) instance;
        ins.setPipeline(pipeline);
        CorpusData inputCorpus = (CorpusData) ins.getInput();
        task.progress(4, 20, "Loading " + inputCorpus.getPath().get());
        inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));
        task.progress(10, 20, inputCorpus.getFiles().size() + " files loaded");

        SentimentBody sb = new SentimentBody(ins);

        task.progress(11, 20, "Running Sentiment Analysis...");
        //Run corpus statistics
        try {
			if (!sb.RunSentimentAnalysis()) {
			    System.out.println("Error in detection");
			    return instance;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //Write the output to CSV
        // Need the selected output File path name !!!
        String filepath = ins.getTabularOutput(0).getPath().get();
        task.progress(17, 20, "Saving results in " + filepath);
        sb.writeOutput(filepath);
        task.progress(20, 20, "Sentiment Analysis successfully done");

        task.done();
        return ins;
    }

}
