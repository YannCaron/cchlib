package com.googlecode.cchlib.apps.emptyfiles;

import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.LinkOption;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import com.googlecode.cchlib.apps.duplicatefiles.DFToolKit;
import com.googlecode.cchlib.apps.emptyfiles.interfaces.FileInfoFormater;
import com.googlecode.cchlib.apps.emptyfiles.tasks.FindTask;
import com.googlecode.cchlib.i18n.AutoI18n;
import com.googlecode.cchlib.i18n.I18nString;
import com.googlecode.cchlib.i18n.config.I18nAutoUpdatable;
import java.awt.BorderLayout;
import org.apache.log4j.Logger;
import java.awt.CardLayout;

public class RemoveEmptyFilesJPanel extends JPanel implements I18nAutoUpdatable
{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger( RemoveEmptyFilesJPanel.class );

    private JPanel                mainJPanel;
    private CardLayout            cardLayout;
    private SelecDirecoriesJPanel selecDirecoriesJPanel;
    private WorkingJPanel         workingJPanel;

    private WorkingTableModel     tableModel;

    @I18nString private String findFilesFmt = "%d files in list";
    @I18nString private String fileLengthFmt = "%d bytes";
    @I18nString private String fileAttributsDelete = "Deleted";

    // private JFileChooserInitializer jFileChooserInitializer;
    private DFToolKit dfToolKit;

    /**
     * Create the panel.
     */
    public RemoveEmptyFilesJPanel( DFToolKit dfToolKit )
    {
        this.dfToolKit = dfToolKit;
        init();

        setLayout(new BorderLayout(0, 0));

        this.mainJPanel = new JPanel();
        add(this.mainJPanel);

        this.cardLayout = new CardLayout(0, 0);
        this.mainJPanel.setLayout( this.cardLayout );

        this.selecDirecoriesJPanel = new SelecDirecoriesJPanel( this );
        this.mainJPanel.add( this.selecDirecoriesJPanel, "SelecDirecoriesJPanel" );

        this.workingJPanel = new WorkingJPanel( this, tableModel );
        this.mainJPanel.add( this.workingJPanel, "WorkingJPanel" );
     }

    private void init()
    {
        getDFToolKit().initJFileChooser();

        FileInfoFormater fileInfoFormater = new FileInfoFormater()
        {
            @Override
            public String formatAttributs( File file )
            {
                return "TODO"; // TODO
            }

            @Override
            public String formatAttributsDelete()
            {
                return fileAttributsDelete;
            }

            @Override
            public String formatLength( File file )
            {
                return String.format( fileLengthFmt, file.length() );
            }
        };
        this.tableModel = new WorkingTableModel( fileInfoFormater );
    }

    public void doFindFiles( final Collection<File> directoryFiles, final JProgressBar progressBar )
    {
        Set<FileVisitOption> fileVisitOption = EnumSet.noneOf( FileVisitOption.class ); // TODO
        int                  maxDepth        = 150; // TODO
        LinkOption           linkOption      = LinkOption.NOFOLLOW_LINKS; // TODO

        progressBar.setEnabled( true );
        progressBar.setIndeterminate( true );
        progressBar.setStringPainted( true );

        TableModelListener tableModelListener = new TableModelListener() {
            @Override
            public void tableChanged( TableModelEvent e )
            {
                progressBar.setString( String.format( findFilesFmt, tableModel.getRowCount() ) );
            }
        };
        this.tableModel.addTableModelListener( tableModelListener  );

        FindTask findTask = new FindTask(this.tableModel, fileVisitOption, maxDepth, linkOption);

        findTask.start( directoryFiles );

        this.tableModel.removeTableModelListener( tableModelListener  );
        this.tableModel.commit();

        progressBar.setStringPainted( false );
        progressBar.setIndeterminate( false );
        progressBar.setEnabled( false );

        this.cardLayout.next( this.mainJPanel );
    }

    public void restart()
    {
        this.tableModel.clear();
        this.selecDirecoriesJPanel.autoSetEnabled();

        this.cardLayout.previous( this.mainJPanel );
    }

    protected DFToolKit getDFToolKit()
    {
        return dfToolKit;
    }

    public void doImport( final DefaultListModel<File> directoriesJListModel )
    {
        logger.info( "doImport()" );

        Runnable r = new Runnable() {
            @Override
            public void run()
            {
                List<File> dirs = getDFToolKit().getRootDirectoriesList();

                for( File file : dirs ) {
                    directoriesJListModel.addElement( file );
                    }

                logger.info( "doImport() done" );
            }
        };
        new Thread( r, "doImport()" ).start();
    }

    public JFileChooser getJFileChooser()
    {
        return getDFToolKit().getJFileChooser( dfToolKit.getMainFrame(), this );
    }

    @Override
    public void performeI18n( AutoI18n autoI18n )
    {
        autoI18n.performeI18n( this, this.getClass() );

        autoI18n.performeI18n( this.selecDirecoriesJPanel, SelecDirecoriesJPanel.class );
        autoI18n.performeI18n( this.workingJPanel, WorkingJPanel.class );
    }
}