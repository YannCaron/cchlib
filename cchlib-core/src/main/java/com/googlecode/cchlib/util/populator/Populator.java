package com.googlecode.cchlib.util.populator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field annotation for {@link MapPopulator}
 *
 * @see MapPopulator
 * @see PropertiesPopulator
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface Populator
{
    /**
     * Returns default value if field is not define, see {@link #defaultValueIsNull()}
     * to define null as default value.
     *
     * @return default value if field is not define
     */
    public String defaultValue() default "";

    /**
     * Default value is null, if true ignore {@link #defaultValue()} result
     * @return true if value is null
     */
    public boolean defaultValueIsNull() default false;
}
