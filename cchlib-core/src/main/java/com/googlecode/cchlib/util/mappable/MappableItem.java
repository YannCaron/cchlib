package com.googlecode.cchlib.util.mappable;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Attributes for {@link MappableBuilder}
 */
public enum MappableItem
{
    /**
     *  Add all primitives (boolean, int, long, ...) to map
     *  (also add primitives arrays if {@link #DO_ARRAYS} enabled)
     */
    ALL_PRIMITIVE_TYPE,

    /**
     * Include {@link Mappable} child into map
     */
    DO_RECURSIVE,

    /**
     *  Include arrays into map
     */
    DO_ARRAYS,

    /**
     * Handle {@link Iterator} for adding into map
     */
    DO_ITERATOR,

    /**
     * Handle {@link Iterable} for adding into map
     */
    DO_ITERABLE,

    /**
     * Handle {@link Enumeration} for adding into map
     */
    DO_ENUMERATION,

    /**
     * Include parents classes into introspection, and
     * add result to map.
     */
    DO_PARENT_CLASSES,

    /**
     * Try to include private methods result into map
     */
    TRY_PRIVATE_METHODS,

    /**
     * Try to include protected methods result into map
     */
    TRY_PROTECTED_METHODS
}