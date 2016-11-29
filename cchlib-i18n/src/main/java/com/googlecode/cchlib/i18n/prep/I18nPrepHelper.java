package com.googlecode.cchlib.i18n.prep;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import com.googlecode.cchlib.NeedDoc;
import com.googlecode.cchlib.i18n.AutoI18nConfig;
import com.googlecode.cchlib.i18n.AutoI18nTypeLookup;
import com.googlecode.cchlib.i18n.core.AutoI18nCore;
import com.googlecode.cchlib.i18n.core.I18nAutoCoreUpdatable;
import com.googlecode.cchlib.i18n.core.I18nPrep;
import com.googlecode.cchlib.i18n.resources.I18nResourceBundleName;

/**
 * Create resources bundles files
 */
public final class I18nPrepHelper
{
    private static final class DefaultResult implements Result
    {
        private final PrepCollector<String>  notUseCollector;
        private final File                   outputFile;
        private final PrepCollector<Integer> usageStatCollector;

        private DefaultResult(
                final PrepCollector<String>  notUseCollector,
                final File                   outputFile,
                final PrepCollector<Integer> usageStatCollector
                )
        {
            this.notUseCollector = notUseCollector;
            this.outputFile = outputFile;
            this.usageStatCollector = usageStatCollector;
        }

        @Override
        public PrepCollector<Integer> getUsageStatCollector()
        {
            return this.usageStatCollector;
        }

        @Override
        public PrepCollector<String> getNotUseCollector()
        {
            return this.notUseCollector;
        }

        @Override
        public File getOutputFile()
        {
            return this.outputFile;
        }
    }

    /**
     * Results for {@link I18nPrepHelper}
     */
    @NeedDoc
    public interface Result
    {
        /**
         * NEEDDOC
         * @return NEEDDOC
         */
        PrepCollector<Integer> getUsageStatCollector();

        /**
         * NEEDDOC
         * @return NEEDDOC
         */
        PrepCollector<String> getNotUseCollector();

        /**
         * NEEDDOC
         * @return NEEDDOC
         */
        File getOutputFile();
    }

    private I18nPrepHelper()
    {
        // All static
    }

    /**
     * Invoke {@link #createI18nPrep(Set, I18nResourceBundleName, Locale)},
     * just here to have same method name that I18n default process
     *
     * @param config NEEDDOC
     * @param messageBundleName NEEDDOC
     * @param locale NEEDDOC
     * @return NEEDDOC
     */
    public static I18nPrep createAutoI18nCore(
        final Set<AutoI18nConfig>     config,
        final I18nResourceBundleName  messageBundleName,
        final Locale                  locale
        )
    {
        return createI18nPrep( config, messageBundleName, locale );
    }

    /**
     * NEEDDOC
     *
     * @param config NEEDDOC
     * @param messageBundleName NEEDDOC
     * @param locale NEEDDOC
     * @return NEEDDOC
     */
    public static I18nPrep createI18nPrep(
        final Set<AutoI18nConfig>     config,
        final I18nResourceBundleName  messageBundleName,
        final Locale                  locale
        )
    {
        final AutoI18nTypeLookup defaultAutoI18nTypes = null; // Use default implementation

        return createI18nPrep( config, defaultAutoI18nTypes, messageBundleName, locale );
    }

    /**
     * NEEDDOC
     *
     * @param config NEEDDOC
     * @param defaultAutoI18nTypes NEEDDOC
     * @param messageBundleName NEEDDOC
     * @param locale NEEDDOC
     * @return NEEDDOC
     */
    public static I18nPrep createI18nPrep(
        final Set<AutoI18nConfig>     config,
        final AutoI18nTypeLookup      defaultAutoI18nTypes,
        final I18nResourceBundleName  messageBundleName,
        final Locale                  locale
        )
    {
        return new I18nPrep( config, defaultAutoI18nTypes, locale, messageBundleName );
    }

    /**
     * NEEDDOC
     *
     * @param usageStatPrintStream NEEDDOC
     * @param result NEEDDOC
     */
    public static void fmtUsageStatCollector(
        final PrintStream usageStatPrintStream,
        final Result      result
        )
    {
        final PrepCollector<Integer> usageStatCollector = result.getUsageStatCollector();

        for( final Map.Entry<String,Integer> entry : usageStatCollector ) {
            usageStatPrintStream.println("K="+entry.getKey()+" Usage= "+entry.getValue());
            }

        usageStatPrintStream.println();
    }

    /**
     * NEEDDOC
     *
     * @param notUsePrintStream NEEDDOC
     * @param result NEEDDOC
     */
    public static void fmtNotUseCollector(
        final PrintStream notUsePrintStream,
        final Result      result
        )
    {
        final PrepCollector<String> notUseCollector = result.getNotUseCollector();

        notUsePrintStream.println( "### not use list ###" );

         for( final Map.Entry<String,String> entry : notUseCollector ) {
            notUsePrintStream.println("### not use ["+entry.getKey()+'='+entry.getValue()+']');
            }

        notUsePrintStream.println( "### Done" );
        notUsePrintStream.println();
    }

    /**
     * NEEDDOC
     *
     * @param i18nPrep NEEDDOC
     * @param i18nConteners NEEDDOC
     * @return NEEDDOC
     * @throws I18nPrepException NEEDDOC
     */
    public static Result defaultPrep(
        final I18nPrep                 i18nPrep,
        final I18nAutoCoreUpdatable... i18nConteners
        ) throws I18nPrepException
    {
        final PrepCollector<Integer> usageStatCollector = new PrepCollector<>();
        final PrepCollector<String>  notUseCollector    = new PrepCollector<>();
        final File                   outputFile         = new File(
            new File(".").getAbsoluteFile(),
            i18nPrep.getI18nResourceBundleName().getName()
            );

        final AutoI18nCore autoI18n = i18nPrep.getAutoI18nCore();

        i18nPrep.openOutputFile( outputFile );

        defaultPrep(
                i18nPrep,
                autoI18n,
                usageStatCollector,
                notUseCollector,
                i18nConteners
                );

        closeAll( i18nPrep, outputFile );

        return new DefaultResult( notUseCollector, outputFile, usageStatCollector );
    }

    private static void defaultPrep(
        final I18nPrep                 i18nPrep,
        final AutoI18nCore             autoI18n,
        final PrepCollector<Integer>   usageStatCollector,
        final PrepCollector<String>    notUseCollector,
        final I18nAutoCoreUpdatable... i18nConteners
        )
    {
        for( final I18nAutoCoreUpdatable i18nContener : i18nConteners ) {
            i18nContener.performeI18n( autoI18n );
            }

        final ResourceBundle      rb          = i18nPrep.getResourceBundle();
        final Enumeration<String> enu         = rb.getKeys();
        final Map<String,String>  knowKeyMap  = new HashMap<>();

        while( enu.hasMoreElements() ) {
            final String k = enu.nextElement();

            assert k != null : "Key is null";

            knowKeyMap.put( k, rb.getString( k ) );
            }

        final Map<String,Integer> statsMap = new HashMap<>( i18nPrep.getUsageMap() );

        for( final Entry<String, Integer> entry : statsMap.entrySet() ) {
            final String key = entry.getKey();

            usageStatCollector.add( key, entry.getValue() );
            knowKeyMap.remove( key );
            }

        for( final String key : statsMap.keySet() ) {
            notUseCollector.add( key, knowKeyMap.get( key ) );
            }
    }

    private static void closeAll( final I18nPrep i18nPrep, final File outputFile )
        throws I18nPrepException
    {
        try {
            i18nPrep.closeOutputFile();
            }
        catch( final IOException cause ) {
            throw new I18nPrepException( outputFile, cause );
            }
    }
}
