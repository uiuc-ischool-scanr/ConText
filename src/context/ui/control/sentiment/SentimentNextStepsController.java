package context.ui.control.sentiment;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class SentimentNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public SentimentNextStepsController(String taskname) {
        super(taskname);
    }

    /**
     *
     */
    @Override
    public void init() {
        addOpenTabularViewNextStepItem();
        addOpenOutputNextStepItem();
        addReRunTaskNextStepItem();
    }

}
