package com.googlecode.cchlib.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * For test ONLY
 */
public class IO
{
    public final static String PNG_FILE = "test.png";

    /**
     * Returns an InputStream within a valid PNG file
     * @return an InputStream within a valid PNG file
     * @throws FileNotFoundException if file not found
     */
    public final static InputStream createPNGInputStream()
        throws FileNotFoundException
    {
        final InputStream is = IO.class.getResourceAsStream( PNG_FILE );

        if( is == null ) {
            throw new FileNotFoundException( PNG_FILE );
            }

        return is;
    }

    public final static File createPNGTempFile() throws IOException
    {
        return IOHelper.toFile( createPNG(), File.createTempFile( "cchlib-test-io", ".png" ) );
    }

    public final static File createZipTempFile() throws IOException
    {
        return IOHelper.copy( createZipInputFile(), File.createTempFile( "cchlib-test-io", ".zip" ) );
    }

    public final static InputStream createZipInputFile() throws FileNotFoundException
    {
        return new FileInputStream( "./src/test/resources/com/googlecode/cchlib/io/mysrc.zip" );
    }

    /**
     * Returns a byte array within a valid PNG file
     * @return a byte array within a valid PNG file
     * @throws FileNotFoundException if file not found
     * @throws IOException if any IO error occurred
     */
   public final static byte[] createPNG()
       throws FileNotFoundException, IOException
    {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            IOHelper.copy( createPNGInputStream(), os );
            }
        finally {
            os.close();
            }

        return os.toByteArray();
    }
}
