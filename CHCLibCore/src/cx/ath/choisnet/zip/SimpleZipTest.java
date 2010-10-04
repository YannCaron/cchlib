package cx.ath.choisnet.zip;

import cx.ath.choisnet.io.FileHelper;
import cx.ath.choisnet.zip.impl.SimpleZipEntryFactoryImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.log4j.Logger;
import junit.framework.TestCase;

/**
 *
 * @author Claude CHOISNET
 *
 */
public class SimpleZipTest extends TestCase
{
    final private static Logger slogger = Logger.getLogger(SimpleZipTest.class);

    public static final File TEMP_DIR_FILE = new File( System.getProperty("java.io.tmpdir" ) );
    public static final File ZIP_SOURCE_DIR_FILE = new File( new File("."), "src" );
    public static final File ZIP_DESTINATION_ZIP = new File( TEMP_DIR_FILE, "mysrc.zip" );

    public final static String UNZIP_ZIP_FILENAME  = "./libs/log4j-1.2.15.jar";
    public final static File   UNZIP_DEST_DIR_FILE = new File( TEMP_DIR_FILE, "log4j-1.2.15.jar" );

    public void testSimpleZip()
        throws java.io.IOException
    {
        SimpleZip instance = new SimpleZip(
                new FileOutputStream( ZIP_DESTINATION_ZIP )
                );

        instance.addFolder(
                ZIP_SOURCE_DIR_FILE,
                new SimpleZipEntryFactoryImpl( ZIP_SOURCE_DIR_FILE )
                {

            // Just to debug !
            public SimpleZipEntry wrappe(File file)
            {
                SimpleZipEntry sze = super.wrappe(file);

                slogger.info("add: " + file + " -> " + sze.getZipEntry().getName() );

                return sze;
            }
        });

        instance.close();

        boolean del = ZIP_DESTINATION_ZIP.delete();

        assertTrue( "Can't delete: " + ZIP_DESTINATION_ZIP, del);
        // TODO: can't delete file ?
    }

    public void testUnzip()
        throws java.io.IOException
    {
        InputStream is       = new FileInputStream( UNZIP_ZIP_FILENAME );
        SimpleUnZip instance = new SimpleUnZip( is );

        instance.saveAll( UNZIP_DEST_DIR_FILE );
        instance.close();

        int count = instance.getFileCount();

        slogger.info( "Unzip file count:" + count );

        assertTrue( "No file to unzip: ", count > 0 );

        FileHelper.deleteTree( UNZIP_DEST_DIR_FILE );
    }
}