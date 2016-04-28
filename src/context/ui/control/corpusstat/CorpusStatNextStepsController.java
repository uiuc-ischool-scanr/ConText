package context.ui.control.corpusstat;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class CorpusStatNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public CorpusStatNextStepsController(String taskname) {
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
