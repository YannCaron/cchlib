package com.googlecode.cchlib.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.List;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class FileIterableTest
{
    final private static Logger slogger = Logger.getLogger(FileIterableTest.class);

    public static final File TEMP_DIR_FILE = new File( System.getProperty("java.io.tmpdir" ) );
    //public static final File ROOT_FILE = new File( "C:/" );
    public static final File ROOT_FILE = TEMP_DIR_FILE;
    public static final File SYSTEM_ROOT_FILE = new File( "/" );
    public static final File NOT_EXIST_FILE =  new File( "thisFileShoundNotExists" );
    private File currentFile;

//    public static final int ITER_GETCOUNT = 19;
//    public static final int ITER_XX = 20;

    @Before
    public void setUp() throws java.io.IOException
    {
        currentFile = new File( "." ).getCanonicalFile();
    }


    @Test
    public void testNotExist()
    {
        try {
            @SuppressWarnings("unused")
            FileIterable iter = new FileIterable( NOT_EXIST_FILE );

            fail( "Should crash here" );
            }
        catch( IllegalArgumentException e ) { // $codepro.audit.disable logExceptions
            slogger.info( "Ok: does not exist" );
        }
    }
    /* OLD VERSION
    public void testNotExist()
    {
        Iterator<File> iter = new FileIterable( NOT_EXIST_FILE );

        if( iter.hasNext() ) {
            StringBuilder msg = new StringBuilder()
                    .append( "Hum! : File '" )
                    .append( NOT_EXIST_FILE )
                    .append( " should not exist, so file: " )
                    .append( iter.next() )
                    .append( "' should not exist..." );

            slogger.error( msg );
            fail(msg.toString());
        }

        if( iter.hasNext() ) {
            String msg = "*** error: this Iterator should be empty";

            slogger.error( (new StringBuilder())
                    .append( "*** error: " )
                    .append( iter.next() )
                    .toString()
                    );
            fail( msg );
        }
    }
    */
    @Test
    public void testFileIterableCounter()
    {
        File rootFile = TEMP_DIR_FILE;
        FileIterable fi = new FileIterable( rootFile );
        int countFile = 0;
        int countDir = 0;
        int countOther = 0;
        final int displayMax = 5;
        int displayCount = 0;

        slogger.info( "---------------------" );
        slogger.info( rootFile );
        slogger.info( "* testFileIterableCounter( <<no filter>> )" );
        slogger.info( "---------------------" );
        long begin  = System.currentTimeMillis();
        for( File f : fi ) {
            if( displayCount++<displayMax ) {
                slogger.info( String.format( "f %d:%s\n", displayCount, f ) );
            }
            if( f.isFile() ) {
                countFile++;
            }
            else if( f.isDirectory() ) {
                countDir++;
            }
            else {
                countOther++; // well
                slogger.info( "unkown file type: " + f );
            }
        }
        long end = System.currentTimeMillis();
        slogger.info( "---------------------" );
        slogger.info( "dir        : " + rootFile );
        slogger.info( "file  count: " + countFile );
        slogger.info( "dir   count: " + countDir );
        slogger.info( "other count: " + countOther );
        slogger.info( "ms         : " + (end-begin) );
        slogger.info( "---------------------" );
    }

    @Test
    public void testFileIterableFileFilter()
    {
        File rootFile = currentFile;
        FileFilter fileFilter = new FileFilter(){
            @Override
            public boolean accept( File f )
            {
                return f.getName().endsWith( ".java" );
            }
        };
        FileIterable fi = new FileIterable( rootFile, fileFilter );
        int countFile = 0;
        int countDir = 0;
        int countOther = 0;
        final int displayMax = 5;
        int displayCount = 0;

        slogger.info( "---------------------" );
        slogger.info( rootFile );
        slogger.info( "* testFileIterableFileFilter( *.java )" );
        slogger.info( "---------------------" );
        long begin  = System.currentTimeMillis();
        for( File f : fi ) {
            if( displayCount++<displayMax ) {
                slogger.info( String.format( "f %d:%s\n", displayCount, f ) );
            }

            assertTrue( "file should be a java file :" + f, fileFilter.accept( f ) );

            if( f.isFile() ) {
                countFile++;
            }
            else if( f.isDirectory() ) {
                countDir++;
            }
            else {
                countOther++; // well
                slogger.info( "unkown file type: " + f );
            }
        }
        long end = System.currentTimeMillis();
        slogger.info( "---------------------" );
        slogger.info( "dir        : " + rootFile );
        slogger.info( "file  count: " + countFile );
        slogger.info( "dir   count: " + countDir );
        slogger.info( "other count: " + countOther );
        slogger.info( "ms         : " + (end-begin) );
        slogger.info( "---------------------" );

    }

    @Test
    public void testDirStruct() throws IOException
    {
        File dirRootFile = new File(TEMP_DIR_FILE, getClass().getName());

        IOHelper.deleteTree(dirRootFile);

        boolean res = dirRootFile.exists();
        assertFalse( "Already exists (Can't delete): " + dirRootFile, res);

        res = dirRootFile.mkdirs();
        assertTrue( "Can't mkdirs(): " + dirRootFile, res);

        File[] dirs = {
                new File(dirRootFile, "dir1"),
                new File(dirRootFile, "dir2"),
                new File(dirRootFile, "dir2/dir21"),
                new File(dirRootFile, "dir2/dir22"),
                new File(dirRootFile, "dir2/dir23"),
        };
        File[] files = {
                new File(dirRootFile, "a.txt"),
                new File(dirRootFile, "ab"),
                new File(dirRootFile, "dir2/dir21/b.txt"),
                new File(dirRootFile, "dir2/dir21/b.tmp"),
        };

        List<File> allFiles = new ArrayList<File>();

        // TODO: check if it should be in iterator or not !
        // allFiles.add(dirRootFile);

        for( File d : dirs ) {
            res = d.mkdirs();
            assertTrue( "Can't mkdirs(): " + d, res);
            allFiles.add(d);
            }

        for( File f : files ) {
            IOHelper.toFile( f.getPath(), f );
            allFiles.add( f );
            }

        List<File> notFoundInFileIterable = new ArrayList<File>(allFiles);
        List<File> foundInFileIterable    = new ArrayList<File>();

        FileIterable fi = new FileIterable( dirRootFile );

        for( File f : fi ) {
            foundInFileIterable.add( f );

            boolean oldFound = notFoundInFileIterable.remove( f );
            assertTrue( "File should not be here: " + f, oldFound);
        }

        slogger.info( "allFiles # " + allFiles.size() );
        slogger.info( "foundInFileIterable # " + foundInFileIterable.size() );
        slogger.info( "notFoundInFileIterable # " + notFoundInFileIterable.size() );

        for( File f : notFoundInFileIterable ) {
            slogger.info( "  > not found by Iterator: " + f );
        }

        assertEquals("File count not equals !",allFiles.size(),foundInFileIterable.size());
        assertEquals("Somes files not founds !",0,notFoundInFileIterable.size());

        // cleanup !
        IOHelper.deleteTree(dirRootFile);

        res = dirRootFile.exists();
        assertFalse( "Can't delete(): " + dirRootFile, res);
    }
}
