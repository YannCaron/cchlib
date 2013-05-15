package com.googlecode.cchlib.apps.duplicatefiles.gui.panels.result;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.apps.duplicatefiles.KeyFileState;
import com.googlecode.cchlib.apps.duplicatefiles.KeyFiles;
import com.googlecode.cchlib.apps.duplicatefiles.Resources;
import com.googlecode.cchlib.apps.duplicatefiles.prefs.DividersLocation;
import com.googlecode.cchlib.i18n.I18nIgnore;
import com.googlecode.cchlib.i18n.I18nToolTipText;
import com.googlecode.cchlib.swing.XComboBoxPattern;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *
 */
public abstract class JPanelResultWB extends JPanel
{
    private static final long serialVersionUID = 1L;
    private final static Logger logger = Logger.getLogger( JPanelResultWB.class );
    private JTextField jTextFieldFileInfo;
    private JToggleButton jToggleButtonSelectByRegEx;
    private XComboBoxPattern xComboBoxPatternRegEx;
    private JCheckBox jCheckBoxKeepOne;
    private JButton jButtonRegExDelete;
    private JButton jButtonRegExKeep;
    private JPanelResultListModel listModelDuplicatesFiles;
    private JSplitPane jSplitPaneResultMain;
    private JSplitPane jSplitPaneResultRight;
    private JList<KeyFileState> jListKeptIntact;
    private JList<KeyFileState> jListWillBeDeleted;
    private JList<KeyFiles> jListDuplicatesFiles;
    @I18nIgnore @I18nToolTipText private JButton refreshButton;
    @I18nIgnore @I18nToolTipText private JButton jButtonPrevSet;
    @I18nIgnore @I18nToolTipText private JButton jButtonNextSet;

    /**
     *
     */
    public JPanelResultWB( Resources resources )
    {
        Color errorColor = Color.RED;

        setSize(488, 240);

        listModelDuplicatesFiles = new JPanelResultListModel();

        {
            GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.columnWidths = new int[]{32, 32, 32, 0, 0, 0, 0, 0};
            gridBagLayout.rowHeights = new int[]{25, 0, 0, 0};
            gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
            gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
            setLayout(gridBagLayout);
        }
        {
            GridBagConstraints gbc_jButtonPrevSet = new GridBagConstraints();
            gbc_jButtonPrevSet.fill = GridBagConstraints.HORIZONTAL;
            gbc_jButtonPrevSet.insets = new Insets(0, 0, 5, 5);
            gbc_jButtonPrevSet.gridx = 0;
            gbc_jButtonPrevSet.gridy = 0;

            //JButton jButtonPrevSet = new JButton( "<<" );
            this.jButtonPrevSet = new JButton( resources.getPrevIcon() );
            this.jButtonPrevSet.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onPrevSet();
                }
            });
            add(this.jButtonPrevSet, gbc_jButtonPrevSet);
        }
        {
            GridBagConstraints gbc_jButtonNextSet = new GridBagConstraints();
            gbc_jButtonNextSet.fill = GridBagConstraints.HORIZONTAL;
            gbc_jButtonNextSet.insets = new Insets(0, 0, 5, 5);
            gbc_jButtonNextSet.gridx = 2;
            gbc_jButtonNextSet.gridy = 0;

            //JButton jButtonNextSet = new JButton( ">>" );
            this.jButtonNextSet = new JButton( resources.getNextIcon() );
            this.jButtonNextSet.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onNextSet();
                }
            });
            add(this.jButtonNextSet, gbc_jButtonNextSet);
        }
        {
            //this.refreshButton = new JButton("R");
            this.refreshButton = new JButton( resources.getRefreshIcon() );
            this.refreshButton.setToolTipText( "Refresh file list (remove deleted entries from an other process)" );
            this.refreshButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onRefresh();
                }
            });
            GridBagConstraints gbc_refreshButton = new GridBagConstraints();
            gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
            gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
            gbc_refreshButton.gridx = 1;
            gbc_refreshButton.gridy = 0;
            add(this.refreshButton, gbc_refreshButton);
        }
        {
            GridBagConstraints gbc_jTextFieldFileInfo = new GridBagConstraints();
            gbc_jTextFieldFileInfo.gridwidth = 4;
            gbc_jTextFieldFileInfo.fill = GridBagConstraints.HORIZONTAL;
            gbc_jTextFieldFileInfo.insets = new Insets(0, 0, 5, 0);
            gbc_jTextFieldFileInfo.gridx = 3;
            gbc_jTextFieldFileInfo.gridy = 0;

            jTextFieldFileInfo = new JTextField();
            jTextFieldFileInfo.setEditable( false );
            jTextFieldFileInfo.setHorizontalAlignment( JTextField.CENTER );
            add(jTextFieldFileInfo, gbc_jTextFieldFileInfo);
        }
        {
            GridBagConstraints gbc_jSplitPaneResultMain = new GridBagConstraints();
            gbc_jSplitPaneResultMain.gridwidth = 7;
            gbc_jSplitPaneResultMain.fill = GridBagConstraints.BOTH;
            gbc_jSplitPaneResultMain.insets = new Insets(0, 0, 5, 0);
            gbc_jSplitPaneResultMain.gridx = 0;
            gbc_jSplitPaneResultMain.gridy = 1;
            add(getJSplitPaneResultMain(), gbc_jSplitPaneResultMain);
        }
        {
            GridBagConstraints gbc_jToggleButtonSelectByRegEx = new GridBagConstraints();
            gbc_jToggleButtonSelectByRegEx.fill = GridBagConstraints.HORIZONTAL;
            gbc_jToggleButtonSelectByRegEx.gridwidth = 3;
            gbc_jToggleButtonSelectByRegEx.insets = new Insets(0, 0, 0, 5);
            gbc_jToggleButtonSelectByRegEx.gridx = 0;
            gbc_jToggleButtonSelectByRegEx.gridy = 2;

            jToggleButtonSelectByRegEx = new JToggleButton( "Select by RegEx" );
            jToggleButtonSelectByRegEx.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent event) {
                    jToggleButtonSelectByRegExChangeStateChanged(event);
                    }
                });
            add( jToggleButtonSelectByRegEx, gbc_jToggleButtonSelectByRegEx );
        }
        {
            xComboBoxPatternRegEx = new XComboBoxPattern();
            xComboBoxPatternRegEx.setErrorBackGroundColor( errorColor );
            xComboBoxPatternRegEx.setModel(
                new DefaultComboBoxModel<String>(new String[] { ".*\\.jpg", ".*\\.gif", ".*\\.tmp" })
                );

            GridBagConstraints gbc_xComboBoxPatternRegEx = new GridBagConstraints();
            gbc_xComboBoxPatternRegEx.fill = GridBagConstraints.HORIZONTAL;
            gbc_xComboBoxPatternRegEx.insets = new Insets(0, 0, 0, 5);
            gbc_xComboBoxPatternRegEx.gridx = 3;
            gbc_xComboBoxPatternRegEx.gridy = 2;

            add( xComboBoxPatternRegEx, gbc_xComboBoxPatternRegEx );
        }
        {
            GridBagConstraints gbc_jCheckBoxKeepOne = new GridBagConstraints();
            gbc_jCheckBoxKeepOne.fill = GridBagConstraints.HORIZONTAL;
            gbc_jCheckBoxKeepOne.insets = new Insets(0, 0, 0, 5);
            gbc_jCheckBoxKeepOne.gridx = 4;
            gbc_jCheckBoxKeepOne.gridy = 2;

            jCheckBoxKeepOne = new JCheckBox( "Keep one" );
            jCheckBoxKeepOne.setSelected( true );
            add( jCheckBoxKeepOne, gbc_jCheckBoxKeepOne );
        }
        {
            GridBagConstraints gbc_jButtonRegExDelete = new GridBagConstraints();
            gbc_jButtonRegExDelete.fill = GridBagConstraints.HORIZONTAL;
            gbc_jButtonRegExDelete.insets = new Insets(0, 0, 0, 5);
            gbc_jButtonRegExDelete.gridx = 5;
            gbc_jButtonRegExDelete.gridy = 2;

            jButtonRegExDelete = new JButton( "Delete" );
            jButtonRegExDelete.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    jButtonRegExDeleteMouseMousePressed();
                    }
                });
            add(jButtonRegExDelete, gbc_jButtonRegExDelete);
        }
        {
            GridBagConstraints gbc_jButtonRegExKeep = new GridBagConstraints();
            gbc_jButtonRegExKeep.fill = GridBagConstraints.HORIZONTAL;
            gbc_jButtonRegExKeep.gridx = 6;
            gbc_jButtonRegExKeep.gridy = 2;

            jButtonRegExKeep = new JButton( "Keep" );
            jButtonRegExKeep.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    jButtonRegExKeepMouseMousePressed();
                    }
                });
            add( jButtonRegExKeep, gbc_jButtonRegExKeep );
        }
        {
            //listModelDuplicatesFiles = new JPanelResultListModel();
            jListDuplicatesFiles.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
            jListDuplicatesFiles.setModel( listModelDuplicatesFiles );
            jListDuplicatesFiles.addListSelectionListener(
                new ListSelectionListener() {
                    @Override
                    public void valueChanged( ListSelectionEvent event )
                    {
                        logger.info( "valueChanged: " + event );

                        if( ! event.getValueIsAdjusting() ) {
                            int i = jListDuplicatesFiles.getSelectedIndex();

                            updateDisplayKeptDelete( i );
                            }
//                        if( i >= 0 ) {
//                            //KeyFiles kf = (KeyFiles)listModelDuplicatesFiles.get( i );
//                            KeyFiles kf = listModelDuplicatesFiles.getElementAt( i );
//                            String   k = kf.getKey();
    //
//                            updateDisplayKeptDelete( k );
//                            }
                    }
                } );
        }
    }

    protected JTextField getJTextFieldFileInfo()
    {
        return jTextFieldFileInfo;
    }
    protected JToggleButton getJToggleButtonSelectByRegEx()
    {
        return jToggleButtonSelectByRegEx;
    }
    protected XComboBoxPattern getXComboBoxPatternRegEx()
    {
        return xComboBoxPatternRegEx;
    }
    protected JCheckBox getJCheckBoxKeepOne()
    {
        return jCheckBoxKeepOne;
    }
    protected JButton getJButtonRegExDelete()
    {
        return jButtonRegExDelete;
    }
    protected JButton getJButtonRegExKeep()
    {
        return jButtonRegExKeep;
    }
    protected JList<KeyFileState> getJListKeptIntact()
    {
        return jListKeptIntact;
    }
    protected JList<KeyFileState> getJListWillBeDeleted()
    {
        return jListWillBeDeleted;
    }

    private JSplitPane getJSplitPaneResultMain()
    {
        if (jSplitPaneResultMain == null) {
            jSplitPaneResultMain = new JSplitPane();

            JScrollPane jScrollPaneDuplicatesFiles = new JScrollPane();
            jScrollPaneDuplicatesFiles.setViewportView(getJListDuplicatesFiles());

            jSplitPaneResultMain.setLeftComponent( jScrollPaneDuplicatesFiles );
            jSplitPaneResultMain.setRightComponent(getJSplitPaneResultRight());
            }
        return jSplitPaneResultMain;
    }

    private JSplitPane getJSplitPaneResultRight()
    {
        if (jSplitPaneResultRight == null) {
            jSplitPaneResultRight = new JSplitPane();
            jSplitPaneResultRight.setDividerLocation( 0.5 ); // Proportional
            jSplitPaneResultRight.setOrientation(JSplitPane.VERTICAL_SPLIT);

            JScrollPane jScrollPaneKeptIntact = new JScrollPane();
            final KeyFileStateListModel jListKeptIntactListModel = listModelDuplicatesFiles.getKeptIntactListModel();
            jListKeptIntact = new JList<KeyFileState>();
            this.jListKeptIntact.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            jListKeptIntact.setModel( jListKeptIntactListModel );
            jListKeptIntact.setCellRenderer( listModelDuplicatesFiles.getKeptIntactListCellRenderer() );
            //jListKeptIntact.setModel( listModelKeptIntact );
            //jListKeptIntact.setModel( listModelDuplicatesFiles.getKeptIntactListModel() );
            jListKeptIntact.addMouseListener( new MouseAdapter() {
                @Override
                public void mouseClicked( MouseEvent e )
                {
                    if( e.getClickCount() > 0 ) {
                        jListWillBeDeleted.clearSelection();
                        int index = jListKeptIntact.locationToIndex( e.getPoint() );

                        if( index >= 0 ) {
                            //KeyFileState kf = (KeyFileState)listModelKeptIntact.get( index );
                            KeyFileState kf = jListKeptIntactListModel.getElementAt( index );

                            displayFileInfo( kf );
                            }
                        }
                    if( e.getClickCount() == 2 ) { // Double-click
                        int index = jListKeptIntact.locationToIndex( e.getPoint() );

                        if( index >= 0 ) {
                            //KeyFileState kf = (KeyFileState)listModelKeptIntact.remove( index );
                            KeyFileState kf = jListKeptIntactListModel.remove( index );

                            onDeleteThisFile( kf, true );
                            }
                        }
                }
            } );
            jScrollPaneKeptIntact.setViewportView( jListKeptIntact );
            jSplitPaneResultRight.setTopComponent( jScrollPaneKeptIntact );

            JScrollPane jScrollPaneWillBeDeleted = new JScrollPane();
            jListWillBeDeleted = new JList<KeyFileState>();
            this.jListWillBeDeleted.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            final KeyFileStateListModel jListWillBeDeletedListModel = listModelDuplicatesFiles.getWillBeDeletedListModel();
            jListWillBeDeleted.setModel( jListWillBeDeletedListModel );
            jListWillBeDeleted.setCellRenderer( listModelDuplicatesFiles.getKeptIntactListCellRenderer() );
            //jListWillBeDeleted.setModel( listModelWillBeDeleted );
            //jListKeptIntact.setModel( listModelDuplicatesFiles.getWillBeDeletedListModel() );
            jListWillBeDeleted.addMouseListener( new MouseAdapter() {
                @Override
                public void mouseClicked( MouseEvent e )
                {
                    if( e.getClickCount() > 0 ) {
                        jListKeptIntact.clearSelection();
                        int index = jListWillBeDeleted.locationToIndex( e.getPoint() );

                        if( index >= 0 ) {
                            //KeyFileState kf = (KeyFileState)listModelWillBeDeleted.get( index );
                            KeyFileState kf = jListWillBeDeletedListModel.getElementAt( index );

                            displayFileInfo( kf );
                            }
                        }
                    if( e.getClickCount() == 2 ) { // Double-click
                        int index = jListWillBeDeleted.locationToIndex( e.getPoint() );

                        if( index >= 0 ) {
                            //KeyFileState kf = (KeyFileState)listModelWillBeDeleted.remove( index );
                            KeyFileState kf = jListWillBeDeletedListModel.remove( index );

                            onKeepThisFile( kf, true );
                            }
                        }
                }
            });
            jScrollPaneWillBeDeleted.setViewportView( jListWillBeDeleted );
            jSplitPaneResultRight.setBottomComponent( jScrollPaneWillBeDeleted );
            }
        return jSplitPaneResultRight;
    }

    protected JList<KeyFiles> getJListDuplicatesFiles()
    {
        if (jListDuplicatesFiles == null) {
            jListDuplicatesFiles = new JList<KeyFiles>();
            jListDuplicatesFiles.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if( e.getClickCount() == 2 ) {
                        // TODO: move current file to other list !
                        }
                    }
                });
            }
        return jListDuplicatesFiles;
    }

    protected JPanelResultListModel getListModelDuplicatesFiles()
    {
        return listModelDuplicatesFiles;
    }


    protected void setDividersLocation(
        final DividersLocation dividersLocation
        )
    {
        Integer mainDividerLocation = dividersLocation.getMainDividerLocation();
        if( mainDividerLocation != null ) {
            this.jSplitPaneResultMain.setDividerLocation( mainDividerLocation.intValue() );
            }
        else {
            jSplitPaneResultMain.setDividerLocation( 0.10 ); // Proportional
            }

        Integer rightDividerLocation = dividersLocation.getRightDividerLocation();
        if( rightDividerLocation != null ) {
            this.jSplitPaneResultRight.setDividerLocation( rightDividerLocation.intValue() );
            }
        else {
            jSplitPaneResultRight.setDividerLocation( 0.50 ); // Proportional
            }
    }

    protected abstract void jButtonRegExKeepMouseMousePressed();
    protected abstract void jButtonRegExDeleteMouseMousePressed();
    protected abstract void jToggleButtonSelectByRegExChangeStateChanged(ChangeEvent event);
    protected abstract void onNextSet();
    protected abstract void onPrevSet();
    protected abstract void onRefresh();

    protected abstract void updateDisplayKeptDelete(int index);
    protected abstract void onKeepThisFile(KeyFileState kf, boolean updateDisplay);
    protected abstract void onDeleteThisFile(KeyFileState kf, boolean updateDisplay) ;
    protected abstract void displayFileInfo(KeyFileState kf);
}
