package com.googlecode.cchlib.net.download;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Define a downloadable URL that should be store in a {@link String}
 *
 * @since 4.1.7
 */
public /* TODO add final */ class StringDownloader
    extends AbstractContentDownloadURI<String>
        implements ContentDownloadURI<String>
{
    private static final long serialVersionUID = 2L;
    private String str;

    /**
     * Define the {@link URL} for this {@link DownloadURI}
     * @param url The {@link URL}
     * @param requestPropertyMap    A {@link Map} of request properties to put
     *                              on {@link URLConnection} (could be null)
     * @param proxy                 {@link Proxy} to use for download (could be null)
     * @throws URISyntaxException if this URL is not formatted
     *         strictly according to to RFC2396 and cannot be
     *         converted to a URI.
     */
    public StringDownloader(
        final URL                   url,
        final Map<String, String>   requestPropertyMap,
        final Proxy                 proxy
        ) throws URISyntaxException
    {
        super( url, requestPropertyMap, proxy );
    }

    /**
     * Define the {@link URL} for this {@link DownloadURI}
     * @param spec                  the {@link String} to parse as a URL.
     * @param requestPropertyMap    A {@link Map} of request properties to put
     *                              on {@link URLConnection} (could be null)
     * @param proxy                 {@link Proxy} to use for download (could be null)
     * @throws MalformedURLException If the spec specifies an unknown protocol
     * @throws URISyntaxException if this URL is not formatted
     *         strictly according to to RFC2396 and cannot be
     *         converted to a URI.
     */
    public StringDownloader(
        final String                spec,
        final Map<String, String>   requestPropertyMap,
        final Proxy                 proxy
        ) throws MalformedURLException, URISyntaxException
    {
        super( new URL( spec ), requestPropertyMap, proxy );
    }

    @Override
    public String getResult()
    {
        return this.str;
    }

    @Override
    public void setResult( final String str )
    {
        this.str = str;
    }

    @Override
    public Class<String> getType()
    {
        return String.class;
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();

        builder.append( "StringDownloader [getURL()=" );
        builder.append( getURL() );
        builder.append( ", properties=" );
        builder.append( propertiesToString( this ) );
        builder.append( ']' );

        return builder.toString();
    }
}
