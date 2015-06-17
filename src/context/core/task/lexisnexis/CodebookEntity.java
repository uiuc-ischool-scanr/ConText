/*
 * To change this template, choose Tools | Templates
 
 */
package context.core.task.lexisnexis;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author jkim362-admin
 */
public class CodebookEntity {

    private String text;
    private String name;
    private int percent;
    private String type;
    private String subtype;
    private static String pattern = "(.+?)\\((\\d+)%\\).*";
    private static List<String> rejectList = new ArrayList<String>();

    /**
     *
     * @return
     */
    public static List<String> getRejectList() {
        return rejectList;
    }

    /**
     *
     */
    public static void clearRejectList() {
        rejectList.clear();
    }

    /**
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     *
     * @param subtype
     */
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public int getPercent() {
        return percent;
    }

    /**
     *
     * @param percent
     */
    public void setPercent(int percent) {
        this.percent = percent;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public static String getPattern() {
        return pattern;
    }

    /**
     *
     * @param pattern
     */
    public static void setPattern(String pattern) {
        CodebookEntity.pattern = pattern;
    }

    /**
     *
     * @param line
     * @param type
     * @param subtype
     * @param textID
     * @return
     */
    public static List<CodebookEntity> parseLine(String line, String type, String subtype, String textID) {
        List<CodebookEntity> codebooks = new ArrayList<CodebookEntity>();
        if (line == null || line.length() == 0) {
            return codebooks;
        }
//        System.out.println("Starting parseLine for " + line);
        line = line.replace(',', ' ');
        line = line.replace("\u00A0", "");
//        System.out.println("replace u00A0: " + line);
        line = line.replaceAll("\\s+", " ");
//        System.out.println("replace s+ :" + line);
        String[] split = line.split(";");
        for (int i = 0; i < split.length; i++) {
            CodebookEntity cb = new CodebookEntity();
            cb.text = split[i].replaceFirst(pattern, "$1");
//            System.out.println("cb.text (1) :" + cb.text);
            cb.text = itrim(cb.text);
//            System.out.println("cb.text (2) :" + cb.text);
            cb.name = toNormalize(cb.text);
//            System.out.println("cb.name :" + cb.name);
            final String percentStr = split[i].replaceFirst(pattern, "$2");
            try {
                if (percentStr != null && percentStr.length() > 0) {
                    cb.percent = Integer.parseInt(percentStr);
                } else {
                    cb.percent = 0;
                }
            } catch (Exception ex) {
                // System.out.println("line=" + line + " end!");
                //  System.out.println("split " + i + " " + split[i] + " end2!");
                //  System.out.println("final " + split[i].replaceFirst(pattern, "$2") + " end3!");
                addToRejectList("ID:" + textID + " Type:" + type + " Text:" + line);
            }
            cb.type = type;
            cb.subtype = subtype;
            codebooks.add(cb);
        }
        return codebooks;
    }

    /**
     *
     * @param text
     * @return
     */
    public static String toNormalize(String text) {
        return WordUtils.capitalizeFully(text).replace(' ', '_');
    }

    @Override
    public String toString() {
        return "Text=" + this.text + "Name=" + this.name + " Percent=" + this.percent + " Type=" + this.type; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param line
     */
    public static void addToRejectList(String line) {
        rejectList.add(line);
    }

    /**
     *
     * @param str
     * @return
     */
    public static String itrim(String str) {
        str = str.replaceFirst("^[\\x00-\\x200\\xA0]+", "").replaceFirst("[\\x00-\\x20\\xA0]+$", "");
        str = str.replace(String.valueOf((char) 160), " ").trim();
        return str;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        String line = "MUAM, MAR ,GADD,AFI (53%); OSAMA BIN LADEN (52%)";
        final List<CodebookEntity> cbs = parseLine(line, "geo", "specific", "1");
        System.out.println(cbs);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + this.name.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CodebookEntity other = (CodebookEntity) obj;
        if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
