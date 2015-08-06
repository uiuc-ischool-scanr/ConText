package context.core.task.topicmodeling;

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
public class TopicModelingTask extends CTask {

    /**
     *
     * @param progress
     * @param progressMessage
     */
    public TopicModelingTask(DoubleProperty progress, StringProperty progressMessage) {
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
        task.progress(1, 20, "Starting Topic Modeling process");

        TopicModelingTaskInstance ins = (TopicModelingTaskInstance) instance;

        CorpusData inputCorpus = (CorpusData) ins.getInput();
        task.progress(4, 20, "Loading " + inputCorpus.getPath().get());
        inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));
        task.progress(8, 20, inputCorpus.getFiles().size() + " files loaded");
        TopicModelingBody tmb = new TopicModelingBody(ins);

        task.progress(11, 20, "Running Topic Modeling...");
        //Run topic modeling
        if (!tmb.ModelTopic()) {
            System.out.println("Error in modeling");
            return instance;
        }

        //Write the output to CSV
        String filepath1 = ins.getTabularOutput(0).getPath().get();
        String filepath2 = ins.getTabularOutput(1).getPath().get();
        task.progress(17, 20, "Saving results in " + filepath1 + " and " + filepath2);
        tmb.writeOutput(filepath1, filepath2);
        task.progress(20, 20, "Results saved successfully");

        task.done();
        return ins;
    }

}
