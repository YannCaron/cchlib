package com.googlecode.cchlib.apps.duplicatefiles.swing.gui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.TooManyListenersException;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.apps.duplicatefiles.swing.AppToolKit;
import com.googlecode.cchlib.apps.duplicatefiles.swing.gui.panels.confirm.JPanelConfirm;
import com.googlecode.cchlib.apps.duplicatefiles.swing.gui.panels.filtersconfig.JPanelConfig;
import com.googlecode.cchlib.apps.duplicatefiles.swing.gui.panels.result.JPanelResult;
import com.googlecode.cchlib.apps.duplicatefiles.swing.gui.panels.search.JPanelSearching;
import com.googlecode.cchlib.apps.duplicatefiles.swing.gui.panels.search.JPanelSearchingParallel;
import com.googlecode.cchlib.apps.duplicatefiles.swing.gui.panels.search.JPanelSearchingSingleThread;
import com.googlecode.cchlib.apps.duplicatefiles.swing.gui.panels.select.JPanelSelectFoldersOrFiles;
import com.googlecode.cchlib.apps.duplicatefiles.swing.services.AppToolKitService;
import com.googlecode.cchlib.i18n.AutoI18n;
import com.googlecode.cchlib.i18n.annotation.I18nIgnore;
import com.googlecode.cchlib.i18n.annotation.I18nName;
import com.googlecode.cchlib.i18n.core.I18nAutoUpdatable;

/**
 * Main frame layout for DuplicateFilesManager
 */
@I18nName("DuplicateFilesMainPanel")
public class DuplicateFilesMainPanel
    extends JPanel
        implements I18nAutoUpdatable
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger( DuplicateFilesMainPanel.class );

    public static final String ACTIONCMD_RESTART = "ACTIONCMD_RESTART";
    public static final String ACTIONCMD_NEXT    = "ACTIONCMD_NEXT";
    public static final String ACTIONCMD_CANCEL  = "ACTIONCMD_CANCEL";

    @I18nIgnore // Prevent recursive analysis
    private DuplicateFilesFrameWB       mainActionListenerSupport;

    private CardLayout                  jPanelMainCardLayout;
    private JButton                     jButtonCancel;
    private JButton                     jButtonNextStep;
    private JButton                     jButtonRestart;
    @I18nIgnore // Prevent recursive analysis
    private JPanel                      jPanelMain;
    @I18nIgnore // Prevent recursive analysis
    private JPanelSelectFoldersOrFiles  jPanel0Select;
    @I18nIgnore // Prevent recursive analysis
    private JPanelConfig                jPanel1Config;
    @I18nIgnore // Prevent recursive analysis
    private JPanelSearching             jPanel2Searching;
    @I18nIgnore // Prevent recursive analysis
    private JPanelResult                jPanel3Result;
    @I18nIgnore // Prevent recursive analysis
    private JPanelConfirm               jPanel4Confirm;

    /**
     * Fake JPannel for builder
     */
    public DuplicateFilesMainPanel()
    {
        //Empty
    }

    @SuppressWarnings({
        "squid:RedundantThrowsDeclarationCheck",
        })
    public DuplicateFilesMainPanel(
        final DuplicateFilesFrameWB mainActionListenerSupport
        ) throws HeadlessException, TooManyListenersException
    {
        this.mainActionListenerSupport = mainActionListenerSupport;

        final GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{100, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        this.setLayout(gbl_contentPane);

        {
            this.jPanelMain = new JPanel();
            final GridBagConstraints gbc_jPanelMain = new GridBagConstraints();
            gbc_jPanelMain.anchor = GridBagConstraints.WEST;
            gbc_jPanelMain.gridwidth = 4;
            gbc_jPanelMain.gridheight = 1;
            gbc_jPanelMain.insets = new Insets(0, 0, 5, 0);
            gbc_jPanelMain.fill = GridBagConstraints.BOTH;
            gbc_jPanelMain.gridx = 0;
            gbc_jPanelMain.gridy = 0;
            this.add(this.jPanelMain, gbc_jPanelMain);
            this.jPanelMainCardLayout = new CardLayout(0, 0);
            this.jPanelMain.setLayout( this.jPanelMainCardLayout );

            this.jPanel0Select = createJPanel0Select();
            this.jPanelMain.add( this.jPanel0Select, DuplicateFilesState.STATE_SELECT_DIRS.name() );

            this.jPanel1Config = createJPanel1Config();
            this.jPanelMain.add( this.jPanel1Config, DuplicateFilesState.STATE_SEARCH_CONFIG.name() );

            this.jPanel2Searching = createJPanel2Searching();
            this.jPanelMain.add( this.jPanel2Searching, DuplicateFilesState.STATE_SEARCHING.name() );

            this.jPanel3Result = createJPanel3Result();
            this.jPanelMain.add( this.jPanel3Result, DuplicateFilesState.STATE_RESULTS.name() );

            this.jPanel4Confirm = createJPanel4Confirm();
            this.jPanelMain.add( this.jPanel4Confirm, DuplicateFilesState.STATE_CONFIRM.name() );
        }

        this.jButtonRestart = new JButton("Restart");
        this.jButtonRestart.setActionCommand( ACTIONCMD_RESTART );
        this.jButtonRestart.addActionListener( getActionListener() );
        final GridBagConstraints gbc_jButtonRestart = new GridBagConstraints();
        gbc_jButtonRestart.anchor = GridBagConstraints.WEST;
        gbc_jButtonRestart.insets = new Insets(0, 0, 0, 5);
        gbc_jButtonRestart.gridx = 0;
        gbc_jButtonRestart.gridy = 1;
        this.add(this.jButtonRestart, gbc_jButtonRestart);

        this.jButtonCancel = new JButton("Cancel");
        this.jButtonCancel.setActionCommand( ACTIONCMD_CANCEL );
        this.jButtonCancel.addActionListener( getActionListener() );
        final GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
        gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
        gbc_jButtonCancel.insets = new Insets(0, 0, 0, 5);
        gbc_jButtonCancel.gridx = 1;
        gbc_jButtonCancel.gridy = 1;
        this.add(this.jButtonCancel, gbc_jButtonCancel);

        this.jButtonNextStep = new JButton( "Next" );
        this.jButtonNextStep.setActionCommand( ACTIONCMD_NEXT );
        this.jButtonNextStep.addActionListener( getActionListener() );
        final GridBagConstraints gbc_jButtonNextStep = new GridBagConstraints();
        gbc_jButtonNextStep.anchor = GridBagConstraints.EAST;
        gbc_jButtonNextStep.gridx = 3;
        gbc_jButtonNextStep.gridy = 1;
        this.add(getJButtonNextStep(), gbc_jButtonNextStep);
    }

    private ActionListener getActionListener()
    {
        return this.mainActionListenerSupport.getActionListener();
    }

    protected JButton getJButtonNextStep()
    {
        return this.jButtonNextStep;
    }

    protected JButton getJButtonCancel()
    {
        return this.jButtonCancel;
    }

    public JPanelSelectFoldersOrFiles getJPanel0Select()
    {
        return this.jPanel0Select;
    }

    protected JPanelConfig getJPanel1Config()
    {
        return this.jPanel1Config;
    }

    protected JPanelSearching getJPanel2Searching()
    {
        return this.jPanel2Searching;
    }

    public JPanelResult getJPanel3Result()
    {
        return this.jPanel3Result;
    }

    protected JPanelConfirm getJPanel4Confirm()
    {
        return this.jPanel4Confirm;
    }

    protected AbstractButton getJButtonRestart()
    {
        return this.jButtonRestart;
    }

    protected void selectedPanel( final DuplicateFilesState state )
    {
        this.jPanelMainCardLayout.show(
                this.jPanelMain,
                state.name()
                );
    }

    public final AppToolKit getAppToolKit()
    {
        return AppToolKitService.getInstance().getAppToolKit();
    }

    /**
     * @wbp.factory
     *
     * @return the {@link JPanelSelectFoldersOrFiles} panel
     * @throws HeadlessException if any
     * @throws TooManyListenersException if any
     */
    @SuppressWarnings({
        "squid:RedundantThrowsDeclarationCheck",
        })
    public JPanelSelectFoldersOrFiles createJPanel0Select()
        throws HeadlessException, TooManyListenersException
    {
        return new JPanelSelectFoldersOrFiles();
    }

    /**
     * @wbp.factory
     *
     * @return the {@link JPanelConfig} panel
     */
    public JPanelConfig createJPanel1Config()
    {
        return new JPanelConfig();
    }

    /**
     * @wbp.factory
     *
     * @return the {@link JPanelSearching} panel
     */
    public JPanelSearching createJPanel2Searching()
    {
        if( LOGGER.isDebugEnabled() ) {
            LOGGER.debug( "NumberOfThreads : " + getAppToolKit().getPreferences().getNumberOfThreads() );
        }

        if( getAppToolKit().getPreferences().getNumberOfThreads() < 2 ) {
            return new JPanelSearchingSingleThread();
        } else {
            return new JPanelSearchingParallel();
        }
    }

    /**
     * @wbp.factory
     *
     * @return the {@link JPanelResult} panel
     */
    public JPanelResult createJPanel3Result()
    {
        return new JPanelResult();
    }

    /**
     * @wbp.factory
     *
     * @return the {@link JPanelConfirm} panel
     */
    public JPanelConfirm createJPanel4Confirm()
    {
        return new JPanelConfirm();
    }

    @Override // I18nAutoUpdatable
    public void performeI18n( final AutoI18n autoI18n )
    {
        autoI18n.performeI18n( this, this.getClass() );
        autoI18n.performeI18n( getJPanel0Select(), getJPanel0Select().getClass() );

        getJPanel1Config().performeI18n( autoI18n );
        getJPanel2Searching().performeI18n( autoI18n );
        getJPanel3Result().performeI18n( autoI18n );

        autoI18n.performeI18n( getJPanel4Confirm(), getJPanel4Confirm().getClass( ));
    }

    public void initFixComponents()
    {
        getJPanel0Select().initFixComponents();
        getJPanel2Searching().initFixComponents();
    }
}
