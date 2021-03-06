package com.googlecode.cchlib.apps.duplicatefiles.swing.gui.panels.result;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.apps.duplicatefiles.swing.AppToolKit;
import com.googlecode.cchlib.apps.duplicatefiles.swing.KeyFileState;
import com.googlecode.cchlib.apps.duplicatefiles.swing.prefs.PreferencesControler;
import com.googlecode.cchlib.apps.duplicatefiles.swing.services.AppToolKitService;
import com.googlecode.cchlib.i18n.AutoI18n;
import com.googlecode.cchlib.i18n.annotation.I18nIgnore;
import com.googlecode.cchlib.i18n.annotation.I18nName;
import com.googlecode.cchlib.i18n.annotation.I18nString;
import com.googlecode.cchlib.i18n.core.I18nAutoUpdatable;
import com.googlecode.cchlib.lang.StringHelper;
import com.googlecode.cchlib.swing.menu.AbstractJPopupMenuBuilder;
import com.googlecode.cchlib.swing.menu.AbstractJPopupMenuBuilder.Attributs;

@I18nName("duplicatefiles.JPanelResult")
@SuppressWarnings({
    "squid:MaximumInheritanceDepth" // because swing
    })
public final class JPanelResult extends JPanelResultWB implements I18nAutoUpdatable
{
    private static final long               serialVersionUID = 2L;
    private static final Logger             LOGGER           = Logger.getLogger( JPanelResult.class );

    @I18nIgnore  // Prevent recursive analysis
    private final AppToolKit                dFToolKit;

    @I18nIgnore  // Prevent recursive analysis
    private final DuplicatesContextualMenu  duplicatesContextualMenu;
    @I18nIgnore  // Prevent recursive analysis
    private FilesContextualMenu             keptIntactContextualMenu;
    @I18nIgnore  // Prevent recursive analysis
    private FilesContextualMenu             willBeDeletedContextualMenu;

    // TODO: @I18nString private String txtCanExecuteFirstLetter ?
    @I18nString private String              txtHiddenFirstLetter;
    @I18nString private String              txtCanWriteFirstLetter;
    @I18nString private String              txtCanReadFirstLetter;

    public JPanelResult()
    {
        super();

        beSurNonFinal();

        this.dFToolKit                = AppToolKitService.getInstance().getAppToolKit();
        this.duplicatesContextualMenu = new DuplicatesContextualMenu( this );

        createPopupMenus();

        SwingUtilities.invokeLater( this::setDividersLocation );
    }

    private void setDividersLocation()
    {
        setDividersLocation(
            this.dFToolKit.getPreferences().getJPaneResultDividerLocations()
            );
    }

    private void beSurNonFinal()
    {
        this.txtHiddenFirstLetter = "H";
        // TODO: txtCanExecuteFirstLetter = "E"; ?
        this.txtCanWriteFirstLetter = "W";
        this.txtCanReadFirstLetter = "R";
    }

    public void populate( final Map<String, Set<KeyFileState>> duplicateFiles )
    {
        final PreferencesControler preferences = this.dFToolKit.getPreferences();

        final SortMode sortMode = preferences.getDefaultSortMode();
        final SelectFirstMode selectFirstMode = preferences.getDefaultSelectFirstMode();

        getListModelDuplicatesFiles().setDuplicateFiles( duplicateFiles );
        getListModelDuplicatesFiles().setSortMode( sortMode );
        getListModelDuplicatesFiles().setSelectFirstMode( selectFirstMode );
        getListModelDuplicatesFiles().updateCache();
        updateDisplay();
    }

    public void clear()
    {
        getListModelDuplicatesFiles().clearKeepDelete();
        getListModelDuplicatesFiles().updateCache();
    }

    @Override
    public void updateDisplay()
    {
        getSelectorsJPanel().updateDisplay();

        final int index = getJListDuplicatesFiles().getSelectedIndex();

        updateDisplayKeptDelete( index );
    }

    @Override
    protected void onPrevSet()
    {
        final int size = getJListDuplicatesFiles().getModel().getSize();

        if( size > 0 ) {
            int i = getJListDuplicatesFiles().getSelectedIndex() - 1;

            if( i < 0 ) {
                i = size - 1;
            }
            getJListDuplicatesFiles().setSelectedIndex( i );
        }
    }

    @Override
    protected void onNextSet()
    {
        final int size = getJListDuplicatesFiles().getModel().getSize();

        if( size > 0 ) {
            int i = getJListDuplicatesFiles().getSelectedIndex() + 1;

            if( i >= size ) {
                i = 0;
            }
            getJListDuplicatesFiles().setSelectedIndex( i );
        }
    }

    @Override
    protected void displayFileInfo( final KeyFileState kf )
    {
        if( kf == null ) {
            getJTextFieldFileInfo().setText( StringHelper.EMPTY );
        } else {
            final File f = kf.getCurrentFile();
            final Locale locale = this.dFToolKit.getValidLocale();

            final String date = DateFormat.getDateTimeInstance( DateFormat.FULL, DateFormat.SHORT, locale ).format( new Date( f.lastModified() ) );

            getJTextFieldFileInfo().setText(
                    String.format( "%s - %d [%s%s%s] %s (%s)", f.getName(), Long.valueOf( f.length() ),
                            // f.canExecute()?txtCanExecuteFirstLetter:"-",
                            f.canRead() ? this.txtCanReadFirstLetter : "-", f.canWrite() ? this.txtCanWriteFirstLetter : "-",
                            f.isHidden() ? this.txtHiddenFirstLetter : "-", date, kf.getKey() ) );
        }
    }

    @Override
    protected void updateDisplayKeptDelete( final int index )
    {
        LOGGER.info( "updateDisplayKeptDelete: index = " + index );

        if( index < 0 ) {
            return;
        }

        updateDisplayKeptDelete( getListModelDuplicatesFiles().getElementAt( index ).getKey() );
    }

    void updateDisplayKeptDelete( final String key )
    {
        if( LOGGER.isTraceEnabled() ) {
            LOGGER.trace( "updateDisplayKeptDelete: " + key );
        }

        displayFileInfo( null );
        final Set<KeyFileState> s = getListModelDuplicatesFiles().getStateSet( key );
        if( s != null ) {
            getListModelDuplicatesFiles().setKeepDelete( key, s );
        } else {
            LOGGER.error( "updateDisplayKeptDelete() * Missing key:" + key );
        }
    }

    @Override
    protected void onDeleteThisFile( final KeyFileState kf, final boolean updateDisplay )
    {
        if( kf != null ) {
            kf.setSelectedToDelete( true );
            updateDisplayKeptDelete( kf.getKey() );
        }
    }

    @Override
    protected void onKeepThisFile( final KeyFileState kf, final boolean updateDisplay )
    {
        if( kf != null ) {
            kf.setSelectedToDelete( false );
            updateDisplayKeptDelete( kf.getKey() );
        }
    }

    private void createPopupMenus()
    {
        // Require to keep reference for I18n
        this.keptIntactContextualMenu    = createKeyFileStatePopupMenu( getJListKeptIntact() );
        this.willBeDeletedContextualMenu = createKeyFileStatePopupMenu( getJListWillBeDeleted() );

        this.duplicatesContextualMenu.setPopupMenu();
    }

    private FilesContextualMenu createKeyFileStatePopupMenu(
        final JList<KeyFileState> jList
        )
    {
        final FilesContextualMenu menu = new FilesContextualMenu(
                this,
                jList,
                AbstractJPopupMenuBuilder.Attributs.MUST_BE_SELECTED,
                new Attributs[] {}
                );

        menu.addMenu();

        return menu;
    }

    public void clearSelected()
    {
        disableAllWidgets();

        new Thread( ( ) -> {
            try {
                getListModelDuplicatesFiles().clearSelected();
            }
            catch( final Exception e ) {
                LOGGER.error( "clearSelected()", e );
            }
            finally {
                enableAllWidgets();

                this.dFToolKit.setEnabledJButtonCancel( true );
            }
        }, "clearSelected()" ).start();
    }

    @Override
    protected void onRefresh()
    {
        LOGGER.info( "onRefresh() - start" );

        disableAllWidgets();

        new Thread( ( ) -> {
            try {
                getListModelDuplicatesFiles().refreshList();
            }
            catch( final Exception e ) {
                LOGGER.error( "onRefresh()", e );
            }
            finally {
                enableAllWidgets();

                LOGGER.info( "onRefresh() - done" );
            }
        }, "onRefresh()" ).start();
    }

    @Override // I18nAutoUpdatable
    public void performeI18n( final AutoI18n autoI18n )
    {
        autoI18n.performeI18n( this, getClass() );
        autoI18n.performeI18n( this.duplicatesContextualMenu, DuplicatesContextualMenu.class );
        autoI18n.performeI18n( this.keptIntactContextualMenu, FilesContextualMenu.class );
        autoI18n.performeI18n( this.willBeDeletedContextualMenu, FilesContextualMenu.class );

        this.duplicatesContextualMenu.setPopupMenu();

        super.getSelectorsJPanel().performeI18n( autoI18n );
    }

    public void lockGUI( final boolean lock )
    {
        if( lock ) {
            disableAllWidgets();
        } else {
            enableAllWidgets();
        }
    }
}
