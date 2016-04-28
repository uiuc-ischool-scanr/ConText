/*
 * To change this template, choose Tools | Templates
 
 */
package context.core.task.lexisnexis;

import context.app.AppConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author Aale
 */
public class LxNxExcelWriter {

    private static final String templateFile = AppConfig.getUserDirLoc() + "/data/templates/lexisnexis-metadata.xls";

    /**
     *
     * @param toFile
     * @param metadata
     */
    public static void writetoFile(String toFile, List metadata) {
        writetoFile(toFile, metadata, templateFile);
    }

    /**
     *
     * @param toFile
     * @param metadata
     * @param templateFile
     */
    public static void writetoFile(String toFile, List metadata, String templateFile) {
        try {
            Map beans = new HashMap();
            beans.put("metadatas", metadata);
            XLSTransformer transformer = new XLSTransformer();
            
            transformer.transformXLS(templateFile, beans, toFile);
        } catch (ParsePropertyException ex) {
            Logger.getLogger(LxNxExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            // TODO Auto-generated catch block
            Logger.getLogger(LxNxExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            Logger.getLogger(LxNxExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
