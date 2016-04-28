package context.ui.control.entitynetwork;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 * @author cchin6 (add tabular and file list result)
 */
public class EntityNetworkNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public EntityNetworkNextStepsController(String taskname) {
        super(taskname);
    }

    /**
     *
     */
    @Override
    public void init() {
        addOpenTabularViewNextStepItem();
    	addOpenFileListViewNextStepItem();
        addOpenOutputNextStepItem();
        addReRunTaskNextStepItem();

    }

}
