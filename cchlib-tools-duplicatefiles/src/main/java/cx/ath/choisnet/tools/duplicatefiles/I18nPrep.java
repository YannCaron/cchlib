package cx.ath.choisnet.tools.duplicatefiles;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import com.googlecode.cchlib.i18n.config.I18nPrepHelper;
import cx.ath.choisnet.tools.duplicatefiles.gui.DuplicateFilesFrame;

/**
 * Create resources bundles files
 */
public class I18nPrep
{
    public static void main( String[] args ) throws IOException
    {
        // Default language !
        Locale locale = Locale.ENGLISH;

        // Build frame
        DuplicateFilesFrame duplicateFilesFrame     = new DuplicateFilesFrame();
        PrintStream         usageStatPrintStream    = System.err;
        PrintStream         notUsePrintStream       = System.out;

        I18nPrepHelper.defaultPrep(
            locale,
            usageStatPrintStream,
            notUsePrintStream,
            duplicateFilesFrame/*,
            otherFrames*/
            );
/*
        // Prepare custom I18n to get all statics fields
        I18nPropertyResourceBundleAutoUpdate autoI18n = I18nBundle.getI18nPropertyResourceBundleAutoUpdate(locale);
        File outputFile = new File(
                new File(".").getAbsoluteFile(),
                I18nBundle.getMessagesBundle()
                );
        autoI18n.setOutputFile( outputFile );

        duplicateFilesFrame.performeI18n( autoI18n );

        ResourceBundle      rb          = I18nBundle.getAutoI18nSimpleStatsResourceBundle(locale).getResourceBundle();
        Enumeration<String> enu         = rb.getKeys();
        Map<String,String>  knowKeyMap  = new HashMap<String,String>();

        while( enu.hasMoreElements() ) {
            final String k = enu.nextElement();
            knowKeyMap.put( k, rb.getString( k ) );
            }

        autoI18n.close();

        Map<String,Integer> statsMap        = new HashMap<String,Integer>( I18nBundle.getAutoI18nSimpleStatsResourceBundle( locale ).getUsageMap() );
        List<String>        sortedKeyList   = new ArrayList<String>( statsMap.keySet() );

        Collections.sort( sortedKeyList );

        for( String key : sortedKeyList ) {
            System.out.println(
                    "K=" + key
                    + " Usage= " + statsMap.get( key )
                    );
            knowKeyMap.remove( key );
            }

        System.out.println();
        System.out.flush();

        sortedKeyList.clear();
        sortedKeyList.addAll( knowKeyMap.keySet() );
        Collections.sort( sortedKeyList );

        System.err.println();
        System.err.flush();
        System.err.println( "### not use list ###" );

        for( String key : sortedKeyList ) {
            System.err.println( "### not use ["
                    + key + "=" + knowKeyMap.get( key )
                    + "]"
                    );
            }

        System.err.println( "### Done" );
        System.err.println();
        System.err.flush();
        System.out.println( "Done." );
        System.out.flush();

        System.exit( 0 );
*/
    }

}
