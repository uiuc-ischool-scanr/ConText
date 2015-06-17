/**
 * 
 */
package context.core.task.csvparser;

import java.util.*;
import context.core.entity.TaskInstance;
import javafx.beans.property.StringProperty;

/**
 * @author julianchin
 *
 */
public class CsvParserTaskInstance extends TaskInstance {
	
	private int csvSeparatorIndex;
	private String csvSeparatorCustomText;
	private char csvSeparatorChar;
	private char csvQuoteChar;
	private List<String> csvColumnList;
	private int groupByColumnIndex;
	private int textColumnIndex;	
	private int groupThreshold;
	private String outputDir;
	
    /**
     *
     * @param name
     */
    public CsvParserTaskInstance(StringProperty name){
		super(name);
	} 
	
    /**
     *
     * @return
     */
    public synchronized String getOutputDir(){
		return outputDir;		
	}
		
    /**
     *
     * @return
     */
    public int getCsvSeparatorIndex(){
		return this.csvSeparatorIndex;
	}
	
    /**
     *
     * @return
     */
    public String getCsvSeparatorCustomText(){
		return this.csvSeparatorCustomText;
	}
	
    /**
     *
     * @return
     */
    public char getCsvSeparatorChar(){
		return this.csvSeparatorChar;
	}
	
    /**
     *
     * @return
     */
    public List<String> getCsvColumnList(){
		return this.csvColumnList;
	}
	
    /**
     *
     * @return
     */
    public int getGroupByColumnIndex(){
		return this.groupByColumnIndex;
	}
	
    /**
     *
     * @return
     */
    public int getTextColumnIndex(){
		return this.textColumnIndex;
	}
	
    /**
     *
     * @return
     */
    public char getCsvQuoteChar(){
		return this.csvQuoteChar;
				
	}
	
    /**
     *
     * @return
     */
    public int getGroupThreshold(){
		return this.groupThreshold;
	}
	
    /**
     *
     * @param outputDir
     */
    public synchronized void setOutputDir(String outputDir){
		this.outputDir=outputDir;
	}
	
    /**
     *
     * @param csvSeparatorIndex
     */
    public void setCsvSeparatorIndex(int csvSeparatorIndex){
		this.csvSeparatorIndex=csvSeparatorIndex;
	}
	
    /**
     *
     * @param csvSeparatorCustomText
     */
    public void setCsvSeparatorCustomText(String csvSeparatorCustomText){
		this.csvSeparatorCustomText=csvSeparatorCustomText;
	}
	
    /**
     *
     * @param csvSeparatorChar
     */
    public void setCsvSeparatorChar(char csvSeparatorChar){
		this.csvSeparatorChar=csvSeparatorChar;
	}
	
    /**
     *
     * @param csvColumnList
     */
    public void setCsvColumnList(List<String> csvColumnList){
		this.csvColumnList=csvColumnList;
	}
	
    /**
     *
     * @param groupByColumnIndex
     */
    public void setGroupByColumnIndex(int groupByColumnIndex){
		this.groupByColumnIndex=groupByColumnIndex;
	}
	
    /**
     *
     * @param textColumnIndex
     */
    public void setTextColumnIndex(int textColumnIndex){
		this.textColumnIndex = textColumnIndex;
	}
	
    /**
     *
     * @param csvQuoteChar
     */
    public void setCsvQuoteChar(char csvQuoteChar){
		this.csvQuoteChar=csvQuoteChar;
	}
	
    /**
     *
     * @param groupThreshold
     */
    public void setGroupThreshold(int groupThreshold){
		this.groupThreshold=groupThreshold;
	}
	
}

