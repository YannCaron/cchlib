package com.googlecode.cchlib.apps.regexbuilder.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class InputJPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private JTextField regExpJTextField;
    private JLabel regExpJLabel;
    private JLabel javaCodeJLabel;
    private JTextField javaCodeJTextField;

    /**
     * Create the panel.
     */
    public InputJPanel()
    {
        setBorder(new EmptyBorder(2, 2, 2, 2));
        final GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        {
            this.regExpJLabel = new JLabel("Reg Exp");
            final GridBagConstraints gbc_regExpJLabel = new GridBagConstraints();
            gbc_regExpJLabel.insets = new Insets(0, 0, 5, 5);
            gbc_regExpJLabel.anchor = GridBagConstraints.EAST;
            gbc_regExpJLabel.gridx = 0;
            gbc_regExpJLabel.gridy = 0;
            add(this.regExpJLabel, gbc_regExpJLabel);
        }
        {
            this.regExpJTextField = new JTextField();
            this.regExpJTextField.addActionListener(new ActionListener() {
                int todo;
                @Override
                public void actionPerformed(final ActionEvent event) {
                    // TODO
                    this.todo = 42;
                }
            });
            final GridBagConstraints gbc_regExpJTextField = new GridBagConstraints();
            gbc_regExpJTextField.insets = new Insets(0, 0, 5, 0);
            gbc_regExpJTextField.fill = GridBagConstraints.HORIZONTAL;
            gbc_regExpJTextField.gridx = 1;
            gbc_regExpJTextField.gridy = 0;
            add(this.regExpJTextField, gbc_regExpJTextField);
            this.regExpJTextField.setColumns(30);
        }
        {
            this.javaCodeJLabel = new JLabel("Java Code");
            final GridBagConstraints gbc_javaCodeJLabel = new GridBagConstraints();
            gbc_javaCodeJLabel.anchor = GridBagConstraints.EAST;
            gbc_javaCodeJLabel.insets = new Insets(0, 0, 0, 5);
            gbc_javaCodeJLabel.gridx = 0;
            gbc_javaCodeJLabel.gridy = 1;
            add(this.javaCodeJLabel, gbc_javaCodeJLabel);
        }
        {
            this.javaCodeJTextField = new JTextField();
            this.javaCodeJTextField.setEditable(false);
            final GridBagConstraints gbc_javaCodeJTextField = new GridBagConstraints();
            gbc_javaCodeJTextField.fill = GridBagConstraints.HORIZONTAL;
            gbc_javaCodeJTextField.gridx = 1;
            gbc_javaCodeJTextField.gridy = 1;
            add(this.javaCodeJTextField, gbc_javaCodeJTextField);
            this.javaCodeJTextField.setColumns(30);
        }
    }

}
