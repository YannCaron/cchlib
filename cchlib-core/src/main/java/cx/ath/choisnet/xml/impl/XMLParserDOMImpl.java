package cx.ath.choisnet.xml.impl;

import cx.ath.choisnet.xml.XMLParser;
import cx.ath.choisnet.xml.XMLParserErrorHandler;
import cx.ath.choisnet.xml.XMLParserException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.EnumSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * TODO: doc!
 *
 * @author Claude CHOISNET
 */
public class XMLParserDOMImpl
    implements XMLParser
{
    /**
     * Help to setup configuration for {@link DocumentBuilderFactory}
     *
     * @author Claude CHOISNET
     */
    public enum Attributs {
        /**
         * set {@link DocumentBuilderFactory#setValidating(boolean)}
         * to true if present in EnumSet
         */
        ENABLE_VALIDATING,

        /**
         * set {@link DocumentBuilderFactory#setIgnoringComments(boolean)}
         * to true if present in EnumSet
         */
        IGNORE_COMMENTS,

        /**
         * set {@link DocumentBuilderFactory#setIgnoringElementContentWhitespace(boolean)}
         * to true if present in EnumSet
         */
        IGNORE_WHITESPACE,

        /**
         * set {@link DocumentBuilderFactory#setCoalescing(boolean)}
         * to true if present in EnumSet
         */
        PUT_CDATA_INTO_TEXT,

        /**
         * set {@link DocumentBuilderFactory#setCoalescing(boolean)}
         * to false if present in EnumSet (default true)
         */
        CREATE_ENTITY_REFERENCES
        };

        /*
    public static final EnumSet<Attributs> DEFAULT_ATTRIBUTS = EnumSet.of( Attributs.IGNORE_WHITESPACE );
    public static final EnumSet<Attributs> VALIDATE_ONLY = EnumSet.of( Attributs.ENABLE_VALIDATING );
    */

    private DocumentBuilder documentBuilder;
    private Document        document;

    /**
     *
     * @param errorHandler
     * @param attributes
     * @throws XMLParserException
     */
    protected XMLParserDOMImpl(
            XMLParserErrorHandler   errorHandler,
            EnumSet<Attributs>      attributes
            )
        throws XMLParserException
    {
        try {
            documentBuilder = XMLParserDOMImpl.createDocumentBuilder(attributes, errorHandler.getSAXErrorHandler());
            document        = null;
            }
        catch(ParserConfigurationException e) {
            errorHandler.parserError(e);
            }
    }

    /**
     *
     * @param file
     * @param errorHandler
     * @param attributes
     * @throws XMLParserException
     */
    public XMLParserDOMImpl(
            File                    file,
            XMLParserErrorHandler   errorHandler,
            EnumSet<Attributs>      attributes
            )
        throws XMLParserException
    {
        this(errorHandler, attributes );

        try {
            document = documentBuilder.parse( file );
            }
        catch(IOException e) {
            errorHandler.ioError(e);
            }
        catch(SAXException e) {
            errorHandler.saxError(e);
            }
    }

    /**
     * TODO: doc!
     *
     * @param anURL
     * @param errorHandler
     * @param attributes
     * @throws XMLParserException
     */
    public XMLParserDOMImpl(
            URL                     anURL,
            XMLParserErrorHandler   errorHandler,
            EnumSet<Attributs>      attributes
            )
        throws XMLParserException
    {
        this(errorHandler, attributes );

        try {
            document = documentBuilder.parse(anURL.openStream(), anURL.toString());
            }
        catch(IOException e) {
            errorHandler.ioError(e);
            }
        catch(SAXException e) {
            errorHandler.saxError(e);
            }
    }

    /**
     * TODO: doc!
     *
     * @param aStream
     * @param errorHandler
     * @param attributes
     * @throws XMLParserException
     */
    public XMLParserDOMImpl(
            InputStream             aStream,
            XMLParserErrorHandler   errorHandler,
            EnumSet<Attributs>      attributes
            )
        throws XMLParserException
    {
        this(errorHandler, attributes );

        try {
            document = documentBuilder.parse(aStream);
            }
        catch( SAXException e ) {
            errorHandler.saxError(e);
            }
        catch( IOException e ) {
            errorHandler.ioError(e);
            }
    }

    @Override
    public Document getDocument()
    {
        return document;
    }

    /**
     * TODO: doc!
     *
     * @param document
     */
    protected void setDocument(Document document)
    {
        this.document = document;
    }


    private static DocumentBuilderFactory createDocumentBuilderFactory(
            EnumSet<Attributs> attributes
            )
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(attributes.contains(Attributs.ENABLE_VALIDATING));
        dbf.setIgnoringComments(attributes.contains(Attributs.IGNORE_COMMENTS));
        dbf.setIgnoringElementContentWhitespace(attributes.contains(Attributs.IGNORE_WHITESPACE));
        dbf.setCoalescing(attributes.contains(Attributs.PUT_CDATA_INTO_TEXT));
        dbf.setExpandEntityReferences(!attributes.contains(Attributs.CREATE_ENTITY_REFERENCES));

        return dbf;
    }

    private static DocumentBuilder createDocumentBuilder(
            EnumSet<Attributs>  attributes,
            ErrorHandler        errorHandler
            )
        throws ParserConfigurationException
    {
        DocumentBuilderFactory  dbf = XMLParserDOMImpl.createDocumentBuilderFactory(attributes);
        DocumentBuilder         db;

        try {
            db = dbf.newDocumentBuilder();
            }
        catch( ParserConfigurationException pce ) {
            throw pce;
            }

        db.setErrorHandler(errorHandler);

        return db;
    }

}