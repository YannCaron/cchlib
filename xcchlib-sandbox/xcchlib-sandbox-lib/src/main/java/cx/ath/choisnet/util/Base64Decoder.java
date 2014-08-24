/*
** -----------------------------------------------------------------------
** Nom           : cx/ath/choisnet/util/Base64Decoder.java
** Description   :
** Encodage      : ANSI
**
**  1.54.005 2005.09.12 Claude CHOISNET
**                      Version initiale.
**  2.02.012 2005.12.16 Claude CHOISNET
**                      Reprise de la classe.
** -----------------------------------------------------------------------
**
** cx.ath.choisnet.util.Base64Decoder
**
*/
package cx.ath.choisnet.util;

//
// Base64Decoder.java
// $Id: Base64Decoder.java,v 1.6 2000/08/16 21:37:48 ylafon Exp $
// (c) COPYRIGHT MIT and INRIA, 1996.
//
// package org.w3c.tools.codec ;
//

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
** Decode a BASE64 encoded input stream to some output stream.
** This class implements BASE64 decoding, as specified in the
** <a href="http://ds.internic.net/rfc/rfc1521.txt">MIME specification</a>.
**
** @see Base64Encoder
** @version 2.02.012
*/
public class Base64Decoder
{
private static final int BUFFER_SIZE = 1024 ;

/**
** Create a decoder to decode a stream.
**
*/
public Base64Decoder() // -------------------------------------------------
{
    // empty
}

/**
**
*/
private final int get1( final byte buf[], final int off ) // --------------------------
{
 return ((buf[off] & 0x3f) << 2) | ((buf[off+1] & 0x30) >>> 4);
}

/**
**
*/
    private final int get2 (final byte buf[], final int off) {
    return ((buf[off+1] & 0x0f) << 4) | ((buf[off+2] &0x3c) >>> 2);
    }

/**
**
*/
    private final int get3 (final byte buf[], final int off) {
    return ((buf[off+2] & 0x03) << 6) | (buf[off+3] & 0x3f);
    }

/**
**
*/
    private final int check (final int ch) {
    if ((ch >= 'A') && (ch <= 'Z')) {
        return ch - 'A' ;
    } else if ((ch >= 'a') && (ch <= 'z')) {
        return ch - 'a' + 26 ;
    } else if ((ch >= '0') && (ch <= '9')) {
        return ch - '0' + 52 ;
    } else {
        switch (ch) {
          case '=':
          return 65 ;
          case '+':
          return 62 ;
          case '/':
          return 63 ;
          default:
          return -1 ;
        }
    }
    }

/**
** <p>
** Do the actual decoding.
** </p>
**
** Process the input stream by decoding it and emiting the resulting bytes
** into the output stream.
**
** @param in    The input stream (to be decoded).
** @param out   The output stream, to write decoded data to.
**
** @exception Base64FormatException If the input stream is not compliant
**                                  with the BASE64 specification.
** @exception IOException           If the input or output stream accesses failed.
*/
public void decode( final InputStream in, final OutputStream out ) // -----
    throws Base64FormatException, IOException
{
 final byte[]   buffer  = new byte[ BUFFER_SIZE ] ;
 final byte[]   chunk   = new byte[ 4 ] ;
 int            got     = -1 ;
 int            ready   = 0 ;

 fill:

 while( (got = in.read( buffer ) ) > 0 ) {
    int skiped = 0 ;

    while( skiped < got ) {

        // Check for un-understood characters:
        while( ready < 4 ) {
            if( skiped >= got ) {
                continue fill;
            }

            final int ch = check (buffer[skiped++]);

            if( ch >= 0 ) {
                chunk[ready++] = (byte) ch ;
            }
            }

        if( chunk[ 2 ] == 65 ) {
            out.write( get1( chunk, 0 ) );
            return;
            }
        else if( chunk[ 3 ] == 65 ) {
            out.write( get1( chunk, 0 ) );
            out.write( get2( chunk, 0 ) );
            return ;
            }
        else {
            out.write( get1( chunk, 0 ) );
            out.write( get2( chunk, 0 ) );
            out.write( get3( chunk, 0 ) );
            }

        ready = 0 ;
        }
    }

 if( ready != 0 ) {
    throw new Base64FormatException( "Invalid length." );
    }

 out.flush();
}


/**
** Create a decoder to decode a stream.
**
** @param datas array of bytes to be decoded
** @param out   The output stream, to write decoded data to.
**
*/
public void decode( final byte[] datas, final OutputStream out ) // -------------------
    throws Base64FormatException, IOException
{
 decode( new ByteArrayInputStream( datas ), out );
}

/**
** Test the decoder.
** Run it with one argument: the string to be decoded, it will print out
** the decoded value.
*/
@SuppressWarnings("resource")
public static void main( final String[] args ) // -------------------------------
{
 if( args.length == 1 ) {
    try {
        final ByteArrayOutputStream   out = new ByteArrayOutputStream();
        final Base64Decoder           b   = new Base64Decoder();

        b.decode( args[ 0 ].getBytes(), out );

        System.out.println( "[" + out.toString() + "]" );
        }
    catch( final Exception e ) {
        System.out.println ("Invalid Base64 format !");
        System.exit( 1 );
        }
    }
 else if( (args.length == 2) && (args[0].equals("-f"))) {
    try {
        final FileInputStream in  = new FileInputStream( args[1] );
        final Base64Decoder   b   = new Base64Decoder();

        b.decode( in, System.out );
        }
    catch( final Exception ex ) {
        System.out.println("error: " + ex.getMessage());
        System.exit(1);
        }
    }
 else {
    System.out.println("Base64Decoder [strong] [-f file]");
    }
}

} // class

    /**
     * Do the decoding, and return a String.
     * This methods should be called when the decoder is used in
     * <em>String</em> mode. It decodes the input string to an output string
     * that is returned.
     * @exception RuntimeException If the object wasn't constructed to
     *    decode a String.
     * @exception Base64FormatException If the input string is not compliant
     *     with the BASE64 specification.
    public String processString ()
    throws Base64FormatException
    {
    if ( ! stringp )
        throw new RuntimeException (this.getClass().getName()
                    + "[processString]"
                    + "invalid call (not a String)");
    try {
        process();
    } catch (IOException e) {
    }
    String s;
    try {
        s = ((ByteArrayOutputStream) out).toString("ISO-8859-1");
    } catch (UnsupportedEncodingException ex) {
        throw new RuntimeException(this.getClass().getName() +
                       "[processString] Unable to convert" +
                       "properly char to bytes");
    }
    return s;
    }
     */

    /**
     * Create a decoder to decode a String.
     * @param input The string to be decoded.

    public Base64Decoder (String input) {
    byte bytes[] ;
    try {
        bytes = input.getBytes ("ISO-8859-1");
    } catch (UnsupportedEncodingException ex) {
        throw new RuntimeException(this.getClass().getName() +
                       "[Constructor] Unable to convert" +
                       "properly char to bytes");
    }
    this.stringp = true ;
    this.in      = new ByteArrayInputStream(bytes);
    this.out     = new ByteArrayOutputStream ();
    }
*/
/*
    private void printHex (int x) {
    int h = (x&0xf0) >> 4 ;
    int l = (x&0x0f);
    System.out.print
        ((new Character((char)((h>9) ? 'A'+h-10 : '0'+h))).toString()
         +(new Character((char)((l>9) ? 'A'+l-10 : '0'+l))).toString());
    }

    private void printHex (byte buf[], int off, int len) {
    while (off < len) {
        printHex (buf[off++]);
        System.out.print (" ");
    }
    System.out.println ("");
    }

    private void printHex (String s) {
    byte bytes[] ;
    try {
        bytes = s.getBytes ("ISO-8859-1");
    } catch (UnsupportedEncodingException ex) {
        throw new RuntimeException(this.getClass().getName() +
                       "[printHex] Unable to convert" +
                       "properly char to bytes");
    }
    printHex (bytes, 0, bytes.length);
    }
*/