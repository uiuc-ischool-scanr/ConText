package context.core.util;

import context.core.entity.CorpusData;
import context.core.task.removestopword.RemoveStopwordsTaskInstance;

/**
 *
 * @author Aale
 */
public class KeywordBody {
	private RemoveStopwordsTaskInstance instance;
    private CorpusData input;
    private CorpusData output;
	private String replaceString;
	private String[] keywords;
	
    /**
     *
     * @param instance
     */
    public KeywordBody(RemoveStopwordsTaskInstance instance) {
	        // TODO Auto-generated method stub
	        this.instance = instance;
	        init();
	    }

	    private void init() {

	    	 if (instance.getType() == 1) {
	             this.replaceString = "";
	         } else {
	             this.replaceString = "```";
	         }

	         this.input = (CorpusData) instance.getInput();
	         this.output = (CorpusData) instance.getTextOutput();
	    }

    /**
     *
     * @return
     */
    public boolean applyKeywords(){
	    	return true;
	    }
	    
}
