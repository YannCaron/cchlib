package cx.ath.choisnet.tools.i18n;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

/**
 * 
 * @author Claude CHOISNET
 */
interface CustomPropertiesInterface
    extends Serializable 
{
    /**
     * @return FileObject use by the CustomProperties
     */
    public FileObject getFileObject();

    /**
     * @return true if current object handle lines numbers,
     * false otherwise.
     */
    public boolean handleLinesNumbers();
    
    /**
     * Get line number for giving property
     * @param key key property
     * @return line number of property,
     *         0 if not found (should not occur)
     */
    public int getLineNumber(String key);
    
    /**
     * Get value for giving property
     * @param key key property
     * @return value of property, 
     *         null if not exist (should not occur)
     */
    public String getProperty( String key );

    /**
     * @param key property to set
     * @param value for this property
     */
    public void setProperty( String key, String value );

    /**
     * @return a set of String keys
     */
    public Set<String> stringPropertyNames();

//    /**
//     * Load from fileObject
//     * @param keyBuilderSet 
//     * 
//     * @throws FileNotFoundException
//     * @throws IOException 
//     */
//    public void load(Set<String> keyBuilderSet) 
//        throws FileNotFoundException, IOException;
    
    /**
     * Save from fileObject
     *  
     * @return true if file has been saved
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean store()
        throws FileNotFoundException, IOException;
/* TODO:
    public boolean storeAs(File file)
        throws FileNotFoundException, IOException;
        */
}