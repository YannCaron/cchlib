package cx.ath.choisnet.zip.impl;

import cx.ath.choisnet.util.Wrappable;
import cx.ath.choisnet.zip.SimpleZipEntry;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;

/**
 *
 * @author Claude CHOISNET
 *
 */
public class SimpleZipEntryFactoryImpl
    implements Wrappable<File,SimpleZipEntry>
{
    private String refFolder;
    private int    refFolderLen;

    /**
     * 
     * @param refFolderFile
     * @throws IOException
     */
    public SimpleZipEntryFactoryImpl(
            File refFolderFile
            ) 
        throws IOException
    {
        String canonicalPath = refFolderFile.getCanonicalPath();

        refFolder = canonicalPath.replace('\\', '/') + '/';
        refFolderLen = refFolder.length();
    }

    /**
     * 
     * @param refFolder
     * @throws IOException
     */
    public SimpleZipEntryFactoryImpl(
            String refFolder
            ) 
        throws IOException
    {
        this(new File(refFolder));
    }

    @Override
    public SimpleZipEntry wrappe(File file)
    {
        try {
            return private_wrappe( file );
        }
        catch( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private SimpleZipEntry private_wrappe(File file) throws IOException
    {
        String name = file.getCanonicalPath().replace('\\', '/');

        if( /*file.isAbsolute() &&*/ name.startsWith(refFolder)) {
            name = name.substring(refFolderLen);
        }

        if(file.isDirectory() && !name.endsWith("/")) {
            name = (new StringBuilder()).append(name).append('/').toString();
        }

        ZipEntry zipEntry = new ZipEntry(name);

        zipEntry.setTime(file.lastModified());
//        zipEntry.setComment( comment );
//        zipEntry.setCompressedSize( csize );
//        zipEntry.setCrc( crc );
//        zipEntry.setExtra( extra );
//        zipEntry.setMethod( method );
//        zipEntry.setSize( size );

        return new SimpleZipEntryImpl(file, zipEntry);
    }
}
