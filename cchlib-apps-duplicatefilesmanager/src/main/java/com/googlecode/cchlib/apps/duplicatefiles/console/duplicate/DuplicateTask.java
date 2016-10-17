package com.googlecode.cchlib.apps.duplicatefiles.console.duplicate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.googlecode.cchlib.apps.duplicatefiles.console.CLIHelper;
import com.googlecode.cchlib.apps.duplicatefiles.console.CLIParameters;
import com.googlecode.cchlib.apps.duplicatefiles.console.CLIParametersException;
import com.googlecode.cchlib.apps.duplicatefiles.console.CommandTask;
import com.googlecode.cchlib.apps.duplicatefiles.console.CommandTaskException;
import com.googlecode.cchlib.apps.duplicatefiles.console.HashFile;
import com.googlecode.cchlib.apps.duplicatefiles.console.jsonfilter.AbstractJsonLoader;
import com.googlecode.cchlib.lang.StringHelper;

/**
 * Filter JSON list, keep only duplicate
 */
public class DuplicateTask
    extends AbstractJsonLoader
        implements CommandTask
{
    private class FindDuplicates
    {
        private static final String FAKE_HASH = StringHelper.EMPTY;

        private final List<HashFile> sortedList;
        private final List<HashFile> result = new ArrayList<>();
        private HashFile             prevHashFile;
        private String               prevHash;

        FindDuplicates( final List<HashFile> list )
        {
            this.sortedList = list;
        }

        List<HashFile> findDuplicate()
        {
            this.prevHashFile = null;
            this.prevHash     = FAKE_HASH;

            for( final HashFile hf : this.sortedList ) {
                //
                // Skip entry if not exist on disk
                //
                if( hf.getFile().exists() ) {
                    handleExistingFile( hf );
                }
            }

            return this.result;
        }

        private void handleExistingFile( final HashFile hf )
        {
            //
            // At least 2 occurrences.
            //
            if( isAtLeastTwoExistingOccurences( hf ) ) {
                //
                // Previous occurrences not yet saved
                //
               if( this.prevHashFile != null ) {
                    this.result.add( this.prevHashFile );

                    if( DuplicateTask.this.notQuiet ) {
                        CLIHelper.printMessage( this.prevHashFile.toString() );
                    }

                    this.prevHashFile = null;
                }
                this.result.add( hf );

                if( DuplicateTask.this.notQuiet ) {
                    CLIHelper.printMessage( hf.toString() );
                }
            } else {
                if( this.prevHashFile != null ) {
                    //
                    // Previous value not stored
                    // So this file should be ignore
                    //
                    if( DuplicateTask.this.verbose ) { // NOSONAR
                        CLIHelper.trace( "#Ignore", this.prevHashFile );
                    }
                }

                // New file - store values
                this.prevHashFile = hf;
                this.prevHash     = hf.getHash();
            }
        }

        private boolean isAtLeastTwoExistingOccurences( final HashFile hf )
        {
            return hf.getHash().compareTo( this.prevHash ) == 0;
        }

    }
    private final boolean   verbose;
    private final boolean   notQuiet;
    private final File      jsonInputFile;

    /**
     * Create a {@link DuplicateTask} based on <code>cli</code>
     *
     * @param cli Parameters from CLI
     * @throws CLIParametersException if any
     */
    public DuplicateTask( final CLIParameters cli ) throws CLIParametersException
    {
        this.jsonInputFile = cli.getJsonInputFile();
        this.verbose       = cli.isVerbose();
        this.notQuiet      = !cli.isQuiet();
    }

    @Override
    public List<HashFile> doTask()
        throws CommandTaskException, CLIParametersException // NOSONAR
    {
        final List<HashFile> list = loadJsonInputFile();

        Collections.sort(
            list,
            ( hf1, hf2 ) -> hf1.getHash().compareTo( hf2.getHash() )
            );

        return new FindDuplicates( list ).findDuplicate();
    }

    @Override
    protected File getJsonInputFile()
    {
        return this.jsonInputFile;
    }
}
