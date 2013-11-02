package com.googlecode.cchlib.apps.duplicatefiles;

import java.util.Date;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import com.googlecode.cchlib.apps.duplicatefiles.gui.DuplicateFilesFrame;
import com.googlecode.cchlib.apps.duplicatefiles.prefs.Preferences;
import com.googlecode.cchlib.swing.DialogHelper;
import com.googlecode.cchlib.swing.JFrames;

/**
 * Application launcher
 */
public class DuplicateFilesApp
{
    private static final Logger logger = Logger.getLogger( DuplicateFilesApp.class );

    /**
     * Launch application
     */
    public static void main( final String[] args )
    {
        logger.info( "starting... : " + new Date() );
        logger.info( "availableProcessors = " + Runtime.getRuntime().availableProcessors() );
        logger.info( "freeMemory          = " + Runtime.getRuntime().freeMemory() );
        logger.info( "maxMemory           = " + Runtime.getRuntime().maxMemory() );
        logger.info( "totalMemory         = " + Runtime.getRuntime().totalMemory() );

        final Preferences   preferences = Preferences.createPreferences();
        final String        title       = "Duplicate Files Manager";

        preferences.applyLookAndFeel();

        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run()
            {
                try {
                    DefaultDFToolKit    defaultDFToolKit    = new DefaultDFToolKit( preferences );
                    DuplicateFilesFrame frame               = new DuplicateFilesFrame( defaultDFToolKit );
                    defaultDFToolKit.setMainWindow( frame );

                    //frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                    frame.setTitle( title );
                    frame.getContentPane().setPreferredSize( frame.getSize() );
                    frame.pack();
                    frame.setLocationRelativeTo( null );
                    frame.setVisible( true );
                    frame.getDFToolKit().initJFileChooser();

                    JFrames.handleMinimumSize(frame, preferences.getMinimumWindowDimension());
                    }
                catch( Throwable e ) {
                    logger.fatal( "Can't load application", e );

                    DialogHelper.showMessageExceptionDialog( title, e );
                    }
            }
        } );
    }
}
