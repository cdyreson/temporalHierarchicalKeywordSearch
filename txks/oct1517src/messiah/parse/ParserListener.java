package messiah.parse;

/**
 * This interface encapsulates the important steps during parsing
 * @author John Truong Ba Quan
 */
public interface ParserListener {
    /**
     * Start parsing a document
     */
    public void startDocument();
    
    /**
     * Stop parsing a document
     */
    public void endDocument();
    
    /**
     * Start parsing an element/attribute/value
     * @param str           Element/attribute label or value string (note the attribute label
     *                          does not have @
     * @param isAttribute   Attribute label/value or not
     * @param isValue       Element/attribute value or not
     */
    public void start(String str, boolean isAttribute, boolean isValue);
    
    /**
     * Stop parsing an element/attribute/value
     */
    public void end(boolean isAttribute, boolean isValue);
}
