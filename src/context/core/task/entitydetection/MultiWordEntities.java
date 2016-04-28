package context.core.task.entitydetection;

import java.util.List;

import context.core.util.ForAggregation;

/**
 *
 * @author Aale
 */
public class MultiWordEntities {

    /**
     */
    public final List<ForAggregation> forAgg;

    /**
     *
     */
    public final List<Integer> startInd;

    /**
     *
     * @param forAgg
     * @param startInd
     */
    public MultiWordEntities(List<ForAggregation> forAgg, List<Integer> startInd) {
        // TODO Auto-generated method stub
        this.forAgg = forAgg;
        this.startInd = startInd;

    }

}
