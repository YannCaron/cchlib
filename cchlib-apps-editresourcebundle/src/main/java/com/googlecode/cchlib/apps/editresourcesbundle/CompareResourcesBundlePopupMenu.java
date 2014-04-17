package com.googlecode.cchlib.apps.editresourcesbundle;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.i18n.annotation.I18nString;
import com.googlecode.cchlib.i18n.core.AutoI18nCore;
import com.googlecode.cchlib.swing.table.JPopupMenuForJTable;

/**
 *
 */
class CompareResourcesBundlePopupMenu
    extends JPopupMenuForJTable
{
    private static final long serialVersionUID = 1L;
    //
    // http://www.velocityreviews.com/forums/t146956-popupmenu-for-a-cell-in-a-jtable.html
    //
    private static final transient Logger LOGGER = Logger.getLogger( CompareResourcesBundlePopupMenu.class );
    private CompareResourcesBundleFrame frame;
    private final AbstractTableModel  abstractTableModel;
    /** @serial */
    private final CompareResourcesBundleTableModel.Colunms colunms;

    @I18nString
    private final String txtHTMLPreview = "HTML Preview";
    @I18nString
    private final String txtEditLines = "Edit lines";
    @I18nString
    private final String txtCopy = "Copy";
    @I18nString
    private final String txtPaste = "Paste";

    /**
     * @param jTable
     * @param abstractTableModel
     * @param colunms
     * @param autoI18n
     */
    public CompareResourcesBundlePopupMenu(
            final JTable                                      jTable,
            final AbstractTableModel                          abstractTableModel,
            final CompareResourcesBundleTableModel.Colunms    colunms,
            final AutoI18nCore                                autoI18n
            )
    {
        super( jTable );

        autoI18n.performeI18n(this,this.getClass());

        this.abstractTableModel = abstractTableModel;
        this.colunms            = colunms;
    }

    @Override
    protected JPopupMenu createContextMenu(
            final int rowIndex,
            final int columnIndex
            )
    {
        final JPopupMenu contextMenu = new JPopupMenu();

        addCopyMenuItem(contextMenu, txtCopy, rowIndex, columnIndex);
        addPasteMenuItem(contextMenu, txtPaste, rowIndex, columnIndex);
        contextMenu.addSeparator();

        addShowHTMLMenuItem(contextMenu, rowIndex, columnIndex);
        addEditMultiLineMenuItem(contextMenu, rowIndex, columnIndex);

        return contextMenu;
    }

    protected void addShowHTMLMenuItem(
            final JPopupMenu  contextMenu,
            final int         rowIndex,
            final int         columnIndex
            )
    {
        if( columnIndex == colunms.getColunmKey()) {
            return;
            }
        else if( colunms.getColunmLineIndex( columnIndex ) != -1 ) {
            return;
            }

        final JMenuItem previewMenu = new JMenuItem();

        previewMenu.setText( txtHTMLPreview );
        previewMenu.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed( final ActionEvent e )
                    {
                        final Object value = getValueAt( rowIndex, columnIndex );

                        if( value instanceof String ) {
                            final HTMLPreviewDialog d = new HTMLPreviewDialog(
                                    getFrame(),
                                    txtHTMLPreview,
                                    value.toString()
                                    );
                            d.pack();
                            d.setVisible( true );
                            }
                    }
                });

        contextMenu.add( previewMenu );
    }

    protected void addEditMultiLineMenuItem(
        final JPopupMenu  contextMenu,
        final int         rowIndex,
        final int         columnIndex
        )
    {
        if( columnIndex == colunms.getColunmKey() ) {
            return;
            }
        else if( colunms.getColunmLineIndex( columnIndex ) != -1 ) {
            return;
            }

        final JMenuItem copyMenu = new JMenuItem();

        copyMenu.setText( txtEditLines );
        copyMenu.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed( final ActionEvent e )
                    {
                        final Object value = getValueAt( rowIndex, columnIndex );

                        if( value instanceof String ) {
                            openMultiLineEditor(
                                    //txtEditLines,
                                    String.class.cast( value ),
                                    rowIndex,
                                    columnIndex
                                    );
                        }
                    }
                });

        contextMenu.addSeparator();
        contextMenu.add( copyMenu );
    }

    protected void openMultiLineEditor(
        final String    contentText,
        final int       rowIndex,
        final int       columnIndex
        )
    {
        if( LOGGER.isTraceEnabled() ) {
            LOGGER.trace(
                    String.format(
                        "openMultiLineEditor @(%d;%d)\n",
                        Integer.valueOf( rowIndex ),
                        Integer.valueOf( columnIndex )
                        )
                    );
            }

        final MultiLineEditorDialog.StoreResult storeResult = new MultiLineEditorDialog.StoreResult()
        {
            @Override
            public void storeResult( final String text )
            {
                if( LOGGER.isTraceEnabled() ) {
                    LOGGER.trace(
                        String.format( "Update value @(%d;%d)\n", Integer.valueOf( rowIndex ), Integer.valueOf( columnIndex ) )
                        );
                    }

                setValueAt(text, rowIndex, columnIndex);

                // Update display
                final int row = getJTable().convertRowIndexToModel( rowIndex );
                final int col = getJTable().convertColumnIndexToModel( columnIndex );

                if( LOGGER.isTraceEnabled() ) {
                    LOGGER.trace(
                        String.format( "Update display @(%d;%d)\n", Integer.valueOf( row ), Integer.valueOf( col ) )
                        );
                    }

                abstractTableModel.fireTableCellUpdated(
                        row,
                        col
                        );
            }
        };

        final MultiLineEditorDialog d = new MultiLineEditorDialog(
            getFrame(),
            storeResult , txtEditLines,
            contentText
            );
        d.pack();
        d.setModal( true );
        d.setVisible( true );
    }

    private CompareResourcesBundleFrame getFrame()
    {
        if( frame == null ) {
            for( Container c = getJTable(); c != null; c = c.getParent() ) {
                if( c instanceof CompareResourcesBundleFrame ) {
                    frame = CompareResourcesBundleFrame.class.cast( c );
                    break;
                    }
                else if( c instanceof Frame ) {
                    LOGGER.fatal( "Found a frame but not expected one !" + c );
                    break;
                   }
                }

            if( frame == null ) {
                LOGGER.fatal( "Parent frame not found" );
                }
            }

        return frame;
    }

}
