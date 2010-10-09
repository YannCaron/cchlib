/**
 * 
 */
package cx.ath.choisnet.i18n.builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import cx.ath.choisnet.i18n.AutoI18nExceptionHandler;

/***
 * TODO: Doc!
 * 
 * @author Claude CHOISNET
 */
public abstract class AbstractI18nPropertiesResourceAutoUpdate 
    extends AbstractI18nResourceAutoUpdate
{
    private static Logger slogger = Logger.getLogger(AbstractI18nPropertiesResourceAutoUpdate.class);
    private Properties properties = new Properties();

    /**
     * @param i18nAutoUpdateInterface
     * @param handler 
     * @param attributes 
     */
    public AbstractI18nPropertiesResourceAutoUpdate( 
            I18nAutoUpdateInterface     i18nAutoUpdateInterface,
            AutoI18nExceptionHandler    handler,
            EnumSet<Attrib>             attributes
            )
    {
        super(i18nAutoUpdateInterface,handler,attributes);
    }

    @Override
    final //TODO: remove this
    public void loadKnowValue() throws IOException
    {
        try {
            InputStream is = getResourceBundleInputStream();

            if( is == null ) {
                slogger.warn( "Can't open resource bundle for reading !" );
            } 
            else {
                properties.load( is );
                is.close();
            }
        }
        catch( IOException e ) {
            slogger.warn( "Can't read resource bundle", e );
        }

        slogger.info( "Resource bundle entries count: " + properties.size() );        
    }
    
    @Override
    final //TODO: remove this
    public void setUnknownMap(Map<String,String> mapOfUnknownEntries)
    {
        properties.putAll( mapOfUnknownEntries );
    }
    
    @Override
    final //TODO: remove this
    public void saveValues() throws IOException
    {
        OutputStream os = getResourceBundleOutputStream();

        if( os == null ) {
            slogger.warn( "Can't open resource bundle for writing !" );
        } 
        else {
            properties.store( os, "Creat by :" + getClass().getName() );
            os.close();
        }
    }

    @Override // Closeable
    final //TODO: remove this
    public void close() throws IOException
    {
        properties.clear();
        
        super.close();
        
        slogger.info( "New Resource bundle entries count: " + properties.size() );
        
        properties.clear(); // free some memory ;)
    }

    /**
     * InputStream to use to build Properties.
     * 
     * @return an InputStream, may be null
     * @throws IOException
     */
    public abstract InputStream getResourceBundleInputStream() 
        throws IOException;

    /**
     * OutputStream to use to store Properties.
     * 
     * @return an InputStream, should not null
     * @throws IOException
     */
    public abstract OutputStream getResourceBundleOutputStream()
        throws IOException; 
}