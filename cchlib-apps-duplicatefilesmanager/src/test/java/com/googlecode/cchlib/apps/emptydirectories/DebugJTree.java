package com.googlecode.cchlib.apps.emptydirectories;

import java.io.File;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.googlecode.cchlib.apps.emptydirectories.gui.tree.EmptyDirectoryTree;
import com.googlecode.cchlib.apps.emptydirectories.gui.tree.EmptyDirectoryTreeCellRenderer;
import com.googlecode.cchlib.apps.emptydirectories.gui.tree.model.FolderTreeModel2;
import com.googlecode.cchlib.apps.emptydirectories.gui.tree.model.FolderTreeModelable2;
import com.googlecode.cchlib.io.FileHelper;
import static org.mockito.BDDMockito.given;

public class DebugJTree {
    private static final Logger LOGGER = Logger.getLogger( DebugJTree.class );

    @Mock
    private EmptyFolder emptyFolder0;
    @Mock
    private EmptyFolder emptyFolder1;

    private DebugJTree()
    {
        MockitoAnnotations.initMocks( this );

        File empty0File = FileHelper.createTempDir();

        given( emptyFolder0.getPath() ).willReturn( empty0File.toPath() );

        File empty1File = new File( empty0File, "empty" );
        empty1File.mkdir();

        given( emptyFolder1.getPath() ).willReturn( empty1File.toPath() );
    }

    private void createAndShowUI() {
        JFrame frame = new JFrame();
        final FolderTreeModelable2 model = buildDebugModel();
        final EmptyDirectoryTree tree = new EmptyDirectoryTree( model );

        tree.setCellRenderer( new EmptyDirectoryTreeCellRenderer( model ) );
        tree.setVisibleRowCount(10);
        frame.add(new JScrollPane(tree));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }

    private FolderTreeModelable2 buildDebugModel()
    {
        final FolderTreeModelable2 model = new FolderTreeModel2();

        LOGGER.info( "model.size() = " + model.size() );

        model.add( emptyFolder0 );
        model.add( emptyFolder1 );

        LOGGER.info( "model.size() = " + model.size() );

        return model;
    }

//    private static DefaultTreeModel buildDemoModel() {
//        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
//
//        root.add(new DefaultMutableTreeNode("A"));
//        root.add(new DefaultMutableTreeNode("B"));
//        root.add(new DefaultMutableTreeNode("C"));
//
//        return new DefaultTreeModel(root);
//    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DebugJTree().createAndShowUI();
            }
        });
    }
}