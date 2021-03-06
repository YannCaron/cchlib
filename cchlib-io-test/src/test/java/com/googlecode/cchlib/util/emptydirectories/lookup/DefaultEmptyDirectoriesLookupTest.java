package com.googlecode.cchlib.util.emptydirectories.lookup;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.googlecode.cchlib.util.CancelRequestException;
import com.googlecode.cchlib.util.emptydirectories.EmptyDirectoriesLookup;
import com.googlecode.cchlib.util.emptydirectories.EmptyFolder;

public class DefaultEmptyDirectoriesLookupTest
{
    private static final Logger LOGGER = Logger.getLogger( DefaultEmptyDirectoriesLookupTest.class );

    private LoggerEmptyDirectoriesListener  emptyDirectoriesListener;
    private final List<Path>                pathToCleanUpList = new ArrayList<>();
    private final List<Path>                isEmptyList       = new ArrayList<>();
    private final List<Path>                couldBeEmptyList  = new ArrayList<>();
    private Path                            rootPath;

    @Before
    public void setUp() throws Exception
    {
        this.emptyDirectoriesListener = new LoggerEmptyDirectoriesListener();

        this.rootPath = Files.createTempDirectory( getClass().getSimpleName() );
        deleteOnExit( this.rootPath );

        this.pathToCleanUpList.add( this.rootPath );

        // emptyPath1 (is empty, should be deleted)
        final Path dirPath1 = this.rootPath.resolve( "empty1" );
        Files.createDirectory( dirPath1 );
        deleteOnExit( dirPath1 );
        this.isEmptyList.add( dirPath1 );

        // path2
        // +--- folder2_1
        //      +--- file2_1_1
        // +--- couldBeEmpty2_2
        //      +--- couldBeEmpty2_2_1
        //           +--- empty2_2_1_1
        final Path dirPath2   = this.rootPath.resolve( "dirPath2" );
        Files.createDirectory( dirPath2 );
        deleteOnExit( dirPath2 );
        this.pathToCleanUpList.add( dirPath2 );

        final Path folder2_1 = dirPath2.resolve( "folder2_1" );
        Files.createDirectory( folder2_1 );
        deleteOnExit( folder2_1 );
        this.pathToCleanUpList.add( folder2_1 );

        final Path file2_1_1 = folder2_1.resolve( "file2_1_1" );
        Files.copy( getSomeData(), file2_1_1 );
        deleteOnExit( file2_1_1 );
        this.pathToCleanUpList.add( file2_1_1 );

        final Path dirPath2_2 = dirPath2.resolve( "couldBeEmpty2_2" );
        Files.createDirectory( dirPath2_2 );
        deleteOnExit( dirPath2_2 );
        this.couldBeEmptyList.add( dirPath2_2 );

        final Path dirPath2_2_1 = dirPath2_2.resolve( "couldBeEmpty2_2_1" );
        Files.createDirectory( dirPath2_2_1 );
        deleteOnExit( dirPath2_2_1 );
        this.couldBeEmptyList.add( dirPath2_2_1 );

        final Path dirPath2_2_1_1 = dirPath2_2_1.resolve( "empty2_2_1_1" );
        Files.createDirectory( dirPath2_2_1_1 );
        deleteOnExit( dirPath2_2_1_1 );
        this.isEmptyList.add( dirPath2_2_1_1 );
    }

    private void deleteOnExit( final Path path )
    {
        path.toFile().deleteOnExit();
    }

    private InputStream getSomeData()
    {
        final String data = "Hello world";
        return new ByteArrayInputStream( data.getBytes() );
    }

    @Test
    public void testLookup() throws CancelRequestException, IOException
    {
        final EmptyDirectoriesLookup<ExcludeDirectoriesFileFilter> finder
            = new DefaultEmptyDirectoriesLookup( this.rootPath );

        finder.addListener( this.emptyDirectoriesListener );
        finder.lookup();

        for( final Path p : this.isEmptyList ) {
            LOGGER.trace( "isEmptyList: " + p );
            }
        for( final Path p : this.couldBeEmptyList ) {
            LOGGER.trace( "couldBeEmptyList: " + p );
            }

        for( final Iterator<EmptyFolder> iter =  this.emptyDirectoriesListener.getEmptyFolderList().iterator(); iter.hasNext(); ) {
            final EmptyFolder ef   = iter.next();
            final Path        path = ef.getPath();

            LOGGER.info( "check pass1: " + ef + " : path=" + path );

            if( this.isEmptyList.contains( path ) ) {
                Assert.assertTrue( ef.isEmpty() );
                Files.delete( path );
                iter.remove();
                }
            else if( this.couldBeEmptyList.contains( path ) ) {
                Assert.assertFalse( ef.isEmpty() );
                }
            else {
                LOGGER.warn( "Unexpected entry: " + ef );

                final File[] subFiles = ef.getFile().listFiles();
                LOGGER.warn( "subFiles=" + hideArray(subFiles) );

                if( subFiles != null ) {
                    LOGGER.warn( "subFiles.length=" + subFiles.length );
                    }

                Assert.fail( "Unexpected entry: " + ef );
                }
            }

        for( final Iterator<EmptyFolder> iter =  this.emptyDirectoriesListener.getEmptyFolderList().iterator(); iter.hasNext(); ) {
            final EmptyFolder ef   = iter.next();
            final Path        path = ef.getPath();

            LOGGER.info( "check pass2: " + ef );

            if( this.isEmptyList.contains( path ) ) {
                Assert.fail( "Should be delete: " + ef );
                }
            else if( this.couldBeEmptyList.contains( path ) ) {
                Assert.assertFalse( ef.isEmpty() );
                ef.check();
                Assert.assertTrue( ef.isEmpty() );

                Files.delete( path );
                iter.remove();
                }
            else {
                Assert.fail( "Unexpected entry: " + ef );
                }
            }

        Assert.assertEquals( 0, this.emptyDirectoriesListener.getEmptyFolderList().size() );

        LOGGER.info( "done" );
    }

    private Object hideArray( final File[] subFiles )
    {
        return subFiles;
    }

}
