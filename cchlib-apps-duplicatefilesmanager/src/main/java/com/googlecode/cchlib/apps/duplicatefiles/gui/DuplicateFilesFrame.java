// $codepro.audit.disable largeNumberOfFields
package com.googlecode.cchlib.apps.duplicatefiles.gui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TooManyListenersException;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.googlecode.cchlib.apps.duplicatefiles.ConfigMode;
import com.googlecode.cchlib.apps.duplicatefiles.DFToolKit;
import com.googlecode.cchlib.apps.duplicatefiles.KeyFileState;
import com.googlecode.cchlib.apps.duplicatefiles.Tools;
import com.googlecode.cchlib.apps.duplicatefiles.common.AboutDialog;
import com.googlecode.cchlib.apps.duplicatefiles.prefs.Preferences;
import com.googlecode.cchlib.apps.duplicatefiles.prefs.PreferencesDialogWB;
import com.googlecode.cchlib.apps.emptydirectories.gui.RemoveEmptyDirectoriesStandaloneApp;
import com.googlecode.cchlib.i18n.annotation.I18nName;
import com.googlecode.cchlib.i18n.annotation.I18nString;
import com.googlecode.cchlib.i18n.core.AutoI18nCore;
import com.googlecode.cchlib.i18n.core.AutoI18nCoreFactory;
import com.googlecode.cchlib.i18n.core.I18nAutoCoreUpdatable;
import com.googlecode.cchlib.swing.JFrames;
import com.googlecode.cchlib.swing.SafeSwingUtilities;
import com.googlecode.cchlib.swing.menu.LookAndFeelMenu;
import com.googlecode.cchlib.util.HashMapSet;
import com.googlecode.cchlib.util.duplicate.MessageDigestFile;

/**
 *
 */
@I18nName("DuplicateFilesFrame")
final public class DuplicateFilesFrame
    extends DuplicateFilesFrameWB
        implements I18nAutoCoreUpdatable
{
    private static final long serialVersionUID = 2L;
    private static final Logger LOGGER = Logger.getLogger( DuplicateFilesFrame.class );

    private RemoveEmptyDirectoriesStandaloneApp removeEmptyDirectories;
    private ActionListener mainActionListener;

    private HashMapSet<String,KeyFileState> duplicateFiles = new HashMapSet<String,KeyFileState>(); // $codepro.audit.disable declareAsInterface

    private int                 state;
    private int                 subState;
    private static final int    STATE_SELECT_DIRS      = 0;
    private static final int    STATE_SEARCH_CONFIG    = 1;
    private static final int    STATE_SEARCHING        = 2;
    private static final int    STATE_RESULTS          = 3;
    private static final int    STATE_CONFIRM          = 4;
    private static final int    SUBSTATE_CONFIRM_INIT  = 0;
    private static final int    SUBSTATE_CONFIRM_DONE  = 1;

    private AutoI18nCore autoI18n;

    private Icon iconContinue;
    private Icon iconRestart;

    @I18nString private String txtContinue  = "Continue";
    @I18nString private String txtRestart   = "Restart";
    @I18nString private String txtRemove    = "Remove";
    @I18nString private String txtDeleteNow = "Delete now";
    @I18nString private String txtBack      = "Back";
    @I18nString private String txtCancel            = "Cancel";
    @I18nString private String txtClearSelection    = "Clear selection";

    public DuplicateFilesFrame(
        final DFToolKit dfToolKit
        )
        throws HeadlessException, TooManyListenersException
    {
        super( dfToolKit );

        //
        // Menu: configMode
        //
        {
            Enumeration<AbstractButton> modeEntriesEnum = getButtonGroupConfigMode().getElements();
            ConfigMode                  configMode      = getDFToolKit().getPreferences().getConfigMode();

            while( modeEntriesEnum.hasMoreElements() ) {
                AbstractButton  entry   = modeEntriesEnum.nextElement();
                Object          cf      = entry.getClientProperty( ConfigMode.class );

                entry.setSelected( configMode.equals( cf ) );
                }
        }

        //
        // Menu: Locale
        //
        final Locale locale = getDFToolKit().getPreferences().getLocale();

        {
            Enumeration<AbstractButton> localEntriesEnum = getButtonGroupLanguage().getElements();

            while( localEntriesEnum.hasMoreElements() ) {
                AbstractButton  entry   = localEntriesEnum.nextElement();
                Object          l       = entry.getClientProperty( Locale.class );

                if( locale == null ) {
                    entry.setSelected( l == null );
                    }
                else {
                    entry.setSelected( locale.equals( l ) );
                    }
                }
        }

        // Init i18n
        if( LOGGER.isTraceEnabled() ) {
            LOGGER.info( "I18n Init: Locale.getDefault()=" + Locale.getDefault() );
            LOGGER.info( "I18n Init: locale = " + locale );
            LOGGER.info( "I18n Init: getValidLocale() = " + getDFToolKit().getValidLocale() );
            LOGGER.info( "I18n Init: getI18nResourceBundleName() = " + getDFToolKit().getI18nResourceBundleName() );
            }

        // Apply i18n !
        {
            this.autoI18n = AutoI18nCoreFactory.createAutoI18nCore(
                    getDFToolKit().getAutoI18nConfig(),
                    getDFToolKit().getI18nResourceBundleName(),
                    getDFToolKit().getValidLocale()
                    );
            performeI18n( autoI18n );
       }

        setSize( getDFToolKit().getPreferences().getWindowDimension() );

        // Init display
        initFixComponents();
        updateDisplayAccordingState();
        LOGGER.info( "DuplicateFilesFrame() done." );
    }

    @Override // I18nPrepHelperAutoUpdatable
    public void performeI18n(AutoI18nCore autoI18n)
    {
        autoI18n.performeI18n(this,this.getClass());
        autoI18n.performeI18n(getDFToolKit(),getDFToolKit().getClass());

        //updateI18nData();
        getDuplicateFilesMainPanel().performeI18n( autoI18n );
        getRemoveEmptyDirectoriesPanel().performeI18n( autoI18n );
        getDeleteEmptyFilesPanel().performeI18n( autoI18n );
    }

    private void initFixComponents()
    {
        setIconImage( getDFToolKit().getResources().getAppImage() );

        this.iconContinue = getDFToolKit().getResources().getContinueIcon();
        this.iconRestart  = getDFToolKit().getResources().getRestartIcon();

        this.state = STATE_SELECT_DIRS;

        getDuplicateFilesMainPanel().initFixComponents();

        // initDynComponents
        LookAndFeelMenu lafMenu = new LookAndFeelMenu( this );

        lafMenu.addChangeLookAndFeelListener( getDuplicateFilesMainPanel().getJPanel1Config() );
        lafMenu.buildMenu( getJMenuLookAndFeel() );

        // Handle lookAndFeel dynamics modifications.
        UIManager.addPropertyChangeListener(
            new PropertyChangeListener()
            {
                @Override
                public void propertyChange( PropertyChangeEvent e )
                {
                    if( "lookAndFeel".equals( e.getPropertyName() ) ) {
                        LookAndFeel oldLAF = LookAndFeel.class.cast( e.getOldValue() );
                        LookAndFeel newLAF = LookAndFeel.class.cast( e.getNewValue() );

                        if( ! newLAF.equals( oldLAF ) ) {
                             //
                             // Add extra customization here
                             //

                            if( DuplicateFilesFrame.this.removeEmptyDirectories != null ) {
                                SwingUtilities.updateComponentTreeUI(
                                    DuplicateFilesFrame.this.removeEmptyDirectories
                                    );
                                }
                            }
                        }
                }
            });

        super.setSize( getDFToolKit().getPreferences().getWindowDimension() );
    }

    private void updateDisplayAccordingState()
    {
        final Runnable safeRunner = new Runnable()
        {
            @Override
            public void run()
            {
                applyConfigMode();

                LOGGER.debug( "updateDisplayAccordState: " + state );

                getDuplicateFilesMainPanel().selectedPanel( state );

                getDuplicateFilesMainPanel().getJButtonNextStep().setText( txtContinue );
                getDuplicateFilesMainPanel().getJButtonNextStep().setIcon( iconContinue );

                getDuplicateFilesMainPanel().getJButtonRestart().setText( txtRestart );
                getDuplicateFilesMainPanel().getJButtonRestart().setIcon( iconRestart );

                getDuplicateFilesMainPanel().getJButtonCancel().setText( txtCancel );

                if( state == STATE_SELECT_DIRS ) {
                    getDuplicateFilesMainPanel().getJPanel2Searching().clear();
                    getDuplicateFilesMainPanel().getJPanel3Result().clear();

                    getDuplicateFilesMainPanel().getJButtonRestart().setEnabled( false );
                    getDuplicateFilesMainPanel().getJButtonNextStep().setEnabled( true );
                    }
                else if( state == STATE_SEARCH_CONFIG ) {
                    getDuplicateFilesMainPanel().getJButtonRestart().setEnabled( true );
                    getDuplicateFilesMainPanel().getJButtonNextStep().setEnabled( true );
                    }
                else if( state == STATE_SEARCHING ) {
                    getDuplicateFilesMainPanel().getJButtonRestart().setEnabled( false );
                    getDuplicateFilesMainPanel().getJButtonNextStep().setEnabled( false );
                    getJMenuLookAndFeel().setEnabled( false );

                    try {
                        getDuplicateFilesMainPanel().getJPanel2Searching().prepareScan(
                                new MessageDigestFile(
                                        "MD5",
                                        getDFToolKit().getPreferences().getMessageDigestBufferSize()
                                        ),
                                    getDuplicateFilesMainPanel().getJPanel1Config().isIgnoreEmptyFiles()
                                );
                        }
                    catch( NoSuchAlgorithmException ignore ) {
                        LOGGER.error( ignore );
                        }

                    new Thread( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                getDuplicateFilesMainPanel().getJPanel2Searching().doScan(
                                    getDuplicateFilesMainPanel().getJPanel0Select().entriesToScans(),
                                    getDuplicateFilesMainPanel().getJPanel0Select().entriesToIgnore(),
                                    getDuplicateFilesMainPanel().getJPanel1Config().getFileFilterBuilders(),
                                    duplicateFiles
                                    );
                                getJMenuLookAndFeel().setEnabled( true );
                                getDuplicateFilesMainPanel().getJButtonRestart().setEnabled( true );
                            }
                        }, "STATE_SEARCHING").start();
                    }
                else if( state == STATE_RESULTS ) {
                    getDuplicateFilesMainPanel().getJButtonRestart().setEnabled( false );
                    getDuplicateFilesMainPanel().getJButtonNextStep().setEnabled( false );
                    getDuplicateFilesMainPanel().getJButtonNextStep().setText( txtRemove );
                    getDuplicateFilesMainPanel().getJButtonCancel().setText( txtClearSelection );
                    getDuplicateFilesMainPanel().getJButtonCancel().setEnabled( true );

                    SwingUtilities.invokeLater( new Runnable() {
                        @Override
                        public void run()
                        {
                            //getJPanel3Result().initDisplay();
                            getDuplicateFilesMainPanel().getJPanel3Result().clear();
                            getDuplicateFilesMainPanel().getJButtonRestart().setEnabled( true );
                            getDuplicateFilesMainPanel().getJButtonNextStep().setEnabled( true );
                        }} );
                    }
                else if( state == STATE_CONFIRM ) {
                    getDuplicateFilesMainPanel().getJButtonRestart().setEnabled( true );
                    getDuplicateFilesMainPanel().getJButtonRestart().setText( txtBack );

                    if( subState == SUBSTATE_CONFIRM_INIT ) {
                        getDuplicateFilesMainPanel().getJButtonNextStep().setEnabled( true );
                        getDuplicateFilesMainPanel().getJButtonNextStep().setText( txtDeleteNow  );
                        getDuplicateFilesMainPanel().getJPanel4Confirm().populate( duplicateFiles );
                        }
                    else {
                        getDuplicateFilesMainPanel().getJButtonNextStep().setEnabled( false );
                        }
                    }
            }
        };
        SafeSwingUtilities.invokeLater( safeRunner, "updateDisplayAccordingState()" );
    }

    private void jButtonNextStep_ActionPerformed()
    {
        if( getDuplicateFilesMainPanel().getJButtonNextStep().isEnabled() ) {
            LOGGER.info( "Next: " + state );
            getDuplicateFilesMainPanel().getJButtonNextStep().setEnabled( false );

            if( state == STATE_SELECT_DIRS ) {
                if( getDuplicateFilesMainPanel().getJPanel0Select().getEntriesToScanSize() > 0 ) {
                    state = STATE_SEARCH_CONFIG;
                    }
                else {
                    getDFToolKit().beep();
                    LOGGER.info( "No dir selected" );
                    // TODO: Show alert
                    }
                }
            else if( state == STATE_SEARCH_CONFIG ) {
                state = STATE_SEARCHING;
                }
            else if( state == STATE_SEARCHING ) {
                state = STATE_RESULTS;
                }
            else if( state == STATE_RESULTS ) {
                state = STATE_CONFIRM;
                subState = SUBSTATE_CONFIRM_INIT;
                }
            else if( state == STATE_CONFIRM ) {
                if( subState == SUBSTATE_CONFIRM_INIT ) {
                    getDuplicateFilesMainPanel().getJButtonRestart().setEnabled( false );

                    SafeSwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run()
                        {
                            getDuplicateFilesMainPanel().getJPanel4Confirm().doDelete( duplicateFiles );

                            //state = STATE_RESULTS;
                            subState = SUBSTATE_CONFIRM_DONE;
                            updateDisplayAccordingState();
                        }
                        }, "SUBSTATE_CONFIRM_INIT");
                    return;
                    }
                }

            updateDisplayAccordingState();
        }
    }

    @Override
    public ActionListener getActionListener()
    {
        if( this.mainActionListener == null ) {
            this.mainActionListener = new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent event )
                {
                    switch( event.getActionCommand() ) {
                        case ACTIONCMD_EXIT :
                            exitApplication();
                            break;

                        case DuplicateFilesMainPanel.ACTIONCMD_RESTART :
                            if( getDuplicateFilesMainPanel().getJButtonRestart().isEnabled() ) {
                                if( state == STATE_CONFIRM ) {
                                    state = STATE_RESULTS;
                                    }
                                else {
                                    state = STATE_SELECT_DIRS;
                                    getDuplicateFilesMainPanel().getJPanel2Searching().clear();
                                    }

                                updateDisplayAccordingState();
                                }
                            break;

                        case DuplicateFilesMainPanel.ACTIONCMD_NEXT:
                            jButtonNextStep_ActionPerformed();
                            break;

                        case DuplicateFilesMainPanel.ACTIONCMD_CANCEL :
                            if( getDuplicateFilesMainPanel().getJButtonCancel().isEnabled() ) {
                                getDuplicateFilesMainPanel().getJButtonCancel().setEnabled( false );
                                getDuplicateFilesMainPanel().getJPanel2Searching().cancelProcess();
                                getDuplicateFilesMainPanel().getJPanel3Result().clearSelected();
                                }
                            break;

                        case ACTIONCMD_SET_MODE :
                            {
                            AbstractButton sourceConfigMode = AbstractButton.class.cast( event.getSource() );
                            LOGGER.debug( "source: " + sourceConfigMode );

                            getDFToolKit().getPreferences().setConfigMode(
                                ConfigMode.class.cast(
                                    sourceConfigMode.getClientProperty( ConfigMode.class )
                                    )
                                );

                            applyConfigMode();
                            }
                            break;

                        case ACTIONCMD_PREFS :
                            Tools.run( new Runnable() {
                                @Override
                                public void run()
                                {
                                    openPreferences();
                                }}, ACTIONCMD_PREFS );
                            break;

                        case ACTIONCMD_ABOUT :
                            Tools.run( new Runnable() {
                                @Override
                                public void run()
                                {
                                    openAbout();
                                }}, ACTIONCMD_ABOUT );
                            break;

                        default:
                            LOGGER.warn( "Undefined ActionCommand: " + event.getActionCommand() );
                            break;
                    }
                }
            };
        }
        return this.mainActionListener;
    }

    protected void applyConfigMode()
    {
        ConfigMode mode = getDFToolKit().getPreferences().getConfigMode();
        LOGGER.debug( "ConfigMode:" + mode );

        getDuplicateFilesMainPanel().getJPanel1Config().updateDisplay( true );
        getDuplicateFilesMainPanel().getJPanel3Result().updateDisplay();

        boolean advanced = mode != ConfigMode.BEGINNER;

        setEnabledAt( REMOVE_EMPTY_DIRECTORIES_TAB, advanced );
        setEnabledAt( DELETE_EMPTY_FILES_TAB, advanced );
        //TODO: more panel ?
    }

    @Override
    protected void exitApplication()
    {
        // TODO Perform some checks: running : this frame ?
        // TODO Perform some checks: running : empty directories frame ?
        // TODO Save prefs ???

        System.exit( 0 ); // $codepro.audit.disable noExplicitExit
    }

    private void openPreferences()
    {
        LOGGER.info( "openPreferences() : " + getDFToolKit().getPreferences() );

        final Preferences preferences = getDFToolKit().getPreferences();
        PreferencesDialogWB dialog = new PreferencesDialogWB(
                preferences ,
                getSize()
                );
        dialog.performeI18n( autoI18n );
        dialog.setVisible( true );

        JFrames.handleMinimumSize(dialog, preferences.getMinimumPreferenceDimension());

        LOGGER.info( "openPreferences done : " + getDFToolKit().getPreferences() );
    }

    public void openAbout()
    {
        AboutDialog.open( getDFToolKit(), this.autoI18n );

        LOGGER.info( "openAbout done" );
    }

    public void initComponentsJPanelConfirm()
    {
        getDuplicateFilesMainPanel().getJButtonNextStep().setEnabled( false );

        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                LOGGER.info( "initComponentsJPanelConfirm begin" );

                try {
                    Thread.sleep( 1000 );
                    }
                catch( InterruptedException e ) {
                    LOGGER.warn( "Interrupted", e );
                    }

                LOGGER.info( "initComponentsJPanelConfirm start" );
                getDuplicateFilesMainPanel().getJPanel3Result().populate( DuplicateFilesFrame.this.duplicateFiles );
                getDuplicateFilesMainPanel().getJButtonNextStep().setEnabled( true );
                LOGGER.info( "initComponentsJPanelConfirm done" );
            }
        };
        new Thread( task, "initComponentsJPanelConfirm()" ).start();
    }

    public void setJButtonCancelEnabled( boolean b )
    {
        getDuplicateFilesMainPanel().getJButtonCancel().setEnabled( b );
    }

    public boolean isEnabledJButtonCancel()
    {
        return getDuplicateFilesMainPanel().getJButtonCancel().isEnabled();
    }
}
