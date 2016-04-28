/*
 * To change this template, choose Tools | Templates
 
 */
package context.core.task.lexisnexis;

/**
 *
 * @author Aale
 */
public class LxNxMetadata {

    private String source;
    private String bestDate;
    private String pubType;
    private String section;
    private int cleanLength;
    private String journalCode;
    private String language;
    private String loadDate;
    private String title;
    private String author;
    private String graphic;
    private String geo;
    private String city;
    private String organization;
    private String person;
    private String company;
    private String industry;
    private String ticker;
    private String subject;
    private String country;
    private int geoSudanRatio;
    private int countrySudanRatio;
    private String textID;
    private String refDoc;

    LxNxMetadata(LxNxTextParser tp) {
        setSource(tp.getSource());
        setBestDate(tp.getBestDate());
        setPubType(tp.getPubType());
        setSection(tp.getSection());
        setCleanLength(tp.getCleanLength());
        setJournalCode(tp.getJournalCode());
        setLanguage(tp.getLanguage());
        setLoadDate(tp.getLoadDate());
        setTitle(tp.getTitle());
        setAuthor(tp.getAuthor());
        setGraphic(tp.getGraphic());
        setGeo(tp.getGeo());
        setCity(tp.getCity());
        setOrganization(tp.getOrganization());
        setPerson(tp.getPerson());
        setCompany(tp.getCompany());
        setIndustry(tp.getIndustry());
        setTicker(tp.getTicker());
        setSubject(tp.getSubject());
        setCountry(tp.getCountry());
        setGeoSudanRatio(tp.getGeoSudanRatio());
        setCountrySudanRatio(tp.getCountrySudanRatio());
        setTextID(tp.getTextID());
        setRefDoc(tp.getTextID());

    }

    /**
     *
     * @return
     */
    public String getRefDoc() {
        return refDoc;
    }

    /**
     *
     * @param refDoc
     */
    public void setRefDoc(String refDoc) {
        this.refDoc = refDoc;
    }

    /**
     *
     * @return
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return
     */
    public String getBestDate() {
        return bestDate;
    }

    /**
     *
     * @param bestDate
     */
    public void setBestDate(String bestDate) {
        this.bestDate = bestDate;
    }

    /**
     *
     * @return
     */
    public String getPubType() {
        return pubType;
    }

    /**
     *
     * @param pubType
     */
    public void setPubType(String pubType) {
        this.pubType = pubType;
    }

    /**
     *
     * @return
     */
    public String getSection() {
        return section;
    }

    /**
     *
     * @param section
     */
    public void setSection(String section) {
        this.section = section;
    }

    /**
     *
     * @return
     */
    public int getCleanLength() {
        return cleanLength;
    }

    /**
     *
     * @param cleanLength
     */
    public void setCleanLength(int cleanLength) {
        this.cleanLength = cleanLength;
    }

    /**
     *
     * @return
     */
    public String getJournalCode() {
        return journalCode;
    }

    /**
     *
     * @param journalCode
     */
    public void setJournalCode(String journalCode) {
        this.journalCode = journalCode;
    }

    /**
     *
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     *
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     *
     * @return
     */
    public String getLoadDate() {
        return loadDate;
    }

    /**
     *
     * @param loadDate
     */
    public void setLoadDate(String loadDate) {
        this.loadDate = loadDate;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return
     */
    public String getGraphic() {
        return graphic;
    }

    /**
     *
     * @param graphic
     */
    public void setGraphic(String graphic) {
        this.graphic = graphic;
    }

    /**
     *
     * @return
     */
    public String getGeo() {
        return geo;
    }

    /**
     *
     * @param geo
     */
    public void setGeo(String geo) {
        this.geo = geo;
    }

    /**
     *
     * @return
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     */
    public String getOrganization() {
        return organization;
    }

    /**
     *
     * @param organization
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     *
     * @return
     */
    public String getPerson() {
        return person;
    }

    /**
     *
     * @param person
     */
    public void setPerson(String person) {
        this.person = person;
    }

    /**
     *
     * @return
     */
    public String getCompany() {
        return company;
    }

    /**
     *
     * @param company
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     *
     * @return
     */
    public String getIndustry() {
        return industry;
    }

    /**
     *
     * @param industry
     */
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    /**
     *
     * @return
     */
    public String getTicker() {
        return ticker;
    }

    /**
     *
     * @param ticker
     */
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    /**
     *
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     *
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     */
    public int getGeoSudanRatio() {
        return geoSudanRatio;
    }

    /**
     *
     * @param geoSudanRatio
     */
    public void setGeoSudanRatio(int geoSudanRatio) {
        this.geoSudanRatio = geoSudanRatio;
    }

    /**
     *
     * @return
     */
    public int getCountrySudanRatio() {
        return countrySudanRatio;
    }

    /**
     *
     * @param countrySudanRatio
     */
    public void setCountrySudanRatio(int countrySudanRatio) {
        this.countrySudanRatio = countrySudanRatio;
    }

    /**
     *
     * @return
     */
    public String getTextID() {
        return textID;
    }

    /**
     *
     * @param textID
     */
    public void setTextID(String textID) {
        this.textID = textID;
    }
}
