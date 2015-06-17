package context.core.util.comparators;

import java.util.Comparator;

/**
 *
 * @author Kiumars Soltani
 *
 */
public class StringLengthComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        if (o1.length() > o2.length()) {
            return 1;
        } else if (o1.length() < o2.length()) {
            return -1;
        }
        return o1.compareTo(o2);
    }
}
