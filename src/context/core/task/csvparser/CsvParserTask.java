/**
 * 
 */
package context.core.task.csvparser;

import context.core.entity.CTask;
import context.core.entity.CorpusData;
import context.core.entity.GenericTask;
import context.core.entity.TaskInstance;
import java.io.File;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

/**
 * @author julianchin
 *
 */
public class CsvParserTask extends CTask {
	
    /**
     *
     * @param progress
     * @param progressMessage
     */
    public CsvParserTask(DoubleProperty progress, StringProperty progressMessage){
		super(progress,progressMessage);
	} 
	
    /**
     *
     * @param instance
     * @param task
     * @return
     */
    @Override
	public TaskInstance run(TaskInstance instance,GenericTask task){
		System.out.println("Starting csv parser");
		
		
		CsvParserTaskInstance ins=(CsvParserTaskInstance) instance;
		task.progress(3, 20,"Loading Data...");
		CorpusData inputCorpus=(CorpusData)ins.getInput();
		inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));
		
		task.progress(5, 20,"Reading CSV Files...");
		CsvParserBody cpb = new CsvParserBody(ins);
		cpb.readCsvFiles();
		
		System.out.println("Files:" + inputCorpus.getFiles());
		
		final String path = ins.getOutputDir();
        System.out.println("Output Directory:" + path);
        
        task.progress(10, 20,"Parsing and saving text Files...");
        
		cpb.parseCsvToTextData();
		
		task.progress(20,20,"Done.");
		System.out.println("writing done");
		
		return ins;
	}
}
