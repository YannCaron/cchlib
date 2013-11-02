package cx.ath.choisnet.xml.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.EnumSet;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.googlecode.cchlib.io.IOHelper;
import cx.ath.choisnet.xml.XMLParser;
import cx.ath.choisnet.xml.XMLParserErrorHandler;
import cx.ath.choisnet.xml.XMLParserException;

/**
 *
 *
 */
public class XMLFileParserDOMImplTest
{
    private static final Logger slogger = Logger.getLogger( XMLFileParserDOMImplTest.class );
    private File xmlFile;
    private File dtdFile;

    @Before
    public void setUpClass() throws Exception
    {
        this.xmlFile = File.createTempFile( "~XMLParser_", ".xml" );
        InputStream s = XMLParser.class.getResourceAsStream( "survey-sample.xml" );
        IOHelper.copy( s, this.xmlFile );
        s.close();

        this.dtdFile = new File( this.xmlFile.getParentFile(), "survey.dtd" );
        s = XMLParser.class.getResourceAsStream( "survey.dtd" );
        IOHelper.copy( s, this.dtdFile );
        s.close();

        slogger.info( "XML File is " + this.xmlFile );
        //slogger.info( "XML File: " + FileHelper.toString( this.xmlFile ) );
        slogger.info( "DTD File is " + this.dtdFile );
        //slogger.info( "DTD File: " + FileHelper.toString( this.dtdFile ) );
    }

    @After
    public void cleanUpClass() throws Exception
    {
        if( xmlFile != null ) {
            boolean deleted = xmlFile.delete();
            slogger.info( "XML File delete? " + deleted );
            }
        if( xmlFile != null ) {
            boolean deleted = xmlFile.delete();
            slogger.info( "DTD File delete? " + deleted );
            }
    }

    @Test
    public void test_XMLFileParserDOMImpl()
        throws FileNotFoundException, XMLParserException
    {
        XMLParserErrorHandler errorHandler =
            new XMLParserErrorHandler(
                new SAXErrorHandlerImpl(
                        new PrintWriter( System.err )
                        )
                );

        // Validate XML according to DTD
        XMLFileParserDOMImpl xmlParser =
            new XMLFileParserDOMImpl(
                    xmlFile,
                    errorHandler,
                    EnumSet.of( XMLParserDOMImpl.Attributs.ENABLE_VALIDATING )
                    );

        xmlParser.getDocument();
    }
}
