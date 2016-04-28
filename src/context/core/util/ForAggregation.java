package context.core.util;

import java.util.Arrays;

/**
 *
 * @author Aale
 */
public class ForAggregation {

    /**
     */
    public String[] toAggregate;

    /**
     *
     * @param toAggregate
     */
    public ForAggregation(String[] toAggregate) {
        // TODO Auto-generated method stub
        this.toAggregate = toAggregate;

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toAggregate);
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && Arrays.equals(((ForAggregation) object).toAggregate, this.toAggregate)) {
            sameSame = true;
        }

        return sameSame;
    }

}
