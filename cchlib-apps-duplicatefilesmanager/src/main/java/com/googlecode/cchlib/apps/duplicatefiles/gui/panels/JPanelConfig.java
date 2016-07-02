package com.googlecode.cchlib.apps.duplicatefiles.gui.panels;

import java.awt.event.ActionListener;
import java.nio.file.PathMatcher;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.apps.duplicatefiles.ConfigMode;
import com.googlecode.cchlib.apps.duplicatefiles.FileFilterBuilder;
import com.googlecode.cchlib.apps.duplicatefiles.FileFilterBuilderConfigurator;
import com.googlecode.cchlib.apps.duplicatefiles.FileFilterBuilders;
import com.googlecode.cchlib.apps.duplicatefiles.gui.panels.config.JPanelConfigFilter;
import com.googlecode.cchlib.apps.duplicatefiles.prefs.PreferencesControler;
import com.googlecode.cchlib.apps.duplicatefiles.tools.FileFilterBuilderImpl;
import com.googlecode.cchlib.i18n.annotation.I18nName;
import com.googlecode.cchlib.i18n.core.AutoI18nCore;
import com.googlecode.cchlib.swing.SafeSwingUtilities;
import com.googlecode.cchlib.swing.menu.LookAndFeelListener;

@I18nName("duplicatefiles.JPanelConfig")
public class JPanelConfig
    extends JPanelConfigI18n
        implements LookAndFeelListener
{
    private final class FileFilterBuilderConfiguratorImpl implements FileFilterBuilderConfigurator {
        @Override
        public FileFilterBuilder createIncludeFilesFileFilterBuilder()
        {
            return createIncludeFileFilter(
                getJComboBoxFilesFilters().getSelectedIndex() == FilterType.INCLUDE_FILTER.ordinal(),
                JPanelConfig.this.jPanelIncFilesFilter
                );
        }

        @Override
        public FileFilterBuilder createExcludeFilesFileFilterBuilder()
        {
            return createIncludeFileFilter(
                getJComboBoxFilesFilters().getSelectedIndex() == FilterType.EXCLUDE_FILTER.ordinal(),
                JPanelConfig.this.jPanelExcFilesFilter
                );
        }

        @Override
        public FileFilterBuilder createIncludeDirectoriesFileFilterBuilder()
        {
            return createIncludeFileFilter(
                getJComboBoxDirsFilters().getSelectedIndex() == FilterType.INCLUDE_FILTER.ordinal(),
                JPanelConfig.this.jPanelIncDirsFilter
                );
        }

        @Override
        public FileFilterBuilder createExcludeDirectoriesFileFilterBuilder()
        {
            return createExcludeFileFilter(
                getJComboBoxDirsFilters().getSelectedIndex() == FilterType.EXCLUDE_FILTER.ordinal(),
                JPanelConfig.this.jPanelExcDirsFilter
                );
        }
    }

    private static final long serialVersionUID = 2L;
    private static final Logger LOGGER = Logger.getLogger( JPanelConfig.class );

    //private ActionListener actionListener;
    private ConfigMode mode;

    private PathMatcher tryToUseThis; // TODO - http://docs.oracle.com/javase/tutorial/essential/io/find.html
    private Scanner scanner;

    private JPanelConfigFilter jPanelIncFilesFilter;
    private JPanelConfigFilter jPanelExcDirsFilter;
    private JPanelConfigFilter jPanelExcFilesFilter;
    private JPanelConfigFilter jPanelIncDirsFilter;

    public JPanelConfig()
    {
        super();
    }

    @Override//LookAndFeelListener
    public void setLookAndFeel( final String lookAndFeelName )
    {
        if( this.jPanelIncFilesFilter != null ) {
            SwingUtilities.updateComponentTreeUI( this.jPanelIncFilesFilter );
            }
        if( this.jPanelExcFilesFilter != null ) {
            SwingUtilities.updateComponentTreeUI( this.jPanelExcFilesFilter );
            }
        if( this.jPanelIncDirsFilter != null ) {
            SwingUtilities.updateComponentTreeUI( this.jPanelIncDirsFilter );
            }
        if( this.jPanelExcDirsFilter != null ) {
            SwingUtilities.updateComponentTreeUI( this.jPanelExcDirsFilter );
            }
    }

    @Override
    protected ActionListener getActionListener()
    {
//        if( this.actionListener == null ) {
//            this.actionListener = event -> SafeSwingUtilities.invokeLater( () -> updateDisplay( true ), "updateDisplay()" );
//            }
//        return this.actionListener;
        return event -> SafeSwingUtilities.invokeLater( () -> updateDisplay( true ), "updateDisplay()" );
    }

    /**
     * Must be call to have a
     * @param autoI18n
     */
    public void performeI18n(final AutoI18nCore autoI18n)
    {
        autoI18n.performeI18n(this,this.getClass());

        final Properties           prop  = getAppToolKit().getResources().getJPanelConfigProperties(); // $codepro.audit.disable declareAsInterface
        final PreferencesControler prefs = getAppToolKit().getPreferences();

        this.jPanelIncFilesFilter = new JPanelConfigFilter(
                getjPanelIncFilesFilterTitle(),
                getjPanelIncFilesFilterRegExp(),
                prop,
                "filetype",
                getActionListener()
                );

        for( final String exp : prefs.getIncFilesFilterPatternRegExpList() ) {
            this.jPanelIncFilesFilter.addPatternRegExp( exp );
            }
//        prefs.getIncFilesFilterPatternRegExpList().stream().forEach( exp -> this.jPanelIncFilesFilter.addPatternRegExp( exp ) );

        this.jPanelExcFilesFilter = new JPanelConfigFilter(
                getjPanelExcFilesFilterTitle(),
                getjPanelExcFilesFilterRegExp(),
                prop,
                "filetype",
                getActionListener()
                );
        this.jPanelIncDirsFilter = new JPanelConfigFilter( // No values
                getjPanelIncDirsFilterTitle(),
                getjPanelIncDirsFilterRegExp()
                );
        this.jPanelExcDirsFilter = new JPanelConfigFilter(
                getjPanelExcDirsFilterTitle(),
                getjPanelExcDirsFilterRegExp(),
                prop,
                "dirtype",
                getActionListener()
                );

        updateDisplay( false );
    }

    public void updateDisplay(
            final boolean doRepaint
            )
    {
        final ConfigMode prevMode   = this.mode;
        this.mode                   = getAppToolKit().getPreferences().getConfigMode();

        if( LOGGER.isDebugEnabled() ) {
            LOGGER.debug( "updateDisplayMode()"
                    + prevMode
                    + " -> "
                    + this.mode
                    );
        }

        final boolean isModeChanged = !this.mode.equals( prevMode );

        if( isModeChanged ) {
            switch( this.mode ) {
                case BEGINNER:
                    updateDisplayBeginner();
                    break;
                case ADVANCED:
                    updateDisplayAdvanced();
                    break;
                case EXPERT:
                    updateDisplayExpert();
                    break;
                default:
                    throw new IllegalStateException( "Unexpected mode: " + this.mode );
            }
        }

        updateDisplayCommon( doRepaint );
    }

    private void updateDisplayCommon( final boolean doRepaint )
    {
        final JPanel jp = getJPanelFilters();

        if( this.jPanelIncFilesFilter != null ) {
            jp.remove( this.jPanelIncFilesFilter );
            }
        if( this.jPanelExcFilesFilter != null ) {
            jp.remove( this.jPanelExcFilesFilter );
            }
        if( this.jPanelIncDirsFilter != null ) {
            jp.remove( this.jPanelIncDirsFilter );
            }
        if( this.jPanelExcDirsFilter != null ) {
            jp.remove( this.jPanelExcDirsFilter );
            }

        //jsp.revalidate();
        //this.repaint();//repaint a JFrame jframe in this case

        if( getJComboBoxFilesFilters().getSelectedIndex() == FilterType.INCLUDE_FILTER.ordinal() ) {
            jp.add( this.jPanelIncFilesFilter );
            LOGGER.debug( "Display jPanelIncFilesFilter" );
            }
        else if( getJComboBoxFilesFilters().getSelectedIndex() == FilterType.EXCLUDE_FILTER.ordinal() ) {
            jp.add( this.jPanelExcFilesFilter );
            LOGGER.debug( "Display jPanelExcFilesFilter" );
            }
        else { // FILES_FILTER_DISABLED
            LOGGER.debug(
                "No Display getJComboBoxFilesFilters()="
                    + getJComboBoxFilesFilters().getSelectedIndex()
                );
            }

        if( getJComboBoxDirsFilters().getSelectedIndex() == FilterType.EXCLUDE_FILTER.ordinal() ) {
            jp.add( this.jPanelIncDirsFilter );
            LOGGER.debug( "Display jPanelIncDirsFilter" );
            }
        else if( getJComboBoxDirsFilters().getSelectedIndex() == FilterType.INCLUDE_FILTER.ordinal() ) {
            jp.add( this.jPanelExcDirsFilter );
            LOGGER.debug( "Display jPanelExcDirsFilter" );
            }
        else { // DIRS_FILTER_DISABLED
            LOGGER.debug( "No Display" );
            }

        if( doRepaint ) {
            jp.revalidate();

            // repaint a JFrame jframe in this case
            getAppToolKit().getMainFrame().repaint();

            LOGGER.debug( "repaint MainWindow" );
            }
    }

    private void updateDisplayBeginner()
    {
        getJComboBoxFilesFilters().setModel(
                new DefaultComboBoxModel<>(
                    new String[] {
                        getTxtDisableFilesFilters()
                        }
                    )
                );
        getJComboBoxFilesFilters().setEnabled( false );
        getJComboBoxDirsFilters().setModel(
                new DefaultComboBoxModel<>(
                    new String[] {
                        getTxtDisableDirsFilters()
                        }
                    )
                );
        getJComboBoxDirsFilters().setEnabled( false );
    }

    private void updateDisplayAdvanced()
    {
        getJComboBoxFilesFilters().setModel(
                new DefaultComboBoxModel<>(
                    new String[] {
                        getTxtDisableFilesFilters(),
                        getTxtIncludeFilesFilters(),
                        getTxtExcludeFilesFilters()
                        }
                    )
                );
        getJComboBoxFilesFilters().setEnabled( true );
        getJComboBoxDirsFilters().setModel(
                new DefaultComboBoxModel<>(
                    new String[] {
                        getTxtDisableDirsFilters()
                        }
                    )
                );
        getJComboBoxDirsFilters().setEnabled( false );
    }

    private void updateDisplayExpert()
    {
        getJComboBoxFilesFilters().setModel(
                new DefaultComboBoxModel<>(
                    new String[] {
                        getTxtDisableFilesFilters(),
                        getTxtIncludeFilesFilters(),
                        getTxtExcludeFilesFilters()
                        }
                    )
                );
        getJComboBoxFilesFilters().setEnabled( true );
        getJComboBoxDirsFilters().setModel(
                new DefaultComboBoxModel<>(
                    new String[] {
                        getTxtDisableDirsFilters(),
                        getTxtExcludeDirsFilters(),
                        getTxtIncludeDirsFilters()
                        }
                    )
                );
        getJComboBoxDirsFilters().setEnabled( true );
    }

    /**
     * Returns true if Empty Files should be skipped
     * @return true if Empty Files should be skipped
     */
    public boolean isIgnoreEmptyFiles()
    {
        return getjCheckBoxIgnoreEmptyFiles().isSelected();
    }

    private static final void addExtIfItMakeSense(
        final Collection<String> c,
        final FileTypeCheckBox   ft
        )
    {
        if( ft.getJCheckBox().isSelected() ) {
            for(final String s:ft.getData().split( "," )) {
                if(s.length()>0) {
                    c.add( "." + s.toLowerCase() );
                }
            }
        }
    }

    private static final void addNameIfItMakeSense( //
        final Collection<String>    c, //
        final FileTypeCheckBox      ft //
        )
    {
        if( ft.getJCheckBox().isSelected() ) {
            for(final String s:ft.getData().split( "," )) {
                if( s.length() > 0 ) {
                    c.add( s.toLowerCase() );
                }
            }
        }
    }

    private static FileFilterBuilderImpl createExcludeFileFilter( //
            final boolean            userExcludeFileFilter,
            final JPanelConfigFilter jPanelConfigFilter
            )
    {
        final Set<String>   namesList = new HashSet<>();
        Pattern             pattern   = null;

        if( userExcludeFileFilter ) {
            for( final FileTypeCheckBox ft : jPanelConfigFilter ) {
                addNameIfItMakeSense(namesList,ft);
                }

            final boolean useRegExp = jPanelConfigFilter.getJCheckBoxRegExp().isSelected();

            if( useRegExp ) {
                try {
                    pattern = jPanelConfigFilter.getSelectedPattern();
                    }
                catch( final Exception ignore ){
                    LOGGER.error( ignore );
                    }
                }
            }

        return new FileFilterBuilderImpl( namesList, pattern );
    }

    private static FileFilterBuilderImpl createIncludeFileFilter( //
        final boolean            userIncludeFileFilter,
        final JPanelConfigFilter jPanelConfigFilter
        )
    {
        final Set<String>   extsList = new HashSet<>();
        Pattern             pattern  = null;

        if( userIncludeFileFilter ) {
            for( final FileTypeCheckBox ft : jPanelConfigFilter ) {
                addExtIfItMakeSense( extsList, ft );
                }

            final boolean useRegExp = jPanelConfigFilter.getJCheckBoxRegExp().isSelected();

            if( useRegExp ) {
                try {
                    pattern = jPanelConfigFilter.getSelectedPattern();
                    }
                catch( final Exception ignore ) {
                    LOGGER.error( ignore );
                    }
                }
            }

        return new FileFilterBuilderImpl( extsList, pattern );
    }

    public FileFilterBuilders getFileFilterBuilders()
    {
        final FilterType ffType  = FilterType.buildFromOrdinal( getJComboBoxFilesFilters().getSelectedIndex() );
        final FilterType efType  = FilterType.buildFromOrdinal( getJComboBoxDirsFilters().getSelectedIndex() );

        LOGGER.info( "FFtype = " + ffType );
        LOGGER.info( "EFtype = " + efType);

        // Special cases
        final boolean ignoreEmptyFiles      = getjCheckBoxIgnoreEmptyFiles().isSelected();
        final boolean ignoreHiddedFiles     = getjCheckBoxFFIgnoreHidden().isSelected();
        final boolean ignoreReadOnlyFiles   = getjCheckBoxIgnoreReadOnlyFiles().isSelected();
        final boolean ignoreHiddedDirs      = getjCheckBoxFDIgnoreHidden().isSelected();

        if( LOGGER.isDebugEnabled() ) {
            LOGGER.debug( "ignoreEmptyFiles    = " + ignoreEmptyFiles);
            LOGGER.debug( "ignoreHiddedFiles   = " + ignoreHiddedFiles);
            LOGGER.debug( "ignoreReadOnlyFiles = " + ignoreReadOnlyFiles);
            LOGGER.debug( "ignoreHiddedDirs    = " + ignoreHiddedDirs);
        }

        final FileFilterBuilderConfigurator configurator = this.toFileFilterBuilderConfigurator();

        if( LOGGER.isDebugEnabled() ) {
            LOGGER.debug( "X createExcludeDirectoriesFileFilterBuilder() = " + configurator.createExcludeDirectoriesFileFilterBuilder() );
            LOGGER.debug( "X createIncludeDirectoriesFileFilterBuilder() = " + configurator.createIncludeDirectoriesFileFilterBuilder() );
            LOGGER.debug( "X createExcludeFilesFileFilterBuilder() = " + configurator.createExcludeFilesFileFilterBuilder() );
            LOGGER.debug( "X createIncludeFilesFileFilterBuilder() = " + configurator.createIncludeFilesFileFilterBuilder() );
        }

        final ConfigFileFilterBuilders configFileFilterBuilders = new ConfigFileFilterBuilders( configurator, ignoreHiddedFiles, efType, ignoreHiddedDirs, ffType, ignoreEmptyFiles, ignoreReadOnlyFiles );

        LOGGER.info( "configFileFilterBuilders = " + configFileFilterBuilders);

        return configFileFilterBuilders;
    }

    private FileFilterBuilderConfigurator toFileFilterBuilderConfigurator()
    {
        return new FileFilterBuilderConfiguratorImpl();
    }
}

