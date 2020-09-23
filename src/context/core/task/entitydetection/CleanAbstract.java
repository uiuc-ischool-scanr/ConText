/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package context.core.task.entitydetection;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author nikolausn
 */
public class CleanAbstract {
    private List<Object> cleanParameters;

    public CleanAbstract() {
    }    
    
    public CleanAbstract(List<Object> cleanParameters) {
        this.cleanParameters = cleanParameters;
    }        
    
    public String clean(String text){
        for (Iterator<Object> iterator = cleanParameters.iterator(); iterator.hasNext();) {
            Object next = iterator.next();
            // Do cleaning for every method
        }
        return text;
    }       

    public void setCleanParameters(List<Object> cleanParameters) {
        this.cleanParameters = cleanParameters;
    }        
    
}
