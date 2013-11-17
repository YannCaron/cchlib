package com.googlecode.cchlib.apps.emptydirectories.gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.apps.duplicatefiles.DFToolKit;
import com.googlecode.cchlib.apps.emptydirectories.EmptyFolder;
import com.googlecode.cchlib.apps.emptydirectories.gui.tree.EmptyDirectoryTreeCellRenderer;
import com.googlecode.cchlib.apps.emptydirectories.gui.tree.FolderTreeCellEditor;
import com.googlecode.cchlib.apps.emptydirectories.gui.tree.model.FolderTreeModel;
import com.googlecode.cchlib.apps.emptydirectories.gui.tree.model.FolderTreeModelable;
import com.googlecode.cchlib.apps.emptydirectories.gui.tree.model.FolderTreeNode;
import com.googlecode.cchlib.i18n.annotation.I18nName;
import com.googlecode.cchlib.i18n.annotation.I18nString;
import com.googlecode.cchlib.i18n.core.AutoI18nCore;
import com.googlecode.cchlib.i18n.core.I18nAutoCoreUpdatable;
import com.googlecode.cchlib.swing.filechooser.JFileChooserInitializerCustomize;
import com.googlecode.cchlib.swing.filechooser.LasyJFCCustomizer;
import com.googlecode.cchlib.swing.filechooser.WaitingJFileChooserInitializer;
import com.googlecode.cchlib.swing.list.LeftDotListCellRenderer;

/**
 *
 */
@I18nName("RemoveEmptyDirectoriesPanel")
public class RemoveEmptyDirectoriesPanel
    extends RemoveEmptyDirectoriesPanelWB
        implements I18nAutoCoreUpdatable
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger( RemoveEmptyDirectoriesPanel.class );

    private ActionListener actionListener;
    private DFToolKit dfToolKit;
    private Window mainWindow;
    private FolderTreeModelable treeModel;
    private FindDeleteAdapter findDeleteAdapter;
    private WaitingJFileChooserInitializer waitingJFileChooserInitializer;

    @I18nString private String jFileChooserInitializerMessage    = "Analyze disk structure";
    @I18nString private String jFileChooserInitializerTitle     = "Waiting...";
    @I18nString private String txtProgressBarComputing = "Computing...";
    @I18nString private String txtSelectDirToScan = "Select directory to scan";
    @I18nString private String txtProgressBarScanCancel = "Scan canceled !";
    @I18nString private String txtProgressBarSelectFileToDelete = "Select file to delete";
    @I18nString private String txtProgressBarDeleteSelectedFiles = "Delete selected files";

    /**
     *
     */
    public RemoveEmptyDirectoriesPanel(
        final DFToolKit dfToolKit,
        final Window    mainWindow
        )
    {
        super( dfToolKit.getResources() );

        this.dfToolKit  = dfToolKit;
        this.mainWindow = mainWindow;

        init();
    }

    private void init()
    {
        JProgressBar pBar = super.getProgressBar();
        pBar.setStringPainted( true );
        pBar.setString( txtSelectDirToScan );
        pBar.setIndeterminate( false );

        // Create a JTree and tell it to display our model
        final JTree jTreeDir = super.getJTreeEmptyDirectories();
        treeModel = new FolderTreeModel( jTreeDir );
        jTreeDir.setModel( treeModel );

        jTreeDir.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent event)
            {
                Object currentSelectedNodeModel = jTreeDir.getLastSelectedPathComponent();

                if( currentSelectedNodeModel instanceof FolderTreeNode ) {
                    FolderTreeNode selectedNode = (FolderTreeNode)currentSelectedNodeModel;

                    if( selectedNode.getFolder() instanceof EmptyFolder ) {
                        treeModel.toggleSelected( selectedNode );
                    } // else click on a none empty folder => ignore
                 }
            }});
        EmptyDirectoryTreeCellRenderer cellRenderer = new EmptyDirectoryTreeCellRenderer( treeModel );
        jTreeDir.setCellRenderer( cellRenderer );
        jTreeDir.setCellEditor( new FolderTreeCellEditor( treeModel, cellRenderer ) );
        jTreeDir.setEditable( true );

        enable_findTaskDone();

        final LeftDotListCellRenderer leftListCellRenderer = new LeftDotListCellRenderer( super.getJListRootDirectories(), true );
        super.getJListRootDirectories().setCellRenderer( leftListCellRenderer );

        super.getJListRootDirectories().addListSelectionListener(
            new ListSelectionListener()
            {
                @Override
                public void valueChanged( ListSelectionEvent e )
                {
                    final int count = getJListRootDirectories().getSelectedValuesList().size();

                    if( count > 0 ) {
                        setButtonRemoveRootDirectoryEnabled( true );
                        }
                    else {
                        // No selection
                        setButtonRemoveRootDirectoryEnabled( false );
                        }
                }
            });

        // Init Adapter
        FindDeleteListener findDeleteListener = new FindDeleteListener()
        {
            @Override
            public void findTaskDone( final boolean isCancel )
            {
                LOGGER.info( "find thread done" );
                // Bad workaround !!!!
                // TODO: find a better solution to expand tree
                // during build.

                treeModel.expandAllRows();

                enable_findTaskDone();

                JProgressBar pBar = getProgressBar();
                pBar.setIndeterminate( false );

                if( isCancel ) {
                    pBar.setString( txtProgressBarScanCancel );
                    }
                else {
                    pBar.setString( txtProgressBarSelectFileToDelete );
                    }
            }
        };

        findDeleteAdapter = new FindDeleteAdapter(
                getJListRootDirectoriesModel(),
                treeModel,
                findDeleteListener
                );

        // Prepare JFileChooser
        /*frame.*/getWaitingJFileChooserInitializer();

        LOGGER.info( "init() done" );
    }

    protected DFToolKit getDFToolKit()
    {
        return dfToolKit;
    }

    private void onRemoveRootDirectory()
    {
        LOGGER.info( "onRemoveRootDirectory()" );

        if( super.isButtonRemoveRootDirectoryEnabled() ) {
            JList<File>             rootList        = super.getJListRootDirectories();
            List<File>              selectedList    = rootList.getSelectedValuesList();
            DefaultListModel<File>  model           = super.getJListRootDirectoriesModel();

            for( File f : selectedList ) {
                model.removeElement( f );
                }

            rootList.clearSelection();
            setEnableFind( model.size() > 0 );
            }
    }

    private void onFindEmptyDirectories()
    {
        if( super.isButtonStartScanEnabled() ) {
            LOGGER.info( "onFindEmptyDirectories()" );

            findBegin(); // Launch a thread
            }
    }

    private void onCancel()
    {
        LOGGER.info( "onCancel()" );

        if( super.getBtnCancel().isEnabled() ) {
            findDeleteAdapter.cancel();
            LOGGER.info( "Cancel!" );
            }
    }

    private void onSelectAll( final boolean onlyLeaf )
    {
        if( super.isBtnSelectAllEnabled() ) {
            LOGGER.info( "onSelectAll() : onlyLeaf=" + onlyLeaf );

            treeModel.setSelectAll( onlyLeaf, true );
            LOGGER.info( "onSelectAll() : size=" + treeModel.getSelectedEmptyFoldersSize() );

            treeModel.expandAllRows();
            }
    }

    private void onUnselectAll()
    {
        if( super.isBtnUnselectAllEnabled() ) {
            LOGGER.info( "onUnselectAll()" );

            treeModel.setSelectAll( false, false );
            //treeModel.setSelectAllLeaf( false );
            treeModel.expandAllRows();
            }
    }

    private void onStartDelete()
    {
        LOGGER.info( "onStartDelete()" );

        if( super.getBtnStartDelete().isEnabled() ) {
            Runnable task = new Runnable()
            {
                @Override
                public void run()
                {
                    startDelete();
                }
            };
            new Thread( task, "onStartDelete()" ).start();
            // NOTE: do not use of SwingUtilities.invokeLater( r );
        }
    }
    @Override
    protected ActionListener getActionListener()
    {
        if( actionListener == null ) {
            actionListener = new ActionListener()
            {
                @Override
                public void actionPerformed( final ActionEvent event )
                {
                    final String cmd = event.getActionCommand();

                    if( ACTION_IMPORT_DIRS.equals( cmd ) ) {
                        onImportDirectories();
                        }
                    else if( ACTION_ADD_DIRS.equals( cmd ) ) {
                        onAddRootDirectory();
                        }
                    else if( ACTION_REMOVE_DIR.equals( cmd ) ) {
                        onRemoveRootDirectory();
                        }
                    else if( ACTION_FIND_EMPTY_DIRS.equals( cmd ) ) {
                        onFindEmptyDirectories();
                        }
                    else if( ACTION_CANCEL.equals( cmd ) ) {
                        onCancel();
                        }
                    else if( ACTION_SELECT_ALL_SELECTABLE_NODES.equals( cmd ) ) {
                        onSelectAll( false );
                        }
                    else if( ACTION_SELECT_ALL_LEAFONLY.equals( cmd ) ) {
                        onSelectAll( true );
                        }
                    else if( ACTION_UNSELECT_ALL.equals( cmd ) ) {
                        onUnselectAll();
                        }
                    else if( ACTION_START_REMDIRS.equals( cmd ) ) {
                        onStartDelete();
                        }
                    else {
                        LOGGER.warn( "Action not handled : " + cmd );
                        }
                }
            };
        }
        return actionListener;
    }

    private void onAddRootDirectory()
    {
        LOGGER.info( "btnAddRootDirectory()" );

        if( super.isButtonAddRootDirectoryEnabled() ) {
            Runnable task = new Runnable()
            {
                @Override
                public void run()
                {
                    addRootDirectory();
                }
            };
           new Thread( task, "onAddRootDirectory()" ).start();
           LOGGER.info( "btnAddRootDirectory() done" );
        }
    }

    @Override
    protected void addRootDirectory( final List<File> files )
    {
        addRootDirectory( files.toArray( new File[files.size()] ));
    }

    private void addRootDirectory( final File[] files )
    {
        DefaultListModel<File> model = super.getJListRootDirectoriesModel();

        for( File f: files ) {
            if( f.isDirectory() ) {
                model.addElement( f );
                LOGGER.info( "add drop dir:" + f );
                }
            else {
                LOGGER.warn( "Ignore drop : " + f );
                }
            }

        setEnableFind( model.size() > 0 );
    }

    private void onImportDirectories()
    {
        LOGGER.info( "btnImportDirectories()" );

        if( super.isButtonImportDirectoriesEnabled() ) {
            Runnable note = new Runnable()
            {
                @Override
                public void run()
                {
                    List<File> dirs = getDFToolKit().getRootDirectoriesList();
                    addRootDirectory( dirs );
                    LOGGER.info( "btnImportDirectories() done" );
                }
            };
            new Thread( note, "onImportDirectories()" ).start();
            }
    }

    private void addRootDirectory()
    {
        LOGGER.info( "addRootDirectory()" );

        if( super.isButtonAddRootDirectoryEnabled() ) {
            JFileChooser jfc = getJFileChooser();

            LOGGER.info( "getJFileChooser() done" );

            jfc.setMultiSelectionEnabled( true );
            int returnVal = jfc.showOpenDialog( this );

            if( returnVal == JFileChooser.APPROVE_OPTION ) {
                final File[] files = jfc.getSelectedFiles();

                addRootDirectory( files );
                }
            LOGGER.info( "addRootDirectory() done" );
        }
    }

    private WaitingJFileChooserInitializer getWaitingJFileChooserInitializer()
    {
        if( waitingJFileChooserInitializer == null ) {
            JFileChooserInitializerCustomize configurator
                = new LasyJFCCustomizer()
                    .setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

            waitingJFileChooserInitializer = new WaitingJFileChooserInitializer(
                    configurator,
                    mainWindow,
                    jFileChooserInitializerTitle,
                    jFileChooserInitializerMessage
                    );

            }
        return waitingJFileChooserInitializer;
    }

    private JFileChooser getJFileChooser()
    {
        JFileChooser jfc = getWaitingJFileChooserInitializer().getJFileChooser();

        return jfc;
    }

    private void findBegin()
    {
        LOGGER.info( "find thread started" );

        final JProgressBar pBar = getProgressBar();
        pBar.setString( txtProgressBarComputing );
        pBar.setIndeterminate( true );

        enable_findBegin();

        Runnable doRun = new Runnable()
        {
            @Override
            public void run()
            {
                findDeleteAdapter.doFind();
            }
        };

        // KO Lock UI doRun.run();
        // KO Lock UI SwingUtilities.invokeAndWait( doRun );
        // KO Lock UI SwingUtilities.invokeLater( doRun );
        new Thread( doRun, "findBegin()" ).start();
    }

    private void startDelete()
    {
        LOGGER.info( "DELETE Thread started" );

        enable_startDelete();

        getJTreeEmptyDirectories().setEditable( false );

        final JProgressBar pBar = getProgressBar();
        pBar.setString( txtProgressBarDeleteSelectedFiles );
        pBar.setIndeterminate( true );

        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    findDeleteAdapter.doDelete();
                    }
                catch( Exception e ) {
                    LOGGER.warn( "doDelete()", e );
                    }
                catch( Error e ) {
                    LOGGER.fatal( "doDelete()", e );
                    }
                finally {
                    getBtnStartDelete().setEnabled( true );

                    getBtnCancel().setEnabled( false );
                    getJTreeEmptyDirectories().setEditable( true );
                    pBar.setIndeterminate( false );

                    findBegin();
                    }

                LOGGER.info( "DELETE Thread done" );
            }
        }, "startDelete()").start();
    }

    @Override//I18nAutoUpdatable
    public void performeI18n( AutoI18nCore autoI18n )
    {
        autoI18n.performeI18n( this, this.getClass() );
    }
}
