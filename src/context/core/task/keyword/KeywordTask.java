package context.core.task.keyword;

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
public class KeywordTask extends CTask {

    /**
     *
     * @param progress
     * @param progressMessage
     */
    public KeywordTask(DoubleProperty progress, StringProperty progressMessage) {
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
        task.progress(1, 20, "Starting Keyword in Context process");

        KeywordTaskInstance ins = (KeywordTaskInstance) instance;
        CorpusData inputCorpus = (CorpusData) ins.getInput();
        task.progress(4, 20, "Loading " + inputCorpus.getPath().get());
        inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));
        task.progress(8, 20, inputCorpus.getFiles().size() + " files loaded");

        KeywordInContext kwic = new KeywordInContext(ins);
        task.progress(10, 20, "Running Keyword in Context...");
        if (kwic.removeOthers()) {
            task.progress(20, 20, "Keyword in Context successfully done");
            task.done();
            return ins;
        } else {
            System.out.println("Failed to run the job");
            return instance;
        }
    }

}
