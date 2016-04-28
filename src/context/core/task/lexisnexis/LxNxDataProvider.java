package context.core.task.lexisnexis;

import context.core.util.CSVWriter;
import context.core.util.DistanceUtil;
import context.core.util.MyPair;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.openide.util.Exceptions;

/**
 *
 * @author Jana Diesner
 */
public class LxNxDataProvider {

    private int idCounter = 0;
    private String subectsOutputDir;
    private String metadataFile;
    private String year = "";
    private Hashtable<String, Integer> htSubjectsPerData = new Hashtable<String, Integer>();
    private HashSet<String> hsIdsOfConsideredTexts = new HashSet<String>();
    private boolean disregardTextIdList = true;
    private List<LxNxMetadata> originalList = new ArrayList<LxNxMetadata>();
    private List<LxNxMetadata> uniqueList = new ArrayList<LxNxMetadata>();
    private List<LxNxMetadata> duplicateList = new ArrayList<LxNxMetadata>();
    private Map<Integer, LxNxMetadata> textIDMap = new HashMap<Integer, LxNxMetadata>();
    private Map<Integer, List<Integer>> duplicateMap = new HashMap<Integer, List<Integer>>();
    private Map<Integer, List<Integer>> docGraph = new HashMap<Integer, List<Integer>>();
    private Map<Integer, Boolean> mark = new HashMap<Integer, Boolean>();
    private String uniqueFile;
    private String duplicateFile;
    private String fromDirectory;
    private String toDirectory;
    private String duplicateDirectory;
    private String uniqueDirectory;
    private String textBodyToDirectory;
    private String textBodyDuplicateDirectory;
    private String textBodyUniqueDirectory;
    private String uniqueAllTextFile;
    private String gexfAllNetworkFile;
    private String gexfPersonNetworkFile;
    private String gexfSubjectNetworkFile;
    private String networkDir;
    private List<CodebookEntity> codebook = new ArrayList<CodebookEntity>();
    private Map<String, CodebookEntity> codebookMap = new HashMap<String, CodebookEntity>();
    private String codebookCSVFile;
    private String codebookRejectCSVFile;
    private boolean genOutputFiles;
    List<String> codebookTemplate = Arrays.asList("Text", "Name", "Percent", "Type", "Subtype");
    private Map<CodebookEntity, List<MyPair<CodebookEntity, Integer>>> allGraph = new HashMap<CodebookEntity, List<MyPair<CodebookEntity, Integer>>>();
    private Map<CodebookEntity, List<MyPair<CodebookEntity, Integer>>> personGraph = new HashMap<CodebookEntity, List<MyPair<CodebookEntity, Integer>>>();
    private Map<CodebookEntity, List<MyPair<CodebookEntity, Integer>>> subjectGraph = new HashMap<CodebookEntity, List<MyPair<CodebookEntity, Integer>>>();
    private String fileSeperator = System.getProperty("file.separator");
    // private static double treshold = 70;

    /**
     *
     */
    public LxNxDataProvider() {
    }

    /**
     *
     * @return
     */
    public List<LxNxMetadata> getUniqueList() {
        return uniqueList;
    }

    private void clearAllVariables() {
        uniqueList.clear();
        duplicateList.clear();
        duplicateMap.clear();
        // originalList.clear();
        allGraph.clear();
        personGraph.clear();
        subjectGraph.clear();
        textIDMap.clear();
        codebook.clear();
        codebookMap.clear();
        docGraph.clear();
        hsIdsOfConsideredTexts.clear();
        htSubjectsPerData.clear();
    }

    /**
     *
     * @param originalDir
     * @param networkDir
     */
    public void generateNetwork(String originalDir, String networkDir) {
        parseAndDeduplicate(originalDir, null, null, null, null, null, null, false);
        LxNxDataProvider.initializeDir(networkDir);
        setGexfAllNetworkFile(networkDir + fileSeperator + "all_all.gexf");
        setGexfPersonNetworkFile(networkDir + fileSeperator + "person_person.gexf");
        setGexfSubjectNetworkFile(networkDir + fileSeperator + "subject_subject.gexf");
        generateCodebook(uniqueList, null, null);
    }

    /**
     *
     * @param list
     * @param outputDir
     * @param type1
     * @param type2
     * @param threshold
     */
    public void generateAggrMDNetwork(List<LxNxMetadata> list, String outputDir, MetadataType type1, MetadataType type2, int threshold) {
        generateCustomNetwork(list, outputDir, type1, type2, threshold, false);
    }

    /**
     *
     * @param list
     * @param outputDir
     * @param type1
     * @param type2
     * @param threshold
     */
    public void generateVerboseMDNetwork(List<LxNxMetadata> list, String outputDir, MetadataType type1, MetadataType type2, int threshold) {
        generateCustomNetwork(list, outputDir, type1, type2, threshold, true);
    }

    /**
     *
     * @param list
     * @param outputDir
     * @param type1
     * @param type2
     * @param threshold
     */
    public void generateBothMDNetwork(List<LxNxMetadata> list, String outputDir, MetadataType type1, MetadataType type2, int threshold) {
        generateCustomNetwork(list, outputDir, type1, type2, threshold, false);
        generateCustomNetwork(list, outputDir, type1, type2, threshold, true);
    }

    private void generateCustomNetwork(List<LxNxMetadata> list, String outputDir, MetadataType type1, MetadataType type2, int threshold, boolean verbose) {
        LxNxDataProvider.initializeDirSafe(outputDir);
        String name = type1.getValue() + "_" + type2.getValue();
        if (verbose) {
            name += "_verbose";
        } else {
            name += "_aggr";
        }
        String codebookCSVFile_ = outputDir + fileSeperator + name + ".csv";
        String codebookRejectCSVFile_ = outputDir + fileSeperator + name + ".reject";
        generateCustomCodebook(list, type1, type2, codebookCSVFile_, codebookRejectCSVFile_, threshold, verbose);
    }

    private void parseAndDeduplicate(String originalDir, String parsedDir, String uniqueDir, String duplicateDir, String metadataXLSFile, String uniqueXLSFile, String duplicateXLSFile, String codebookCSVFile, String codebookRejectCSVFile) {
        parseAndDeduplicate(originalDir, parsedDir, uniqueDir, duplicateDir, metadataXLSFile, uniqueXLSFile, duplicateXLSFile, true);
        this.codebookCSVFile = codebookCSVFile;
        this.codebookRejectCSVFile = codebookRejectCSVFile;
        FileUtils.deleteQuietly(new File(this.codebookCSVFile));
        FileUtils.deleteQuietly(new File(this.codebookRejectCSVFile));
        generateCodebook(uniqueList, this.codebookCSVFile, this.codebookRejectCSVFile);
    }

    /**
     *
     * @param list
     * @param type1
     * @param type2
     * @param codebookCSVFile
     * @param codebookRejectCSVFile
     * @param threshold
     * @param verbose
     */
    public void generateCustomCodebook(List<LxNxMetadata> list, MetadataType type1, MetadataType type2, String codebookCSVFile, String codebookRejectCSVFile, int threshold, boolean verbose) {
        CSVWriter csvWriter = new CSVWriter(codebookCSVFile, ";", "\n");
        CSVWriter csvRejectWriter = new CSVWriter(codebookRejectCSVFile);
        Map<CodebookEntity, List<MyPair<CodebookEntity, Integer>>> graph = new HashMap<CodebookEntity, List<MyPair<CodebookEntity, Integer>>>();

        System.out.println(codebookCSVFile + "," + verbose);
        csvWriter.append("source,target,weight,type");

        CodebookEntity.clearRejectList();
        for (LxNxMetadata mdata : list) {
            System.out.println("Processing document " + mdata.getTextID() + "...");
            List<CodebookEntity> type1_cbs = getCodebookEntities(type1, mdata);
            List<CodebookEntity> type2_cbs = getCodebookEntities(type2, mdata);

            if (type1.equals(type2)) {
                if (verbose) {
                    for (CodebookEntity ent1 : type1_cbs) {
                        for (CodebookEntity ent2 : type2_cbs) {
                            if (ent1.getName().compareTo(ent2.getName()) > 0) {
                                if ((ent1.getPercent() > threshold) && (ent2.getPercent() > threshold)) {
                                    csvWriter.append(mdata.getTextID() + "," + ent1.getName() + "," + ent1.getPercent() + "," + ent2.getName() + "," + ent2.getPercent());
                                }
                            }
                        }
                    }
                } else {
                    addToGraph(type1_cbs, graph, threshold);
                }
            } else {
                if (verbose) {
                    for (CodebookEntity ent1 : type1_cbs) {
                        for (CodebookEntity ent2 : type2_cbs) {
                            if ((ent1.getPercent() > threshold) && (ent2.getPercent() > threshold)) {
                                csvWriter.append(mdata.getTextID() + "," + ent1.getName() + "," + ent1.getPercent() + "," + ent2.getName() + "," + ent2.getPercent());
                            }
                        }
                    }
                } else {
                    addToGraph(type1_cbs, type2_cbs, graph, threshold);
                }
            }
        }
        if (!verbose) {
            writeGraphToCSV(graph, csvWriter);
        }
        csvRejectWriter.appendAll(CodebookEntity.getRejectList());
        csvWriter.close();
        csvRejectWriter.close();
    }

    /**
     *
     * @param list
     * @param codebookCSVFile
     * @param codebookRejectCSVFile
     */
    public void generateCodebook(List<LxNxMetadata> list, String codebookCSVFile, String codebookRejectCSVFile) {
        CSVWriter csvWriter = null;
        CSVWriter csvRejectWriter = null;
        if (genOutputFiles) {
            csvWriter = new CSVWriter(codebookCSVFile, codebookTemplate);
            csvRejectWriter = new CSVWriter(codebookRejectCSVFile);
        }

        for (LxNxMetadata mdata : list) {
            List<CodebookEntity> person_cbs = CodebookEntity.parseLine(mdata.getPerson(), "agent", "specific", mdata.getTextID());
            //  System.out.println("person:"+ mdata.getTextID() + " " + person_cbs.size());
            codebook.addAll(person_cbs);
            addToGraph(person_cbs, allGraph);
            addToGraph(person_cbs, personGraph);
            List<CodebookEntity> geo_cbs = CodebookEntity.parseLine(mdata.getGeo(), "location", "specific", mdata.getTextID());
            codebook.addAll(geo_cbs);
            addToGraph(geo_cbs, allGraph);
            List<CodebookEntity> organization_cbs = CodebookEntity.parseLine(mdata.getOrganization(), "organization", "specific", mdata.getTextID());
            codebook.addAll(organization_cbs);
            addToGraph(organization_cbs, allGraph);
            List<CodebookEntity> company_cbs = CodebookEntity.parseLine(mdata.getCompany(), "organization", "specific", mdata.getTextID());
            codebook.addAll(company_cbs);
            addToGraph(company_cbs, allGraph);
            List<CodebookEntity> subject_cbs = CodebookEntity.parseLine(mdata.getSubject(), "knowledge", "specific", mdata.getTextID());
            codebook.addAll(subject_cbs);
            //   System.out.println("subject:"+ mdata.getTextID() + " " + subject_cbs.size());
            addToGraph(subject_cbs, allGraph);
            addToGraph(subject_cbs, subjectGraph);
            //   System.out.println();
            //   System.out.println("<>");
            //   System.out.println("");
        }
        System.out.println("Codebook Size=" + codebook.size());
        System.out.println("RejectList Size=" + CodebookEntity.getRejectList().size());
        for (CodebookEntity ent : codebook) {
            if (!codebookMap.containsKey(ent.getName())) {
                codebookMap.put(ent.getName(), ent);
            }
        }
        System.out.println("CodebookMap Size=" + codebookMap.keySet().size());
        if (genOutputFiles) {
            csvRejectWriter.appendAll(CodebookEntity.getRejectList());
            csvWriter.appendAll(codebookMap.values());
            csvWriter.close();
            csvRejectWriter.close();
        }
        if (getGexfAllNetworkFile() != null) {
            GephiNetworkGenerator allgraphgen = new GephiNetworkGenerator(allGraph, getGexfAllNetworkFile());
            allgraphgen.script();
        }
        if (getGexfPersonNetworkFile() != null) {
            GephiNetworkGenerator persongraphgen = new GephiNetworkGenerator(personGraph, getGexfPersonNetworkFile());
            persongraphgen.script();
        }
        if (getGexfSubjectNetworkFile() != null) {
            GephiNetworkGenerator subjectgraphgen = new GephiNetworkGenerator(subjectGraph, getGexfSubjectNetworkFile());
            subjectgraphgen.script();
        }

    }

    /**
     *
     * @param originalDir
     * @param parsedDir
     * @param uniqueDir
     * @param duplicateDir
     * @param metadataXLSFile
     * @param uniqueXLSFile
     * @param duplicateXLSFile
     */
    public void parseAndDeduplicate(String originalDir, String parsedDir, String uniqueDir, String duplicateDir, String metadataXLSFile, String uniqueXLSFile, String duplicateXLSFile) {
        if (originalDir == null || parsedDir == null || uniqueDir == null || duplicateDir == null || metadataXLSFile == null || uniqueXLSFile == null || duplicateXLSFile == null) {
            throw new RuntimeException("All of the parameters of parseAndDuplicate should have valid values");
        }
        parseAndDeduplicate(originalDir, parsedDir, uniqueDir, duplicateDir, metadataXLSFile, uniqueXLSFile, duplicateXLSFile, true);
    }

    /**
     *
     * @param originalDir
     * @param parsedDir
     * @param uniqueDir
     * @param duplicateDir
     * @param metadataXLSFile
     * @param uniqueXLSFile
     * @param duplicateXLSFile
     * @param genOutputFiles
     */
    public void parseAndDeduplicate(String originalDir, String parsedDir, String uniqueDir, String duplicateDir, String metadataXLSFile, String uniqueXLSFile, String duplicateXLSFile, boolean genOutputFiles) {
        this.fromDirectory = originalDir;
        this.toDirectory = parsedDir;
        this.uniqueDirectory = uniqueDir;
        this.duplicateDirectory = duplicateDir;
        this.metadataFile = metadataXLSFile;
        this.uniqueFile = uniqueXLSFile;
        this.duplicateFile = duplicateXLSFile;
        this.genOutputFiles = genOutputFiles;

        clearAllVariables();
        //   final String fileNameListFile = baseDirectory + "files_to_store.txt";
        //    subectsOutputDir = baseDirectory + "subject";

        /*      try {
         metadatafw = new FileWriter(metadataFile);
         metadatapw = new PrintWriter(metadatafw);
         } catch (IOException ex) {
         Logger.getLogger(LexisDataToDB.class.getName()).log(Level.SEVERE, null, ex);
         }
         */
        //hsIdsOfConsideredTexts = this.getIdsOfTextsFromList();
        //prepareDB();
        //fills db and folder with ALL texts
        if (genOutputFiles) {
            initializeDir(toDirectory);
            initializeDir(uniqueDir);
            if (getTextBodyDuplicateDirectory() != null) {
                initializeDir(getTextBodyDuplicateDirectory());
            }
            if (getTextBodyToDirectory() != null) {
                initializeDir(getTextBodyToDirectory());
            }
            if (getTextBodyUniqueDirectory() != null) {
                initializeDir(getTextBodyUniqueDirectory());
            }
        }
        processTexts();

        if (genOutputFiles) {
            LxNxExcelWriter.writetoFile(metadataXLSFile, originalList);
        }
        // recognizeDuplicates(metadataList);
        //in here switch for dedpuing during oad or not
        // stores only selected files as specified per hand in DB
        //saveSelectedFilesAsPlainText(fromDirectory, toDirectory, fileNameListFile);

        removeDuplicatedTextsFromDB();

        if (genOutputFiles) {
            LxNxExcelWriter.writetoFile(duplicateFile, duplicateList);
            LxNxExcelWriter.writetoFile(uniqueFile, uniqueList);
            if (getUniqueAllTextFile() == null) {
                setUniqueAllTextFile(toDirectory + fileSeperator + "AllUniqueText.txt");
            }
            File allTextFile = new File(getUniqueAllTextFile());
            for (LxNxMetadata data : uniqueList) {
                File fromFile = new File(toDirectory + fileSeperator + data.getBestDate() + "_" + data.getTextID() + ".txt");
                File toFile = new File(uniqueDir + fileSeperator + data.getBestDate() + "_" + data.getTextID() + ".txt");
                copyFile(fromFile, toFile);
                if (getTextBodyUniqueDirectory() != null) {
                    copyAndRefineFile(uniqueDir + fileSeperator + data.getBestDate() + "_" + data.getTextID() + ".txt", getTextBodyUniqueDirectory() + fileSeperator + data.getBestDate() + "_" + data.getTextID() + ".txt");
                    if (getUniqueAllTextFile() != null) {
                        try {
                            String file1Str = FileUtils.readFileToString(new File(getTextBodyUniqueDirectory() + fileSeperator + data.getBestDate() + "_" + data.getTextID() + ".txt"));
                            FileUtils.write(allTextFile, file1Str, true);
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }

            }
        }
        //storeSubjectsPerCorpus();
    }

    private void removeDuplicatedTextsFromDB() {
        int max_domain = 20;

        for (int i = 0; i < originalList.size(); i++) {
            final int textID = Integer.parseInt(originalList.get(i).getTextID());
            textIDMap.put(textID, originalList.get(i));
            docGraph.put(textID, new ArrayList<Integer>());
        }
        for (int i = 0; i < originalList.size(); i++) {
            for (int j = 1; j < max_domain; j++) {
                int ind = i - j;
                if (ind < 0) {
                    break;
                }
                if (isSimilar(originalList.get(i), originalList.get(ind))) {
                    int iID = Integer.parseInt(originalList.get(i).getTextID());
                    int indID = Integer.parseInt(originalList.get(ind).getTextID());
                    docGraph.get(iID).add(indID);
                    docGraph.get(indID).add(iID);
                }
            }
        }

        for (Integer docID : docGraph.keySet()) {
            mark.put(docID, false);
        }

        for (Integer docID : docGraph.keySet()) {
            if (mark.get(docID).equals(false)) {
                duplicateMap.put(docID, new ArrayList<Integer>());
                dfs(docID, docID);
            }
        }

        if (genOutputFiles) {
            initializeDir(duplicateDirectory);
        }
        for (Integer docID : duplicateMap.keySet()) {
            uniqueList.add(textIDMap.get(docID));
            //  duplicateList.add(textIDMap.get(docID));
            if (duplicateMap.get(docID).size() > 1) {
                for (Integer others : duplicateMap.get(docID)) {
                    final LxNxMetadata otherObj = textIDMap.get(others);
                    duplicateList.add(otherObj);
                    if (!docID.equals(others)) {
                        if (genOutputFiles) {
                            File fromFile = new File(toDirectory + fileSeperator + otherObj.getBestDate() + "_" + otherObj.getTextID() + ".txt");
                            File toFile = new File(duplicateDirectory + fileSeperator + otherObj.getBestDate() + "_" + otherObj.getTextID() + ".txt");
                            copyFile(fromFile, toFile);
                            if (getTextBodyDuplicateDirectory() != null) {
                                copyAndRefineFile(duplicateDirectory + fileSeperator + otherObj.getBestDate() + "_" + otherObj.getTextID() + ".txt", getTextBodyDuplicateDirectory() + fileSeperator + otherObj.getBestDate() + "_" + otherObj.getTextID() + ".txt");
                            }
                        }
                    }
                }
            }
        }
    }

    private void dfs(Integer root, Integer docID) {
        mark.put(docID, true);
        duplicateMap.get(root).add(docID);
        originalList.get(docID).setRefDoc(root + "");
        for (Integer other : docGraph.get(docID)) {
            if (mark.get(other).equals(false)) {
                dfs(root, other);
            }
        }
    }

    private boolean isValid(String str) {
        if (str != null && str.length() > 1) {
            return true;
        }
        return false;
    }

    private boolean isEqual(String str1, String str2) {
        if (isValid(str1) && isValid(str2)) {
            if (str1.equals(str2)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSimilar(LxNxMetadata d1, LxNxMetadata d2) {
        double totalDiff = 0;
        double threshold = 15;
        if (isEqual(d1.getTitle(), d2.getTitle())) {
            return true;
        }
        totalDiff += DistanceUtil.editDistance(d1.getSubject(), d2.getSubject());

        totalDiff += DistanceUtil.editDistance(d1.getAuthor(), d2.getAuthor());
        //totalDiff += DistanceUtil.editDistance(d1.getBestDate(), d2.getBestDate());

        totalDiff += DistanceUtil.editDistance(d1.getPubType(), d2.getPubType());
        totalDiff += DistanceUtil.editDistance(d1.getSource(), d2.getSource());
        //     totalDiff += DistanceUtil.editDistance(d1.getTitle(), d2.getTitle());
        if (Math.abs(d1.getCleanLength() - d2.getCleanLength()) > 10) {
            totalDiff += 2;
        }
        if (totalDiff < threshold) {
            return true;
        }
        return false;

    }

    private void copyFile(File fromFile, File toFile) {
        try {
            FileUtils.copyFile(fromFile, toFile);
        } catch (IOException ex) {
            Logger.getLogger(LxNxDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processTexts() {
        try {

            File d = new File(fromDirectory);

            int counter = 0;
            if (d.isDirectory()) {
                File[] fs = d.listFiles();
                for (int i = 0; i < fs.length; i++) {
                    counter = counter + 1;
                    File f = fs[i];
                    System.out.println(this.getClass().getName() + ".processTexts() file: " + f.getAbsolutePath());
                    year = f.getName();
                    System.out.println("year" + year);
                    year = year.substring(0, year.length() - 7);
                    if (f.getName().toLowerCase().endsWith(".txt")) {
                        if (f.length() != 0) {
                            this.extractContentFromFile(f);
                            //System.out.print(counter+"/"+fs.length +" ");

                        }
                    }
                }
                System.out.println(counter);
            }

        } catch (Exception e) {
            System.out.println(this.getClass().getName()
                    + ".LexisDataToDB() error: " + e);
            e.printStackTrace();
        }// END catch
    }

    /**
     *
     * @param dir
     */
    public static void initializeDirSafe(String dir) {
        try {
            FileUtils.forceMkdir(new File(dir));
        } catch (IOException ex) {
            Logger.getLogger(LxNxDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dir
     */
    public static void initializeDir(String dir) {
        try {
            FileUtils.deleteDirectory(new File(dir));
            FileUtils.forceMkdir(new File(dir));
        } catch (IOException ex) {
            Logger.getLogger(LxNxDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void extractContentFromFile(File f) {
        try {
            System.out.print("extractContentFromFile:" + f.getName());
            //FileReader fr = new FileReader(f);
            FileInputStream  fis = new FileInputStream(f);
            InputStreamReader streamReader = new InputStreamReader(fis, "UTF-8");
            LineNumberReader lnr = new LineNumberReader(streamReader);
            String line = "";
            StringBuffer sb = new StringBuffer();
            int textCounter = 0;
            boolean beforeArticlesPart = true;
            while ((line = lnr.readLine()) != null) {
                if (line != null && line.contains(" DOCUMENTS")) {
                    // System.out.print(line.trim() + "; " + f.getName());
                    beforeArticlesPart = false;
                }
                if (beforeArticlesPart) {
                    continue;
                }
                // System.out.println("line: "+line);
                if (line.contains(" DOCUMENTS") && line.contains(" of ")) {
                    // frage
                    // sb.append(line);
                    // System.out.println("sb: "+sb.toString());
                    String text = sb.toString().trim();
                    if (text.length() > 0) {
                        //	System.out.println(this.getClass().getName()+".extractContentFromFile() text2:"+text);
                        parseText(text);
                        textCounter++;
                    }
                    sb = new StringBuffer();
                } else {
                    // System.out.println("else line:"+line);
                    sb.append(line);
                    sb.append("\n");
                }
            }
            String text = sb.toString().trim();
            if (text.length() > 0) {
                parseText(text);
                textCounter++;
            }
            System.out.println(" textCounter: " + textCounter);
            // System.out.println(this);
            lnr.close();
        } catch (Exception e) {
            System.out.println(this.getClass().getName()
                    + ".extractContentFromFile() error: " + e);
            e.printStackTrace();
        }// END catch

    }

    private void parseText(String s) {
        try {
            int pos = s.indexOf("\n");
            String firstContentLine = "";
            if (pos > 0) {
                s.substring(0, pos);
            } else {
                firstContentLine = s;
            }
            // System.out.println("s: "+s);
            LxNxTextParser tp = new LxNxTextParser(s, "" + this.idCounter);

            HashSet<String> hsCharacteristicStrings = new HashSet<String>();
            TextDataSet tds = new TextDataSet();
            tds.id = tp.getTextID();
            tds.pubDate = tp.getBestDate();
            tds.title = tp.getTitle();

            //this is the switch to consider or ignore duplicates at the time of populating the database and saving the clean text files
            boolean includeDuplicates = true;

            if (includeDuplicates || !hsCharacteristicStrings.contains(tds.getCharacteristicString())) {
                hsCharacteristicStrings.add(tds.getCharacteristicString());
                idCounter = idCounter + 1;
                if (genOutputFiles) {
                    this.storeTexts(s, tp.getTextID(), tp.getBestDate());
                }
                // test for output lenght
                // tp.cleanLength();
                // tp.getDateAsDate();
                HashSet<String> subjectsPerText = tp.getSubjectsPerText();

                for (String s2 : subjectsPerText) {
                    // figure out details about text formatting
                    char c = s2.toCharArray()[0];
                    boolean b = Character.isLetterOrDigit(c);
                    if (!b) {
                        // System.out.println("character:"+c+"---"+s2+" "+(c=='
                        // ')+Character.getNumericValue(c));
                    }

                    s2 = s2.trim();

                    if (disregardTextIdList || this.hsIdsOfConsideredTexts.contains(tp.getTextID())) {
                        Integer existingValue = this.htSubjectsPerData.get(s2);
                        if (existingValue == null) {
                            this.htSubjectsPerData.put(s2, 1);
                        } else {
                            this.htSubjectsPerData.put(s2, existingValue + 1);
                        }
                    }

                }

                if (false) {
                    // this is incomplete now
                    System.out.println("source: " + tp.getSource());
                    // System.out.println(s);
                    System.out.println("date: " + tp.getDate());
                    System.out.println("title: " + tp.getTitle());
                    System.out.println("author: " + tp.getAuthor());
                    System.out.println("section: " + tp.getSection());
                    System.out.println("length: " + tp.getLength());
                    System.out.println("length: " + tp.getSubject());

                    System.out.println("geo: " + tp.getGeo());
                    System.out.println("language: " + tp.getLanguage());
                    System.out.println("loadDate: " + tp.getLoadDate());
                    System.out.println("org: " + tp.getOrganization());
                    System.out.println("pubType: " + tp.getPubType());
                    System.out.println("graphic: " + tp.getGraphic());
                    System.out.println("person: " + tp.getPerson());
                    System.out.println("company: " + tp.getCompany());
                    System.out.println("ticker: " + tp.getTicker());
                    System.out.println("industry: " + tp.getIndustry());
                    System.out.println("journal_code: " + tp.getJournalCode());
                    System.out.println("city: " + tp.getCity());
                    System.out.println("ID: " + tp.getTextID());

                    System.out
                            .println("\n---------------------------------------------------------");
                }

                // fill db
                if (true) {
                    //System.out.println("hallo");
                    LxNxMetadata lxnxmetadata = new LxNxMetadata(tp);
                    originalList.add(lxnxmetadata);

                }
                // end fill db

            }

        } catch (Exception e) {
            System.out.println("parseText() error: " + e);
            e.printStackTrace();
        }// END catch
    }

    private void storeTexts(String text, String id, String date) {

        try {
            FileWriter fw = new FileWriter(toDirectory + fileSeperator + date + "_" + id + ".txt");
            PrintWriter pw = new PrintWriter(fw);
            String[] lines = text.split("\n");
            for (String line : lines) {
                pw.println(line);
            }
            pw.close();
            fw.close();
            if (getTextBodyToDirectory() != null) {
                copyAndRefineFile(toDirectory + fileSeperator + date + "_" + id + ".txt", getTextBodyToDirectory()
                        + fileSeperator + date + "_" + id + ".txt");
            }

        } catch (Exception e3) {
            System.out.println("storeTexts() error: " + e3);
            e3.printStackTrace();
        }// END catch
    }

    private void saveSelectedFilesAsPlainText(String fromDirectory, String toDirectory, String fileNameListFile) {
        try {
            File d = new File(toDirectory);
            d.delete();
            d = new File(toDirectory);
            d.mkdirs();

            HashSet<String> hsFileNames = new HashSet<String>();
            FileReader fr = new FileReader(fileNameListFile);
            LineNumberReader lnr = new LineNumberReader(fr);
            String line = "";
            while ((line = lnr.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    hsFileNames.add(line.toLowerCase());
                }
            }
            lnr.close();
            fr.close();

            int numberOfFilesToCopy = hsFileNames.size();
            int numberOfFilesToReview = 0;
            System.out.println("number of files to copy:" + hsFileNames.size());
            File fromDir = new File(fromDirectory);
            File toDir = new File(toDirectory);
            if (fromDir.isDirectory()) {
                File[] fs = fromDir.listFiles();
                numberOfFilesToReview = fs.length;
                int filesToReviewCounter = 0;
                int filesToCopyCounter = 0;
                for (int i = 0; i < fs.length; i++) {
                    filesToReviewCounter++;
                    File f = fs[i];
                    System.out.println("testFileName:" + f.getName());
                    int startID = 0;
                    int endID = 0;
                    startID = f.getName().indexOf("_");
                    endID = f.getName().indexOf(".");
                    String idOfText = f.getName().substring(startID + 1, endID);
                    System.out.println("idOfText: " + idOfText);
                    if (hsFileNames.contains(idOfText)) {
                        filesToCopyCounter++;
                        System.out.println("copyFile:" + f.getName());
                        copyAndRefineFile(f.getAbsolutePath(), toDir + fileSeperator + f.getName());
                        if (filesToCopyCounter % 100 == 0) {
                            System.out.println("saveSelectedFilesAsPlainText: review: " + filesToReviewCounter + fileSeperator + numberOfFilesToReview + "  " + filesToCopyCounter + "/" + numberOfFilesToCopy);
                        }
                    }
                }
                // System.out.println(counter);
            }

        } catch (Exception e3) {
            System.out.println("creatSelectedFilesDirectory() error: " + e3);
            e3.printStackTrace();
        }// END catch

    }

    private void copyAndRefineFile(String fromFile, String toFile) {
        try {
            //System.out.println("copyAndRefineFile: "+toFile);
            FileReader fr = new FileReader(fromFile);
            LineNumberReader lnr = new LineNumberReader(fr);
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = lnr.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            lnr.close();
            fr.close();
            LxNxTextParser tp = new LxNxTextParser(sb.toString(), "");

            String text = tp.getTextBody();
            //System.out.println("sb.toString(): " + sb.toString());
            //System.out.println("text: " + text);
            File file = new File(toFile);
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);
            String[] lines = text.split("\n");
            for (String l : lines) {
                pw.println(l);
            }
            pw.close();
            fw.close();

        } catch (FileNotFoundException ex) {
            System.out
                    .println(ex.getMessage() + " in the specified directory.");
            ex.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //
    }

    private void storeSubjectsPerCorpus() {
        try {
            FileWriter fw = new FileWriter(subectsOutputDir + "subjects2.csv");
            PrintWriter pw = new PrintWriter(fw);
            for (String s : htSubjectsPerData.keySet()) {
                pw.println(s.trim() + "\t " + this.htSubjectsPerData.get(s));
            }
            pw.close();
            fw.close();
        } catch (Exception e4) {
            System.out.println("storeSubjectsPerCorpus() error: " + e4);
            e4.printStackTrace();
        }// END catch
    }

    private void storeSubjectsPerSet() {
        try {
            HashSet<String> hsidsForSubject = this.getIdsOfTextsFromList();
            FileWriter fw = new FileWriter(subectsOutputDir + "subjects2.csv");
            PrintWriter pw = new PrintWriter(fw);
            for (String s : htSubjectsPerData.keySet()) {
                if (hsidsForSubject.contains(s)) {
                    pw.println(s.trim() + "\t " + this.htSubjectsPerData.get(s));
                }
            }
            pw.close();
            fw.close();
        } catch (Exception e4) {
            System.out.println("storeSubjectsPerCorpus() error: " + e4);
            e4.printStackTrace();
        }// END catch
    }

    private HashSet<String> getIdsOfTextsFromList() {
        HashSet<String> hs = new HashSet<String>();
        try {
            //FileReader fr = new FileReader("C:\\myMind\\projects\\sudan\\ids_for_subject_after_sports.txt");
            FileReader fr = new FileReader("C:\\myMind\\projects\\IAN_counterterrorism\\ids_for_subject_after_sports.txt");
            LineNumberReader lnr = new LineNumberReader(fr);
            String line = "";
            while ((line = lnr.readLine()) != null) {
                line = line.trim();
                hs.add(line);
            }
            lnr.close();
            fr.close();
        } catch (Exception e5) {
            System.out.println("storeSubjectsPerSet() error: " + e5);
            e5.printStackTrace();
        }// END catch
        return hs;
    }

    private void removeDuplicatedTextsFromDB(String duplicateIdsFile) {
        try {

            //FileReader fr = new FileReader("C:\\myMind\\projects\\sudan\\id_duplicates.txt");
            FileReader fr = new FileReader(duplicateIdsFile);

            LineNumberReader lnr = new LineNumberReader(fr);
            String line = "";
            StringBuffer sb = new StringBuffer();
            int numberOfLines = 0;
            int counter = 0;

            while ((line = lnr.readLine()) != null) {
                numberOfLines++;
            }
            //fr = new FileReader("C:\\myMind\\projects\\sudan\\id_duplicates.txt");
            fr = new FileReader(duplicateIdsFile);
            lnr = new LineNumberReader(fr);
            while ((line = lnr.readLine()) != null) {
                line = line.trim();

                //TODO : delete record in metadata file
                //st.execute("DELETE from 6_moreRedundantsOut WHERE ID='" + line + "'");
                System.out.println((counter++) + "/" + "numberOfLines: " + numberOfLines);
            }
            lnr.close();
            fr.close();

        } catch (Exception e5) {
            System.out.println("removeDuplicatedTextsFromDB() error: " + e5);
            e5.printStackTrace();
        }// END catch
    }

    private void removeDuplicates() {
        try {
            //TODO : fix this for metadata
            Connection con = null; /*this.getConnection();*/

            PreparedStatement ps = con.prepareStatement("SELECT * FROM texts_orig");
            ResultSet rs = ps.executeQuery();

            Hashtable<String, Vector<TextDataSet>> htData = new Hashtable<String, Vector<TextDataSet>>();
            HashSet<String> hsIdsOfDuplicates = new HashSet<String>();

            while (rs.next()) {
                TextDataSet tds = new TextDataSet();
                String id = rs.getString("ID");
                System.out.println(this.getClass().getName() + ".removeDuplicates() id " + id);
                /*
                 tds.id = rs.getString("ID");
                 System.out.println(this.getClass().getName()+".removeDuplicates() tds.id "+tds.id);
                 tds.title = rs.getString("Title");
				
                 tds.pubDate="";
                 //System.out.println(this.getClass().getName()+".removeDuplicates() tds.id "+tds.id);
                 //tds.pubDate = rs.getString("PubDate");
				
				
                 Vector<TextDataSet> v = htData.get(tds.getCharacteristicString());
                 if(v==null){
                 v=new Vector<TextDataSet>();
                 v.add(tds);
                 if(tds.id!=null && tds.pubDate!=null && tds.title!=null){
                 htData.put(tds.getCharacteristicString(), v);
                 }
                 }else{
                 TextDataSet existingDataSet = v.get(0);
                 System.out.println(this.getClass().getName()+".removeDuplicates() douplicate id "+tds.id+" of existing id "+existingDataSet.id);
                 hsIdsOfDuplicates.add(tds.id);
                 }
                 */

            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ".removeDuplicates() error:" + e);
            e.printStackTrace();
        }
    }

    private void addToGraph(List<CodebookEntity> entityList, Map<CodebookEntity, List<MyPair<CodebookEntity, Integer>>> graph) {
        addToGraph(entityList, graph, 75);
    }

    private void addToGraph(List<CodebookEntity> entityList, Map<CodebookEntity, List<MyPair<CodebookEntity, Integer>>> graph, int threshold_) {
        for (CodebookEntity first : entityList) {
            for (CodebookEntity sec : entityList) {
                if (first.getName().compareTo(sec.getName()) > 0) {
                    if ((first.getPercent() > threshold_) && (sec.getPercent() > threshold_)) {
                        if (!graph.containsKey(first)) {
                            graph.put(first, new ArrayList<MyPair<CodebookEntity, Integer>>());
                        }
                        int index = getPairIndex(first, sec, graph);
                        if (index != -1) {
                            int weigth = graph.get(first).get(index).getSecond();
                            graph.get(first).get(index).setSecond(weigth + 1);
                        } else {
                            graph.get(first).add(new MyPair<CodebookEntity, Integer>(sec, 1));
                        }
                    }
                }
            }
        }
    }

    private void addToGraph(List<CodebookEntity> entityList1, List<CodebookEntity> entityList2, Map<CodebookEntity, List<MyPair<CodebookEntity, Integer>>> graph, int threshold_) {
        for (CodebookEntity first : entityList1) {
            for (CodebookEntity sec : entityList2) {
                if ((first.getPercent() > threshold_) && (sec.getPercent() > threshold_)) {
                    if (!graph.containsKey(first)) {
                        graph.put(first, new ArrayList<MyPair<CodebookEntity, Integer>>());
                    }
                    int index = getPairIndex(first, sec, graph);
                    if (index != -1) {
                        int weigth = graph.get(first).get(index).getSecond();
                        graph.get(first).get(index).setSecond(weigth + 1);
                    } else {
                        graph.get(first).add(new MyPair<CodebookEntity, Integer>(sec, 1));
                    }
                }
            }
        }
        //  System.out.println("addToGraph: entity#"+ entityList.size()+" weight#: "+totalWeight );
    }

    private Integer getPairIndex(CodebookEntity first, CodebookEntity sec, Map<CodebookEntity, List<MyPair<CodebookEntity, Integer>>> graph) {
        for (int i = 0; i < graph.get(first).size(); i++) {
            MyPair<CodebookEntity, Integer> pair = graph.get(first).get(i);
            if (pair.getFirst().equals(sec)) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @return
     */
    public String getTextBodyToDirectory() {
        return textBodyToDirectory;
    }

    /**
     *
     * @param textBodyToDirectory
     */
    public void setTextBodyToDirectory(String textBodyToDirectory) {
        this.textBodyToDirectory = textBodyToDirectory;
    }

    /**
     *
     * @return
     */
    public String getTextBodyDuplicateDirectory() {
        return textBodyDuplicateDirectory;
    }

    /**
     *
     * @param textBodyDuplicateDirectory
     */
    public void setTextBodyDuplicateDirectory(String textBodyDuplicateDirectory) {
        this.textBodyDuplicateDirectory = textBodyDuplicateDirectory;
    }

    /**
     *
     * @return
     */
    public String getTextBodyUniqueDirectory() {
        return textBodyUniqueDirectory;
    }

    /**
     *
     * @param textBodyUniqueDirectory
     */
    public void setTextBodyUniqueDirectory(String textBodyUniqueDirectory) {
        this.textBodyUniqueDirectory = textBodyUniqueDirectory;
    }

    /**
     *
     * @return
     */
    public String getUniqueAllTextFile() {
        return uniqueAllTextFile;
    }

    /**
     *
     * @param uniqueAllTextFile
     */
    public void setUniqueAllTextFile(String uniqueAllTextFile) {
        this.uniqueAllTextFile = uniqueAllTextFile;
    }

    /**
     *
     * @return
     */
    public String getGexfAllNetworkFile() {
        return gexfAllNetworkFile;
    }

    /**
     *
     * @param gexfAllNetworkFile
     */
    public void setGexfAllNetworkFile(String gexfAllNetworkFile) {
        this.gexfAllNetworkFile = gexfAllNetworkFile;
    }

    /**
     *
     * @return
     */
    public String getGexfPersonNetworkFile() {
        return gexfPersonNetworkFile;
    }

    /**
     *
     * @param gexfPersonNetworkFile
     */
    public void setGexfPersonNetworkFile(String gexfPersonNetworkFile) {
        this.gexfPersonNetworkFile = gexfPersonNetworkFile;
    }

    /**
     *
     * @return
     */
    public String getGexfSubjectNetworkFile() {
        return gexfSubjectNetworkFile;
    }

    /**
     *
     * @param gexfSubjectNetworkFile
     */
    public void setGexfSubjectNetworkFile(String gexfSubjectNetworkFile) {
        this.gexfSubjectNetworkFile = gexfSubjectNetworkFile;
    }

    /**
     *
     * @param type1
     * @param mdata
     * @return
     */
    public List<CodebookEntity> getCodebookEntities(MetadataType type1, LxNxMetadata mdata) {
        List<CodebookEntity> type_cbs = null;
        if (type1.equals(MetadataType.PERSON)) {
            type_cbs = CodebookEntity.parseLine(mdata.getPerson(), "agent", "specific", mdata.getTextID());
        } else if (type1.equals(MetadataType.LOCATION)) {
            type_cbs = CodebookEntity.parseLine(mdata.getGeo(), "location", "specific", mdata.getTextID());
        } else if (type1.equals(MetadataType.ORGANIZATION)) {
            type_cbs = CodebookEntity.parseLine(mdata.getOrganization(), "organization", "specific", mdata.getTextID());
        } else if (type1.equals(MetadataType.SUBJECT)) {
            type_cbs = CodebookEntity.parseLine(mdata.getSubject(), "knowledge", "specific", mdata.getTextID());
        } else {
            System.out.println("Invalid type in getCodebookEntities");
        }
        return type_cbs;
    }

    private void writeGraphToCSV(Map<CodebookEntity, List<MyPair<CodebookEntity, Integer>>> graph, CSVWriter csvWriter) {
        for (CodebookEntity node : graph.keySet()) {
            for (MyPair<CodebookEntity, Integer> other : graph.get(node)) {
                CodebookEntity node2 = other.getFirst();
                float weight = other.getSecond();
                csvWriter.append(node.getName() + "," + node2.getName() + "," + weight + "," + "Undirected");
            }
        }
    }
}
