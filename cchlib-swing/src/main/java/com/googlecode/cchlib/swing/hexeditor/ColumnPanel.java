package com.googlecode.cchlib.swing.hexeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 *
 *
 */
class ColumnPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private HexEditorModel model;

    /**
     *
     * @param hexEditorModel
     */
    public ColumnPanel(
        HexEditorModel hexEditorModel
        )
    {
        this.model = hexEditorModel;
        this.setLayout(new BorderLayout(1,1));
    }

    @Override
    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }

    @Override
    public Dimension getMinimumSize()
    {
        Dimension d=new Dimension();
        //FontMetrics fn=getFontMetrics(this.model.getCustomFont());
        FontMetrics fn = model.getFontMetrics();
        int h=fn.getHeight();
        int nl=1;
        d.setSize(
            ((fn.stringWidth(" ")+1)*+((16*3)-1))+(this.model.getBorderWidth()*2)+1,
            h*nl+(this.model.getBorderWidth()*2)+1
            );
        return d;
    }

    @Override
    public void paint(Graphics g)
    {
        Dimension d=getMinimumSize();
        g.setColor(Color.white);
        g.fillRect(0,0,d.width,d.height);
        g.setColor(Color.black);
        g.setFont( this.model.getFont() );

        for( int n=0; n<16; n++ ) {
            if( n==(this.model.getCursorPos()%16) ) {
                this.model.drawTable(g,n*3,0,2);
                }
            String s="00"+Integer.toHexString(n);
            s=s.substring(s.length()-2);
            this.model.printString(g,s,n*3,0);
            }
    }
}
