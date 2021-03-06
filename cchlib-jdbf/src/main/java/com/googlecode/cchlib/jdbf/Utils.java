// $codepro.audit.disable
package com.googlecode.cchlib.jdbf;

import java.io.DataInput;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Miscellaneous functions required by the JavaDBF package.
 */
/*NOT public*/ final class Utils
{
    public static final int ALIGN_LEFT = 10;
    public static final int ALIGN_RIGHT = 12;

    private Utils(){}

    public static int readLittleEndianInt( final DataInput in )
        throws IOException
    {
        int bigEndian = 0;

        for( int shiftBy=0; shiftBy<32; shiftBy+=8) {
            bigEndian |= (in.readUnsignedByte()&0xff) << shiftBy;
            }

        return bigEndian;
    }

    public static short readLittleEndianShort( final DataInput in )
        throws IOException
    {
        final int low = in.readUnsignedByte() & 0xff;
        final int high = in.readUnsignedByte();

        return (short )((high << 8) | low);
    }

    public static byte[] trimLeftSpaces( final byte[] arr )
    {
        final StringBuilder t_sb = new StringBuilder( arr.length );

        for( int i=0; i<arr.length; i++) {
            if( arr[i] != ' ') {
                t_sb.append( (char)arr[ i] );
                }
            }

        return t_sb.toString().getBytes();
    }

    public static short littleEndian( final short value )
    {
        final short num1 = value;
        short mask = (short)0xff;

        short num2 = (short)(num1&mask);
        num2<<=8;
        mask<<=8;

        num2 |= (num1&mask)>>8;

        return num2;
    }

    public static int littleEndian( final int value )
    {
        final int num1 = value;
        int mask = 0xff;
        int num2 = 0x00;

        num2 |= num1 & mask;

        for( int i=1; i<4; i++) {
            num2<<=8;
            mask <<= 8;
            num2 |= (num1 & mask)>>(8*i);
            }

        return num2;
    }

    public static byte[] textPadding( final String text, final String characterSetName, final int length)
        throws java.io.UnsupportedEncodingException
    {
        return textPadding( text, characterSetName, length, Utils.ALIGN_LEFT);
    }

    public static byte[] textPadding( final String text, final String characterSetName, final int length, final int alignment)
        throws java.io.UnsupportedEncodingException
    {

        return textPadding( text, characterSetName, length, alignment, (byte)' ');
    }

    public static byte[] textPadding(
            final String  text,
            final String  characterSetName,
            final int     length,
            final int     alignment,
            final byte    paddingByte
            )
        throws java.io.UnsupportedEncodingException
    {
        if( text.length() >= length) {
            return text.substring( 0, length).getBytes( characterSetName);
            }

        final byte[] byte_array = new byte[ length];
        Arrays.fill( byte_array, paddingByte);

        switch( alignment ) {
            case ALIGN_LEFT:
                System.arraycopy( text.getBytes( characterSetName), 0, byte_array, 0, text.length());
                break;

            case ALIGN_RIGHT:
                final int t_offset = length - text.length();
                System.arraycopy( text.getBytes( characterSetName), 0, byte_array, t_offset, text.length());
                break;
            }

        return byte_array;
    }

    public static byte[] doubleFormating( final Double doubleNum, final String characterSetName, final int fieldLength, final int sizeDecimalPart )
        throws java.io.UnsupportedEncodingException
    {
        final int sizeWholePart = fieldLength - ((sizeDecimalPart>0)?( sizeDecimalPart + 1):0);
        final StringBuilder format = new StringBuilder( fieldLength );

        for( int i=0; i<sizeWholePart; i++) {
            format.append( '#' );
            }

        if( sizeDecimalPart > 0) {
            format.append( '.' );

            for( int i=0; i<sizeDecimalPart; i++) {
                format.append( '0' );
                }
            }

        final DecimalFormat df = new DecimalFormat( format.toString() );

        return textPadding( df.format( doubleNum.doubleValue()).toString(), characterSetName, fieldLength, ALIGN_RIGHT);
    }

    public static boolean contains( final byte[] arr, final byte value) // $codepro.audit.disable booleanMethodNamingConvention
    {
        boolean found = false;

        for( int i=0; i<arr.length; i++) {
            if( arr[i] == value) {
                found = true;
                break;
                }
            }

        return found;
    }
}
