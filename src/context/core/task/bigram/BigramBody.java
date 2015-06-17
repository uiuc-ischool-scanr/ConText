package context.core.task.bigram;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import context.core.entity.CorpusData;
import context.core.entity.FileData;
import context.core.util.CodebookUtils;
import context.core.util.DefaultValueHashMap;
import context.core.util.ForAggregation;
import context.core.util.JavaIO;

/**
 *
 * @author Aale
 */
public class BigramBody {

	private BigramApplicationTaskInstance instance;
	private CorpusData input;
	private DefaultValueHashMap baseWords;
	private DefaultValueHashMap bigrams;
	private int totalBigrams;

    /**
     *
     * @param instance
     */
    public BigramBody(BigramApplicationTaskInstance instance) {
		// TODO Auto-generated method stub
		this.instance = instance;
		init();
	}

	private void init() {

		this.input = (CorpusData) instance.getInput();
		int[] defaultVal = {0,0};
		totalBigrams = 0;
		baseWords = new DefaultValueHashMap(defaultVal);
		bigrams = new DefaultValueHashMap(0);
	}

    /**
     *
     * @return
     */
    public boolean getMutualInfo(){
		List<FileData> files = input.getFiles();
		try{
			for (FileData ff : files) {
				File file = ff.getFile();
				String fullText = JavaIO.readFile(file);
				List<ArrayList<String>> sentences = new ArrayList<ArrayList<String>>();
				ArrayList<String> tempSent = new ArrayList<String>();
				for (String text: CodebookUtils.make_sentences(fullText)){
					if (text == null){
						sentences.add((ArrayList<String>) tempSent.clone());
						tempSent.clear();
						continue;
					}
					tempSent.add(text);
				}
					
				for (ArrayList<String> listSentence: sentences){
					String sentence = "";
					for (String s : listSentence)
					{
						sentence += s + " ";
					}
					sentence = sentence.replaceAll("[^A-Za-z0-9\\w ]", "");
					sentence = sentence.replaceAll("LRB", "");
					sentence = sentence.replaceAll("RRB", "");
					String temp = "";
					for (String word: sentence.split("\\s+")){
						if (temp.equals("")){
							temp = word;
							continue;
						}
						totalBigrams++;
						String[] keyStrings = {temp,word};
						ForAggregation key = new ForAggregation(keyStrings);
						bigrams.put(key,((int) bigrams.get(key))+1);
						int[] baseValTemp = {0,0};
						baseValTemp = ((int[])baseWords.get(temp)).clone();
						baseValTemp[0]++;
						baseWords.put(temp, baseValTemp.clone());
						int[] baseValWord = {0,0};
						baseValWord = ((int[])baseWords.get(word)).clone();
						baseValWord[1]++;
						baseWords.put(word, baseValWord.clone());
						temp = word;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

    /**
     *
     * @param filepath
     */
    public void writeOutput(String filepath) {
		//Write CSV
		this.writeCsv(baseWords, bigrams, totalBigrams, filepath);
	}

	private void writeCsv(DefaultValueHashMap baseWords, DefaultValueHashMap bigrams, int totalBigrams, String filepath) {
		StringBuffer sb = new StringBuffer();

		//sb.append("Bigram,Frequency,Mutual Information\n");
		//Separate bigramstring to Word a & Word b -- by Ming Jiang
		sb.append("Word a,Word b,Frequency,Mutual Information\n");
		String toWrite = "";
		for (Object bigram: bigrams.keySet().toArray()) {
			String[] bigramStr = ((ForAggregation) bigram).toAggregate;
			try{
			int checker = ((int[])baseWords.get("brave"))[0];
			}
			catch(Exception e){
				e.printStackTrace();
			}
			double check = (double)((int[])baseWords.get(bigramStr[0]))[0];
			double probX = ((double)((int[])baseWords.get(bigramStr[0]))[0])/(double)totalBigrams;
			double probY = ((double)((int[])baseWords.get(bigramStr[1]))[1])/(double)totalBigrams;
			double probXY = ((double)((int)bigrams.get(bigram)))/((double)totalBigrams);
			double mutualInfo = (double) (probXY*Math.log(probXY/(probX*probY))/Math.log(2));
			//Separate bigramstring to Word a & Word b -- by Ming Jiang
			toWrite = bigramStr[0] + "," + bigramStr[1] + "," + Integer.toString((int)bigrams.get(bigram)) + "," + Double.toString(mutualInfo) + "\n";
			sb.append(toWrite);
		}
		//	        System.out.println("size of string to write=" + sb.toString().length());
		FileData.writeDataIntoFile(sb.toString(), filepath);
	}

}
