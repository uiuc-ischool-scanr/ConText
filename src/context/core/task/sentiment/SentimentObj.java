package context.core.task.sentiment;

import java.util.Arrays;

/**
 *
 * @author Aale
 */
public class SentimentObj {

    /**
     */
    protected String[] sentiment;

    /**
     *
     * @param sentiment
     */
    public SentimentObj(String[] sentiment) {
        // TODO Auto-generated method stub
        this.sentiment = sentiment;

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(sentiment);
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && Arrays.equals(((SentimentObj) object).sentiment, this.sentiment)) {
            sameSame = true;
        }

        return sameSame;
    }

}
