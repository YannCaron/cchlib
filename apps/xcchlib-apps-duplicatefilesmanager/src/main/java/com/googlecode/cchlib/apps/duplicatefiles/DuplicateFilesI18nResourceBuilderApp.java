package com.googlecode.cchlib.apps.duplicatefiles;

import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.VisibleForTesting;
import com.googlecode.cchlib.apps.duplicatefiles.swing.about.AboutDialog;
import com.googlecode.cchlib.apps.duplicatefiles.swing.gui.DuplicateFilesFrame;
import com.googlecode.cchlib.apps.duplicatefiles.swing.gui.prefs.PreferencesDialogWB;
import com.googlecode.cchlib.apps.duplicatefiles.swing.prefs.PreferencesControler;
import com.googlecode.cchlib.apps.duplicatefiles.swing.prefs.PreferencesControlerFactory;
import com.googlecode.cchlib.apps.duplicatefiles.swing.ressources.ResourcesPath;
import com.googlecode.cchlib.apps.duplicatefiles.swing.services.AutoI18nService;
import com.googlecode.cchlib.i18n.core.I18nAutoUpdatable;
import com.googlecode.cchlib.i18n.resourcebuilder.I18nResourceBuilder;
import com.googlecode.cchlib.i18n.resourcebuilder.I18nResourceBuilderFactory;
import com.googlecode.cchlib.i18n.resourcebuilder.I18nResourceBuilderHelper;
import com.googlecode.cchlib.i18n.resourcebuilder.I18nResourceBuilderResult;
import com.googlecode.cchlib.lang.Threads;

/**
 * Create resources bundles files
 * <br>
 * This class is design to generate i18n files
 */
@SuppressWarnings("ucd") // Development configuration entry point
public class DuplicateFilesI18nResourceBuilderApp
{
    private static final Logger LOGGER = Logger.getLogger( DuplicateFilesI18nResourceBuilderApp.class );

    private DuplicateFilesI18nResourceBuilderApp()
    {
        // Empty
    }

    @SuppressWarnings("squid:S106") // System.out
    public static void main( final String[] args ) throws Exception
    {
        LOGGER.info( "Launch I18nResourceBuilder" );

        final I18nResourceBuilderResult result = runResourceBuilder();
        I18nResourceBuilderHelper.fmtAll( System.out, result );

        LOGGER.info( "I18nResourceBuilder done" );
    }

    @VisibleForTesting
    @SuppressWarnings({
        "squid:RedundantThrowsDeclarationCheck"
        })
    static I18nResourceBuilderResult runResourceBuilder()
        throws IOException, HeadlessException, TooManyListenersException
    {
        // Default language !
        final PreferencesControler preferences = getPreferences();

        // Build frame
        final DuplicateFilesFrame duplicateFilesFrame  = new DuplicateFilesFrame( preferences );

        final I18nAutoUpdatable[] i18nConteners = {
            duplicateFilesFrame,
            new AboutDialog(),
            new PreferencesDialogWB(),
            // new RemoveEmptyFilesJPanel() - Already discovered in duplicateFilesFrame
            };

        final I18nResourceBuilder builder
            = I18nResourceBuilderFactory.newI18nResourceBuilder(
                    AutoI18nService.getInstance().getAutoI18n()
                    );

        for( final I18nAutoUpdatable i18nObject : i18nConteners ) {
            builder.append( i18nObject );
        }

        final File outputFile = I18nResourceBuilderHelper.newOutputFile(
                ResourcesPath.class.getPackage()
                );

        builder.saveMissingResourceBundle( outputFile );

        // Compute result
        final I18nResourceBuilderResult result = builder.getResult();

        // Dispose all windows.
        for( final I18nAutoUpdatable contener : i18nConteners ) {
            if( contener instanceof Window ) {
                ((Window)contener).dispose();
                }
            }

        // Wait to avoid some error trace from awt/swing
        Threads.sleep( 1, TimeUnit.SECONDS );

        return result;
    }

    private static PreferencesControler getPreferences()
    {
        final PreferencesControler preferences = PreferencesControlerFactory.createDefaultPreferences();

        preferences.setLocale( Locale.ENGLISH );

        return preferences;
    }
}
