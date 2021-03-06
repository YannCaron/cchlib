package com.googlecode.cchlib.io;

import java.io.IOException;
import java.io.Writer;

/**
 * A {@link Writer} that write nothing
 */
public class EmptyWriter extends Writer
{
    private boolean open;

    public EmptyWriter()
    {
        open = true;
    }

    @Override
    public void close() throws IOException
    {
        if(!open) {
            throw new IOException("aleary close");
            }
        else {
            open = false;
        }
    }

    @Override
    public void flush() throws IOException
    {
        if(!open) {
            throw new IOException("is close");
            }
    }

    @Override
    public void write(char[] cbuf, int off, int len)
        throws IOException
    {
        if(!open) {
            throw new IOException("is close");
            }
    }
}
