package context.ui.control.wordcloud;

import context.ui.control.nextsteps.NextStepsViewController;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class WordCloudNextStepsController extends NextStepsViewController {

    /**
     *
     * @param taskname
     */
    public WordCloudNextStepsController(String taskname) {
        super(taskname);
    }

    /**
     *
     */
    @Override
    public void init() {
//        addOpenTabularViewNextStepItem();
        addOpenWebViewNextStepItem();
        addOpenOutputNextStepItem();
        addReRunTaskNextStepItem();
    }

    

}
