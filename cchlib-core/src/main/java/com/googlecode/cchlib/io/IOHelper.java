package com.googlecode.cchlib.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import cx.ath.choisnet.util.iterator.ArrayIterator;
import cx.ath.choisnet.util.iterator.IteratorFilter;

/**
 * Provide some extra tools for I/O operations
 *
 * @see InputStream
 * @see Reader
 * @see File
 * @since 4.1.5
 */
public final class IOHelper
{
    private final static int DEFAULT_BUFFER_SIZE = 4096;

    private IOHelper()
    {
        //All static
    }

    /**
     * Copy a File to an other File
     *
     * @param inputFile     File to copy
     * @param outputFile    File to receive inputFile content.
     * @param buffer        Buffer to use for copy
     * @throws IOException if any IO occurred
     */
    public static void copy(
            final File  inputFile,
            final File  outputFile,
            byte[]      buffer
            )
        throws IOException
    {
        InputStream  input  = new BufferedInputStream(new FileInputStream(inputFile));
        OutputStream output = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {
            copy(input, output, buffer);
            }
        finally {
            try { input.close();  } catch(Exception ignore) {}
            try { output.close(); } catch(Exception ignore) {}
            }
    }

    /**
     * Copy a File to an other File
     *
     * @param inputFile     File to copy
     * @param outputFile    File to receive inputFile content.
     * @throws IOException if any IO occurred
     */
    public static void copy( final File inputFile, final File outputFile )
        throws IOException
    {
        copy(inputFile, outputFile, new byte[DEFAULT_BUFFER_SIZE] );
    }

    /**
     * Copy an InputStream to a File
     *
     * @param is            InputStream to copy
     * @param outputFile    File to receive InputStream content.
     * @throws IOException if any IO occurred
     */
    public static void copy( final InputStream is, final File outputFile )
        throws IOException
    {
        OutputStream output = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {
            copy(is, output, DEFAULT_BUFFER_SIZE);
            }
        finally {
            try { output.close(); } catch(Exception ignore) { }
            }
    }

    /**
     * Copy a String into a File
     *
     * @param file  File to create
     * @param s     String to store into file.
     * @throws IOException if any IO occurred
     */
    public static void toFile( File file, String s ) throws IOException
    {
        FileWriter fos = new FileWriter( file );

        try {
            fos.write( s );
            }
        finally {
            fos.close();
            }
    }

    /**
     * Delete all files and folders giving folder
     *
     * @param rootDirFile a valid File object (could be a File or a Directory)
     * @throws FileDeleteException if any error occur during delete process
     */
    public static void deleteTree( File rootDirFile )
        throws FileDeleteException
    {
        if( !rootDirFile.exists() ) {
            return;
            }

        File[] files = rootDirFile.listFiles();

        if( files != null ) {
            for(File f : files) {
                if( f.isFile() ) {
                    boolean res = f.delete();

                    if( !res ) {
                        throw new FileDeleteException(f);
                    }
                }
                else if( f.isDirectory() ) {
                    deleteTree( f );
                }
            }
        }

        boolean res = rootDirFile.delete();
        if( !res ) {
            throw new FileDeleteException(rootDirFile);
            }
    }

    /**
     * Wrap an Iterator on a File array and apply a filter
     *
     * @param files
     * @param fileFilter
     * @return return a file Iterator
     */
    public static Iterator<File> toIterator(
            File[]     files,
            FileFilter fileFilter
            )
    {
        return new IteratorFilter<File>(
                new ArrayIterator<File>(files),
                IteratorFilter.wrappe(fileFilter)
                );
    }

    /**
     * Copy all remaining data in File to a String
     *
     * @param file File to read
     * @return content of File
     * @throws IOException  if any error occur
     */
    public static String toString( File file )
        throws IOException
    {
        Reader r = new FileReader( file );

        try {
            return toString( r );
            }
        finally {
            r.close();
            }
    }

    /**
     * Copy all remaining data in {@link InputStream} to an byte array
     * and close the {@link InputStream}.
     *
     * @param input {@link InputStream} to read
     * @return content of {@link InputStream}
     * @throws IOException if any
     */
    public static byte[] toByteArray( final InputStream input )
        throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        copy( input, out );
        input.close();

        return out.toByteArray();
    }

    /**
     * Copy input content to output.
     *
     * @param input  {@link Reader} to read from
     * @param output {@link Writer} to write to
     * @param buffer Buffer to use for copy
     * @throws IOException if any
     */
    public static void copy(
            final InputStream   input,
            final OutputStream  output,
            byte[]              buffer
            )
        throws IOException
    {
        int len;

        while((len = input.read(buffer)) != -1) {
            output.write(buffer, 0, len);
            }

        output.flush();
    }

    /**
     *
     * Copy input content to output.
     *
     * @param input  {@link Reader} to read from
     * @param output {@link Writer} to write to
     * @param bufferSize Buffer size to use for copy
     * @throws IOException if any
     */
    public static void copy(
            final InputStream   input,
            final OutputStream  output,
            final int           bufferSize
            )
        throws IOException
    {
        copy(input, output, new byte[bufferSize] );
    }

    /**
    *
    * Copy input content to output.
    *
    * @param input  {@link Reader} to read from
    * @param output {@link Writer} to write to
    * @throws IOException if any
    */
    public static void copy(
            final InputStream   input,
            final OutputStream  output
            )
        throws IOException
    {
        copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Compare content of two {@link InputStream}. {@link InputStream} are consumed but
     * are not closed after this call.
     *
     * @param is1   an {@link InputStream}
     * @param is2   an other {@link InputStream}
     * @return true if content (and size) of {@link InputStream} are equals.
     * @throws IOException if any IO error occur.
     */
    public final static boolean isEquals( InputStream is1, InputStream is2 )
        throws IOException
    {
        for(;;) {
            int c1 = is1.read();
            int c2 = is2.read();

            if( c1 != c2 ) {
                return false;
                }
            if( c1 == -1 ) { // and c2 == -1 since c1 == c2
                return true;
                }
        }
    }

    /**
     * Compare content of an {@link InputStream} with an array of bytes. {@link InputStream} is consumed but
     * are not closed after this call.
     *
     * @param is    an {@link InputStream}
     * @param bytes an array of bytes
     * @return true if content (and size) of {@link InputStream} is equals to array content.
     * @throws IOException if any IO error occur.
     */
    public static boolean isEquals( final InputStream is, final byte[] bytes )
        throws IOException
    {
        int index = 0;

        for(;;) {
            int c1 = is.read();
            int c2 = index < bytes.length ? (0x00FF & bytes[ index++ ]) : -1;

            if( c1 != c2 ) {
                return false;
                }
            if( c1 == -1 ) { // and c2 == -1 since c1 == c2
                return true;
                }
        }
    }

    /**
     * Copy all remaining data in Reader to a String
     * and close the Reader.
     *
     * @param input Reader to read
     * @return content of Reader
     * @throws IOException
     */
   public static String toString( Reader input )
       throws IOException
   {
       StringBuilder sb     = new StringBuilder();
       char[]        buffer = new char[ DEFAULT_BUFFER_SIZE ];

       try {
           int len;

           while( (len = input.read(buffer)) != -1 ) {
               sb.append(buffer, 0, len);
               }
           }
       finally {
           input.close();
           }

       return sb.toString();
   }

   /**
    * Copy input content to output.
    *
    * @param input  {@link Reader} to read from
    * @param output {@link Writer} to write to
    * @param buffer Buffer to use for copy
    * @throws IOException if any
    */
   public static void copy(
           final Reader input,
           final Writer output,
           final char[] buffer
           )
       throws IOException
   {
       int len;

       while((len = input.read(buffer)) != -1) {
           output.write(buffer, 0, len);
           }

       output.flush();
   }

   /**
    * Copy input content to output.
    *
    * @param input  {@link Reader} to read from
    * @param output {@link Writer} to write to
    * @param bufferSize Buffer size to use for copy
    * @throws IOException if any
    */
   public static void copy(
           final Reader   input,
           final Writer   output,
           final int      bufferSize
           )
       throws IOException
   {
       copy( input, output, new char[bufferSize] );
   }

   /**
    * Copy input content to output.
    *
    * @param input  {@link Reader} to read from
    * @param output {@link Writer} to write to
    * @throws IOException if any
    */
   public static void copy( final Reader input, final Writer output )
       throws IOException
   {
       copy(input, output, DEFAULT_BUFFER_SIZE);
   }
}