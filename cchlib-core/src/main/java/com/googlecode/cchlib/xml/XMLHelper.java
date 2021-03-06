package com.googlecode.cchlib.xml;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

/**
 * NEEDDOC
 *
 */
public final class XMLHelper
{
   private static final Logger LOGGER = Logger.getLogger( XMLHelper.class );

   private XMLHelper(){} // All static

    /**
     * NEEDDOC
     *
     * @param node a {@link Node} object.
     * @param output a {@link OutputStream} object.
     *
     * @throws TransformerConfigurationException if any.
     * @throws TransformerException if any.
     */
    @SuppressWarnings({"squid:RedundantThrowsDeclarationCheck","squid:S1160"})
    public static void writeXML( final Node node, final OutputStream output )
        throws TransformerConfigurationException, TransformerException
    {
        final Source source = new DOMSource(node);
        final Result result = new StreamResult(output);
        final Transformer xformer = TransformerFactory.newInstance().newTransformer();

        xformer.transform(source, result);
    }

    /**
     * NEEDDOC
     *
     * @param node a {@link Node} object.
     * @param output a {@link Writer} object.
     *
     * @throws TransformerConfigurationException if any.
     * @throws TransformerException if any.
     */
    @SuppressWarnings({"squid:RedundantThrowsDeclarationCheck","squid:S1160"})
    public static void writeXML( final Node node, final Writer output)
        throws TransformerConfigurationException, TransformerException
    {
        final Source source = new DOMSource(node);
        final Result result = new StreamResult(output);
        final Transformer xformer = TransformerFactory.newInstance().newTransformer();

        xformer.transform(source, result);
    }

    /**
     * For quick debugging only
     *
     * @param node a {@link Node} object.
     * @return a {@link String} object.
     */
    public static String toString( final Node node )
    {
        final StringWriter buffer = new StringWriter();

        try{
            XMLHelper.writeXML( node, buffer );
            }
        catch( final TransformerConfigurationException e ) {
            buffer.append("\n\nTransformerConfigurationException: ");
            buffer.append( e.getMessage() );

            LOGGER.error( XMLHelper.class.getSimpleName() + ".toString()", e );
            }
        catch( final TransformerException e ) {
            buffer.append("\n\nTransformerException: ");
            buffer.append( e.getMessage() );

            LOGGER.error( XMLHelper.class.getSimpleName() + ".toString()", e );
            }

        return buffer.toString();
    }
}
