/**
 * 
 */
package context.core.textnets;

import context.core.textnets.Corpus.LABELTYPES;
import java.util.HashMap;

/**
 * @author Shubhanshu
 *
 */
public abstract class Token {
	
	private String text;
	/**
	 * labels - list of labels for the given token
	 */
	private HashMap<LABELTYPES, String> labels;
	
    /**
     *
     * @param text
     */
    public Token(String text) {
		// TODO Auto-generated constructor stub
		this.setText(text);
		labels = new HashMap<Corpus.LABELTYPES, String>();
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
     * @param l
	 * @return the label
	 */
	public String getLabel(LABELTYPES l) {
		return labels.get(l);
	}

	/**
	 * @param label the label to set
     * @param l
	 */
	public void setLabel(String label, LABELTYPES l) {
		this.labels.put(l, label);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
