/*
 * To change this template, choose Tools | Templates
 
 */
package context.core.task.lexisnexis;

import java.util.List;

/**
 *
 * @author Aale
 */
public class LxNxDataProviderTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        customNetworkGenerateTest();
        // networkGenerateTest();
        //parseAndDeduplicationTest();
    }

    /**
     *
     */
    public static void networkGenerateTest() {
        String baseDirectory = "C:\\Users\\Aale\\Dropbox\\Education\\Others\\De-Duplication\\jana\\news_data_\\MMS\\";
        String originalDir = baseDirectory + "original";
        String networkDir = baseDirectory + "networks";
        LxNxDataProvider lexisDataProvider = new LxNxDataProvider();
        lexisDataProvider.generateNetwork(originalDir, networkDir);
    }

    /**
     *
     */
    public static void customNetworkGenerateTest() {
        //   String baseDirectory = "C:\\Users\\Aale\\Dropbox\\Education\\Others\\De-Duplication\\jana\\news_data_\\MMS\\";
        String baseDirectory = "C:\\Users\\Aale\\Dropbox\\Education\\Others\\De-Duplication\\jana\\news_data_\\NATO\\";

        String originalDir = baseDirectory + "original";
        String outputDir = baseDirectory + "output_nc";
        LxNxDataProvider lexisDataProvider = new LxNxDataProvider();
        lexisDataProvider.parseAndDeduplicate(originalDir, null, null, null, null, null, null, false);
        List<LxNxMetadata> list = lexisDataProvider.getUniqueList();
        lexisDataProvider.generateAggrMDNetwork(list, outputDir, MetadataType.ORGANIZATION, MetadataType.SUBJECT, 80);
        lexisDataProvider.generateVerboseMDNetwork(list, outputDir, MetadataType.ORGANIZATION, MetadataType.SUBJECT, 80);

    }

    /**
     *
     */
    public static void parseAndDeduplicationTest() {
        String baseDirectory = "C:\\Users\\Aale\\Dropbox\\Education\\Others\\De-Duplication\\jana\\news_data_\\NATO\\";
        //String baseDirectory = "\\Users\\vkit\\Dropbox\\Education\\Others\\De-Duplication\\jana\\news_data_\\HILI\\";
        String originalDir = baseDirectory + "original";
        String parsedDir = baseDirectory + "splitFiles";
        String uniqueDir = baseDirectory + "uniqueFiles";
        String duplicateDir = baseDirectory + "duplicateFiles";
        String metadataXLSFile = baseDirectory + "metadata.xls";
        String uniqueXLSFile = baseDirectory + "uniqueList.xls";
        String duplicateXLSFile = baseDirectory + "duplicateList.xls";

        String textBodyParsedDir = baseDirectory + "splitFiles-onlyText";
        String textBodyUniqueDir = baseDirectory + "uniqueFiles-onlyText";
        String textBodyDuplicateDir = baseDirectory + "duplicateFiles-onlyText";
        String uniqueAllTextFile = baseDirectory + "AllUniqueText.txt";

        LxNxDataProvider lexisDataProvider = new LxNxDataProvider();
        lexisDataProvider.setTextBodyDuplicateDirectory(textBodyDuplicateDir);
        lexisDataProvider.setTextBodyToDirectory(textBodyParsedDir);
        lexisDataProvider.setTextBodyUniqueDirectory(textBodyUniqueDir);
        lexisDataProvider.setUniqueAllTextFile(uniqueAllTextFile);
        lexisDataProvider.parseAndDeduplicate(originalDir, parsedDir, uniqueDir, duplicateDir, metadataXLSFile, uniqueXLSFile, duplicateXLSFile);

    }
}
