package com.googlecode.cchlib.apps.regexbuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class REBF_btnAdvancedReplace_actionAdapter
    implements ActionListener
{
	RegExpBuilderWB adaptee;

    REBF_btnAdvancedReplace_actionAdapter( final RegExpBuilderWB adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        adaptee.btnAdvancedReplace_actionPerformed( e );
    }
}
