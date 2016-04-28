package context.ui.control.bigram;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 * @author cchin6 - modified on Nov 7, 2014; change output UI from text files to tabular data output
 */
public class BigramNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public BigramNextStepsController(String taskname) {
        super(taskname);
    }

    /**
     *
     */
    @Override
    public void init() {
    	addOpenTabularViewNextStepItem(false);
        addOpenOutputNextStepItem();
        addReRunTaskNextStepItem();
    }

}
