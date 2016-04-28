/**
 * 
 */
package context.core.textnets;

import context.core.textnets.Corpus.LABELTYPES;
import context.core.textnets.Corpus.UNITOFANALYSIS;
import java.util.ArrayList;

/**
 * @author Shubhanshu
 *
 */
public abstract class TextStream {
	
	private ArrayList<Token> streamText;
	private String fileName;
	private boolean isLower;
	
    /**
     *
     * @param fileName
     */
    public TextStream(String fileName) {
		// TODO Auto-generated constructor stub
		this.fileName = fileName;
		this.streamText = new ArrayList<Token>();
		this.isLower = true;
	}
	
    /**
     *
     * @param t
     */
    public void addToken(Token t){
		this.streamText.add(t);
	}
	
    /**
     *
     * @param isLower
     */
    public void setLowerCase(boolean isLower){
		this.isLower = isLower;
	}
	
    /**
     *
     * @return
     */
    public boolean getIsLowerCase(){
		return this.isLower;
	}
	
    /**
     *
     * @param net
     * @param windowSize
     * @param l
     * @param unitOfAnalysis
     */
    public void makeNetwork(Network net, int windowSize, LABELTYPES l, UNITOFANALYSIS unitOfAnalysis){
		//System.out.println("Window Size: "+windowSize);
		//System.out.println("Stream Size: "+streamText.size());
		//System.out.println("Unit of Analysis: "+unitOfAnalysis);
		Integer wordIndex = null;
		String word = "";
		String label = null;
		Token t = null;
		
		/**
         * Start with an element in the words vector and seek ahead till the
         * window size. If you find an element then create an edge else move
         * ahead. Once all elements in windowSize are finished increment to the
         * next element in the words vector and repeat. TODO - Add feature for
         * skipping the seperator.
         */
        for (int i = 0; i < streamText.size(); i++) {
            t = streamText.get(i);
            word = t.getText();
            label = t.getLabel(l);
            //System.out.println("Source: "+word);
            if (word == null || word.equals(".") || word.matches("\\p{Punct}")) {
                continue;
            }
            word = word.trim();
            if(getIsLowerCase()){
            	word = word.toLowerCase();
            }
            if (label!=null) {
                wordIndex = i;
            } else {
                wordIndex = null;
                continue;
            }
            int dist = 0;
            for (int j = 1; j + i < streamText.size() && dist <= windowSize; j++) {
                Integer targetIndex = i + j;
                Token t_target = streamText.get(targetIndex);
                String target = t_target.getText();
                String t_label = t_target.getLabel(l);
                //System.out.println("Target: "+target);
                if (!unitOfAnalysis.equals(UNITOFANALYSIS.PARAGRAPH) && target == null) {
                    continue;
                }
                if (unitOfAnalysis.equals(UNITOFANALYSIS.PARAGRAPH) && target == null) {
                    break;
                }
                target = target.trim();
                if (!unitOfAnalysis.equals(UNITOFANALYSIS.SENTENCE) && target.matches("\\p{Punct}")) {
                    continue;
                } else if (unitOfAnalysis.equals(UNITOFANALYSIS.SENTENCE) &&
                		(target.equals(".") || target.matches("!\\?\\."))) {
                    break;
                } else if (unitOfAnalysis.equals(UNITOFANALYSIS.SENTENCE) && target.matches("\\p{Punct}")){
                	continue;
                }
                dist++;
                if(getIsLowerCase()){
                	target = target.toLowerCase();
                }
                if (t_label == null) {
                    continue;
                }
                if(word.equals(target)){
                	continue;
                }
                if(word.length() > 0 && target.length() > 0){
                	WordNode n1 = new WordNode(word, label);
                	WordNode n2 = new WordNode(target, t_label);
                	net.addEdge(n1, n2);
                }
            }
        }
        System.out.println("Finished Generating Network now off to printing files. ");
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	

}
