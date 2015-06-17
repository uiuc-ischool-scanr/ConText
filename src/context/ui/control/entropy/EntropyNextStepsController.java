package context.ui.control.entropy;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author cchin6
 */

public class EntropyNextStepsController extends NextStepsViewController{

    /**
     *
     * @param taskname
     */
    public EntropyNextStepsController(String taskname){
		super(taskname);
	}
	
    /**
     *
     */
    @Override
	public void init(){
		addOpenTabularViewNextStepItem(false);
		addOpenOutputNextStepItem();
		addReRunTaskNextStepItem();
	}

}
