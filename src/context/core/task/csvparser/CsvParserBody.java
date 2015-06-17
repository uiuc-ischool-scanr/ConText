
package context.core.task.csvparser;

import au.com.bytecode.opencsv.CSVReader;
import context.core.entity.CorpusData;
import context.core.entity.FileData;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author julianchin
 *
 */
public class CsvParserBody {

	private int csvSeparatorIndex;
	private String csvSeparatorCustomText;
	private char csvSeparatorChar;
	private char csvQuoteChar;
	private List<String> csvColumnList;
	private int groupByColumnIndex;
	private int textColumnIndex;
	private int groupThreshold;
	
	private CsvParserTaskInstance instance;
	
	private CorpusData input;
	private CorpusData output;
	
	private List<String[]> csvLines;
	private HashMap<String, List<String[]>> fileCSVLines;
	
    /**
     *
     * @param instance
     */
    public CsvParserBody(CsvParserTaskInstance instance){
		super();
		this.instance = instance;
		this.csvSeparatorIndex=instance.getCsvSeparatorIndex();
		this.csvSeparatorCustomText=instance.getCsvSeparatorCustomText();
		this.csvSeparatorChar=instance.getCsvSeparatorChar();
		this.csvQuoteChar = instance.getCsvQuoteChar();
		this.csvColumnList=instance.getCsvColumnList();
		this.groupByColumnIndex=instance.getGroupByColumnIndex();		
		this.textColumnIndex=instance.getTextColumnIndex();
		this.input=(CorpusData)instance.getInput();	
		this.output=(CorpusData)instance.getTextOutput();
		this.csvLines=new ArrayList<String[]>();
		this.fileCSVLines = new HashMap<String, List<String[]>>();
		this.groupThreshold=instance.getGroupThreshold();
	}
	
	
	
	/**
	 * Read the CSV files from a given filelist based on the grouping schema. 
	 * @author Shubhanshu
	 */
	public void readCsvFiles(){
		List<FileData> files=input.getFiles();
		try{
			for(FileData f:files){
				File file=f.getFile();
				String fileName=file.getPath();
				CSVReader reader=new CSVReader(new FileReader(fileName),csvSeparatorChar,csvQuoteChar,1);
				if(groupByColumnIndex==0){
					fileCSVLines.put(FilenameUtils.getBaseName(fileName), reader.readAll());
				} else {
					csvLines.addAll(reader.readAll());
				}
				reader.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Parse data from each of the CSV files and store them in the required data structure based on the grouping schema.  
	 * @author Shubhanshu
	 */
	public void parseCsvToTextData(){
		if(csvLines!=null){
			String groupByName = "";
			if(groupByColumnIndex==0){
				for(Entry<String, List<String[]>> entry: fileCSVLines.entrySet()){
					groupByName = entry.getKey();
					for(int i = 0; i < entry.getValue().size(); i++){
						saveTextFile(groupByName+"_"+i,entry.getValue().get(i)[textColumnIndex]);
					}
				}
			} else {
				HashMap<String, ArrayList<String>> groupsMap = getGroupByList(csvLines);
				List<String> defaultContents=new ArrayList<String>();
				for( Entry<String, ArrayList<String>> entry: groupsMap.entrySet()){
					
					if(entry.getValue().size()<groupThreshold){
						defaultContents.addAll(entry.getValue());
					}
					else{
						groupByName = entry.getKey();
						String fileContents = StringUtils.join(entry.getValue(), "\n");
						saveTextFile(groupByName,fileContents);
					}
					
					
					//Save no-group items in the default.txt file
					if(defaultContents.size()>0){
						String defaultFileContents=StringUtils.join(defaultContents,"\n");
						saveTextFile("default",defaultFileContents);
					}
					
				}
			}
			
		}
	}
	
	
	/**
	 * Return the grouped values based on the grouping column. Currently only single level grouping is supported.  
	 * @author Shubhanshu
	 * @param csvLines - lines collected from ALL the files. 
	 * @return - HashMap of groupName with the corresponding List of text values in that group. 
	 */
	public HashMap<String, ArrayList<String>> getGroupByList(List<String[]> csvLines){
		HashMap<String, ArrayList<String>> groupsMap = new HashMap<String, ArrayList<String>>();
		String currentGroup="";
		
		if(groupByColumnIndex==0){
			System.out.println("Don't use groupby in this case.");
			return null;
		}
		for(String[] csvNextLine: csvLines){
			currentGroup=csvNextLine[groupByColumnIndex-1];
			if(!groupsMap.containsKey(currentGroup)){
				groupsMap.put(currentGroup, new ArrayList<String>());
			}
			groupsMap.get(currentGroup).add(csvNextLine[textColumnIndex]);
		}				
		return groupsMap;
	}
	
	
	/**
	 * Write the data to the given file name.  
	 * @author Shubhanshu
	 * @param fileName - Name of the file
	 * @param fileContents - Contents of the file in text format. 
	 */
	public void saveTextFile(String fileName, String fileContents){
		String outputFileName=fileName+".txt";
		int index=output.addFile(outputFileName);
		output.writeFile(index, fileContents);
		System.err.println("Finished Writing the Modified File: "+fileName);
	}
}
