package context.ui.control.codebook;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class CodebookAppNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public CodebookAppNextStepsController(String taskname) {
        super(taskname);
    }

    /**
     *
     */
    @Override
    public void init() {
        addOpenFileListViewNextStepItem();
        addOpenOutputNextStepItem();
        addReRunTaskNextStepItem();
    }

}
