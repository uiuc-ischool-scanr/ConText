package context.ui.control.parsetree;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class ParseTreeNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public ParseTreeNextStepsController(String taskname) {
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
