package samples.downloader;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

/**
 *
 *
 *
 */
public class DownloaderSample2 extends GenericDownloader
{
    private final static int    DOWNLOAD_THREAD = 20;
    //private final static Proxy  PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("195.168.109.60", 8080));
    private final static Proxy  PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("122.194.5.154", 80));

    /* http://www.epins.fr/pins/98699.png */
    /* http://www.epins.fr/pins/104639.png */
    private final static String serverRootURLString = "http://www.epins.fr";
    private final static String htmlURLFmt0         = serverRootURLString + "/pins/";
    //private final static String htmlURLFmt1         = "00000";
    private final static String htmlURLFmt2         = ".png";
    //private final static int    MIN = 0;
    //private final static int    MAX = 1000000;
    private final static int    MIN = 1000000;
    //private final static int    MAX = 1100000;
    private final static int    MAX = 110;

    private final static String CACHE_FOLDER_NAME = "output/epins.fr";

    /**
     *
     * @param destinationFolderFile
     * @throws NoSuchAlgorithmException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public DownloaderSample2( final File destinationFolderFile )
        throws NoSuchAlgorithmException, IOException, ClassNotFoundException
    {
        super(
            destinationFolderFile,
            destinationFolderFile,
            DOWNLOAD_THREAD,
            PROXY
            );
    }

    @Override
    protected void println( String s )
    {
        System.out.println( s );
    }

    @Override
    protected Iterable<URL> collectURLs() throws IOException
    {
        //return collectURLPrepare();
        return new Iterable<URL>()
        {
            @Override
            public Iterator<URL> iterator()
            {
                return new Iterator<URL>()
                {
                    private StringBuilder buildURL_sb1 = new StringBuilder();
                    //private StringBuilder buildURL_sb2 = new StringBuilder();
                    private int i = MIN;

                    @Override
                    public boolean hasNext()
                    {
                        return i<MAX;
                    }
                    @Override
                    public URL next()
                    {
                        try {
                            return buildURL( i++ );
                            }
                        catch( MalformedURLException e ) {
                            throw new RuntimeException( e );
                            }
                    }
                    @Override
                    public void remove()
                    {
                        throw new UnsupportedOperationException();
                    }
                    private URL buildURL( final int i ) throws MalformedURLException
                    {

                        buildURL_sb1.setLength( 0 );
                        //buildURL_sb2.setLength( 0 );

                        buildURL_sb1.append( htmlURLFmt0 );

                        buildURL_sb1.append( i );
                        //buildURL_sb2.append( htmlURLFmt1 ).append( i );
                        //int end     = buildURL_sb2.length();
                        //int start   = end - htmlURLFmt1.length();

                        //buildURL_sb1.append( buildURL_sb2.substring( start, end ) );
                        buildURL_sb1.append( htmlURLFmt2 );

                        return new URL( buildURL_sb1.toString() );
                    }
                };
            }
        };
    }

    /**
     * Start Sample here !
     */
    public static void main( String...args )
        throws IOException, NoSuchAlgorithmException, ClassNotFoundException
    {
        File destinationFolderFile = new File( new File(".").getAbsoluteFile(), CACHE_FOLDER_NAME ).getCanonicalFile();
        destinationFolderFile.mkdirs();

        DownloaderSample2 instance = new DownloaderSample2( destinationFolderFile );

        instance.println( "destinationFolderFile = " + destinationFolderFile );
        instance.downloadAll();
        instance.println( "done" );
    }

}
