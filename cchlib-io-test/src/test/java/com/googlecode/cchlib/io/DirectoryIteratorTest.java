package com.googlecode.cchlib.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 *
 */
public class DirectoryIteratorTest 
{
    final private static Logger LOGGER = Logger.getLogger(DirectoryIteratorTest.class);

    public static final File TEMP_DIR_FILE    = new File( System.getProperty("java.io.tmpdir" ) );
    public static final File SYSTEM_ROOT_FILE = new File( "/" );
    public static final File NOT_EXIST_FILE   =  new File( "thisFileShoundNotExists" );

    @Test
    public void testNotExist()
    {
        Iterator<File> iter = new DirectoryIterator( NOT_EXIST_FILE );

        if( iter.hasNext() ) {
            StringBuilder msg = new StringBuilder()
                    .append( "Hum! : File '" )
                    .append( NOT_EXIST_FILE )
                    .append( " should not exist, so file: " )
                    .append( iter.next() )
                    .append( "' should not exist..." );

            LOGGER.error( msg );
            fail(msg.toString());
        }

        if( iter.hasNext() ) {
            String msg = "*** error: this Iterator should be empty";

            LOGGER.error( (new StringBuilder())
                    .append( "*** error: " )
                    .append( iter.next() )
                    .toString()
                    );
            fail( msg );
        }
    }

    @Test
    public void testDirectoryIterator()
    {
        int  fCount = 0;
        long begin  = System.currentTimeMillis();
        File rootFile = TEMP_DIR_FILE;
        DirectoryIterator dirsIterator = new DirectoryIterator( rootFile );

        long end = System.currentTimeMillis();

        LOGGER.info( "---------------------" );
        LOGGER.info( "Root    : "  + rootFile );
        LOGGER.info( "ms      : "  + (end-begin) );
        LOGGER.info( "---------------------" );

        begin  = System.currentTimeMillis();
        for( File dirFile : dirsIterator ) {
            fCount++;
            LOGGER.info( "dir "  + dirFile );
            assertTrue( "Is not a directory : " + dirFile, dirFile.isDirectory() );
        }
        end = System.currentTimeMillis();

        LOGGER.info( "---------------------" );
        LOGGER.info( "Count   : "  + fCount );
        LOGGER.info( "ms      : "  + (end-begin) );
        LOGGER.info( "---------------------" );
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

        allFiles.add(dirRootFile);

        for( File d : dirs ) {
            res = d.mkdirs();
            assertTrue( "Can't mkdirs(): " + d, res);
            allFiles.add(d);
            }

        for( File f : files ) {
            // Build some files and put something in it
            IOHelper.toFile( f.getPath(), f );
            }

        List<File> notFoundInFileIterator = new ArrayList<File>(allFiles);
        List<File> foundInFileIterator    = new ArrayList<File>();

        DirectoryIterator di = new DirectoryIterator( dirRootFile );

        for( File f : di ) {
            assertTrue( "Is not a directory : " + f, f.isDirectory() );
            foundInFileIterator.add( f );

            boolean oldFound = notFoundInFileIterator.remove( f );
            assertTrue( "File should not be here: " + f, oldFound);
        }

        LOGGER.info( "allFiles # " + allFiles.size() );
        LOGGER.info( "foundInFileIterator # " + foundInFileIterator.size() );
        LOGGER.info( "notFoundInFileIterator # " + notFoundInFileIterator.size() );

        for( File f : notFoundInFileIterator ) {
            LOGGER.info( "  > not found by Iterator: " + f );
        }

        assertEquals("File count not equals !",allFiles.size(),foundInFileIterator.size());
        assertEquals("Somes files not founds !",0,notFoundInFileIterator.size());

        //
        // Test FileFilter !
        //
        FileFilter dirFF = new FileFilter()
        {
            @Override
            public boolean accept( File f )
            {
                //System.out.printf(">>%s\n",f);
                return f.getName().endsWith( "2" );
            }
        };
        DirectoryIterator diFF = new DirectoryIterator(
                            dirRootFile,
                            dirFF
                            );
        int diFFcount = 0;
        for(File f:diFF) {
            System.out.printf(">%s\n",f);
            diFFcount++;
        }
        assertEquals("Must find 2 directories (+1 rootdir)",3,diFFcount);

        // cleanup !
        IOHelper.deleteTree(dirRootFile);

        res = dirRootFile.exists();
        assertFalse( "Can't delete(): " + dirRootFile, res);
    }
}
