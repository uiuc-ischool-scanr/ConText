package context.ui.control.pos;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class POSNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public POSNextStepsController(String taskname) {
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
