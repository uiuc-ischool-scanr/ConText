package context.core.task.entropy;

import context.core.entity.TaskInstance;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class EntropyTaskInstance extends TaskInstance{

	StanfordCoreNLP pipeline;
	
    /**
     *
     * @param name
     */
    public EntropyTaskInstance(StringProperty name){		
		super(name);
	}
	
    /**
     *
     * @param pipeline
     */
    public void setPipeline(StanfordCoreNLP pipeline){
		this.pipeline=pipeline;
	}
	
    /**
     *
     * @return
     */
    public StanfordCoreNLP getPipeline(){
		return pipeline;
		
	}
}
