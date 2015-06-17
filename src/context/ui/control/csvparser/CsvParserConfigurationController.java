/**
 * 
 */
package context.ui.control.csvparser;

import au.com.bytecode.opencsv.CSVReader;
import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.ui.control.configuration.ConfigurationController;
import context.ui.misc.FileHandler;
import context.ui.misc.PropertiesUtil;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


/**
 * @author julianchin
 *
 */
public class CsvParserConfigurationController extends ConfigurationController implements Initializable {

    /**
     *
     */
    public static String path=PropertiesUtil.getFXMLPath(CsvParserConfigurationController.class);
	
	
	private char csvSeparatorChar;
	private List<String> csvColumnList=new ArrayList<String>();
	private char csvQuoteChar;
	private int groupThreshold;
	
	@FXML
	private Label titleLabel;
	@FXML
	private ComboBox<String> csvSeparatorComboBox;
	@FXML
	private ComboBox<String> groupByColumnComboBox;
	@FXML
	private ComboBox<String> textColumnComboBox;
	@FXML
	private TextField csvSeparatorCustomTextField;
	@FXML
	private ComboBox<String> csvQuoteCharComboBox;
	@FXML
	private TextField groupThresholdTextField;
	@FXML
	private Button LoadColumnButton;
	
	private String inputPathString="";
	
    /**
     *
     * @param title
     */
    public void setTitle(String title){
		titleLabel.setText(title);		
	}
	
    /**
     *
     * @param path
     */
    public void setInputPath(String path) {
       	this.inputPathString=path;
    }
	
    /**
     *
     * @param url
     * @param rb
     */
    public void initialize(URL url,ResourceBundle rb){
		
		ObservableList<String> csvSeparatorOptions
		=FXCollections.observableArrayList(
				",", //0
				";", //1
				"tab", //2
				"space",//3
				"Custom Separator" //4
				);
		csvSeparatorComboBox.getItems().addAll(csvSeparatorOptions);
		csvSeparatorComboBox.getSelectionModel().select(0);
		
		
		
		csvSeparatorCustomTextField.setVisible(false);
		
		groupThresholdTextField.setVisible(true);
		groupThresholdTextField.setText("0");
		
		ObservableList<String> csvQuoteCharOptions
		=FXCollections.observableArrayList(
				
				"\"",
				"'",
				"none"
				);
		csvQuoteCharComboBox.getItems().addAll(csvQuoteCharOptions);
		csvQuoteCharComboBox.getSelectionModel().select(2);
		
		
		
	}
	
    /**
     *
     * @param event
     */
    public void handleCsvSeparatorComboBox(ActionEvent event){
		if(csvSeparatorComboBox.getSelectionModel().getSelectedIndex()==4){
			csvSeparatorCustomTextField.setVisible(true);
			csvSeparatorCustomTextField.requestFocus();
		}else{
			
			csvSeparatorCustomTextField.setVisible(false);			
		}
		
	}
	
    /**
     *
     * @param event
     */
    public void handleLoadColumnButton(ActionEvent event){
		
		setCsvSeparatorChar(
				csvSeparatorComboBox.getSelectionModel().getSelectedIndex(),
				csvSeparatorCustomTextField.getText()
				);
		setCsvQuoteChar(csvQuoteCharComboBox.getSelectionModel().getSelectedIndex());
		setColumnComboBox();
		
		
	}
	
    /**
     *
     */
    public void setColumnComboBox(){			
			String filename = FileHandler.getFileName(inputPathString);
			if(filename!=null &&!filename.isEmpty()){
				
				
				CorpusData inputCorpus = new CorpusData(new SimpleStringProperty(filename),new SimpleStringProperty(inputPathString));
				inputCorpus.addAllFiles(new File(inputCorpus.getPath().get()));
				setCsvColumnList(inputCorpus,csvSeparatorChar);
			}
	        
			textColumnComboBox.getItems().clear();
			groupByColumnComboBox.getItems().clear();
		
			ObservableList<String> textColumnOptions
			= FXCollections.observableArrayList();
			textColumnOptions.addAll(csvColumnList);
			textColumnComboBox.getItems().addAll(textColumnOptions);
			//textColumnComboBox.getSelectionModel().select(0);
			
			ObservableList<String> groupByColumnOptions
			= FXCollections.observableArrayList("Each Row");
			groupByColumnOptions.addAll(csvColumnList);
			groupByColumnComboBox.getItems().addAll(groupByColumnOptions);
			groupByColumnComboBox.getSelectionModel().select(0);
		

		
	}
	
    /**
     *
     * @param csvSeparatorIndex
     * @param csvSeparator
     */
    public void setCsvSeparatorChar(int csvSeparatorIndex,String csvSeparator){
		
		char csvSeparatorCharSelected=',';
		if(csvSeparatorIndex==1){
			csvSeparatorCharSelected=';';
		}else if(csvSeparatorIndex==2){
			csvSeparatorCharSelected='\t';
		}else if(csvSeparatorIndex==3){
			csvSeparatorCharSelected=' ';
			}else if(csvSeparatorIndex==4){
				if(csvSeparator != null && !csvSeparator.trim().isEmpty()){
					csvSeparatorCharSelected=csvSeparator.charAt(0);
			}		
		}else{
			csvSeparatorCharSelected=',';
		}
		this.csvSeparatorChar=csvSeparatorCharSelected;
	}
	
    /**
     *
     * @param csvQuoteCharIndex
     */
    public void setCsvQuoteChar(int csvQuoteCharIndex){
		char csvQuoteCharSelected='"';
		if(csvQuoteCharIndex==0){
			csvQuoteCharSelected='"';
		}else if(csvQuoteCharIndex==1){
			csvQuoteCharSelected='\'';
		}else{
			csvQuoteCharSelected='\0';
		}
		
		this.csvQuoteChar=csvQuoteCharSelected;
	}
	
    /**
     *
     * @param input
     * @param csvSeparatorChar
     */
    public void setCsvColumnList(CorpusData input, char csvSeparatorChar){
		
		
		List<FileData> files=input.getFiles();
		List<String> csvColumnListData=new ArrayList<String>();
		
		try{
			FileData firstFileData=files.get(0);
			String firstFileName=firstFileData.getFile().getPath();
			CSVReader reader=new CSVReader(new FileReader(firstFileName),csvSeparatorChar);
			String[] nextLine= reader.readNext();
			if(nextLine!=null){
				csvColumnListData=Arrays.asList(nextLine);
			}
			
			this.csvColumnList=csvColumnListData;
				
			reader.close();		
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
	}
	
    /**
     *
     * @return
     */
    public int getCsvSeparatorIndex(){
		return csvSeparatorComboBox.getSelectionModel().getSelectedIndex();
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
    public char getCsvQuoteChar(){
		return this.csvQuoteChar;
	}
	
    /**
     *
     * @return
     */
    public int getGroupByColumnIndex(){
		return groupByColumnComboBox.getSelectionModel().getSelectedIndex();
	}
	
    /**
     *
     * @return
     */
    public int getTextColumnIndex(){
		return textColumnComboBox.getSelectionModel().getSelectedIndex();
	}
	
    /**
     *
     * @return
     */
    public String getCsvSeparatorCustomText(){
		return csvSeparatorCustomTextField.getText();
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
    public int getGroupThreshold(){
		groupThreshold=Integer.parseInt(groupThresholdTextField.getText());
		return groupThreshold;
	}
	
}
