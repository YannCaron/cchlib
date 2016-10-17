package com.googlecode.cchlib.apps.duplicatefiles.console;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Helper to JSON Serialization
 */
public class JSONHelper
{
    private JSONHelper()
    {
        // Empty - All static
    }

    public static <T> String toJSON( //
        final T value
        ) throws JsonProcessingException
    {
        final ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString( value );
    }

    public static <T> T fromJSON( //
        final String   jsonString,
        final Class<T> clazz
        ) throws JSONHelperException
    {
        final ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue( jsonString, clazz );
        }
        catch( final IOException e ) {
            throw new JSONHelperException( e );
        }
    }

    /**
     *
     * @param jsonFile
     * @param clazz
     * @return
     * @throws JSONHelperException
     */
    public static <T> T load( final File jsonFile, final Class<T> clazz )
        throws JSONHelperException
    {
        final ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue( jsonFile, clazz );
        }
        catch( final IOException e ) {
            throw new JSONHelperException( e );
        }
    }
    public static <T> T load( final File jsonFile, final TypeReference<T> type )
        throws JSONHelperException
    {
        try {
            return new ObjectMapper().readValue( jsonFile, type );
        }
        catch( final IOException e ) {
            throw new JSONHelperException( e );
        }
    }

    /**
     * Store <code>value</code> in file <code>jsonFile</code>
     *
     * @param jsonFile File to use to store <code>value</code>
     * @param value Object to serialize
     * @param prettyJson If true format output
     * @throws JSONHelperException
     */
    public static <T> void toJSON( //
            final File    jsonFile,
            final T       value,
            final boolean prettyJson
            ) throws JSONHelperException
    {
        final ObjectMapper mapper = new ObjectMapper();

        try {
            if( prettyJson ) {
                mapper.writerWithDefaultPrettyPrinter().writeValue( jsonFile, value );
            } else {
                mapper.writeValue( jsonFile, value );
            }
        }
        catch( final IOException e ) {
            throw new JSONHelperException( e );
        }
    }
}
