package context.ui.control.entitydetection;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 * @author cchin6 (add File List output)
 */
public class EntityDetectionNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public EntityDetectionNextStepsController(String taskname) {
        super(taskname);
    }

    /**
     *
     */
    @Override
    public void init() {
        if (isTextOutput()) {
            addOpenFileListViewNextStepItem();
        }
        addOpenTabularViewNextStepItem();
        addOpenOutputNextStepItem();
        addReRunTaskNextStepItem();
    }

}
