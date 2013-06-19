package com.googlecode.cchlib.util.iterable;

import java.util.Comparator;
import java.util.List;
import com.googlecode.cchlib.NeedDoc;
import com.googlecode.cchlib.util.Wrappable;
import com.googlecode.cchlib.util.WrappeException;
import com.googlecode.cchlib.util.iterator.Selectable;

/**
 * e<B>X</B>tended {@link Iterable} interface. Allow to use chaining annotation like :
 * 
 * <code><pre>
 *   List<Integer> result = XIterables.filter( collection, filter ).wrappe( wrapper ).sort( comparator ).filter( filter2 ).wrappe( wrapper2 ).sort( comparator2 ).toList();
 * </pre></code>
 * 
 * @param <T>
 * @since 1.4.8
 * @see XIterables
 * @see Wrappable
 * @see Selectable
 * @see Comparator
 */
@NeedDoc
public interface XIterable<T> extends Iterable<T>
{
    /**
     *
     * @param wrapper
     * @return a new wrapped {@link XIterable} 
     * @throws WrappeException
     */
    public <R> XIterable<R> wrappe( Wrappable<? super T,? extends R> wrapper ) throws WrappeException;

    /**
     *
     * @param filter
     * @return a new filtered {@link XIterable} 
     */
    public XIterable<T> filter(Selectable<? super T> filter);
    
    /**
     * Sort content using comparator
     * @param comparator the comparator to determine the order of the list. 
     *            A null value indicates that the elements' natural ordering should be used.
     * @return a new sorted {@link XIterable} 
     * @throws ClassCastException 
     *            if the list contains elements that are not mutually comparable using 
     *            the specified comparator.
     * @throws UnsupportedOperationException
     *            if the specified list's list-iterator does not support the set operation.
     */
    public XIterable<T> sort( Comparator<? super T> comparator );

    /**
     * @return a new {@link List} with content of {@link Iterable} objects
     */
    public List<T> toList();

    /**
     * Replace <code>list</code> content by element found on this iterable object. 
     * <p>
     * Default implementation should be
     * <pre>
     *   list.clear();
     *   addToList( list );
     * </pre>
     * </p>
     *
     * @param list {@link List} where result will be copied.
     * @return value of <code>list</code> parameter for chaining.
     */
    public List<T> setToList( List<T> list );

    /**
     *
     * @param list {@link List} where result will be appended.
     * @return value of <code>list</code> parameter for chaining.
     */
    public List<T> addToList( List<T> list );

}