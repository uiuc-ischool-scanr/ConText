package context.core.task.lexisnexis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jana Diesner
 */
public class LxNxTextParser {

    private String text = "";
    private String id = "";
    static HashSet<String> hsFirstKeyWords = new HashSet<String>();
    static HashSet<String> hsLastKeyWords = new HashSet<String>();
    static HashSet<String> hsLineKeyWords = new HashSet<String>();
    static boolean setupComplete = false;

    /**
     *
     * @param textToParse
     * @param idperText
     */
    public LxNxTextParser(String textToParse, String idperText) {
        this.text = textToParse;
        this.id = idperText;
        // System.out.println("text: "+text);
        if (!setupComplete) {
            hsFirstKeyWords.add("\nBYLINE:");
            hsFirstKeyWords.add("\nSECTION:");
            hsFirstKeyWords.add("\nLENGTH:");

            hsLastKeyWords.add("\nSUBJECT:");
            hsLastKeyWords.add("\nCOMPANY:");
            hsLastKeyWords.add("\nCOUNTRY:");
            hsLastKeyWords.add("\nCITY:");
            hsLastKeyWords.add("\nLOAD-DATE:");
            hsLastKeyWords.add("\nPERSON:");
            hsLastKeyWords.add("\nCOUNTRY:");
            hsLastKeyWords.add("\nSTATE:");
            hsLastKeyWords.add("\nORGANIZATION:");
            hsLastKeyWords.add("\nGEOGRAPHIC:");
            
            hsLastKeyWords.add("\nLANGUAGE:");
            hsLastKeyWords.add("\nPUBLICATION-TYPE:");
            
            hsLastKeyWords.add("\nGRAPHIC:");
            hsLastKeyWords.add("\nPHOTO:");
            hsLastKeyWords.add("\nPhoto:");
            hsLastKeyWords.add("\nColor Photo:");
            
            hsLastKeyWords.add("\nELEMENT-WITNESS:");
            hsLastKeyWords.add("\nBLOCK-TIME:");
            
            hsLastKeyWords.add("\n(c) Copyright");
            // hsLastKeyWords.add("passage omitted");

            hsLineKeyWords.add("Text of report by");

            hsLineKeyWords.add("Text of report in");
            hsLineKeyWords.add("Excerpt from report by");
            hsLineKeyWords.add("DATELINE: ");
            hsLineKeyWords.add("HIGHLIGHT: ");
            hsLineKeyWords.add("SOURCE: ");
            hsLineKeyWords.add("Source: ");
            hsLineKeyWords.add("IN BRIEF");
            setupComplete = true;
        }
    }

    /**
     *
     * @return
     */
    public String getSource() {

        String workCopy = text;
        workCopy = workCopy.trim();
        String[] lines = workCopy.split("\n");
        // System.out.println(lines.length);
        // System.out.println(workCopy);
        String source = "";

        for (int i = 0; i < lines.length - 1; i++) {
            String line = lines[i];

            // System.out.println("test");
            String nextLine = lines[i + 1].trim();
            if (nextLine.length() == 0 && line.length() > 0) {
                source = source + line;

                // System.out.println(nextLine);
                i = lines.length + 1;
            } else {
                source = source + line;
                // System.out.println("test2");
            }
        }
        return source.trim();

    }

    /**
     *
     * @return
     */
    public String getTitle() {
        String workCopy = text.trim();
        String[] chunks = workCopy.split("\n\n");
        Vector<String> contentChunks = new Vector<String>();
        for (int i = 0; i < chunks.length; i++) {
            if (chunks[i].trim().length() > 0) {
                contentChunks.add(chunks[i]);
            }
        }
        if (contentChunks.size() > 2) {
            return contentChunks.get(2).trim();
        } else {
            return "";
        }

    }

    /**
     *
     * @return
     */
    public String getAuthor() {
        String sAuthor = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("BYLINE: ")) {
                sAuthor = line.substring("BYLINE: ".length()).trim();
            }
        }
        return sAuthor;
    }

    /**
     *
     * @return
     */
    public String getSection() {
        String sSection = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("SECTION: ")) {
                sSection = line.substring("Section: ".length()).trim();
            }
        }
        return sSection;
    }

    /**
     *
     * @return
     */
    public String getLength() {
        String sLength = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("LENGTH: ")) {
                sLength = line.substring("LENGTH: ".length()).trim();
            }
        }
        return sLength;
    }

    /**
     *
     * @return
     */
    public String getSubject() {
        String sSubject = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("SUBJECT: ")) {
                sSubject = line.substring("SUBJECT: ".length()).trim();
            }
        }
        return sSubject;
    }

    /**
     *
     * @return
     */
    public String getOrganization() {
        String sOrganization = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("ORGANIZATION: ")) {
                sOrganization = line.substring("ORGANIZATION: ".length())
                        .trim();
            }
        }
        return sOrganization;
    }

    /**
     *
     * @return
     */
    public String getGeo() {
        String sGeo = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("GEOGRAPHIC: ")) {
                sGeo = line.substring("GEOGRAPHIC: ".length()).trim();
            }
        }
        // System.out.println("sGeo: "+sGeo);
        return sGeo;
    }

    /**
     *
     * @return
     */
    public String getCountry() {
        String sCountry = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("COUNTRY: ")) {
                sCountry = line.substring("COUNTRY: ".length()).trim();
            }
        }
        return sCountry;
    }

    /**
     *
     * @return
     */
    public String getLanguage() {
        String sLanguage = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("LANGUAGE: ")) {
                sLanguage = line.substring("LANGUAGE: ".length()).trim();
            }
        }
        return sLanguage;
    }

    /**
     *
     * @return
     */
    public String getPubType() {
        String sPubType = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("PUBLICATION-TYPE: ")) {
                sPubType = line.substring("PUBLICATION-TYPE: ".length()).trim();
            }
        }
        return sPubType;
    }

    /**
     *
     * @return
     */
    public String getPerson() {
        String sPerson = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("PERSON: ")) {
                sPerson = line.substring("PERSON ".length()).trim();
            }
        }
        return sPerson;
    }

    /**
     *
     * @return
     */
    public String getCompany() {
        String sCompany = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("COMPANY: ")) {
                sCompany = line.substring("COMPANY ".length()).trim();
            }
        }
        return sCompany;
    }

    /**
     *
     * @return
     */
    public String getGraphic() {
        String sGraphic = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("GRAPHIC: ")) {
                sGraphic = line.substring("GRAPHIC ".length()).trim();
            }
            if (line.startsWith("Photo:")){
            	sGraphic=sGraphic+line.trim();
            }
            if (line.startsWith("Color Photo:")){
            	sGraphic=sGraphic+line.trim();
            }
            
        }
        return sGraphic;
    }
    
    /**
    *
    * @return
    */
    public String getElementWitness() {
        String sElementWitness = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("ELEMENT-WITNESS: ")) {
                sElementWitness = line.substring("ELEMENT-WITNESS ".length()).trim();
            }
        }
        return sElementWitness;
    }
   
    /**
    *
    * @return
    */
    public String getBlockTime() {
        String sBlockTime = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("BLOCK-TIME: ")) {
                sBlockTime = line.substring("BLOCK-TIME ".length()).trim();
            }
        }
        return sBlockTime;
    }

    
    
    

    /**
     *
     * @return
     */
    public String getTicker() {
        String sTicker = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("TICKER: ")) {
                sTicker = line.substring("TICKER ".length()).trim();
            }
        }
        return sTicker;
    }

    /**
     *
     * @return
     */
    public String getJournalCode() {
        String sJournalCode = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("JOURNAL-CODE: ")) {
                sJournalCode = line.substring("JOURNAL-CODE ".length()).trim();
            }
        }
        return sJournalCode;
    }

    /**
     *
     * @return
     */
    public String getIndustry() {
        String sIndustry = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("INDUSTRY: ")) {
                sIndustry = line.substring("INDUSTRY ".length()).trim();
            }
        }
        return sIndustry;
    }

    /**
     *
     * @return
     */
    public String getCity() {
        String sCity = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("CITY: ")) {
                sCity = line.substring("CITY ".length()).trim();
            }
        }
        return sCity;
    }

    /**
     *
     * @return
     */
    public Vector<String> getTextChunks() {
        String workCopy = text.trim();
        // System.out.println("workCopy: "+text);
        String[] chunks = workCopy.split("\n\n");
        Vector<String> contentChunks = new Vector<String>();
        for (int i = 0; i < chunks.length; i++) {
            if (chunks[i].trim().length() > 0) {
                contentChunks.add(chunks[i].trim());
            }
        }
        return contentChunks;
    }

    // stuff before actual text body

    /**
     *
     * @return
     */
        public String getTextBody() {
        int highestKeywordPos = 0;
        for (String keyword : this.hsFirstKeyWords) {
            int pos = this.text.indexOf(keyword);
            if (pos > highestKeywordPos) {
                highestKeywordPos = pos;
                // System.out.println("getTextBody : highestKeywordPos:
                // "+highestKeywordPos+" keyword:"+keyword);

            }
        }
        highestKeywordPos = text.indexOf("\n", highestKeywordPos + 1);
        // stuff after actual text body
        int lowestKeywordPos = this.text.length();
        for (String keyword : this.hsLastKeyWords) {
            int pos = this.text.indexOf(keyword);
            if (pos < lowestKeywordPos && pos != -1) {
                // System.out.println("getTextBody : lowestKeywordPos:
                // "+lowestKeywordPos+" keyword:"+keyword);

                lowestKeywordPos = pos;
            }
        }

        if (lowestKeywordPos < highestKeywordPos) {
            // System.out.println("getTextBody problem:
            // lowestKeywordPos"+lowestKeywordPos+"
            // highestKeywordPos:"+highestKeywordPos);
            lowestKeywordPos = highestKeywordPos;
        }

        String textNow = this.text.substring(highestKeywordPos,
                lowestKeywordPos);
        String[] lines = textNow.split("\n");
        StringBuffer sb = new StringBuffer();
        for (String line : lines) {
            boolean noNoiseLine = true;
            for (String lineKeyword : this.hsLineKeyWords) {
                if (line.startsWith(lineKeyword)
                        || line.trim().startsWith(lineKeyword)) {
                    noNoiseLine = false;
                    // System.out.println("identified noise line: ----"+line+"
                    // ----");
                }
            }

            if (noNoiseLine) {
                sb.append(line);
                sb.append("\n");
                // System.out.println("good line:"+line);
            } else {
                // System.out.println("bad line:"+line);
            }

        }
        textNow = sb.toString();
        // System.out.println("textNow:"+textNow);

        /*
         * int endPos = text.indexOf("\nSUBJECT: "); if (endPos < 0) endPos =
         * text.length();
         */
        // System.out.println("getTextBody: "+t);
        // System.out.println("textNow:"+textNow);
        String t = this.getTitle().trim() + "." + "\n\n" + textNow;
        t = t.replace("passage omitted", "");
        t = t.replace("Passage omitted", "");
        // System.out.println("t:"+t);
        return t;
    }

    /**
     *
     * @return
     */
    public String getTextID() {
        return id;
    }

    /**
     *
     * @return
     */
    public int getCleanLength() {
        String length = this.getLength();
        Pattern p = Pattern.compile("([0-9]+)");
        Matcher m = p.matcher(length);
        int length2 = 0;
        if (m.find()) {
            length2 = java.lang.Integer.parseInt(m.group(1));
        }
        // System.out.println("length: " + length2);
        return length2;
    }

    /**
     *
     * @return
     */
    public String getLoadDate() {
        Vector<String> contentChunks = new Vector<String>();
        contentChunks = this.getTextChunks();
        String sLoadDate = "";
        for (String line : getTextChunks()) {
            if (line.startsWith("LOAD-DATE: ")) {
                sLoadDate = line.substring("LOAD-DATE: ".length()).trim();
            }
        }
        return sLoadDate;
    }

    /**
     *
     * @return
     */
    public String getDate() {
        Vector<String> contentChunks = new Vector<String>();
        contentChunks = this.getTextChunks();
        // System.out.println("content chnuks: "+ contentChunks.toString());
        String reply = "";
        if (contentChunks.size() > 1) {
            if (contentChunks.get(1).indexOf("\n") != -1) {
                reply = contentChunks.get(1).split("\n")[0].trim();
            }
        }
        return reply;
    }

    /**
     *
     * @return
     */
    public String getBestDate() {
        String reply = this.getDateAsDate(this.getDate());
        if (reply.equals("dateNotParsable")) {
            reply = this.getDateAsDate(this.getLoadDate());
        }
        return reply;
    }

    /**
     *
     * @param dateToParse
     * @return
     */
    public String getDateAsDate(String dateToParse) {
        String date = "";
        // String dateToParse = this.getDate();

        try {
            Date d = new Date();
            String month = "";
            String day = "";
            String year = "";
            int posFirstSpace = dateToParse.indexOf(" ");
            int posSecondSpace = dateToParse.indexOf(" ", posFirstSpace + 1);
            int posFirstComma = dateToParse.indexOf(", ");
            month = dateToParse.substring(0, posFirstSpace);
            day = dateToParse.substring(posFirstSpace, posSecondSpace - 1)
                    .trim();
            year = dateToParse.substring(posFirstComma);

            Pattern p = Pattern.compile("([0-9]+)");
            Matcher m = p.matcher(year);
            int year2 = 0;
            if (m.find()) {
                year2 = java.lang.Integer.parseInt(m.group(1));
            }

            // System.out.println("month: " + month);
            // System.out.println("day: " + day);
            // System.out.println("year: " + year2);
            int month2 = 0;
            if (month.equalsIgnoreCase("january")) {
                month2 = 0;
            } else if (month.equalsIgnoreCase("february")) {
                month2 = 1;
            } else if (month.equalsIgnoreCase("march")) {
                month2 = 2;
            } else if (month.equalsIgnoreCase("april")) {
                month2 = 3;
            } else if (month.equalsIgnoreCase("may")) {
                month2 = 4;
            } else if (month.equalsIgnoreCase("june")) {
                month2 = 5;
            } else if (month.equalsIgnoreCase("july")) {
                month2 = 6;
            } else if (month.equalsIgnoreCase("august")) {
                month2 = 7;
            } else if (month.equalsIgnoreCase("september")) {
                month2 = 8;
            } else if (month.equalsIgnoreCase("october")) {
                month2 = 9;
            } else if (month.equalsIgnoreCase("november")) {
                month2 = 10;
            } else if (month.equalsIgnoreCase("december")) {
                month2 = 11;
            }

            GregorianCalendar gc = new GregorianCalendar(year2, month2, Integer
                    .parseInt(day));
            d = gc.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(d);
            // System.out.println("date: " + date);

        } catch (Exception ex) {
            // System.out.println("getDateAsDate:"+ex);
            // ex.printStackTrace();
            date = "dateNotParsable";
            // System.out.println(date);
        }
        return date;

    }

    /**
     *
     * @return
     */
    public int getGeoSudanRatio() {
        int pos = -1;
        int ratioGeoSudanRatio = 0;
        String snumber = "";
        try {
            String geotext = this.getGeo();
            geotext = geotext.replace("\n", "");
            geotext = geotext.replace("\t", "");
            int newSearchStartingPoint = -1;
            while ((pos = geotext.indexOf("SUDAN", newSearchStartingPoint)) != -1) {
                try {
                    if (geotext.length() >= pos + 10) {
                        snumber = geotext.substring(pos + 6, pos + 10);

                        snumber = snumber.replace('%', ' ');
                        snumber = snumber.replace('(', ' ');
                        snumber = snumber.replace(')', ' ');
                        snumber = snumber.trim();
                        snumber = getNumberString(snumber);
                        // System.out.println("getGeoSudanRatio
                        // snumber:"+snumber);
                        if (snumber.length() > 0) {
                            int number = Integer.parseInt(snumber);
                            if (number > ratioGeoSudanRatio) {
                                ratioGeoSudanRatio = number;
                            }
                        }
                    }
                } catch (Exception e2) {
                    System.out
                            .println("getGeoSudanRatio could not read number: "
                                    + snumber + "--" + e2);
                }
                newSearchStartingPoint = pos + 1;
            }
        } catch (Exception e) {
            System.out.println("getGeoSudanRatio could not read number: "
                    + snumber + "--" + e);
            e.printStackTrace();
        }

        return ratioGeoSudanRatio;
    }

    private String getNumberString(String s) {
        StringBuffer sb = new StringBuffer();
        char[] cs = s.toCharArray();
        for (char c : cs) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public int getCountrySudanRatio() {
        int ratioCountrySudanRatio = this.getSudanRatio(this.getCountry(),
                "SUDAN");
        return ratioCountrySudanRatio;

    }

    /**
     *
     * @param input
     * @param keyword
     * @return
     */
    public int getSudanRatio(String input, String keyword) {
        int ratio = 0;
        try {
            int posBeingSudanChunk = input.indexOf(keyword);

            if (posBeingSudanChunk >= 0) {
                int posEndSudanChunk = input.indexOf(";", posBeingSudanChunk);
                if (posEndSudanChunk < posBeingSudanChunk) {
                    posEndSudanChunk = input.length() - 1;
                }
                String sudanChunk = input.substring(posBeingSudanChunk,
                        posEndSudanChunk);
                Pattern p = Pattern.compile("([0-9]+)");
                Matcher m = p.matcher(sudanChunk);
                if (m.find()) {
                    ratio = java.lang.Integer.parseInt(m.group(1));
                }
            }
        } catch (Exception ex) {
            System.out.println("getSudanRatio() error:" + ex);
            ex.printStackTrace();
            ratio = 0;
            // System.out.println(date);
        }
        // System.out.println("ratioSudan: " + ratio);
        return ratio;
    }

    /**
     *
     * @return
     */
    public HashSet<String> getSubjectsPerText() {
        HashSet<String> subjectsPerText = new HashSet<String>();
        String s = this.getSubject();
        s = s.replace("\n", " ");
        s = s.replace("\t", "");
        s = s.trim();
        String[] subjectChunks = s.split(";");
        for (String chunk : subjectChunks) {

            String subjects = "";
            chunk = chunk.trim();
            int posEnd = chunk.indexOf("(");
            if (posEnd > 0) {
                subjects = chunk.substring(0, posEnd);
            } else {
                subjects = chunk;
            }
            subjects = this.cleanString(subjects).trim();
            // System.out.println("subjects: " + subjects);
            if (subjects.length() > 0) {
                // System.out.println("getSubjectsPerText() chunk:--"+chunk+"--
                // subjects:--"+subjects+"--");
                subjectsPerText.add(subjects);
            } else {
                // System.out.println("subject of length 0; chunk:"+chunk);
            }
        }

        return subjectsPerText;

    }

    /**
     *
     * @param input
     * @return
     */
    public String cleanString(String input) {
        // System.out.println("cleanString");
        char[] inputText = input.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inputText.length; i++) {
            char c = inputText[i];
            // System.out.println("cleanString: c:"+c+"
            // numValue:"+Character.getNumericValue(c));

            if (Character.isLetter(c) || Character.isSpaceChar(c) || c == ' ') {
                int numericValue = Character.getNumericValue(inputText[i]);
                if (numericValue >= 0 && numericValue != 9) {
                    sb.append(inputText[i]);
                } else if (numericValue == -1) {
                    sb.append(' ');
                }
            }
        }
        return sb.toString().trim();
    }
    private static final String FIELD_SEPARATOR = ",";

    /**
     *
     * @return
     */
    public String toCSV() {
        StringBuffer str = new StringBuffer();
        str.append(getSource());
        str.append(FIELD_SEPARATOR);
        str.append(getBestDate());
        str.append(FIELD_SEPARATOR);
        str.append(getPubType());
        str.append(FIELD_SEPARATOR);
        str.append(getSection());
        str.append(FIELD_SEPARATOR);
        str.append(getCleanLength());
        str.append(FIELD_SEPARATOR);
        str.append(getJournalCode());
        str.append(FIELD_SEPARATOR);
        str.append(getLanguage());
        str.append(FIELD_SEPARATOR);
        str.append(getLoadDate());
        str.append(FIELD_SEPARATOR);
        str.append(getTitle());
        str.append(FIELD_SEPARATOR);
        str.append(getAuthor());
        str.append(FIELD_SEPARATOR);
        str.append(getGraphic());
        str.append(FIELD_SEPARATOR);
        str.append(getGeo());
        str.append(FIELD_SEPARATOR);
        str.append(getCity());
        str.append(FIELD_SEPARATOR);
        str.append(getOrganization());
        str.append(FIELD_SEPARATOR);
        str.append(getPerson());
        str.append(FIELD_SEPARATOR);
        str.append(getCompany());
        str.append(FIELD_SEPARATOR);
        str.append(getIndustry());
        str.append(FIELD_SEPARATOR);
        str.append(getTicker());
        str.append(FIELD_SEPARATOR);
        str.append(getSubject());
        //  str.append(Seperatar);
        //  str.append(this.year.trim());
        str.append(FIELD_SEPARATOR);
        str.append(getCountry());
        str.append(FIELD_SEPARATOR);
        str.append(getGeoSudanRatio());
        str.append(FIELD_SEPARATOR);
        str.append(getCountrySudanRatio());
        str.append(FIELD_SEPARATOR);
        str.append(getTextID());
        return str.toString();
    }
}
