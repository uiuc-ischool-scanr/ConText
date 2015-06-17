package context.ui.control.topicmodeling;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class TopicModelingNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public TopicModelingNextStepsController(String taskname) {
        super(taskname);
    }

    /**
     *
     */
    @Override
    public void init() {
        addOpenTabularViewNextStepItem("nextstep.tabularview.topicmodeling.item1.label", 0, true);
        addOpenTabularViewNextStepItem("nextstep.tabularview.topicmodeling.item2.label", 1, true);
        addOpenOutputNextStepItem();
        addReRunTaskNextStepItem();
    }

}
