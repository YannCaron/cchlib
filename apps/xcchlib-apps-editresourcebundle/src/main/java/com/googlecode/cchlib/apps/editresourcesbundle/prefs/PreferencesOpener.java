package com.googlecode.cchlib.apps.editresourcesbundle.prefs;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.i18n.annotation.I18nName;
import com.googlecode.cchlib.i18n.annotation.I18nString;
import com.googlecode.cchlib.i18n.core.I18nAutoUpdatable;
import com.googlecode.cchlib.swing.DialogHelper;

@I18nName("PreferencesOpener")
public class PreferencesOpener implements Serializable
{
    private final class MyPreferencesDefaultsParametersValues
        implements PreferencesValues
    {
        private final String[] languages;
        private final int      selectedLanguageIndex;

        private MyPreferencesDefaultsParametersValues(
            final String[] languages,
            final int      selectedLanguageIndex
            )
        {
            this.languages = languages;
            this.selectedLanguageIndex = selectedLanguageIndex;
        }

        @Override
        public int getNumberOfFiles()
        {
            return PreferencesOpener.this.preferences.getNumberOfFiles();
        }

        @Override
        public String[] getLanguages()
        {
            return this.languages;
        }

        @Override
        public int getSelectedLanguageIndex()
        {
            return this.selectedLanguageIndex;
        }

        @Override
        public boolean isSaveWindowSize()
        {
            return false; // FIXME ??
        }
    }

    private final class MyPreferencesAction extends AbstractPreferencesAction
    {
        private final Locale[] locales;

        private MyPreferencesAction( final Locale[] locales )
        {
            this.locales = locales;
        }

        @Override
        public void onSave( final PreferencesCurentSaveParameters saveParams )
        {
            final Locale locale = this.locales[ saveParams.getSelectedLanguageIndex() ];
            PreferencesOpener.this.preferences.setLocale( locale );

            if( saveParams.isSaveWindowSize() ) {
                PreferencesOpener.this.preferences.setWindowDimension( PreferencesOpener.this.rootFrame.getSize() );
                }

            PreferencesOpener.this.preferences.setNumberOfFiles( saveParams.getNumberOfFiles() );

            if( saveParams.isSaveLookAndFeel() ) {
                PreferencesOpener.this.preferences.setLookAndFeelClassName();
                }

            savePreferences();

            this.dispose();
        }
    }

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger( PreferencesOpener.class );
    private static final Locale[] LOCALES = {
        null, // System,
        Locale.ENGLISH,
        Locale.FRENCH
        };

    @I18nString protected String msgStringDefaultLocale = "default system";
    @I18nString protected String msgStringSavePrefsExceptionTitle = "Error while saving preferences";

    private final String[] languages = {
        null,
        LOCALES[1].getDisplayLanguage(),
        LOCALES[2].getDisplayLanguage()
        };
    private final JFrame rootFrame;
    private final Preferences preferences;
    private final PreferencesValues initParams;

    public PreferencesOpener( final JFrame rootFrame, final Preferences preferences )
    {
        this.rootFrame      = rootFrame;
        this.preferences    = preferences;
        this.languages[ 0 ] = this.msgStringDefaultLocale;

        final int selectedLanguageIndex = getSelectedLanguageIndex( preferences );

        this.initParams = newPreferencesDefaultsParametersValues(
                this.languages,
                selectedLanguageIndex
                );
    }

    private int getSelectedLanguageIndex( final Preferences preferences )
    {
        final Locale pLocale               = preferences.getLocale();
        int          selectedLanguageIndex = 0;

        if( pLocale == null ) {
            selectedLanguageIndex = 0;
            }
        else {
            for( int i = 1; i<LOCALES.length; i++ ) {
                if( pLocale.equals( LOCALES[ i ] ) ) {
                    selectedLanguageIndex = i;
                    break;
                    }
                }
            }

        return selectedLanguageIndex;
    }

    public I18nAutoUpdatable open()
    {
        final AbstractPreferencesAction action = newPreferencesAction( LOCALES );
        final PreferencesJDialog        dialog = new PreferencesJDialog( this.initParams, action );

        dialog.setVisible( true );

        return dialog;

    }

    private AbstractPreferencesAction newPreferencesAction( final Locale[] locales )
    {
        return new MyPreferencesAction( locales );
    }

    private PreferencesValues newPreferencesDefaultsParametersValues(
            final String[] languages,
            final int      selectedLanguageIndex
            )
    {
        return new MyPreferencesDefaultsParametersValues( languages, selectedLanguageIndex );
    }

    private void savePreferences()
    {
        LOGGER.info( "Save prefs: " + this.preferences );

        try {
            this.preferences.save();
            }
        catch( final IOException e ) {
            DialogHelper.showMessageExceptionDialog(
                this.rootFrame,
                this.msgStringSavePrefsExceptionTitle,
                e
                );
            }
    }
}
