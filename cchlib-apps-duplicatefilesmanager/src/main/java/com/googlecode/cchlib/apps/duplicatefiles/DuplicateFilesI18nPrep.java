package com.googlecode.cchlib.apps.duplicatefiles;

import java.awt.HeadlessException;
import java.awt.Window;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Set;
import java.util.TooManyListenersException;

import com.googlecode.cchlib.apps.duplicatefiles.common.AboutDialog;
import com.googlecode.cchlib.apps.duplicatefiles.gui.DuplicateFilesFrame;
import com.googlecode.cchlib.apps.duplicatefiles.prefs.Preferences;
import com.googlecode.cchlib.apps.duplicatefiles.prefs.PreferencesDialogWB;
import com.googlecode.cchlib.apps.emptyfiles.RemoveEmptyFilesJPanel;
import com.googlecode.cchlib.i18n.AutoI18nConfig;
import com.googlecode.cchlib.i18n.core.I18nAutoCoreUpdatable;
import com.googlecode.cchlib.i18n.core.I18nPrep;
import com.googlecode.cchlib.i18n.prep.I18nPrepHelper;
import com.googlecode.cchlib.i18n.prep.I18nPrepHelper.Result;
import com.googlecode.cchlib.i18n.resources.I18nResourceBundleName;

/**
 * Create resources bundles files
 */
public class DuplicateFilesI18nPrep
{
    public static void main( final String[] args ) throws HeadlessException, TooManyListenersException, InterruptedException
    {
        // Default language !
        final Preferences preferences = getPreferences();

        // Build frame
        final DefaultDFToolKit    defaultDFToolKit_   = new DefaultDFToolKit( preferences );
        final DFToolKit           dfToolKit           = defaultDFToolKit_;
        final DuplicateFilesFrame duplicateFilesFrame = new DuplicateFilesFrame( dfToolKit );
        defaultDFToolKit_.setMainWindow( duplicateFilesFrame );
        final PrintStream         usageStatPrintStream    = System.err;
        final PrintStream         notUsePrintStream       = System.out;

        final I18nAutoCoreUpdatable[] i18nConteners = {
            duplicateFilesFrame,
            new AboutDialog( dfToolKit ),
            new PreferencesDialogWB(),
            new RemoveEmptyFilesJPanel( dfToolKit )
            };

        final Set<AutoI18nConfig>     config                 = dfToolKit.getAutoI18nConfig();
        final I18nResourceBundleName  i18nResourceBundleName = dfToolKit.getI18nResourceBundleName();

        final I18nPrep i18nPrep = I18nPrepHelper.createI18nPrep( config, i18nResourceBundleName, preferences.getLocale() );

        final Result result = I18nPrepHelper.defaultPrep( i18nPrep, i18nConteners );

        I18nPrepHelper.fmtUsageStatCollector( usageStatPrintStream, result );
        I18nPrepHelper.fmtNotUseCollector( notUsePrintStream, result );

        for( final I18nAutoCoreUpdatable contener : i18nConteners ) {
            if( contener instanceof Window ) {
                ((Window)contener).dispose();
                }
            }
        System.gc();
        Thread.sleep( 1000 ); // $codepro.audit.disable numericLiterals
    }


    private static Preferences getPreferences()
    {
        final Preferences preferences = Preferences.createDefaultPreferences();
        preferences.setLocale( Locale.ENGLISH );
        return preferences;
    }
}
