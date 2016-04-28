package context.core.task.keyword;

import context.core.entity.FileData;
import context.core.entity.TaskInstance;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Aale
 */
public class KeywordTaskInstance extends TaskInstance {

    FileData keywordFile;
    Integer leftBound;
    Integer rightBound;

    /**
     *
     * @param name
     */
    public KeywordTaskInstance(StringProperty name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public FileData getKeywordFile() {
        return keywordFile;
    }

    /**
     *
     * @return
     */
    public Integer getLeftBound() {
        return leftBound;
    }

    /**
     *
     * @return
     */
    public Integer getRightBound() {
        return rightBound;
    }

    /**
     *
     * @param fileData
     */
    public void setKeywordFile(FileData fileData) {
        this.keywordFile = fileData;
    }

    /**
     *
     * @param leftBound
     */
    public void setLeftBound(Integer leftBound) {
        this.leftBound = leftBound;
    }

    /**
     *
     * @param rightBound
     */
    public void setRightBound(Integer rightBound) {
        this.rightBound = rightBound;
    }

}
