package context.ui.control.lexisnexisparse;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class LexisNexisParseNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public LexisNexisParseNextStepsController(String taskname) {
        super(taskname);
    }

    /**
     *
     */
    @Override
    public void init() {
        addOpenOutputNextStepItem();
        addReRunTaskNextStepItem();
    }

}
