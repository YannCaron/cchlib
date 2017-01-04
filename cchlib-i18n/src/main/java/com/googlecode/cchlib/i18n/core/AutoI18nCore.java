package com.googlecode.cchlib.i18n.core;

/**
 * Define a class able to be internationalized.
 *
 * @see AutoI18nCoreFactory
 * @see I18nAutoCoreUpdatable
 */
@FunctionalInterface
public interface AutoI18nCore
{
    /**
     * Apply internationalization on {@code objectToI18n} based on
     * class definition {@code clazz}.
     *
     * @param <T>          Type of {@code objectToI18n}
     * @param objectToI18n Object to internationalize
     * @param clazz        Class to use for internationalization
     */
    <T> void performeI18n( T objectToI18n, Class<? extends T> clazz );
}
