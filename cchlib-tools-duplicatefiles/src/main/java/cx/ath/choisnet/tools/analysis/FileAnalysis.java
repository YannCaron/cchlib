package cx.ath.choisnet.tools.analysis;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;
import com.googlecode.cchlib.io.DefaultFileRoller;
import com.googlecode.cchlib.io.FileRoller;
import com.googlecode.cchlib.io.FileRollingWriter;
import cx.ath.choisnet.tools.analysis.FileCollector.CancelState;
import cx.ath.choisnet.tools.analysis.view.Log4JXLogger;
import cx.ath.choisnet.tools.analysis.view.XLogger;

/**
 *
 */
public class FileAnalysis
{
    //static final Logger logger = Logger.getLogger( FileAnalysis.class );
    private final XLogger logger;
    private Writer writerFile;
    private Writer writerDirs;
    private Writer writerIgnore;
    private FileFilter directoryFilter;
    private FileFilter fileFilter;
    private FileCollector fileCollector;

    /**
     *
     * @param commonOutputDirFile
     * @throws IOException
     */
    public FileAnalysis(
        final File      commonOutputDirFile,
        final Charset   charset,
        final XLogger    logger
        ) throws IOException
    {
        this(
            new File( commonOutputDirFile, ".FileAnalysis" + ".files" ),
            new File( commonOutputDirFile, ".FileAnalysis" + ".dirs" ),
            new File( commonOutputDirFile, ".FileAnalysis" + ".ignore" ),
            charset,
            logger
            );
    }

    /**
     *
     * @param outputFileFiles
     * @param outputFileDirs
     * @param outputFileIgnored
     * @param charset
     * @throws IOException
     */
    public FileAnalysis(
        final File outputFileFiles,
        final File outputFileDirs,
        final File outputFileIgnored,
        final Charset charset,
        final XLogger logger
        ) throws IOException
    {
        FileRoller frFiles  = new DefaultFileRoller( outputFileFiles );
        this.writerFile     = new FileRollingWriter( frFiles, 10 * 1024 * 1024, charset );

        FileRoller frDirs   = new DefaultFileRoller( outputFileDirs );
        this.writerDirs     = new FileRollingWriter( frDirs, 1024 * 1024, charset );

        FileRoller frIgnore = new DefaultFileRoller( outputFileIgnored );
        this.writerIgnore   = new FileRollingWriter( frIgnore, 256 * 1024, charset );

        this.directoryFilter = new DefaultDirectoryFilter( writerIgnore );
        this.fileFilter      = new FileFilter()
        {
            @Override
            public boolean accept( File file )
            {
                return true;
            }
        };

        this.logger = logger;
    }

    /**
     *
     */
    public void start()
    {
        if( this.fileCollector != null ) {
            throw new IllegalStateException();
            }

        logger.info( "Start: " + new Date( System.currentTimeMillis()) );

        this.fileCollector = new FileCollector(
                directoryFilter,
                fileFilter,
                File.listRoots()
                );
        FileAnalysisVisitor fce = new FileAnalysisVisitor( writerDirs, writerFile, logger );

        this.fileCollector.walk( fce );
        this.fileCollector = null;

        logger.info( "Done: " + new Date( System.currentTimeMillis()) );
    }

    protected CancelState stop()
    {
        logger.info(
            "stop() recipe: "
                + new Date( System.currentTimeMillis())
                + " fc is null ? "
                + (this.fileCollector == null)
            );

        if( this.fileCollector != null ) {
            FileCollector fc = fileCollector;

            fc.cancel();

            // Wait cancel !
            do {
                try {
                    Thread.sleep( 1000 );
                    }
                catch( InterruptedException ignore ) {}
                } while( this.fileCollector == null );

            return fc.getCancelState();
            }

        return null;
    }

    /**
     *
     */
    public void close()
    {
        if( writerIgnore != null ) {
            try {
                writerIgnore.close();
                }
            catch (IOException e) {
                logger.error( "Close fail", e );
                }
            }

        if( writerFile != null ) {
            try {
                writerFile.close();
                }
            catch (IOException e) {
                logger.error( "Close fail", e );
                }
            }

        if( writerDirs != null ) {
            try {
                writerDirs.close();
                }
            catch (IOException e) {
                logger.error( "Close fail", e );
                }
            }
    }

    public static FileAnalysis createFileAnalysis(
            final File         outputDirectory,
            final XLogger     logger
            ) throws IOException
    {
        final FileAnalysis fa = new FileAnalysis(
                outputDirectory,
                Charset.forName( "ISO-8859-1" ),
                logger
                );

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                CancelState cs = fa.stop();

                logger.info( "ShutdownHook CancelState=" + cs );

                if( cs != null ) {
                    fa.close();
                    // TODO: store cs !!!

                    try {
                        Thread.sleep( 2 * 1000 );
                          }
                    catch(InterruptedException ignore) {}
                          }

                logger.info( "ShutdownHook done" + new Date( System.currentTimeMillis()) );
                  }
               });
        return fa;
    }

    /**
     *
     * @param args
     */
    public static void main( final String[] args )
    {
        File     outputDirectory = new File( "." );
        XLogger logger             = new Log4JXLogger( FileAnalysis.class );

        try {
//            final FileAnalysis fa = new FileAnalysis( outputDirectory, Charset.forName( "ISO-8859-1" ) );
//
//            fa.start();
//            fa.close();
//
//            Runtime.getRuntime().addShutdownHook(new Thread() {
//                public void run() {
//                    CancelState cs = fa.stop();
//
//                    logger.info( "ShutdownHook CancelState=" + cs );
//
//                    if( cs != null ) {
//                        fa.close();
//                        // TODO: store cs !!!
//
//                        try {
//                            Thread.sleep( 2 * 1000 );
//                            }
//                        catch(InterruptedException ignore) {}
//                            }
//
//                    logger.info( "ShutdownHook done" + new Date( System.currentTimeMillis()) );
//                    }
//             });
            final FileAnalysis fa = FileAnalysis.createFileAnalysis( outputDirectory, logger );

            fa.start();
            fa.close();
            }
        catch( IOException e ) {
            logger.error( "I/O error", e );
            }
        catch( Exception e ) {
            logger.error( "Error", e );
            }
    }

}
