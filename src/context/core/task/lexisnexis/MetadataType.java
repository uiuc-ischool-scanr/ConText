/*
 * To change this template, choose Tools | Templates
 
 */
package context.core.task.lexisnexis;

/**
 *
 * @author Aale
 */
public class MetadataType {

    private String value;

    /**
     *
     */
    public static MetadataType LOCATION = new MetadataType("location");

    /**
     *
     */
    public static MetadataType PERSON = new MetadataType("person");

    /**
     *
     */
    public static MetadataType ORGANIZATION = new MetadataType("organization");

    /**
     *
     */
    public static MetadataType SUBJECT = new MetadataType("subject");

    private MetadataType(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.value.hashCode();
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
        final MetadataType other = (MetadataType) obj;
        if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
