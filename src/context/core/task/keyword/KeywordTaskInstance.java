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
    Boolean omit_case;

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
    
    public Boolean getOmitCase() {
        return omit_case;
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
    
    public void setOmitCase(Boolean omit_case) {
        this.omit_case = omit_case;
    }

}
