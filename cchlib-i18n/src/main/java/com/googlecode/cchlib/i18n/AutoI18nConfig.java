package com.googlecode.cchlib.i18n;

import java.awt.Window;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JWindow;
import com.googlecode.cchlib.i18n.core.AutoI18nCore;

/**
 * How to select Fields:
 * <p>
 * By default, I18n process inspect all the fields
 * declared by the class or interface represented.
 * by 'objectToI18n' ({@link AutoI18nCore#performeI18n(Object, Class)}.
 * This includes public, protected, default (package)
 * access, and private fields, but excludes inherited
 * fields.
 * </p>
 */
public enum AutoI18nConfig
{
    /**
     * Select only field objects reflecting all the
     * accessible public fields of the class or
     * interface represented by 'objectToI18n'
     * ({@link AutoI18nCore#performeI18n(Object, Class)}.
     */
    ONLY_PUBLIC,

    /**
     * Also get inspect Fields from super class.
     * <BR>
     * Recurse process into super classes since
     * super class is one of {@link Object},
     * {@link JFrame}, {@link JDialog},
     * {@link JWindow}, {@link Window}
     */
    DO_DEEP_SCAN,

    /**
     * Disable internalization process, could be also done
     * using {@link AutoI18n#DISABLE_PROPERTIES}.
     */
    DISABLE,

    /**
     * Add stack trace in logs
     */
    PRINT_STACKTRACE_IN_LOGS,

    /*
     * TODO : HANDLE_ONLY_FIELDS_WITH_ANNOTATION
     */
    ;

    public static EnumSet<AutoI18nConfig> newAutoI18nConfig()
    {
        return EnumSet.noneOf( AutoI18nConfig.class );
    }

    public static EnumSet<AutoI18nConfig> newAutoI18nConfig(
        final AutoI18nConfig first,
        final AutoI18nConfig...rest
        )
    {
        return EnumSet.of( first, rest );
    }

    public static EnumSet<AutoI18nConfig> newAutoI18nConfig(
        final Set<AutoI18nConfig> userConfig
        )
    {
        if( userConfig == null ) {
            return newAutoI18nConfig();
            }
        else {
            return EnumSet.copyOf( userConfig );
            }
    }
}
