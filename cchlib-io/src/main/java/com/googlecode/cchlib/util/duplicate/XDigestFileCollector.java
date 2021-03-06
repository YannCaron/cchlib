package com.googlecode.cchlib.util.duplicate;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @deprecated use {@link DuplicateFileFinder} instead.
 */
@Deprecated
public interface XDigestFileCollector
    extends Serializable
{
    /**
     * Returns a Map that contain an
     * unique Id (String digest), and a
     * collection of Files for that match
     * to this unique Id.
     * @return Map
     */
    Map<String,Set<File>> getFiles();

    /**
     * Remove duplicate files in collection, and
     * returns number of file removed in Collection.
     * <BR>
     * More formally, remove all entry with a Set&lt;File&gt;
     * size &gt; 1
     *
     * @return number of file removed in Collection
     * @throws UnsupportedOperationException if not supported
     */
    @SuppressWarnings({
        "squid:RedundantThrowsDeclarationCheck",
        })
    int removeDuplicate()
        throws UnsupportedOperationException;

    /**
     * Remove non duplicate files in collection, and
     * returns number of file removed in Collection.
     * <BR>
     * More formally, remove all entry with a Set&lt;File&gt;
     * size &lt; 2
     *
     * @return number of file removed in Collection
     * @throws UnsupportedOperationException if not supported
     */
    @SuppressWarnings({
        "squid:RedundantThrowsDeclarationCheck",
        })
    int removeNonDuplicate()
        throws UnsupportedOperationException;

    /**
     * Removes all of the mappings. The map will
     * be empty after this call returns.
     */
    void clear();

    /**
     * Return count of Set that contain more than
     * on file
     * .
     * @return count of Set that contain more than
     * on file.
     */
    int getDuplicateSetsCount();

    /**
     * Return count of File in a Set that contain
     * more than on file.
     *
     * @return count of File in a Set that contain
     * more than on file
     */
    int getDuplicateFilesCount();

    /**
     * Add a DigestEventListener to this DigestFileCollector
     *
     * @param listener the listener to add
     */
    void addDigestEventListener(DigestEventListener listener);

    /**
     * Remove a DigestEventListener to this DigestFileCollector
     *
     * @param listener the listener to remove
     */
    void removeDigestEventListener(DigestEventListener listener);
    }
