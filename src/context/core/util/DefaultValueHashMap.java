package context.core.util;

import java.util.HashMap;

/**
 *
 * @author Aale
 * @param <K>
 * @param <V>
 */
public class DefaultValueHashMap<K,V> extends HashMap<K,V> {

    /**
     *
     */
    protected V defaultValue;

    /**
     *
     * @param defaultValue
     */
    public DefaultValueHashMap(V defaultValue) {
		    this.defaultValue = defaultValue;
		  }
		  @Override
		  public V get(Object k) {
		    return containsKey(k) ? super.get(k) : defaultValue;
		  }
		
}
