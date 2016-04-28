package context.core.util;

import java.util.Arrays;

/**
 *
 * @author Aale
 */
public class ForAggregationNoCase {

    /**
     */
    public String[] toAggregate;
    private String[] hashString;

    /**
     *
     * @param toAggregate
     */
    public ForAggregationNoCase(String[] toAggregate) {
        // TODO Auto-generated method stub
        this.toAggregate = toAggregate;

        String[] tempArray = new String[toAggregate.length];
    	for (int i = 0; i <toAggregate.length; i++ ){
    		tempArray[i] = toAggregate[i].toLowerCase();
    	}
    	hashString = tempArray;
    }

    @Override
    public int hashCode() {
    	
        return Arrays.hashCode(hashString);
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = true;

        if (object == null || ((ForAggregationNoCase)object).toAggregate.length != this.toAggregate.length){
        	sameSame = false;
        }
       for(int i = 0; sameSame && i < ((ForAggregationNoCase)object).toAggregate.length; i++){
    	   if (!((ForAggregationNoCase)object).toAggregate[i].equalsIgnoreCase(this.toAggregate[i])){
    		   sameSame = false;
    	   }
       }

        return sameSame;
    }

}
