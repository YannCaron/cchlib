package com.googlecode.cchlib.apps.editresourcesbundle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 *
 * @author Claude CHOISNET
 */
public class LoadDialogWB extends JDialog
{
    private static final long   serialVersionUID        = 1L;
    private ButtonGroup buttonGroup_FileType;
    private JButton jButton_Cancel;
    private JButton jButton_Left;
    private JButton jButton_Ok;
    private JButton jButton_Right;
    private JLabel jLabel_Fake1;
    private JPanel jPanel_Left;
    private JPanel jPanel_Params;
    private JPanel jPanel_Right;
    private JPanel jPanel_TabSelect;
    protected JCheckBox jCheckBox_CUT_LINE_AFTER_HTML_BR;
    protected JCheckBox jCheckBox_CUT_LINE_AFTER_HTML_END_P;
    protected JCheckBox jCheckBox_CUT_LINE_AFTER_NEW_LINE;
    protected JCheckBox jCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P;
    protected JCheckBox jCheckBox_CUT_LINE_BEFORE_HTML_BR;
    protected JCheckBox jCheckBox_FormattedProperties;
    protected JCheckBox jCheckBox_LeftReadOnly;
    protected JCheckBox jCheckBox_Properties;
    protected JCheckBox jCheckBox_RightUseLeftHasDefaults;
    protected JCheckBox jCheckBox_ini;
    protected JPanel jPanel_TabFMTProperties;
    protected JPanel jPanel_TabProperties;
    protected JTabbedPane jTabbedPaneRoot;
    protected JTextField jTextField_Left;
    protected JTextField jTextField_Right;
    protected JCheckBox jCheckBox_ShowLineNumbers;
//    private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
    public LoadDialogWB()
    {
        initComponents();
    }

    public LoadDialogWB( Frame parent )
    {
        super( parent );
        initComponents();
    }

    private void initComponents() {
        setFont(new Font("Dialog", Font.PLAIN, 12));
        setBackground(Color.white);
        setForeground(Color.black);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{100, 0, 100, 0};
        gridBagLayout.rowHeights = new int[]{180, 33, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        getContentPane().setLayout(gridBagLayout);
        GridBagConstraints gbc_jTabbedPaneRoot = new GridBagConstraints();
        gbc_jTabbedPaneRoot.gridwidth = 3;
        gbc_jTabbedPaneRoot.fill = GridBagConstraints.BOTH;
        gbc_jTabbedPaneRoot.insets = new Insets(0, 0, 5, 0);
        gbc_jTabbedPaneRoot.gridx = 0;
        gbc_jTabbedPaneRoot.gridy = 0;
        getContentPane().add(getJTabbedRoot(), gbc_jTabbedPaneRoot);
        GridBagConstraints gbc_jButton_Ok = new GridBagConstraints();
        gbc_jButton_Ok.fill = GridBagConstraints.HORIZONTAL;
        gbc_jButton_Ok.insets = new Insets(0, 0, 0, 5);
        gbc_jButton_Ok.gridx = 0;
        gbc_jButton_Ok.gridy = 1;
        getContentPane().add(getJButton_Ok(), gbc_jButton_Ok);
        GridBagConstraints gbc_jButton_Cancel = new GridBagConstraints();
        gbc_jButton_Cancel.fill = GridBagConstraints.HORIZONTAL;
        gbc_jButton_Cancel.gridx = 2;
        gbc_jButton_Cancel.gridy = 1;
        getContentPane().add(getJButton_Cancel(), gbc_jButton_Cancel);
        initButtonGroup_FileType();
        setSize(500, 270);
    }

    private JCheckBox getJCheckBox_ShowLineNumbers() {
        if (jCheckBox_ShowLineNumbers == null) {
            jCheckBox_ShowLineNumbers = new JCheckBox();
            jCheckBox_ShowLineNumbers.setText("Show line numbers (if possible)");
        }
        return jCheckBox_ShowLineNumbers;
    }

    private JCheckBox getJCheckBox_CUT_LINE_AFTER_HTML_END_P() {
        if (jCheckBox_CUT_LINE_AFTER_HTML_END_P == null) {
            jCheckBox_CUT_LINE_AFTER_HTML_END_P = new JCheckBox();
            jCheckBox_CUT_LINE_AFTER_HTML_END_P.setText("CUT_LINE_AFTER_HTML_END_P");
        }
        return jCheckBox_CUT_LINE_AFTER_HTML_END_P;
    }

    private JCheckBox getJCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P() {
        if (jCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P == null) {
            jCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P = new JCheckBox();
            jCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P.setText("CUT_LINE_BEFORE_HTML_BEGIN_P");
        }
        return jCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P;
    }

    private JCheckBox getJCheckBox_CUT_LINE_BEFORE_HTML_BR() {
        if (jCheckBox_CUT_LINE_BEFORE_HTML_BR == null) {
            jCheckBox_CUT_LINE_BEFORE_HTML_BR = new JCheckBox();
            jCheckBox_CUT_LINE_BEFORE_HTML_BR.setText("CUT_LINE_BEFORE_HTML_BR");
        }
        return jCheckBox_CUT_LINE_BEFORE_HTML_BR;
    }

    private JCheckBox getJCheckBox_CUT_LINE_AFTER_HTML_BR() {
        if (jCheckBox_CUT_LINE_AFTER_HTML_BR == null) {
            jCheckBox_CUT_LINE_AFTER_HTML_BR = new JCheckBox();
            jCheckBox_CUT_LINE_AFTER_HTML_BR.setText("CUT_LINE_AFTER_HTML_BR");
        }
        return jCheckBox_CUT_LINE_AFTER_HTML_BR;
    }

    private JCheckBox getJCheckBox_CUT_LINE_AFTER_NEW_LINE() {
        if (jCheckBox_CUT_LINE_AFTER_NEW_LINE == null) {
            jCheckBox_CUT_LINE_AFTER_NEW_LINE = new JCheckBox();
            jCheckBox_CUT_LINE_AFTER_NEW_LINE.setText("CUT_LINE_AFTER_NEW_LINE");
        }
        return jCheckBox_CUT_LINE_AFTER_NEW_LINE;
    }

    private JPanel getJPanel_TabFMTProperties() {
        if (jPanel_TabFMTProperties == null) {
            jPanel_TabFMTProperties = new JPanel();
            GridBagLayout gbl_jPanel_TabFMTProperties = new GridBagLayout();
            gbl_jPanel_TabFMTProperties.columnWidths = new int[]{0, 0, 0};
            gbl_jPanel_TabFMTProperties.rowHeights = new int[]{23, 23, 23, 23, 23, 0};
            gbl_jPanel_TabFMTProperties.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
            gbl_jPanel_TabFMTProperties.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
            jPanel_TabFMTProperties.setLayout(gbl_jPanel_TabFMTProperties);
            GridBagConstraints gbc_jCheckBox_CUT_LINE_AFTER_NEW_LINE = new GridBagConstraints();
            gbc_jCheckBox_CUT_LINE_AFTER_NEW_LINE.anchor = GridBagConstraints.WEST;
            gbc_jCheckBox_CUT_LINE_AFTER_NEW_LINE.insets = new Insets(0, 0, 5, 5);
            gbc_jCheckBox_CUT_LINE_AFTER_NEW_LINE.gridx = 0;
            gbc_jCheckBox_CUT_LINE_AFTER_NEW_LINE.gridy = 0;
            jPanel_TabFMTProperties.add(getJCheckBox_CUT_LINE_AFTER_NEW_LINE(), gbc_jCheckBox_CUT_LINE_AFTER_NEW_LINE);
            GridBagConstraints gbc_jCheckBox_CUT_LINE_AFTER_HTML_BR = new GridBagConstraints();
            gbc_jCheckBox_CUT_LINE_AFTER_HTML_BR.anchor = GridBagConstraints.WEST;
            gbc_jCheckBox_CUT_LINE_AFTER_HTML_BR.insets = new Insets(0, 0, 5, 5);
            gbc_jCheckBox_CUT_LINE_AFTER_HTML_BR.gridx = 0;
            gbc_jCheckBox_CUT_LINE_AFTER_HTML_BR.gridy = 1;
            jPanel_TabFMTProperties.add(getJCheckBox_CUT_LINE_AFTER_HTML_BR(), gbc_jCheckBox_CUT_LINE_AFTER_HTML_BR);
            GridBagConstraints gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BR = new GridBagConstraints();
            gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BR.anchor = GridBagConstraints.WEST;
            gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BR.insets = new Insets(0, 0, 5, 5);
            gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BR.gridx = 0;
            gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BR.gridy = 2;
            jPanel_TabFMTProperties.add(getJCheckBox_CUT_LINE_BEFORE_HTML_BR(), gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BR);
            GridBagConstraints gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P = new GridBagConstraints();
            gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P.anchor = GridBagConstraints.WEST;
            gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P.insets = new Insets(0, 0, 5, 5);
            gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P.gridx = 0;
            gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P.gridy = 3;
            jPanel_TabFMTProperties.add(getJCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P(), gbc_jCheckBox_CUT_LINE_BEFORE_HTML_BEGIN_P);
            GridBagConstraints gbc_jCheckBox_CUT_LINE_AFTER_HTML_END_P = new GridBagConstraints();
            gbc_jCheckBox_CUT_LINE_AFTER_HTML_END_P.insets = new Insets(0, 0, 0, 5);
            gbc_jCheckBox_CUT_LINE_AFTER_HTML_END_P.anchor = GridBagConstraints.WEST;
            gbc_jCheckBox_CUT_LINE_AFTER_HTML_END_P.gridx = 0;
            gbc_jCheckBox_CUT_LINE_AFTER_HTML_END_P.gridy = 4;
            jPanel_TabFMTProperties.add(getJCheckBox_CUT_LINE_AFTER_HTML_END_P(), gbc_jCheckBox_CUT_LINE_AFTER_HTML_END_P);
        }
        return jPanel_TabFMTProperties;
    }

    private JCheckBox getJCheckBox_RightUseLeftHasDefaults() {
        if (jCheckBox_RightUseLeftHasDefaults == null) {
            jCheckBox_RightUseLeftHasDefaults = new JCheckBox();
            jCheckBox_RightUseLeftHasDefaults.setText("Right file use Left file has defaults");
        }
        return jCheckBox_RightUseLeftHasDefaults;
    }

    private JLabel getJLabel_Fake1() {
        if (jLabel_Fake1 == null) {
            jLabel_Fake1 = new JLabel();
            //jLabel_Fake1.setText("jLabel1");
        }
        return jLabel_Fake1;
    }

    private JCheckBox getJCheckBox_LeftReadOnly() {
        if (jCheckBox_LeftReadOnly == null) {
            jCheckBox_LeftReadOnly = new JCheckBox();
            jCheckBox_LeftReadOnly.setText("Left file readonly");
        }
        return jCheckBox_LeftReadOnly;
    }

    private void initButtonGroup_FileType() {
        buttonGroup_FileType = new ButtonGroup();
        buttonGroup_FileType.add(getJCheckBox_Properties());
        buttonGroup_FileType.add(getJCheckBox_FormattedProperties());
        buttonGroup_FileType.add(getJCheckBox_ini());
    }

    private JPanel getJPanel_Params() {
        if (jPanel_Params == null) {
            jPanel_Params = new JPanel();
            jPanel_Params.setLayout(new GridLayout(3, 2));
            jPanel_Params.add(getJLabel_Fake1());
        }
        return jPanel_Params;
    }

    private JCheckBox getJCheckBox_Properties() {
        if (jCheckBox_Properties == null) {
            jCheckBox_Properties = new JCheckBox();
            jCheckBox_Properties.setText("Properties");
        }
        return jCheckBox_Properties;
    }

    private JCheckBox getJCheckBox_ini() {
        if (jCheckBox_ini == null) {
            jCheckBox_ini = new JCheckBox();
            jCheckBox_ini.setText("*.ini files");
            jCheckBox_ini.setEnabled(false);
        }
        return jCheckBox_ini;
    }

    private JCheckBox getJCheckBox_FormattedProperties() {
        if (jCheckBox_FormattedProperties == null) {
            jCheckBox_FormattedProperties = new JCheckBox();
            jCheckBox_FormattedProperties.setText("Formatted Properties");
        }
        return jCheckBox_FormattedProperties;
    }

    private JButton getJButton_Ok() {
        if (jButton_Ok == null) {
            jButton_Ok = new JButton();
            jButton_Ok.setText("OK");
            jButton_Ok.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent event) {
                    jButton_OkMouseMousePressed(event);
                }
            });
        }
        return jButton_Ok;
    }

    private JButton getJButton_Cancel() {
        if (jButton_Cancel == null) {
            jButton_Cancel = new JButton();
            jButton_Cancel.setText("Cancel");
            jButton_Cancel.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent event) {
                    jButton_CancelMouseMousePressed(event);
                }
            });
        }
        return jButton_Cancel;
    }

    private JTabbedPane getJTabbedRoot() {
        if (jTabbedPaneRoot == null) {
            jTabbedPaneRoot = new JTabbedPane();
            jTabbedPaneRoot.addTab("Files", getJPanel_TabSelect());
            jTabbedPaneRoot.addTab("Properties", getJPanel_TabProperties());
            jTabbedPaneRoot.addTab("Formatted Properties", getJPanel_TabFMTProperties());
        }
        return jTabbedPaneRoot;
    }

    private JPanel getJPanel_TabProperties() {
        if (jPanel_TabProperties == null) {
            jPanel_TabProperties = new JPanel();
            GridBagLayout gbl_jPanel_TabProperties = new GridBagLayout();
            gbl_jPanel_TabProperties.columnWidths = new int[]{0, 0, 0};
            gbl_jPanel_TabProperties.rowHeights = new int[]{23, 0};
            gbl_jPanel_TabProperties.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
            gbl_jPanel_TabProperties.rowWeights = new double[]{0.0, Double.MIN_VALUE};
            jPanel_TabProperties.setLayout(gbl_jPanel_TabProperties);
            GridBagConstraints gbc_jCheckBox_RightUseLeftHasDefaults = new GridBagConstraints();
            gbc_jCheckBox_RightUseLeftHasDefaults.insets = new Insets(0, 0, 0, 5);
            gbc_jCheckBox_RightUseLeftHasDefaults.anchor = GridBagConstraints.NORTHWEST;
            gbc_jCheckBox_RightUseLeftHasDefaults.gridx = 0;
            gbc_jCheckBox_RightUseLeftHasDefaults.gridy = 0;
            jPanel_TabProperties.add(getJCheckBox_RightUseLeftHasDefaults(), gbc_jCheckBox_RightUseLeftHasDefaults);
        }
        return jPanel_TabProperties;
    }

    private JPanel getJPanel_TabSelect() {
        if (jPanel_TabSelect == null) {
            jPanel_TabSelect = new JPanel();
            GridBagLayout gbl_jPanel_TabSelect = new GridBagLayout();
            gbl_jPanel_TabSelect.columnWidths = new int[]{0, 0, 0, 0};
            gbl_jPanel_TabSelect.rowHeights = new int[]{47, 47, 0, 0, 0, 0};
            gbl_jPanel_TabSelect.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
            gbl_jPanel_TabSelect.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
            jPanel_TabSelect.setLayout(gbl_jPanel_TabSelect);
            GridBagConstraints gbc_jPanel_Left = new GridBagConstraints();
            gbc_jPanel_Left.fill = GridBagConstraints.HORIZONTAL;
            gbc_jPanel_Left.gridwidth = 3;
            gbc_jPanel_Left.insets = new Insets(0, 0, 5, 0);
            gbc_jPanel_Left.gridx = 0;
            gbc_jPanel_Left.gridy = 0;
            jPanel_TabSelect.add(getJPanel_Left(), gbc_jPanel_Left);
            GridBagConstraints gbc_jCheckBox_Properties = new GridBagConstraints();
            gbc_jCheckBox_Properties.fill = GridBagConstraints.HORIZONTAL;
            gbc_jCheckBox_Properties.insets = new Insets(0, 0, 5, 5);
            gbc_jCheckBox_Properties.gridx = 0;
            gbc_jCheckBox_Properties.gridy = 2;
            jPanel_TabSelect.add(getJCheckBox_Properties(), gbc_jCheckBox_Properties);
            GridBagConstraints gbc_jPanel_Right = new GridBagConstraints();
            gbc_jPanel_Right.gridwidth = 3;
            gbc_jPanel_Right.fill = GridBagConstraints.HORIZONTAL;
            gbc_jPanel_Right.insets = new Insets(0, 0, 5, 5);
            gbc_jPanel_Right.gridx = 0;
            gbc_jPanel_Right.gridy = 1;
            jPanel_TabSelect.add(getJPanel_Right(), gbc_jPanel_Right);
            GridBagConstraints gbc_jPanel_Params = new GridBagConstraints();
            gbc_jPanel_Params.fill = GridBagConstraints.HORIZONTAL;
            gbc_jPanel_Params.insets = new Insets(0, 0, 5, 5);
            gbc_jPanel_Params.gridx = 0;
            gbc_jPanel_Params.gridy = 2;
            jPanel_TabSelect.add(getJPanel_Params(), gbc_jPanel_Params);
            GridBagConstraints gbc_jCheckBox_LeftReadOnly = new GridBagConstraints();
            gbc_jCheckBox_LeftReadOnly.anchor = GridBagConstraints.WEST;
            gbc_jCheckBox_LeftReadOnly.insets = new Insets(0, 0, 5, 5);
            gbc_jCheckBox_LeftReadOnly.gridx = 1;
            gbc_jCheckBox_LeftReadOnly.gridy = 2;
            jPanel_TabSelect.add(getJCheckBox_LeftReadOnly(), gbc_jCheckBox_LeftReadOnly);
            GridBagConstraints gbc_jCheckBox_FormattedProperties = new GridBagConstraints();
            gbc_jCheckBox_FormattedProperties.fill = GridBagConstraints.HORIZONTAL;
            gbc_jCheckBox_FormattedProperties.insets = new Insets(0, 0, 5, 5);
            gbc_jCheckBox_FormattedProperties.gridx = 0;
            gbc_jCheckBox_FormattedProperties.gridy = 3;
            jPanel_TabSelect.add(getJCheckBox_FormattedProperties(), gbc_jCheckBox_FormattedProperties);
            GridBagConstraints gbc_jCheckBox_ShowLineNumbers = new GridBagConstraints();
            gbc_jCheckBox_ShowLineNumbers.anchor = GridBagConstraints.WEST;
            gbc_jCheckBox_ShowLineNumbers.insets = new Insets(0, 0, 5, 5);
            gbc_jCheckBox_ShowLineNumbers.gridx = 1;
            gbc_jCheckBox_ShowLineNumbers.gridy = 3;
            jPanel_TabSelect.add(getJCheckBox_ShowLineNumbers(), gbc_jCheckBox_ShowLineNumbers);
            GridBagConstraints gbc_jCheckBox_ini = new GridBagConstraints();
            gbc_jCheckBox_ini.fill = GridBagConstraints.HORIZONTAL;
            gbc_jCheckBox_ini.insets = new Insets(0, 0, 0, 5);
            gbc_jCheckBox_ini.gridx = 0;
            gbc_jCheckBox_ini.gridy = 4;
            jPanel_TabSelect.add(getJCheckBox_ini(), gbc_jCheckBox_ini);
        }
        return jPanel_TabSelect;
    }

    private JPanel getJPanel_Left() {
        if (jPanel_Left == null) {
            jPanel_Left = new JPanel();
            jPanel_Left.setBorder(BorderFactory.createTitledBorder(null, "Left", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD,
                    12), new Color(51, 51, 51)));
            jPanel_Left.setLayout(new BorderLayout());
            jPanel_Left.add(getJButton_Left(), BorderLayout.EAST);
            jPanel_Left.add(getJTextField_Left(), BorderLayout.CENTER);
        }
        return jPanel_Left;
    }

    private JPanel getJPanel_Right() {
        if (jPanel_Right == null) {
            jPanel_Right = new JPanel();
            jPanel_Right.setBorder(BorderFactory.createTitledBorder(null, "Right", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, new Font("Dialog",
                    Font.BOLD, 12), new Color(51, 51, 51)));
            jPanel_Right.setLayout(new BorderLayout());
            jPanel_Right.add(getJButton_Right(), BorderLayout.EAST);
            jPanel_Right.add(getJTextField_Right(), BorderLayout.CENTER);
        }
        return jPanel_Right;
    }

    private JButton getJButton_Left() {
        if (jButton_Left == null) {
            jButton_Left = new JButton();
            jButton_Left.setText("select");
            jButton_Left.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent event) {
                    jButton_LeftMouseMousePressed(event);
                }
            });
        }
        return jButton_Left;
    }

    private JButton getJButton_Right() {
        if (jButton_Right == null) {
            jButton_Right = new JButton();
            jButton_Right.setText("select");
            jButton_Right.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent event) {
                    jButton_RightMouseMousePressed(event);
                }
            });
        }
        return jButton_Right;
    }

    private JTextField getJTextField_Left() {
        if (jTextField_Left == null) {
            jTextField_Left = new JTextField();
        }
        return jTextField_Left;
    }

    private JTextField getJTextField_Right() {
        if (jTextField_Right == null) {
            jTextField_Right = new JTextField();
          }
        return jTextField_Right;
    }

//    @SuppressWarnings("unused")
//    private static void installLnF()
//    {
//        try {
//            String lnfClassname = PREFERRED_LOOK_AND_FEEL;
//            if( lnfClassname == null )
//                lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
//            UIManager.setLookAndFeel( lnfClassname );
//        }
//        catch( Exception e ) {
//            System.err.println( "Cannot install " + PREFERRED_LOOK_AND_FEEL
//                    + " on this platform:" + e.getMessage() );
//        }
//    }

    /** Test only */
    public static void main( String[] args )
    {
//        installLnF();
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run()
            {
                LoadDialogWB dialog = new LoadDialogWB();
                dialog.setDefaultCloseOperation( LoadDialogWB.DISPOSE_ON_CLOSE );
                dialog.setTitle( "Load Dialog (Debug)" );
                dialog.setLocationRelativeTo( null );
                dialog.getContentPane().setPreferredSize( dialog.getSize() );
                dialog.pack();
                dialog.setVisible( true );
            }
        } );
    }

    protected void jButton_LeftMouseMousePressed(MouseEvent event)
    {
        throw new UnsupportedOperationException();
    }

    protected void jButton_RightMouseMousePressed(MouseEvent event)
    {
        throw new UnsupportedOperationException();
    }

    protected void jButton_CancelMouseMousePressed(MouseEvent event)
    {
        throw new UnsupportedOperationException();
    }

    protected void jButton_OkMouseMousePressed(MouseEvent event)
    {
        throw new UnsupportedOperationException();
    }
}
