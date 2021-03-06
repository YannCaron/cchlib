package com.googlecode.cchlib.sql;

import java.io.IOException;
import com.googlecode.cchlib.lang.StringHelper;

/**
 * Conversions tools for create SQL requests.
 */
public final class SQLTools
{
    /**
     * (Expected String,Replace String)
     */
    private static final String[][] REMPLACEPHRASE = {
            {"'", "\\'"},
            {"\\", "\\\\"}
        };

    private SQLTools()
    {
        // All static !:
    }

    /**
     * Returns convert raw String (from user or database) to be usable as
     * an SQL String.
     * <p>
     * See {@link #parseFieldValue(String)} for more details.
     * </p>
     * @param fieldValue value to convert.
     * @param maxLength maximum length for this field, to ensure result could
     *                  be written in column.
     * @return valid SQL String
     * @throws NullPointerException if input is null
     * @see #parseFieldValue(String)
     */
    public static String parseFieldValue(
            final String    fieldValue,
            final int       maxLength
            )
    {
        final int len = fieldValue.length();

        if(len > maxLength) {
            return SQLTools.parseFieldValue(fieldValue.substring(0, maxLength - 1));
            }
        else {
            return SQLTools.parseFieldValue(fieldValue);
            }
    }

    /**
     * Returns convert raw String (from user or database) to be usable as
     * an SQL String.
     * <p>
     * String result = parseFieldValue( fieldValue );
     * </p>
     * <ul>
     * <li>fieldValue = "I can't";</li>
     * <li>result = "I can\'t";</li>
     * </ul>
     * <p>
     * String sql = "SELECT * FROM `mytable` WHERE `myfield`='" + result + "';";
     * </p>
     * @param fieldValue value to convert.
     * @return valid SQL String
     * @throws NullPointerException if input is null
     * @see #parseFieldValue(String, int)
     */
    public static String parseFieldValue( final String fieldValue )
    {
        if( fieldValue == null ) {
            throw new NullPointerException("'fieldValue' param is null");
            }

        final StringBuilder sb = new StringBuilder();

        parseFieldValue( sb, fieldValue, 0);

        return sb.toString();
    }

    /**
     * Recursive internal converter.
     *
     * @param output        A valid {@link Appendable} object, for result.
     * @param fieldValue    value to convert.
     * @param idx           index in {@link #REMPLACEPHRASE}
     * @throws IOException  If can't append to appender 'a'
     */
    private static void parseFieldValue(
            final StringBuilder/*Appendable*/ output,
            final String                      value,
            final int                         idx
            )
    {
        if( idx >= REMPLACEPHRASE.length ) {
            output.append( value );
            return;
            }
        if( value.length() == 0 ) {
            return;
            }

         final String[] parts = StringHelper.split( value, REMPLACEPHRASE[idx][0] );

         for( int pi = 0; pi<parts.length; pi++ ) {
             parseFieldValue( output, parts[ pi ], idx + 1 );

             if( pi < (parts.length - 1) ) {
                 output.append( REMPLACEPHRASE[idx][1] );
                 }
             }

    }
}
