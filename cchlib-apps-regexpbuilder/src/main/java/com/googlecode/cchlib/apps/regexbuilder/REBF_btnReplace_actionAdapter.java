package com.googlecode.cchlib.apps.regexbuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class REBF_btnReplace_actionAdapter
    implements ActionListener
{
    RegExpBuilderWB adaptee;

    REBF_btnReplace_actionAdapter( final RegExpBuilderWB adaptee )
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e )
    {
        adaptee.btnReplace_actionPerformed( e );
    }
}
