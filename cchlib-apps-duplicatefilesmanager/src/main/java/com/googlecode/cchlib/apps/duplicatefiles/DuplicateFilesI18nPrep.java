package com.googlecode.cchlib.apps.duplicatefiles;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.TooManyListenersException;
import com.googlecode.cchlib.apps.duplicatefiles.gui.DuplicateFilesFrame;
import com.googlecode.cchlib.apps.duplicatefiles.prefs.Preferences;
import com.googlecode.cchlib.apps.emptydirectories.gui.RemoveEmptyDirectories;
import com.googlecode.cchlib.i18n.config.I18nAutoUpdatable;
import com.googlecode.cchlib.i18n.config.I18nPrepHelper;

/**
 * Create resources bundles files
 */
public class DuplicateFilesI18nPrep
{
    public static void main( String[] args ) throws IOException, HeadlessException, TooManyListenersException
    {
        // Default language !
        Preferences preferences = Preferences.createDefaultPreferences();
        Locale locale = Locale.ENGLISH;

        // Build frame
        DefaultDFToolKit    defaultDFToolKit    = new DefaultDFToolKit( preferences );
        DuplicateFilesFrame duplicateFilesFrame = new DuplicateFilesFrame( defaultDFToolKit );
        defaultDFToolKit.setMainWindow( duplicateFilesFrame );
        PrintStream         usageStatPrintStream    = System.err;
        PrintStream         notUsePrintStream       = System.out;

        I18nAutoUpdatable[] otherFrames = {
            new RemoveEmptyDirectories( defaultDFToolKit, null ),
            };
        I18nPrepHelper.defaultPrep(
            locale,
            usageStatPrintStream,
            notUsePrintStream,
            duplicateFilesFrame,
            otherFrames 
            );
    }

}