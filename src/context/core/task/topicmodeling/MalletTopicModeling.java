package context.core.task.topicmodeling;

import cc.mallet.types.*;
import cc.mallet.pipe.*;
import cc.mallet.topics.*;

import java.util.*;
import java.util.regex.*;
import java.io.*;

import context.core.entity.FileData;
import context.core.util.JavaIO;

/**
 *
 * @author Aale
 */
public class MalletTopicModeling {

	// public static void main(String[] args) throws Exception {
	public int numTopics;
	public int numWordsPerTopic;
	public int numIterations;
	public int numOptInterval;
	public double sumAlpha;
	public List<FileData> CorpusFiles;
	public String stopListPath;
	public Boolean isLowercase;
	public ParallelTopicModel model;
	public IDSorter[] sortedTopics;
	public Object[][] TopWords;
	public double[] Weight;
	public double[][] TopicDist;
	public Alphabet dataAlphabet;
	
	public int topicMask;
	public int topicBits;
	public int numTypes;
	public double beta;
	public int[][] typeTopicCounts;	
	public int totalTokens;
        /*private Boolean drop_num;
        private Boolean drop_pun;
        private Boolean keep_pou;*/
	
	public MalletTopicModeling(int numTopics, int numWordsPerTopic,
			int numIterations, int numOptInterval, double sumAlpha, List<FileData> CorpusFiles, String stopListPath,
			Boolean isLowercase){
		this.numTopics = numTopics;
		this.numWordsPerTopic = numWordsPerTopic;
		this.numIterations = numIterations;
		this.numOptInterval=numOptInterval;
		this.sumAlpha=sumAlpha;
		this.CorpusFiles = CorpusFiles;
		this.stopListPath = stopListPath;
		this.isLowercase = isLowercase;
		this.model = new ParallelTopicModel(this.numTopics, sumAlpha, 0.01);
		/*this.drop_num = drop_num;
                this.drop_pun = drop_pun;
                this.keep_pou = keep_pou;*/
		this.topicModeling();
	}
	
	public String[] topicModellingOutput(){
		String docProbs = "";
		String topicWords = "";
		String wordWeights="";
		double llToken=0;
		String llTokenStr="";
		String[] allOuts = new String[4];

		// Output for table 1
		for (int order = 0; order < numTopics; order++) {
			String tempString = "";
			String tempWordWeight="";

			tempString = tempString.concat("Topic"
					+ Integer.toString(order + 1) + ",");
			
			
			
			// get topic in the order of sortedTopics (which is sorted by
			// weight)
			int topic = sortedTopics[order].getID();
			tempString = tempString
					.concat(Double.toString(Weight[topic]) + ",");

			for (int word = 0; word < numWordsPerTopic; word++) {
				tempString = tempString.concat((String) TopWords[topic][word]
						+ " - ");
			}
			
			tempString = tempString.concat("\n");
			
			//Output 3
			for(int type=0;type<numTypes;type++){
				
			int[] topicCounts = typeTopicCounts[type];
				
				double wordWeight = beta;

				int index = 0;
				while (index < topicCounts.length &&
					   topicCounts[index] > 0) {

					int currentTopic = topicCounts[index] & topicMask;
					
					
					if (currentTopic == topic) {
						wordWeight += topicCounts[index] >> topicBits;
						break;
					}

					index++;
				}
				
				for (int word = 0; word < numWordsPerTopic; word++){
					if(dataAlphabet.lookupObject(type)==TopWords[topic][word]){
						tempWordWeight = tempWordWeight.concat("Topic"
								+ Integer.toString(order + 1) + ",");
						tempWordWeight=tempWordWeight
								.concat(TopWords[topic][word]+","+Double.toString(wordWeight));
						tempWordWeight = tempWordWeight.concat("\n");
					}
				}
				
				
				
			}
			
			System.out.print(tempString);
			
			topicWords = topicWords.concat(tempString);
			wordWeights=wordWeights.concat(tempWordWeight);
		}

		System.out.print(wordWeights);
		
		// Output for table 2
		for (int doc = 0; doc < TopicDist.length; doc++) {
			String tempString = "";
			tempString = tempString.concat(CorpusFiles.get(doc).getFile()
					.getName());
			for (int order = 0; order < TopicDist[doc].length; order++) {

				// get topic in the order of sortedTopics (which is sorted by
				// weight)
				int topic = sortedTopics[order].getID();
				tempString = tempString.concat(","
						+ Double.toString(TopicDist[doc][topic]));
			}
			tempString = tempString.concat("\n");
			System.out.print(tempString);
			docProbs = docProbs.concat(tempString);
		}
		
		//Output for table 4
		String tempLLToken="";
		double likelihood = model.modelLogLikelihood();
		
		llToken=likelihood/(double) totalTokens;
		tempLLToken=Double.toString(llToken);
		llTokenStr=llTokenStr.concat(tempLLToken+"\n");
		//System.out.println("modelLogLikelihood: "+model.modelLogLikelihood()+"Token: "+totalTokens+"LL Token: "+llToken);
		System.out.println("LL Token: "+tempLLToken);
		
		allOuts[0] = topicWords;
		allOuts[1] = docProbs;
		allOuts[2]=wordWeights;
		allOuts[3]=llTokenStr;
		return allOuts;
	}
	
	/**
	 *
	 * @param numTopics
	 * @param numWordsPerTopic
	 * @param numIterations
	 * @param CorpusFiles
	 * @param stopListPath
	 * @return
	 */
	public void topicModeling() {

		// Begin by importing documents from text to feature sequences
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		// Pipes: lowercase, tokenize, remove stopwords, map to features
		// pipeList.add( new CharSequenceLowercase() );
		pipeList.add(new CharSequence2TokenSequence(Pattern
				.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));

		File stopList = new File(stopListPath);
		if (stopList.exists()) {
			pipeList.add(new TokenSequenceRemoveStopwords(stopList, "UTF-8",
					false, false, false));
		}
		pipeList.add(new TokenSequence2FeatureSequence());

		InstanceList instances = new InstanceList(new SerialPipes(pipeList));
		File[] fileList = new File[CorpusFiles.size()];
		int indx = 0;
		for (FileData file : CorpusFiles) {
			File filename = null;
			try {
				filename = file.getFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			fileList[indx] = filename;
			indx++;
			try {
				if (isLowercase) {
					final String filecontent = JavaIO.readFile(filename);
					String fileContentLowerCase = filecontent.toLowerCase();
					instances.addThruPipe(new Instance(fileContentLowerCase,
							Integer.toString(indx), Integer.toString(indx),
							Integer.toString(indx)));
				} else {
					instances.addThruPipe(new Instance(JavaIO
							.readFile(filename), Integer.toString(indx),
							Integer.toString(indx), Integer.toString(indx)));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and
		// combine
		// statistics after every iteration.
		model.setNumThreads(2);

		// Run the model for 50 iterations and stop (this is for testing only,
		// for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(numIterations);

		// 2016.01.26 Added by Julian
		// Set optimize interval, SymmetricAlpha, burnin period to optimize
		// alpha
		// (use the default value in PrallelTopicMOdel.java)
		// The default burnin period is 200.
		// The iteration must be larger than burnin period,
		// or model.estimate() won't optimize alpha (i.e. weight)
		// Default alpha = alphaSum/numTopics
		model.setOptimizeInterval(numOptInterval);
		model.setSymmetricAlpha(false);
		model.setBurninPeriod(200);
		model.setRandomSeed(1337);
		// End of adding default value

		// model.setSaveState(model.numIterations, "./data/StateSave.txt");
		// model.setSaveSerializedModel(model.numIterations,
		// "./data/ModelSave.txt");
		try {
			model.estimate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// The data alphabet maps word IDs to strings
		dataAlphabet = instances.getDataAlphabet();
		// Get an array of sorted sets of word ID/count pairs
		TopicDist = new double[model.data.size()][model
		                          				.getNumTopics()];// an array of topic distributions for each
		                          									// document
  		for (int indx1 = 0; indx1 < TopicDist.length; indx1++) {
  			TopicDist[indx1] = model.getTopicProbabilities(indx1);
  		}

  		// Use weight to replace average topic fit
  		// See ParallelTopicModel.displayTopWords(),
  		// it uses alpha as the second value in each topic for output (i.e.
  		// weight)
  		Weight = model.alpha;

  		TopWords = model.getTopWords(numWordsPerTopic);
  		
  		numTypes=model.numTypes;
  		topicBits=model.topicBits;
  		topicMask=model.topicMask;
  		beta=model.beta;
  		typeTopicCounts=model.typeTopicCounts;
  		totalTokens=model.totalTokens;
  		

  		// Sort output topics by weight
  		// Use IDSorter.java in Mallet to pull out initial topic ID after
  		// sorting
  		sortedTopics = new IDSorter[numTopics];
  		for (int topic = 0; topic < numTopics; topic++) {
  			// Initialize the sorters with dummy values
  			sortedTopics[topic] = new IDSorter(topic, Weight[topic]);
  		}
  		
  		
  		Arrays.sort(sortedTopics);
	}

}
