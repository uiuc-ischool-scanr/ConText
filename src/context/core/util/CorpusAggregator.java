package context.core.util;

import gnu.trove.TObjectIntHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Aale
 */
public class CorpusAggregator {

    /**
     *
     * @param toAggregate
     * @return
     */
    public List<String[]> CorpusAggregate(List<List<String[]>> toAggregate) {
        // TODO Auto-generated method stub
//		ArrayList al = new ArrayList();
        TObjectIntHashMap<ForAggregation> intHashMap = new TObjectIntHashMap<ForAggregation>();
        int numFields = 0;
        for (List<String[]> subList : toAggregate) {
//			al.addAll(subList);
            for (String[] subListObj : subList) {

                numFields = subListObj.length;
                String[] tempArray = null;
                try {
                    Integer.parseInt(subListObj[numFields - 1]);
                    tempArray = Arrays.copyOf(subListObj, numFields - 1);

                } catch (Exception e) {
                    numFields++;
                    tempArray = Arrays.copyOf(subListObj, numFields - 1);
                }
                /*
                 for (int i =0; i< numFields -1;i++){
                 tempArray[i] = subListObj[i];
                 }
                 */
                try {
                    ForAggregation forAggregation = new ForAggregation(tempArray);
                    intHashMap.adjustOrPutValue(forAggregation, Integer.parseInt(subListObj[numFields - 1]), Integer.parseInt(subListObj[numFields - 1]));
                } catch (Exception e) {
                    ForAggregation forAggregation = new ForAggregation(tempArray);
                    intHashMap.adjustOrPutValue(forAggregation, 1, 1);
                }
            }
        }
        /*		
         // add elements to al, including duplicates
         HashSet hs = new HashSet();
         hs.addAll(al);
         al.clear();
         al.addAll(hs);
         */
        List<String[]> returnList = new ArrayList<String[]>();
        ForAggregation[] hashMapKeys = new ForAggregation[intHashMap.keys().length];
        hashMapKeys = intHashMap.keys(hashMapKeys);
        for (ForAggregation k_1 : hashMapKeys) {
            String[] k = k_1.toAggregate;
            String[] append = new String[numFields];
            for (int i = 0; i < numFields - 1; i++) {
                append[i] = k[i];
            }
            append[numFields - 1] = Integer.toString(intHashMap.get(k_1));

            returnList.add(append);
        }
        return returnList;
    }

    /**
     *
     * @param toAggregate
     * @return
     */
    public List<String[]> CorpusAggregateNoCase(List<List<String[]>> toAggregate) {
        // TODO Auto-generated method stub
//		ArrayList al = new ArrayList();
        TObjectIntHashMap<ForAggregationNoCase> intHashMap = new TObjectIntHashMap<ForAggregationNoCase>();
        int numFields = 0;
        for (List<String[]> subList : toAggregate) {
//			al.addAll(subList);
            for (String[] subListObj : subList) {

                numFields = subListObj.length;
                String[] tempArray = null;
                try {
                    Integer.parseInt(subListObj[numFields - 1]);
                    tempArray = Arrays.copyOf(subListObj, numFields - 1);

                } catch (Exception e) {
                    numFields++;
                    tempArray = Arrays.copyOf(subListObj, numFields - 1);
                }
                /*
                 for (int i =0; i< numFields -1;i++){
                 tempArray[i] = subListObj[i];
                 }
                 */
                try {
                    ForAggregationNoCase forAggregation = new ForAggregationNoCase(tempArray);
                    intHashMap.adjustOrPutValue(forAggregation, Integer.parseInt(subListObj[numFields - 1]), Integer.parseInt(subListObj[numFields - 1]));
                } catch (Exception e) {
                    ForAggregationNoCase forAggregation = new ForAggregationNoCase(tempArray);
                    intHashMap.adjustOrPutValue(forAggregation, 1, 1);
                }
            }
        }
        /*		
         // add elements to al, including duplicates
         HashSet hs = new HashSet();
         hs.addAll(al);
         al.clear();
         al.addAll(hs);
         */
        List<String[]> returnList = new ArrayList<String[]>();
        ForAggregationNoCase[] hashMapKeys = new ForAggregationNoCase[intHashMap.keys().length];
        hashMapKeys = intHashMap.keys(hashMapKeys);
        for (ForAggregationNoCase k_1 : hashMapKeys) {
            String[] k = k_1.toAggregate;
            String[] append = new String[numFields];
            for (int i = 0; i < numFields - 1; i++) {
                append[i] = k[i];
            }
            append[numFields - 1] = Integer.toString(intHashMap.get(k_1));

            returnList.add(append);
        }
        return returnList;
    }
}
