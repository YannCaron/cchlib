package com.googlecode.cchlib.apps.duplicatefiles.console.taskhash;

import java.io.File;
import com.googlecode.cchlib.apps.duplicatefiles.console.CLIHelper;

final class VerboseFileComputeListener implements FileComputeTaskListener
{
    private File currentFile;
    private long currentLength;

    @Override
    public void computeDigest( final File file, final int length )
    {
        if( file.equals( this.currentFile ) ) {
            this.currentLength += length;
        } else {
            this.currentFile   = file;
            this.currentLength = 0L;
        }

        CLIHelper.printMessage( "> " + this.currentFile + " : " + this.currentLength );
    }

    @Override
    public boolean isCancel()
    {
        return false;
    }

    @Override
    public void printCurrentFile( final Object result, final File file )
    {
        CLIHelper.printMessage( "" + result + "\t" + file );
    }
}
