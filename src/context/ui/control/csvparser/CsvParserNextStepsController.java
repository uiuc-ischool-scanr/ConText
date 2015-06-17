package context.ui.control.csvparser;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Aale
 */
public class CsvParserNextStepsController extends NextStepsViewController {
	
    /**
     *
     * @param taskname
     */
    public CsvParserNextStepsController(String taskname){
		super(taskname);
	} 
	
    /**
     *
     */
    @Override
	public void init(){
		addOpenFileListViewNextStepItem();
		addOpenOutputNextStepItem();
		addReRunTaskNextStepItem();
	} 
}
