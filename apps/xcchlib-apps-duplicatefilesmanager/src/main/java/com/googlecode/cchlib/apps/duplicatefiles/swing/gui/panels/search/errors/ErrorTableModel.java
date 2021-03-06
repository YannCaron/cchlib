package com.googlecode.cchlib.apps.duplicatefiles.swing.gui.panels.search.errors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.i18n.annotation.I18nName;
import com.googlecode.cchlib.i18n.annotation.I18nString;

@I18nName("JPanelSearching.ErrorTableModel")
public final class ErrorTableModel extends AbstractTableModel
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger( ErrorTableModel.class );

    private static final int COLUMN_FILE  = 0;
    private static final int COLUMN_CAUSE = 1;

    @I18nString private String[] columnNames ;

    private final List<FileErrorCause> fileErrors = new ArrayList<>();

    public ErrorTableModel()
    {
        stringForI18n();
    }

    private void stringForI18n()
    {
        this.columnNames = new String[] {
                "Files",
                "Errors class"
                };
    }

    @Override
    public boolean isCellEditable( final int row, final int column )
    {
        return false;
    }

    @Override
    public String getColumnName(final int column) {
        return this.columnNames[ column ];
    }

    @Override
    public int getColumnCount()
    {
        return this.columnNames.length;
    }

    @Override
    public int getRowCount()
    {
        return this.fileErrors.size();
    }

    @Override
    public Object getValueAt( final int rowIndex, final int columnIndex )
    {
        final FileErrorCause fileError;

        synchronized( this.fileErrors ) {
            if( rowIndex < this.fileErrors.size() ) {
                fileError = this.fileErrors.get( rowIndex );
            } else {
                LOGGER.warn( "GUI out of sync : rowIndex=" + rowIndex + " >= content.size()=" + this.fileErrors.size() );
                return null; // Ignore, just vector out of sync
            }
        }

        Object value;

        switch( columnIndex ) {
            case COLUMN_FILE :
                value = fileError.getFile();
                break;

            case COLUMN_CAUSE :
                value = fileError.getCause().getLocalizedMessage();
                break;

            default:
                value = null;
                LOGGER.warn( "GUI column not found : columnIndex=" + columnIndex );
                break;
        }

        return value;
    }

    public void clear()
    {
        this.fileErrors.clear();
    }

    public void addRow( final File file, final IOException cause )
    {
        synchronized( this.fileErrors ) {
            this.fileErrors.add( new FileErrorCause( file, cause ) );
        }

        final int firstRow = this.fileErrors.size();
        super.fireTableRowsInserted( firstRow, /*lastRow*/firstRow );
    }
}
