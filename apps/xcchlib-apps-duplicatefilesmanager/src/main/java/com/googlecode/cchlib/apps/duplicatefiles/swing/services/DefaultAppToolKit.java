package com.googlecode.cchlib.apps.duplicatefiles.swing.services;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.apps.duplicatefiles.swing.AppToolKit;
import com.googlecode.cchlib.apps.duplicatefiles.swing.FileChooserEntryPoint;
import com.googlecode.cchlib.apps.duplicatefiles.swing.gui.DuplicateFilesFrame;
import com.googlecode.cchlib.apps.duplicatefiles.swing.prefs.PreferencesControler;
import com.googlecode.cchlib.apps.duplicatefiles.swing.ressources.ResourcesPath;
import com.googlecode.cchlib.apps.duplicatefiles.swing.tools.MyResourcesLoader;
import com.googlecode.cchlib.apps.duplicatefiles.swing.tools.Resources;
import com.googlecode.cchlib.i18n.AutoI18n;
import com.googlecode.cchlib.i18n.annotation.I18nIgnore;
import com.googlecode.cchlib.i18n.annotation.I18nName;
import com.googlecode.cchlib.i18n.annotation.I18nString;
import com.googlecode.cchlib.i18n.api.I18nResource;
import com.googlecode.cchlib.i18n.core.I18nAutoUpdatable;
import com.googlecode.cchlib.i18n.resources.I18nResourceFactory;
import com.googlecode.cchlib.lang.Threads;
import com.googlecode.cchlib.swing.DialogHelper;
import com.googlecode.cchlib.swing.filechooser.JFileChooserInitializer;
import com.googlecode.cchlib.swing.filechooser.JFileChooserInitializerCustomize;
import com.googlecode.cchlib.swing.filechooser.WaitingJFileChooserInitializer;

//NOT public
@I18nName("DefaultAppToolKit")
final class DefaultAppToolKit
    implements AppToolKit, I18nAutoUpdatable
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger( DefaultAppToolKit.class );

    private final Map<FileChooserEntryPoint,JFileChooserInitializer> jFileChooserInitializerMap= new EnumMap<>( FileChooserEntryPoint.class );
    private final PreferencesControler preferences;

    @I18nIgnore  // Prevent recursive analysis
    private DuplicateFilesFrame mainWindow;

    @I18nString private String jFileChooserInitializerTitle;
    @I18nString private String jFileChooserInitializerMessage;
    @I18nString private String txtOpenDesktopExceptionTitle;

    //NOT public
    DefaultAppToolKit(
        final PreferencesControler preferences
        )
    {
        this.preferences = preferences;

        beSurNonFinal();
    }

    private void beSurNonFinal()
    {
        this.jFileChooserInitializerTitle     = "Waiting...";
        this.jFileChooserInitializerMessage   = "Analyze disk structure";
        this.txtOpenDesktopExceptionTitle     = "Can not open file";
    }

    @Override // I18nAutoUpdatable
    public void performeI18n( final AutoI18n autoI18n )
    {
        autoI18n.performeI18n( this, this.getClass() );
    }

    @Override // DFToolKit
    public I18nResource getI18nResource()
    {
        return I18nResourceFactory.newI18nResourceBundle(
            ResourcesPath.class,
            I18nResourceFactory.DEFAULT_MESSAGE_BUNDLE_BASENAME,
            getValidLocale()
          );
    }

    public void setMainWindow( final DuplicateFilesFrame mainWindow )
    {
        this.mainWindow = mainWindow;
    }

    @SuppressWarnings({"squid:RedundantThrowsDeclarationCheck","squid:S00100"})
    private DuplicateFilesFrame private_getMainWindow() throws IllegalStateException
    {
        if( this.mainWindow == null ) {
            throw new IllegalStateException( "mainWindow not set" );
            }

        return this.mainWindow;
    }

    @Override // DFToolKit
    public Frame getMainFrame()
    {
        return this.mainWindow;
    }

    @Override
    public void initJFileChooser()
    {
        final Window win = getMainFrame();

        getJFileChooserInitializer( win , FileChooserEntryPoint.DUPLICATES );
    }

    @Override // DFToolKit
    public JFileChooser getJFileChooser(
        final Window                parentWindow,
        final FileChooserEntryPoint componentName
        )
    {
        return getJFileChooserInitializer( parentWindow, componentName ).getJFileChooser();
    }

    @Override
    public JFileChooserInitializer getJFileChooserInitializer(
        final Window                parentWindow,
        final FileChooserEntryPoint componentName
        )
    {
        JFileChooserInitializer jFileChooserInitializer = this.jFileChooserInitializerMap.get( componentName );

        if( jFileChooserInitializer == null ) {
            if( LOGGER.isDebugEnabled() ) {
                LOGGER.debug( "Prepare JFileChooser for : " + componentName );
            }

            final JFileChooserInitializerCustomize configurator = new AppJFileChooserInitializerCustomize();

            jFileChooserInitializer = new WaitingJFileChooserInitializer(
                    configurator,
                    parentWindow,
                    this.jFileChooserInitializerTitle,
                    this.jFileChooserInitializerMessage
                    );

            if( LOGGER.isDebugEnabled() ) {
                LOGGER.debug( "JFileChooserInitializer for " + componentName + " is running" );
            }

            this.jFileChooserInitializerMap.put( componentName, jFileChooserInitializer );
        }

        return jFileChooserInitializer;
    }
    @Override // DFToolKit
    public void beep()
    {
        Toolkit.getDefaultToolkit().beep();
    }

    @Override // DFToolKit
    public void openDesktop( final File file )
    {
        final java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        try {
            LOGGER.info( "trying to open: " + file );
            desktop.open( file );
            }
        catch( final IOException e ) {
            DialogHelper.showMessageExceptionDialog(
                    getMainFrame(),
                    this.txtOpenDesktopExceptionTitle,
                    e
                    );
            }
    }

    @Override // DFToolKit
    public void sleep( final long ms )
    {
        Threads.sleep( ms, TimeUnit.MILLISECONDS );
    }

    @Override // DFToolKit
    public PreferencesControler getPreferences()
    {
        return this.preferences;
    }

    @Override // DFToolKit
    public Locale getValidLocale()
    {
        // Get Locale define by prefs
        Locale locale = getPreferences().getLocale();

        if( locale == null ) {
            // Try to get Locale from main JFrame
            try {
                locale = getMainFrame().getLocale();
                }
            catch( final Exception e ) {
                locale = null;
                LOGGER.warn( "Can not use main window to set Locale", e );
                }

            if( locale == null ) {
                // Use Locale locale
                return Locale.getDefault();
                }
            }

        return locale;
    }

    @Override // DFToolKit
    public void setEnabledJButtonCancel( final boolean b )
    {
        private_getMainWindow().setJButtonCancelEnabled( b );
    }

    @Override // DFToolKit
    public boolean isEnabledJButtonCancel()
    {
        return private_getMainWindow().isEnabledJButtonCancel();
    }

    @Override // DFToolKit
    public void initComponentsJPanelConfirm()
    {
        private_getMainWindow().initComponentsJPanelConfirm();
    }

    @Override // DFToolKit
    public Resources getResources()
    {
        return MyResourcesLoader.getResources();
    }

    @Override // DFToolKit
    public List<File> getRootDirectoriesList()
    {
        final List<File> list = new ArrayList<>();

        if( this.mainWindow != null ) {
            for( final File f : this.mainWindow.getDuplicateFilesMainPanel().getJPanel0Select().entriesToScans() ) {
                if( f.isDirectory() ) {
                    list.add( f );
                    }
                }
            }

        return list;
    }
}
