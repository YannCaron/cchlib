package cx.ath.choisnet.tools.duplicatefiles;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import cx.ath.choisnet.i18n.I18nString;
import cx.ath.choisnet.swing.table.JPopupMenuForJTable;
import cx.ath.choisnet.util.HashMapSet;

//VS 4E -- DO NOT REMOVE THIS LINE!
public class JPanelConfirm extends JPanel 
{
    private static final long   serialVersionUID        = 1L;
    private JTable jTableFiles2Delete;
    private JScrollPane jScrollPaneFiles2Delete;
    private JPanel jPanelHeader;
    private JLabel jLabelTitle;
    @I18nString private String[] columnsHeaders = {
            "File to delete",
            "Length",
            "Kept",
            "Deleted",
            "Comment"
    };
    
    public JPanelConfirm()
    {
        initComponents();
    }

    private void initComponents() {
    	setLayout(new BorderLayout());
    	add(getJPanelHeader(), BorderLayout.NORTH);
        add(getJScrollPaneFiles2Delete(), BorderLayout.CENTER);
    	setSize(320, 240);
    }

    private JPanel getJPanelHeader() {
    	if (jPanelHeader == null) {
    	    jPanelHeader = new JPanel();
    	    jPanelHeader.add(getJLabelTitle());
    	}
    	return jPanelHeader;
    }

    private JLabel getJLabelTitle() {
    	if (jLabelTitle == null) {
    	    jLabelTitle = new JLabel();
    	    jLabelTitle.setText("Files selected to be deleted");
    	}
    	return jLabelTitle;
    }

    private JScrollPane getJScrollPaneFiles2Delete() {
    	if (jScrollPaneFiles2Delete == null) {
    	    jScrollPaneFiles2Delete = new JScrollPane();
    	    jScrollPaneFiles2Delete.setViewportView(getJTableFiles2Delete());
    	}
    	return jScrollPaneFiles2Delete;
    }

    private JTable getJTableFiles2Delete() {
    	if (jTableFiles2Delete == null) {
    	    jTableFiles2Delete = new JTable();
    	}
    	return jTableFiles2Delete;
    }

//    public static void main( String[] args )
//    {
//        SwingUtilities.invokeLater( new Runnable() {
//            @Override
//            public void run()
//            {
//                JFrame frame = new JFrame();
//                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//                frame.setTitle( "JPanelConfirm" );
//                JPanelConfirm content = new JPanelConfirm();
//                content.setPreferredSize( content.getSize() );
//                frame.add( content, BorderLayout.CENTER );
//                frame.pack();
//                frame.setLocationRelativeTo( null );
//                frame.setVisible( true );
//                
//                HashMapSet<String,KeyFileState> df = new HashMapSet<String,KeyFileState>();
//                
//                content.initComponents(df);
//            }
//        } );
//    }

    public void initComponents(
            final HashMapSet<String,KeyFileState> dupFiles
            )
    {
        final List<KeyFileState> toDelete = new ArrayList<KeyFileState>();

        for(KeyFileState f:dupFiles) {
            if( f.isSelectedToDelete() ) {
                toDelete.add( f );
            }
        }

        AbstractTableModel tm = new AbstractTableModel()
        {
            private static final long serialVersionUID = 1L;
            @Override
            public int getColumnCount()
            {
                return columnsHeaders.length;
            }
            @Override
            public String getColumnName(int column)
            {
                return columnsHeaders[column];
            }
            @Override
            public int getRowCount()
            {
                return toDelete.size();
            }
            @Override
            public Class<?> getColumnClass(int columnIndex)
            {
                switch(columnIndex) {
                    case 1:
                        return Long.class;
                    case 2:
                    case 3:
                        return Integer.class;
                    default:
                        return String.class;
                }
            }
             @Override
             public Object getValueAt(int rowIndex, int columnIndex)
            {
                 KeyFileState f = toDelete.get( rowIndex );

                 switch(columnIndex) {
                     case 0 : return f.getFile().getPath();
                     case 1 : return f.getFile().length();
                     case 2 : return computeKept(dupFiles,f.getKey());
                     case 3 : return computeDeleted(dupFiles,f.getKey());
                     case 4 : return "TODO"; // TODO
                 }
                 return null;
            }
         };

        jTableFiles2Delete.setModel(tm);

        JPopupMenuForJTable popupMenu = new JPopupMenuForJTable(jTableFiles2Delete)
        {
            @Override
            protected JPopupMenu createContextMenu(
                    int rowIndex,
                    int columnIndex
                    )
            {
                JPopupMenu contextMenu = new JPopupMenu();

                if( rowIndex == 0 ) {
                    addCopyMenuItem(contextMenu, rowIndex, columnIndex);
                }

                return contextMenu;
            }
        };
        popupMenu.setMenu();
    }

    private int computeKept(
            HashMapSet<String,KeyFileState> dupFiles,
            String                          k
            )
    {
        Set<KeyFileState> s = dupFiles.get( k );
        int               c = 0;

        for(KeyFileState f:s) {
            if(!f.isSelectedToDelete()) {
                c++;
            }
        }

        return c;
    }

    private int computeDeleted(
            HashMapSet<String,KeyFileState> dupFiles,
            String                          k
            )
    {
        Set<KeyFileState> s = dupFiles.get( k );
        int               c = 0;

        for(KeyFileState f:s) {
            if(f.isSelectedToDelete()) {
                c++;
            }
        }

        return c;
    }
}