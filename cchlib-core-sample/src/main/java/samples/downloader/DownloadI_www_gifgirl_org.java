package samples.downloader;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import com.googlecode.cchlib.net.download.StringDownloadURL;

/**
 * http://www.gifgirl.org/search?updated-max=2012-06-12T17:20:00-05:00&max-results=40&start=12&by-date=false
 * http://www.gifgirl.org/search?updated-max=2012-06-21T16:09:00-05:00&max-results=4
 * http://www.gifgirl.org/search?updated-max=2012-06-18T16:07:00-05:00&max-results=4&start=4&by-date=false
 * http://www.gifgirl.org/search?updated-max=2012-05-29T23:59:00-05:00&max-results=4&start=44&by-date=false
 */
public class DownloadI_www_gifgirl_org extends AbstractExtentedDownloadInterface
{
    private final static String URL_PATTERN
        = "http://www.gifgirl.org/search?updated-max=%04d-%02d-%02dT23:59:00-05:00&max-results=%d&start=%d&by-date=%s";
        // int:year
        // int:month
        // int:day
        // int:max-results
        // int:start
        // String(boolean):sort?
        //="http://www.gifgirl.org/search?updated-max=2012-05-29T23:59:00-05:00&max-results=4&start=44&by-date=false";
    private ComboBoxConfig configMaxResults;
    private ComboBoxConfig configStart;

    /**
     *
     */
    public DownloadI_www_gifgirl_org()
    {
        super( "www.gifgirl.org",
                50, // numberOfPicturesByPage
                50  // pageCount
                );

        configMaxResults = new DefaultComboBoxConfig(
                "MaxResults",   //labelString
                4,              // min
                99,             // max
                "Number of results by page" // labelStrings
                );
        configStart = new DefaultComboBoxConfig(
                "Start",        //labelString
                1,              // min
                99,             // max
                "'start' parameter value" // labelStrings
                );
        ComboBoxConfig config2 = new DefaultComboBoxConfig(
                "labelString",  //labelString
                new String[]{ "A" }, // comboBoxValues
                new String[]{ "a" }  // labelStrings
                );

        super.addComboBoxConfig( configStart );
        super.addComboBoxConfig( configMaxResults );
        super.addComboBoxConfig( config2 );
    }

    @Override
    public String getCacheRelativeDirectoryCacheName()
    {
        return "www.gifgirl.org";
    }

    @Override
    public CookieHandler getCookieHandler()
    {
        try {
            CookieStore myCookieStore   = new DefaultCookieStore();
/*
GET /search?updated-max=2012-06-08T23:59:00-05:00&max-results=4&start=1&by-date=false HTTP/1.1
Host: www.gifgirl.org
User-Agent: Mozilla/5.0 (Windows NT 5.1; rv:15.0) Gecko/20120626 Firefox/15.0a2
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,* /*;q=0.8
Accept-Language: fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3
Accept-Encoding: gzip, deflate
Proxy-Connection: keep-alive
Cookie: GI=k5b_LjgBAAA.9pAeNNUJa47XABsoKl8KNDJNsIDHeRGFMTT51avGYgI._rCoWTIiNZ3KTF-r93ikbQ; blogger_TID=2430fdb2785994ed

 */
/*
            .www.gifgirl.org    TRUE    /   FALSE   0   GI          k5b_LjgBAAA.9pAeNNUJa47XABsoKl8KNDJNsIDHeRGFMTT51avGYgI._rCoWTIiNZ3KTF-r93ikbQ
            www.gifgirl.org     FALSE   /   FALSE   0   blogger_TID 2430fdb2785994ed
            www.blogger.com     FALSE   /   FALSE   0   blogger_TID 71db18618988a2d0
 */
          //myCookieStore.add( uri, new HttpCookie( "GI", "k5b_LjgBAAA.9pAeNNUJa47XABsoKl8KNDJNsIDHeRGFMTT51avGYgI._rCoWTIiNZ3KTF-r93ikbQ" ) );
            /*
            myCookieStore.add(
                    new URI( "http", "www.gifgirl.org", null, null ), // NOT VALID !!!
                    new HttpCookie( "GI", "cF58MjgBAAA.9pAeNNUJa47XABsoKl8KNDJNsIDHeRGFMTT51avGYgI.vGZByWTt9UwywDAJ_k7zsQ" )
                    );
            myCookieStore.add(
                    new URI( "http://www.gifgirl.org/" ),
                    new HttpCookie( "blogger_TID", "2430fdb2785994ed" )
                    );*/
            //Cookie: GI=k5b_LjgBAAA.9pAeNNUJa47XABsoKl8KNDJNsIDHeRGFMTT51avGYgI._rCoWTIiNZ3KTF-r93ikbQ; blogger_TID=2430fdb2785994ed
            myCookieStore.add(
                    new URI( "http", "www.gifgirl.org", null, null ),
                    new HttpCookie( "GI", "k5b_LjgBAAA.9pAeNNUJa47XABsoKl8KNDJNsIDHeRGFMTT51avGYgI._rCoWTIiNZ3KTF-r93ikbQ" )
                    );
            myCookieStore.add(
                    new URI( "http", "www.gifgirl.org", null, null ),
                    new HttpCookie( "blogger_TID", "2430fdb2785994ed" )
                    );

            return new CookieManager( myCookieStore , CookiePolicy.ACCEPT_ALL );
            }
        catch( URISyntaxException e ) {
            throw new RuntimeException( e );
            }

//        try {
//            final Map<URI,Map<String, List<String>>> map = new HashMap<>();
//
//            URI                         uri     = new URI( "FIXME" );
//            Map<String,List<String>>    data    = new HashMap<>();
//
//            final String    key         = "Cookie";
//            List<String>    valueList   = new ArrayList<>();
//
//            String cookieStr = "FIXME";
//            valueList.add( cookieStr );
//
//            data.put( key, valueList );
//
//            map.put( uri, data );
//
//            return map ;
//            }
//        catch( URISyntaxException e ) {
//            throw new RuntimeException( e );
//            }
    }


    @Override
    public StringDownloadURL getStringDownloadURL( final int pageNumber )
            throws MalformedURLException
    {
        int      amount = 1 - pageNumber; // = - ( pageNumber - 1 )
        Calendar today  = Calendar.getInstance();

        today.add( Calendar.DAY_OF_MONTH, amount );

        int     year        = today.get( Calendar.YEAR );
        int     month       = today.get( Calendar.MONTH ) + 1; // 0 based value
        int     day         = today.get( Calendar.DAY_OF_MONTH );
        int     start       = Integer.parseInt( configStart.getComboBoxSelectedValue() );
        int     maxResults  = Integer.parseInt( configMaxResults.getComboBoxSelectedValue() );
        String  sort        = Boolean.FALSE.toString();

        return new StringDownloadURL(
                String.format( URL_PATTERN, year , month, day, maxResults, start, sort )
                );
    }

    @Override
    public Collection<URL> getURLToDownloadCollection(
            final GenericDownloaderAppUIResults gdauir,
            final String                        content2Parse
            )
            throws MalformedURLException
    {
        // <center><a href="http://gifgirl.org/" target="_blank"><img src="http://2.bp.blogspot.com/-0_HzqSNCHCY/T0BvP-4LHtI/AAAAAAAAbg8/7fRRM9GbZTY/s1600/GGgogiants.gif"></a></center>_
        // </p><center><a href="http://gifgirl.org/" target="_blank"><img src="http://2.bp.blogspot.com/-Xootw5KNh2s/T0Busd__XLI/AAAAAAAAbgI/z4pDzmRz-v8/s1600/GGbitingnipple.gif"></a></center>
        // </p><center><a href="http://gifgirl.org/" target="_blank"><img src="http://1.bp.blogspot.com/-5T7_VbjbB3w/T4jvVOlsLUI/AAAAAAAAeKQ/LLEXC1cGUD0/s1600/GGBoobflasher.gif"></a></center>________________________________________<p></p>
        //
        // <center><a href="http://gifgirl.org/" target="_blank"><img src="
        RegExgSplitter[] regexps = {
            new DefaultRegExgSplitter( "<center><a href=\"http\\://gifgirl\\.org/\" target=\"_blank\"><img src=\"", '"' ),
            //new DefaultRegExgSplitter( "<img src=\"", '"' ),
            };
        return super.getURLToDownloadCollection( gdauir, content2Parse, regexps  );
    }

    @Override
    public URL getURLToDownload( final String src, final int regexpIndex )
            throws MalformedURLException
    {
        //URL u = new URL( src );

        //u.openConnection().setRequestProperty( key, value );


        return new URL( src );
    }

}
