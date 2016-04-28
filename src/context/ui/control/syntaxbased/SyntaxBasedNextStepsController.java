package context.ui.control.syntaxbased;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class SyntaxBasedNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public SyntaxBasedNextStepsController(String taskname) {
        super(taskname);
    }

    /**
     *
     */
    @Override
    public void init() {
        addOpenTabularViewNextStepItem();
        //addOpenFileListViewNextStepItem();
        addOpenOutputNextStepItem();
        addReRunTaskNextStepItem();

    }

}
