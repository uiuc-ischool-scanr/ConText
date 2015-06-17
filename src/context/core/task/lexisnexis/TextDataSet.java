package context.core.task.lexisnexis;

/**
 *
 * @author Jana Diesner
 */
public class TextDataSet {

    String pubDate;
    String title;
    String id;

    /**
     *
     */
    public TextDataSet() {
    }

    /**
     *
     * @return
     */
    public String getCharacteristicString() {
        return (title.trim().toLowerCase() + "--" + pubDate.toLowerCase().trim());
    }
}
