package cx.ath.choisnet.tools.duplicatefiles.gui.panel.result;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import cx.ath.choisnet.tools.duplicatefiles.KeyFileState;
import cx.ath.choisnet.tools.duplicatefiles.KeyFiles;

/**
 *
 *
 */
public abstract class AbstractKeyFiles
    implements KeyFiles // Serializable, Iterable<KeyFileState>
{
    private static final long serialVersionUID = 1L;
    private String key;
    //private String              firstFileDisplayCache;
    //private long                firstFileSizeCache;

    public AbstractKeyFiles(
        final String key
        )
    {
        this.key = key;
    }

    @Override
    public String toString()
    {
        return getFirstFile().getName();
        //return firstFileDisplayCache;
    }

    @Override
    public long length()
    {
        //return this.firstFileSizeCache;
        return getFirstFile().length();
    }

    @Override
    public String getKey()
    {
        return key;
    }

    @Override
    public Iterator<KeyFileState> iterator()
    {
        return getFiles().iterator();
    }

    @Override
    public abstract File getFirstFile();

    @Override
    public abstract Collection<KeyFileState> getFiles();

}