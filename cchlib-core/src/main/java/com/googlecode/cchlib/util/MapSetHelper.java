package com.googlecode.cchlib.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import com.googlecode.cchlib.util.iterator.CascadingIterator;

/**
 * @since 4.2
 */
@SuppressWarnings("squid:S00119")
public final class MapSetHelper
{
    private MapSetHelper()
    {
        // All static
    }

    /**
     * Return a {@link Iterable} with all values in all set
     *
     * @param <KEY>
     *            the type of keys maintained by this map
     * @param <VALUE>
     *            the type of mapped values
     * @param mapSet
     *            A {@link Map} containing {@link Set} of value
     * @return an {@link Iterable} of value
     */
    public static <KEY,VALUE> Iterable<VALUE> valuesIterable(
        final Map<KEY,Set<VALUE>> mapSet
        )
    {
        return ( ) -> values( mapSet );
    }

    /**
     * Return a {@link Iterator} with all values in all set
     *
     * @param <KEY>
     *            the type of keys maintained by this map
     * @param <VALUE>
     *            the type of mapped values
     * @param mapSet
     *            A {@link Map} containing {@link Set} of value
     * @return an {@link Iterator} of value
     */
    public static <KEY,VALUE> Iterator<VALUE> values(
        @Nonnull final Map<KEY,Set<VALUE>> mapSet
        )
    {
        return new CascadingIterator<>( mapSet.values().iterator() );
    }

    /**
     * Compute number of VALUE objects.
     *
     * @param <KEY>
     *            the type of keys maintained by this map
     * @param <VALUE>
     *            the type of mapped values
     * @param mapSet
     *            source Map set
     * @return number of VALUE item in {@code mapSet}
     */
    public static <KEY,VALUE> int size( @Nonnull final Map<KEY,Set<VALUE>> mapSet )
    {
        int size = 0;

        for( final Set<? extends VALUE> s:mapSet.values() ) {
            size += s.size();
            }

        return size;
    }

    /**
     * Remove key-Set&lt;V&gt; pair for null or Set&lt;V&gt; like {@link Set#size()}
     * {@code minSetSize}
     * <p>
     * purge(2) : remove all key-Set&lt;V&gt; pair that not contains more than 1 value.
     *
     * @param <KEY>
     *            the type of keys maintained by this map
     * @param <VALUE>
     *            the type of mapped values
     * @param mapSet
     *            A {@link Map} containing {@link Set} of value
     * @param minSetSize
     *            minimum size for Sets to be keep in {@code mapSet}
     * @return {@code mapSet} for chaining operations
     */
    public static <KEY,VALUE> Map<KEY, Set<VALUE>> purge(
        final Map<KEY, Set<VALUE>> mapSet,
        final int                  minSetSize
        )
    {
        for( final Iterator<Map.Entry<KEY, Set<VALUE>>> iterator = mapSet.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<KEY, Set<VALUE>> entry = iterator.next();
            final Set<VALUE>                 set   = entry.getValue();

            if( (set==null) || (set.size()<minSetSize) ) {
                iterator.remove();
                }
            }

        return mapSet;
    }
}
