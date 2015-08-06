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

    //public static void main(String[] args) throws Exception {

    /**
     *
     * @param numTopics
     * @param numWordsPerTopic
     * @param numIterations
     * @param CorpusFiles
     * @param stopListPath
     * @return
     */
        public String[] topicModeling(int numTopics, int numWordsPerTopic, int numIterations, List<FileData> CorpusFiles, String stopListPath, Boolean isLowercase) {

        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		// Pipes: lowercase, tokenize, remove stopwords, map to features
        // pipeList.add( new CharSequenceLowercase() );
        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
        //pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false) );
        File stopList = new File(stopListPath);
        if (stopList.exists()) {
            pipeList.add(new TokenSequenceRemoveStopwords(stopList, "UTF-8", false, false, false));
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
					instances.addThruPipe(new Instance(fileContentLowerCase, Integer.toString(indx), Integer.toString(indx), Integer
							.toString(indx)));
            	}
            	else {
                instances.addThruPipe(new Instance(JavaIO.readFile(filename), Integer.toString(indx), Integer.toString(indx), Integer.toString(indx)));
            	}
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
		//Pattern p = Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$");
        //FileListIterator fli = new FileListIterator(fileList, null, null, false);
        //instances.addThruPipe(fli); // data, label, name fields

		// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is the parameter for a single dimension of the Dirichlet prior.
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

        model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);

		// Run the model for 50 iterations and stop (this is for testing only, 
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(numIterations);

		// model.setSaveState(model.numIterations, "./data/StateSave.txt");
        // model.setSaveSerializedModel(model.numIterations, "./data/ModelSave.txt");
        try {
            model.estimate();
        } catch (Exception e) {
            e.printStackTrace();
        }

		// Show the words and topics in the first instance
        // The data alphabet maps word IDs to strings
        Alphabet dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        String docProbs = "";
        String topicWords = "";
        String[] allOuts = new String[2];
        /*
         for (int position = 0; position < tokens.getLength(); position++) {
         out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
         }
         System.out.println(out);
         */

        // Get an array of sorted sets of word ID/count pairs
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

        double[][] TopicDist = new double[model.data.size()][model.getNumTopics()];// an array of topic distributions for each document
        for (int indx1 = 0; indx1 < TopicDist.length; indx1++) {
            TopicDist[indx1] = model.getTopicProbabilities(indx1);
        }
        double[] AverageTopicFit = new double[model.getNumTopics()];
        double[] SumSquareTopicFit = new double[model.getNumTopics()];
        Object[][] TopWords = model.getTopWords(numWordsPerTopic);

        
        //Need to Edit it!!!!
        for (int doc = 0; doc < TopicDist.length; doc++) {
            //String tempString = "";
            //tempString = tempString.concat(CorpusFiles.get(doc).getFile().getName());
            for (int topic = 0; topic < TopicDist[doc].length; topic++) {
                //tempString = tempString.concat("," + Double.toString(TopicDist[doc][topic]));
                AverageTopicFit[topic] += TopicDist[doc][topic];
                SumSquareTopicFit[topic] += Math.pow(TopicDist[doc][topic], 2);
            }
            //tempString = tempString.concat("\n");
            //System.out.println(tempString);
            //docProbs = docProbs.concat(tempString);
        }
        
        
        /*
         * Edited by Ming Jiang to sort topics based on AverageTopicFit (decreased order)
         */
        
        double[] AvgTFit = new double[numTopics];
        List<Double> ATF = new ArrayList();
        for (int topic = 0 ; topic < numTopics ; topic ++){
        	AvgTFit[topic] =  AverageTopicFit[topic] / CorpusFiles.size();
        	ATF.add(AvgTFit[topic]);
        }
        
        AvgTFit = Sorted_Topic_Index(AvgTFit, numTopics);
        
        for (int doc = 0; doc < TopicDist.length; doc++) {
            String tempString = "";
            tempString = tempString.concat(CorpusFiles.get(doc).getFile().getName());
            Map<Integer, String> Doc_Top_Dist = new HashMap<Integer, String>();
            for (int topic = 0; topic < TopicDist[doc].length; topic++) {
            	int key = ATF.indexOf(AvgTFit[topic]);
                //tempString = tempString.concat("," + Double.toString(TopicDist[doc][key]));
            	Doc_Top_Dist.put(key, Double.toString(TopicDist[doc][key]));
                //AverageTopicFit[topic] += TopicDist[doc][topic];
                //SumSquareTopicFit[topic] += Math.pow(TopicDist[doc][topic], 2);
            }
            
            for (int key = 0 ; key < Doc_Top_Dist.size() ; key ++){
            	tempString = tempString.concat("," + Doc_Top_Dist.get(key));
            }
            tempString = tempString.concat("\n");
            System.out.println(tempString);
            docProbs = docProbs.concat(tempString);
        }

        for (int topic = 0; topic < numTopics; topic++) {
            String tempString = "";

            tempString = tempString.concat("Topic" + Integer.toString(topic+1) + ",");
            //tempString = tempString.concat(Double.toString(AverageTopicFit[topic] / CorpusFiles.size()) + ",");
            int key = ATF.indexOf(AvgTFit[topic]);
            tempString = tempString.concat(Double.toString(AvgTFit[topic]) + ",");
            //tempString = tempString.concat(Double.toString(SumSquareTopicFit[topic]/numTopics) + ",");

            for (int word = 0; word < numWordsPerTopic; word++) {
                tempString = tempString.concat((String) TopWords[key][word] + " - ");
            }
            tempString = tempString.concat("\n");
            System.out.println(tempString);
            topicWords = topicWords.concat(tempString);
        }

		///////
        allOuts[0] = topicWords;
        allOuts[1] = docProbs;
        return allOuts;
    }
        
     
        // Sort the averagefit of each topic
        double[] Sorted_Topic_Index (double[]AvgTFit, int numtopic){
        	
        	int k = 0;
        	for (int m = numtopic; m >= 0 ; m --){
        		for (int i = 0 ; i < numtopic - 1 ; i ++){
        			k = i + 1;
        			if (AvgTFit[i] < AvgTFit[k]){
        				swapValues(i, k, AvgTFit);
        			}
        			
        		}
        	}
        	
        	return AvgTFit;
        }
        
        private void swapValues(int i, int j, double[] array) {
        	  
            double temp;
            temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        
        /*
         * End of Edition by Ming Jiang
         */

}
